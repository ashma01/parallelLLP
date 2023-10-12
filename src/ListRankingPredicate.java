class ListRankingPredicate implements LatticePredicate<ListNode> {
    @Override
    public boolean evaluate(ListNode node) {
        if (node.next != null) {
            node.rank += (node.next.rank + 1);
            node.next = node.next.next;
        }
        return true;
    }
}
