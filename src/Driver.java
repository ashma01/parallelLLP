import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Driver {
    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(System.in);  // Create a Scanner object


        while (true) {
            System.out.println("\nSelect 1  --------- ListRanking");
            System.out.println("Select 2  --------- Topological Sort");
            System.out.println("Select 3  --------- Boruvka's MST Algorithm");
            System.out.println("Select 4  --------- Transitive Closure Algorithm");
            System.out.println("type exit to end");
            System.out.print("Select your algorithm ::: ");
            String algo = input.nextLine();  // Read user input

            switch (algo) {
                case "1": {
                    System.out.println("\nRunning :: ListRanking\n");  // Output user input
                    Driver app = new Driver();
                    app.listRanker();
                    break;
                }
                case "2": {
                    System.out.println("Running :: Topological Sort\n");  // Output user input
                    Driver app = new Driver();
                    app.topologicalSort();
                    break;
                }
                case "3": {
                    System.out.println("Running :: Boruvka's MST Algorithm\n");  // Output user input
                    Driver app = new Driver();
                    app.boruvka();
                    break;
                }
                case "4": {
                    System.out.println("Running :: Transitive Closure Algorithm\n");  // Output user input
                    Driver app = new Driver();
                    app.transitiveLLP();
                    break;
                }
                case "exit": {
                    System.exit(0);
                }
                default:
                    System.out.println("Invalid Input");
            }
        }

    }

    private void listRanker() {
        InputReader inputReader = new InputReader();
        int[] parent = inputReader.readListRankerInput("input/ListRanker.txt");
        ListRankingParallelLLP listRankingParallelLLP = new ListRankingParallelLLP(parent,4);
        System.out.println("\n***** Running on second input *******\n");
        int[] parent2 = inputReader.readListRankerInput("input/ListRanker2.txt");
        ListRankingParallelLLP listRankingParallelLLP2 = new ListRankingParallelLLP(parent2,4);


    }

    private void transitiveLLP(){

        InputReader inputReader = new InputReader();
        int[][] A = inputReader.readMatrixFromFile("input/TransitiveClosureInput.txt");
        int[][] closure = computeTransitiveClosure(A);
        printMatrix(closure);
        System.out.println("\n***** Running on second input *******\n");
        int[][] A2 = inputReader.readMatrixFromFile("input/TransitiveClosureInput2.txt");
        int[][] closure2 = computeTransitiveClosure(A2);
        printMatrix(closure2);
    }


    public static int[][] computeTransitiveClosure(int[][] A) {
        int n = A.length;
        int[][] G = new int[n][n];

        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, G[i], 0, n);
        }

        List<Entry> matrixEntries = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrixEntries.add(new Entry(i, j));
            }
        }

        ParallelLLP<Entry> pll = new ParallelLLP<>(4);
        boolean updated;
        do {
            updated = false;
            boolean[] results = pll.compute(matrixEntries, new TransitiveClosureLLP(G));

            for (int i = 0; i < results.length; i++) {
                synchronized(G) {
                    if (results[i]) {
                        Entry entry = matrixEntries.get(i);
                        G[entry.i][entry.j] = 1;
                        updated = true;
                    }
                }

            }

        } while (updated);

        pll.shutdown();

        return G;
    }



    private void boruvka() throws IOException {

        InputReader inputReader = new InputReader();
        Graph graph = inputReader.readInputForBoruvka("input/BoruvkaMst.txt");
        // Find the MST using Boruvka's Algorithm
        List<Edge> mst = boruvkaMST(graph);
        // Print the MST
        System.out.println("Edges in the Minimum Spanning Tree:");
        for (Edge e : mst) {
            System.out.println(e.v + " -- " + e.w + " ==> " + e.weight);
        }

        System.out.println("\n***** Running on second input *******\n");

        Graph graph2 = inputReader.readInputForBoruvka("input/BoruvkaMst2.txt");
        // Find the MST using Boruvka's Algorithm
        List<Edge> mst2 = boruvkaMST(graph2);
        // Print the MST
        System.out.println("Edges in the Minimum Spanning Tree:");
        for (Edge e2 : mst2) {
            System.out.println(e2.v + " -- " + e2.w + " ==> " + e2.weight);
        }


    }

    private static List<Edge> boruvkaMST(Graph graph) {
        List<Edge> mstEdges = new ArrayList<>();
        ParallelLLP<List<Edge>> pll = new ParallelLLP<>(4); // Assuming 4 threads
        Edge[] minWeightEdge = new Edge[graph.V]; // Array to hold the minimum weight edge for each component
        int remainingComponents = graph.V;
        while (remainingComponents > 1) {
            MSTEdgePredicate predicate = new MSTEdgePredicate(graph, minWeightEdge);
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
        pll.shutdown();
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


    private void topologicalSort() throws IOException {

        InputReader inputReader = new InputReader();

        List<TopologicalSortLLP> graph = inputReader.readNodesFromFileTopoSort("input/TopoSort.txt");

        runningTopo(graph);

        System.out.println("\n***** Running on second input *******\n");

        List<TopologicalSortLLP> graph2 = inputReader.readNodesFromFileTopoSort("input/TopoSort2.txt");

        runningTopo(graph2);

    }

    private static void runningTopo(List<TopologicalSortLLP> graph) {
        ParallelLLP<TopologicalSortLLP> pll = new ParallelLLP<>(4);

        // Continuously run the compute method until no more changes are observed
        boolean changed;
        do {
            boolean[] results = pll.compute(graph, new TopologicalSortLLP());
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
        pll.shutdown();
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}