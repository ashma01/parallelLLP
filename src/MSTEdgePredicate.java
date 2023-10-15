import java.util.List;

public class MSTEdgePredicate implements LatticePredicate<List<Edge>> {
    private Graph graph;
    private Edge[] minWeightEdge;


    public MSTEdgePredicate(Graph graph, Edge[] minWeightEdge) {
        this.graph = graph;
        this.minWeightEdge = minWeightEdge;
    }

    @Override
    public boolean evaluate(List<Edge> edges) {
        for (Edge e : edges) {
            if (graph.component[e.v].get() != graph.component[e.w].get()) {
                synchronized (minWeightEdge) { // Use synchronized to handle concurrent updates
                    if (minWeightEdge[graph.component[e.v].get()] == null ||
                            e.weight < minWeightEdge[graph.component[e.v].get()].weight) {
                        minWeightEdge[graph.component[e.v].get()] = e;
                    }
                    if (minWeightEdge[graph.component[e.w].get()] == null ||
                            e.weight < minWeightEdge[graph.component[e.w].get()].weight) {
                        minWeightEdge[graph.component[e.w].get()] = e;
                    }
                }
            }
        }
        return true;
    }
}
