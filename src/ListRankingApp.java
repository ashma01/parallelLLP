import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ListRankingApp {
    public static void main(String[] args) {
        ListRankingApp app = new ListRankingApp();

//        System.out.println("call list ranking");
//        app.listRanking();
//

//        System.out.println("call topological sort");
//        app.topo();

//        System.out.println("call boruvka");
//        app.boruvka();

        System.out.println("call transitive closure");
        app.transitiveLLP();

    }

    private void transitiveLLP() {
        // Sample matrix A (using boolean[][] for representation)
        boolean[][] A = {{false, true, false, false},
                         {false, false, true, false},
                         {false, false, false, true},
                         {false, false, false, false}};

        // Clone the matrix to G
        boolean[][] G = new boolean[A.length][];
        System.out.println("Glength-->" + G.length);
        for (int i = 0; i < A.length; i++) {
            G[i] = A[i].clone();
        }

        List<Entry> matrixEntries = new ArrayList<>();
        for (int i = 0; i < G.length; i++) {
            for (int j = 0; j < G[i].length; j++) {
                matrixEntries.add(new Entry(i, j));
            }
        }

        ParallelLLP<Entry> pll = new ParallelLLP<>(4);

        boolean updated;
        do {
            updated = false;
            // Compute using ParallelLLP
            boolean[] results = pll.compute(matrixEntries, new TransitiveClosureLLP(G));

            // Check if there's any update in the matrix
            for (boolean result : results) {
                if (result) {
                    updated = true;
                    break;
                }
            }

        } while (updated);


        // Print the transitive closure
        for (int i = 0; i < G.length; i++) {
            for (int j = 0; j < G[0].length; j++) {
                System.out.print(G[i][j] ? "true " : "false ");
            }
            System.out.println();
        }


    }

    private void boruvka() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 6);
        graph.addEdge(0, 3, 5);
        graph.addEdge(1, 3, 15);
        graph.addEdge(2, 3, 4);

        // Find the MST using Boruvka's Algorithm
        List<Edge> mst = boruvkaMST(graph);

        // Print the MST
        System.out.println("Edges in the Minimum Spanning Tree:");
        for (Edge e : mst) {
            System.out.println(e.v + " -- " + e.w + " == " + e.weight);
        }

    }

    private static List<Edge> boruvkaMST(Graph graph) {
        List<Edge> mstEdges = new ArrayList<>();
        ParallelLLP<List<Edge>> pll = new ParallelLLP<>(4); // Assuming 4 threads
        Edge[] minWeightEdge = new Edge[graph.V]; // Array to hold the minimum weight edge for each component

        int remainingComponents = graph.V;

        while (remainingComponents > 1) {
            MSTEdgePredicate predicate = new MSTEdgePredicate(graph, minWeightEdge);

            // Here, instead of sending individual edges to compute,
            // we're sending the entire edge list. This is because our predicate is now designed to handle lists.
            pll.compute(Arrays.asList(graph.edges), predicate);

            for (int i = 0; i < graph.V; i++) {
                Edge e = minWeightEdge[i];
                if (e != null && graph.component[e.v].get() != graph.component[e.w].get()) {
                    mstEdges.add(e);
                    union(graph.component, e.v, e.w);
                    remainingComponents--;
                }
                minWeightEdge[i] = null; // Reset for next iteration
            }
        }

        return mstEdges;
    }

    private static void union(AtomicInteger[] component, int v, int w) {
        int rootV = find(component, v);
        int rootW = find(component, w);
        if (rootV != rootW) {
            component[rootV].set(rootW);
        }
    }

    private static int find(AtomicInteger[] component, int vertex) {
        while (component[vertex].get() != vertex) {
            vertex = component[vertex].get();
        }
        return vertex;
    }


    private void listRanking() {

        // Sample usage
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;

        parallelListRanking(node1);

        // Output the rank of each node.
        ListNode current = node1;
        while (current != null) {
            System.out.println(current.rank);
            current = current.next;
        }

    }

    public void parallelListRanking(ListNode head) {
        ParallelLLP<ListNode> pll = new ParallelLLP<>(4);  // Using 4 threads.

        List<ListNode> activeNodes = new ArrayList<>();
        ListNode current = head;
        while (current != null) {
            activeNodes.add(current);
            current = current.next;
        }

        while (!activeNodes.isEmpty()) {
            List<ListNode> nextActiveNodes = Collections.synchronizedList(new ArrayList<>());

            pll.compute(activeNodes, node -> {
                if (node.next != null) {
                    node.rank += 1 + node.next.rank;  // Adopt the next's rank
                    if (node.next.next != null) {
                        nextActiveNodes.add(node.next.next);  // Prepare for the next round
                    }
                    node.next = node.next.next;  // Jump two steps
                }
                return true;
            });

            activeNodes = new ArrayList<>(nextActiveNodes);  // Start next round with nodes ready for processing
        }
    }

//    private void topoSort() {
//        int n = 6;  // Assuming 6 nodes in the graph, indexed from 0 to 5
//
//        // Predecessors for each node, as an example:
//        List<Integer>[] predecessors = new List[n];
//        predecessors[0] = new LinkedList<>();  // Node 0 has no predecessors
//        predecessors[1] = Arrays.asList(0);  // Node 1 has Node 0 as its predecessor
//        predecessors[2] = Arrays.asList(0);
//        predecessors[3] = Arrays.asList(1, 2);
//        predecessors[4] = Arrays.asList(2);
//        predecessors[5] = Arrays.asList(3, 4);  // Node 5 has Node 3 and Node 4 as its predecessors
//
//        // Initializing the ParallelLLP with 2 threads
//        ParallelLLP<Integer> pll = new ParallelLLP<>(2);
//
//        // Create an instance of LLP_TopologicalSort and execute the algorithm
//        LLP_TopologicalSort3 topologicalSort = new LLP_TopologicalSort3(n, predecessors, pll);
//        int[] result = topologicalSort.parallelTopologicalSort();
//
//        System.out.println("Topological Order:");
//        for (int value : result) {
//            System.out.print(value + " ");
//        }
//
//    }


    private void topo() {
        LLP_TopologicalSort.Node n0 = new LLP_TopologicalSort.Node();
        LLP_TopologicalSort.Node n1 = new LLP_TopologicalSort.Node();
        LLP_TopologicalSort.Node n2 = new LLP_TopologicalSort.Node();
        LLP_TopologicalSort.Node n3 = new LLP_TopologicalSort.Node();

        n1.predecessors.add(n0);
        n2.predecessors.add(n0);
        n3.predecessors.add(n1);
        n3.predecessors.add(n2);

        List<LLP_TopologicalSort.Node> graph = Arrays.asList(n0, n1, n2, n3);

        ParallelLLP<LLP_TopologicalSort.Node> pll = new ParallelLLP<>(4);

        // Continuously run the compute method until no more changes are observed
        boolean changed;
        do {
            boolean[] results = pll.compute(graph, new LLP_TopologicalSort.TopologicalPredicate());
            changed = false;
            for (boolean val : results) {
                if (val) {
                    changed = true;
                    break;
                }
            }
        } while (changed);

        for (int i = 0; i < graph.size(); i++) {
            System.out.println("Node " + i + " Order: " + graph.get(i).value);
        }
    }

}