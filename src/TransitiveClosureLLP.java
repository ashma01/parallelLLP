public class TransitiveClosureLLP implements LatticePredicate<Entry> {
    private boolean[][] G;

    public TransitiveClosureLLP(boolean[][] G) {
        this.G = G;
    }


    @Override
    public boolean evaluate(Entry entry) {

        if (G[entry.i][entry.j]) return false;  // If it's already true, no need to evaluate further
        for (int k = 0; k < G.length; k++) {
            if (G[entry.i][k] && G[k][entry.j]) {
                G[entry.i][entry.j] = true;
                return true;  // Return true if we make an update
            }
        }
        return false;  // Return false if no update was made
    }
}

