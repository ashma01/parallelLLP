import java.util.List;

public class NodeAdvancer implements LatticePredicate<Node> {
    private List<Node> nodeList;

    public NodeAdvancer(List<Node> nodeList) {
        this.nodeList = nodeList;
    }

    @Override
    public boolean evaluate(Node node) {
        if (node.next >= 0 && node.next < nodeList.size()) {
            Node nextNode = nodeList.get(node.next);
            node.dist += nextNode.dist;
            node.next = nextNode.next;
        }
        return true;
    }
}
