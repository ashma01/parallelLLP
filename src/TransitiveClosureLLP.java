public class TransitiveClosureLLP implements LatticePredicate<Entry> {
    private boolean[][] G;

    public TransitiveClosureLLP(boolean[][] G) {
        this.G = G;
    }

//    @Override
//    public boolean evaluate(int[] cell) {
//        int i = cell[0];
//        int j = cell[1];
//
//        System.out.println("i= " + i + "j= " + j);
//
//        for (int k = 0; k < G.length; k++) {
//            if (G[i][k] && G[k][j]) {
//                G[i][j] = true;
//                return true;
//            }
//        }
//        return false;
//    }


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

//    boolean[][] A = {{false, true, false, false},
//            {false, false, true, false},
//            {false, false, false, true},
//            {false, false, false, false}};
