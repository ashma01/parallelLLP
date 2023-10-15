import java.util.*;

public class LLP_TopologicalSort {
    public static class Node {
        public List<Node> predecessors = new ArrayList<>();
        public int value = 0;
        public boolean fixed = false;
    }

    public static class TopologicalPredicate implements LatticePredicate<Node> {
        @Override
        public boolean evaluate(Node node) {
            if (node.fixed) return false;

            for (Node pred : node.predecessors) {
                if (!pred.fixed) return false;
            }

            node.value = node.predecessors.stream().mapToInt(n -> n.value).max().orElse(-1) + 1;
            node.fixed = true;
            return true;
        }
    }
}