class LLPRankingPredicate implements LatticePredicate<ListRankingParallelLLP.Node> {
    @Override
    public boolean evaluate(ListRankingParallelLLP.Node node) {
        if (node.child != null && node.distance != -1 && node.child.distance == -1) {
            node.child.distance = node.distance + 1;
            return true;
        }
        return false;
    }
}
