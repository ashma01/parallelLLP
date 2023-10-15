import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Graph {
    int V;                  // Number of vertices
    List<Edge> edges;       // List of all edges
    AtomicInteger[] component; // Component to which each vertex belongs

    public Graph(int V) {
        this.V = V;
        edges = new ArrayList<>();
        component = new AtomicInteger[V];
        for (int i = 0; i < V; i++) {
            component[i] = new AtomicInteger(i); // Initially, each vertex is its own component
        }
    }

    // Add an edge to the graph
    public void addEdge(int v, int w, int weight) {
        edges.add(new Edge(v, w, weight));
    }
}
