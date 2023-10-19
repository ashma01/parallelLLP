import java.util.*;

public class TopologicalSortLLP implements LatticePredicate<TopologicalSortLLP> {
    public List<TopologicalSortLLP> predecessors = new ArrayList<>();
    public int value = 0;
    public boolean fixed = false;


    @Override
    public boolean evaluate(TopologicalSortLLP node) {
        if (node.fixed) return false;

        for (TopologicalSortLLP pred : node.predecessors) {
            if (!pred.fixed) return false;
        }

        node.value = node.predecessors.stream().mapToInt(n -> n.value).max().orElse(-1) + 1;
        node.fixed = true;
        return true;
    }

}