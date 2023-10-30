import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListRankingParallelLLP {

    static class Node {
        int value;
        int distance = -1;  // Initialize to -1 to denote uncalculated
        Node child;

        public Node(int value) {
            this.value = value;
        }
    }

    public ListRankingParallelLLP(int[] parent, int numThread) {

        List<Node> nodes = buildHierarchy(parent);
        ParallelLLP<Node> parallelLLP = new ParallelLLP<>(numThread);
        LLPRankingPredicate predicate = new LLPRankingPredicate();

        boolean[] results;

        do {
            results = parallelLLP.compute(nodes, predicate);

        } while (isAnyTrue(results));

        parallelLLP.shutdown();

        nodes.forEach(node -> System.out.println("Node Value: " + node.value + ", Distance from Root: " + (node.distance == -1 ? 0 : node.distance)));
    }

    private static boolean isAnyTrue(boolean[] arr) {
        for (boolean b : arr) {
            if (b) return true;
        }
        return false;
    }

    private static List<Node> buildHierarchy(int[] parent) {
        Map<Integer, Node> nodeMap = new HashMap<>();

        for (int value : parent) {
            nodeMap.put(value, new Node(value));
        }

        // Root node has a distance of 0
        nodeMap.get(parent[0]).distance = 0;

        // Build hierarchy
        for (int i = 0; i < parent.length - 1; i++) {
            nodeMap.get(parent[i]).child = nodeMap.get(parent[i+1]);
        }

        return new ArrayList<>(nodeMap.values());
    }
}
