// Assuming you've provided the LatticePredicate and ParallelLLP code as mentioned previously

import java.util.Arrays;
import java.util.stream.IntStream;

public class ListRankingLLP {
    private final GNode[] G;
    private final int ROOT = -1;
    private final ParallelLLP<GNode> parallelLLP;

    public ListRankingLLP(int[] parent, int numThreads) {
        G = new GNode[parent.length];
        for (int i = 0; i < parent.length; i++) {
            G[i] = new GNode(1, parent[i]);
        }
        parallelLLP = new ParallelLLP<>(numThreads);
    }

    public void compute() {
        boolean[] forbiddenNodes;

        do {
            forbiddenNodes = parallelLLP.compute(Arrays.asList(G), new LLPRankingPredicate());

            boolean[] finalForbiddenNodes = forbiddenNodes;
            IntStream.range(0, G.length).filter(i -> finalForbiddenNodes[i]).forEach(i -> {
                G[i].dist += G[G[i].next].dist;
                G[i].next = G[G[i].next].next;
            });

        } while (containsTrue(forbiddenNodes));


    }

    private boolean containsTrue(boolean[] array) {
        for (boolean val : array) {
            if (val) return true;
        }
        return false;
    }


    public void printResults() {
        IntStream.range(0, G.length).forEach(i -> {
            System.out.println("Node Value " + i + ": Distance from Root: " + G[i].dist );
        });

    }

}

