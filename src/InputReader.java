import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InputReader {

    public List<Node> readNodesFromFileListRanking(String filename) throws IOException {
        List<Node> nodes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int value = Integer.parseInt(parts[0].trim());
                int parent = Integer.parseInt(parts[1].trim());
                nodes.add(new Node(value, parent));
            }
        }
        return nodes;
    }

    public List<TopologicalSortLLP> readNodesFromFileTopoSort(String filename) throws IOException {
        List<TopologicalSortLLP> llpTopologicalSorts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while (br.readLine() != null) {
                llpTopologicalSorts.add(new TopologicalSortLLP()); // Initialize the node with an empty list of predecessors
            }
        }

        // Set predecessors based on the file
        int index = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(":")) {
                    String[] parts = line.split(":");
                    String[] predIndices = parts[1].split(",");
                    for (String predIndex : predIndices) {
                        llpTopologicalSorts.get(index).predecessors.add(llpTopologicalSorts.get(Integer.parseInt(predIndex.trim())));
                    }
                }
                index++;
            }
        }
        return llpTopologicalSorts;
    }

    public Graph readInputForBoruvka(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        int vertices = Integer.parseInt(br.readLine().trim());
        Graph graph = new Graph(vertices);
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(" ");
            int start = Integer.parseInt(parts[0]);
            int end = Integer.parseInt(parts[1]);
            int weight = Integer.parseInt(parts[2]);

            graph.addEdge(start, end, weight);
        }

        br.close();

        return graph;
    }

    public int[][] readMatrixFromFile(String fileName) {
        int[][] matrix = null;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int rowCount = 0;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (matrix == null) {
                    matrix = new int[values.length][values.length];
                }

                for (int colCount = 0; colCount < values.length; colCount++) {
                    matrix[rowCount][colCount] = Integer.parseInt(values[colCount]);
                }

                rowCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matrix;
    }
}
