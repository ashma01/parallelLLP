public class TransitiveClosureLLP implements LatticePredicate<Entry> {
    private final int[][] G;

    public TransitiveClosureLLP(int[][] G) {
        this.G = G;
    }


    @Override
    public boolean evaluate(Entry entry) {

        synchronized(G) {
            if (G[entry.i][entry.j] == 1) return false;

            for (int k = 0; k < G.length; k++) {
                if (G[entry.i][k] == 1 && G[k][entry.j] == 1) {
                    return true;
                }
            }
            return false;
        }
    }
}
