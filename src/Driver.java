import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Driver {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);  // Create a Scanner object


        while (true) {
            System.out.println("Select 1 for ListRanking");
            System.out.println("Select 2 for Topological Sort");
            System.out.println("Select 3 for Boruvka's MST Algorithm");
            System.out.println("Select 4 for Transitive Closure Algorithm");
            System.out.println("type exit to end");
            System.out.print("Select your algorithm ::: ");
            String algo = input.nextLine();  // Read user input

            switch (algo) {
                case "1":
                {
                    System.out.println("Running :: ListRanking");  // Output user input
                    Driver app = new Driver();
                    app.listRanking();
                    break;
                }
                case "2": {
                    System.out.println("Running :: Topological Sort");  // Output user input
                    Driver app = new Driver();
                    app.topologicalSort();
                    break;
                }
                case "3": {
                    System.out.println("Running :: Boruvka's MST Algorithm");  // Output user input
                    Driver app = new Driver();
                    app.boruvka();
                    break;
                }
                case "4": {
                    System.out.println("Running :: Transitive Closure Algorithm");  // Output user input
                    Driver app = new Driver();
                    app.transitiveLLP();
                    break;
                }
                case "exit":{
                    System. exit(0);
                }
                default:
                    System.out.println("Invalid Input");
            }
        }

    }

    private void listRanking() {
//        List<Node> nodes = new ArrayList<>();
//        nodes.add(new Node(0, -1));  // root node
//        nodes.add(new Node(1, 0));
//        nodes.add(new Node(2, 1));
//        nodes.add(new Node(3, 2));


        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(0, -1));  // root node
        nodes.add(new Node(1, 0));  // child of 0
        nodes.add(new Node(2, 0));  // child of 0
        nodes.add(new Node(3, 2));  // child of 2
        nodes.add(new Node(4, 3));  // child of 3
        nodes.add(new Node(5, 3));  // child of 3

        ListRanking listRanking = new ListRanking(nodes);
        listRanking.rank();

        // Output the results
        for (Node node : nodes) {
            System.out.println("Node Value: " + node.value + ", Distance from Root: " + node.dist);
        }
    }

    private void transitiveLLP() {
        // Sample matrix A (using boolean[][] for representation)
        boolean[][] A = {{false, true, false, false},
                {false, false, true, false},
                {false, false, false, true},
                {false, false, false, false}};

        // Clone the matrix to G
        boolean[][] G = new boolean[A.length][];
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


    private void topologicalSort() {
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