import java.util.Arrays;
import java.util.List;

public class ListRanking {
    private final List<Node> nodeList;
    private final ParallelLLP<Node> parallelLLP;
    private static final int ROOT_INDEX = 0;  // Assuming the root node is at index 0

    public ListRanking(List<Node> nodeList) {
        this.nodeList = nodeList;
        this.parallelLLP = new ParallelLLP<>(4);
    }

    public void rank() {
        NodePredicate forbiddenCheck = new NodePredicate(ROOT_INDEX);
        NodeAdvancer advancer = new NodeAdvancer(nodeList);

        boolean[] results;
        boolean hasForbidden;
        do {
            results = parallelLLP.compute(nodeList, forbiddenCheck);

            hasForbidden = false;
            for (boolean result : results) {
                if (result) {
                    hasForbidden = true;
                    break;
                }
            }

            if (hasForbidden) {
                parallelLLP.compute(nodeList, advancer);
            }
        } while (hasForbidden);
    }

}
