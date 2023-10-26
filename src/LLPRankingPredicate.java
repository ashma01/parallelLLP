class LLPRankingPredicate implements LatticePredicate<GNode> {
    private final int ROOT = -1;

    @Override
    public boolean evaluate(GNode element) {
        return element.next != ROOT;
    }
}
