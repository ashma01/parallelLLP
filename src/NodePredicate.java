public class NodePredicate implements LatticePredicate<Node> {
    private final int rootIndex;

    public NodePredicate(int rootIndex) {
        this.rootIndex = rootIndex;
    }

    @Override
    public boolean evaluate(Node node) {
        return node.next != rootIndex && node.next != -1;
    }
}