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

    public void boruvkaLLP(int numThreads) {
        System.out.println("Starting boruvkaLLP method...");

        // Step 1: Process and merge minimum weight edges
        System.out.println("Processing and merging minimum weight edges...");
        // Wait for all threads to finish
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        int chunkSize = mwe.length / numThreads;
        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? mwe.length : (i + 1) * chunkSize;

            executor.execute(() -> {
                for (int v = start; v < end; v++) {
                    processMinimumWeightEdges();

                }
            });
        }

        executor.shutdown();
        try {
            // Wait for all threads to finish
            boolean terminated = executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            if (terminated) {
                System.out.println("All threads have completed execution.");
            } else {
                System.err.println("Some threads did not complete within the specified time.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread execution was interrupted: " + e.getMessage());
        }

        System.out.println("Merged components: " + Arrays.toString(LLP));

    }

    public void initializeG() {
        // Structure G
        int[] g = new int[mwe.length];

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
        int root = v;
        while (LLP[root].parent != -1) {
            root = LLP[root].parent;
        }

        // Perform path compression for all visited nodes
        int current = v;
        while (LLP[current].parent != -1) {
            int parent = LLP[current].parent;
            LLP[current].parent = root;
            current = parent;
        }

        System.out.println("Find " + v + " -> Root: " + root);
        return root;
    }

    public synchronized void merge(int v, int w) {
        int rootV = find(v);
        int rootW = find(w);

        System.out.println("Merging " + v + " (Root: " + rootV + ") with " + w + " (Root: " + rootW + ")");

        if (rootV != rootW) {
            LLP[rootV].parent = rootW;
            System.out.println("Merged " + v + " with " + w);
        }
    }


    public synchronized void processMinimumWeightEdges() {
        System.out.println("Starting processMinimumWeightEdges...");
        System.out.println("Array length: " + mwe.length);
        for (int v = 0; v < mwe.length; v++) {
            System.out.println("Processing vertex " + v);
            int w = mwe[v];
            int parentV = find(v);
            int parentW = find(w);

            System.out.println("mwe[" + v + "] = " + w);
            System.out.println("Parent of " + v + " is " + parentV);
            System.out.println("Parent of " + w + " is " + parentW);

            // Update the LLP tree based on the minimum weight edge
            if (mwe[parentV] > mwe[parentW]) {
                LLP[parentV].parent = parentW;
                System.out.println("Edge processed: " + v + " -> " + w);
            } else {
                LLP[parentW].parent = parentV;
                System.out.println("Edge processed: " + w + " -> " + v);
            }
        }
        System.out.println("Finished processMinimumWeightEdges.");
        mergeMinimumWeightEdges();
    }


    public synchronized void mergeMinimumWeightEdges() {
        for (int v = 0; v < mwe.length; v++) {
            int w = mwe[v];
            if (w != v) {  // Only merge if it's not a self-loop
                int parentV = find(v);
                int parentW = find(w);

                if (mwe[parentV] == w) {
                    merge(parentV, w);
                } else if (mwe[parentW] == v) {
                    merge(parentW, v);
                }
            }
        }
    }
}
