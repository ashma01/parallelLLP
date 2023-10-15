package Boruvka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Boruvka {
    private int[] mwe;  // Minimum weight edge array
    private LLPNode[] LLP;  // LLP tree nodes

    public Boruvka(int[] mwe) {
        this.mwe = mwe;
        this.LLP = new LLPNode[mwe.length];
        for (int i = 0; i < mwe.length; i++) {
            LLP[i] = new LLPNode(i);
        }
    }

    public synchronized void processMinimumWeightEdges() {
        for (int v = 0; v < mwe.length; v++) {
            int w = mwe[v];
            int parentV = find(v);
            int parentW = find(w);

            // Update the LLP tree based on the minimum weight edge
            if (mwe[parentV] > mwe[parentW]) {
                LLP[parentV].parent = parentW;
            } else {
                LLP[parentW].parent = parentV;
            }
        }
    }

    public void boruvkaLLP(int numThreads) {
        // Perform the initial processing of minimum weight edges
        processMinimumWeightEdges();

        // Find the representative component for each vertex
        int[] components = new int[mwe.length];
        Arrays.fill(components, -1);

        for (int v = 0; v < mwe.length; v++) {
            components[v] = find(v);
        }

        // Find the minimum weight edge for each component
        int[] minWeightEdgeForComponent = new int[mwe.length];
        Arrays.fill(minWeightEdgeForComponent, Integer.MAX_VALUE);

        for (int v = 0; v < mwe.length; v++) {
            int component = components[v];
            minWeightEdgeForComponent[component] = Math.min(minWeightEdgeForComponent[component], mwe[v]);
        }

        // Merge components based on minimum weight edges
        for (int v = 0; v < mwe.length; v++) {
            int component = components[v];
            if (mwe[v] == minWeightEdgeForComponent[component]) {
                merge(component, mwe[v]);
            }
        }

        // Wait for all threads to finish
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        int chunkSize = mwe.length / numThreads;
        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? mwe.length : (i + 1) * chunkSize;

            executor.execute(() -> {
                for (int v = start; v < end; v++) {
                    int component = components[v];
                    if (mwe[v] == minWeightEdgeForComponent[component]) {
                        merge(component, mwe[v]);
                    }
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void initializeG() {
        // Structure G
        int[] g = new int[mwe.length];
        Arrays.fill(g, -1);
        for (int v = 0; v < mwe.length; v++) {
            int w = mwe[v];
            if (w == v) {
                int parent = find(v);
                if (mwe[parent] == parent && v < parent) {
                    g[v] = v;
                } else {
                    g[v] = parent;
                }
            }
        }
    }

    public synchronized int find(int v) {
        // Check if v is its own parent (a representative component)
        if (LLP[v].parent == -1) {
            return v;
        }

        // Keep track of the nodes visited in this path
        List<Integer> visitedNodes = new ArrayList<>();

        // Traverse the path to find the root iteratively
        while (LLP[v].parent != -1) {
            visitedNodes.add(v);
            v = LLP[v].parent;
        }

        // Perform path compression for all visited nodes
        int root = v;
        for (int node : visitedNodes) {
            LLP[node].parent = root;
        }

        return root;
    }



    public synchronized void merge(int v, int w) {
        int rootV = find(v);
        int rootW = find(w);
        if (rootV != rootW) {
            LLP[rootV].parent = rootW;
        }
    }
}
