
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelLLP<T> {
    private int numThreads;
    private ExecutorService executor;

    public ParallelLLP(int numThreads) {
        this.numThreads = numThreads;
        this.executor = Executors.newFixedThreadPool(numThreads);
    }

    public boolean[] compute(List<T> elements, LatticePredicate<T> predicate) {
        int size = elements.size();
        boolean[] results = new boolean[size];
        Future<boolean[]>[] futures = new Future[numThreads];

        int chunkSize = size / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? size : start + chunkSize;

            futures[i] = executor.submit(() -> {
                boolean[] localResults = new boolean[end - start];
                for (int j = start; j < end; j++) {
                    localResults[j - start] = predicate.evaluate(elements.get(j));
                }
                return localResults;
            });
        }

        try {
            for (int i = 0; i < numThreads; i++) {
                boolean[] localResults = futures[i].get();
                System.arraycopy(localResults, 0, results, i * chunkSize, localResults.length);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return results;
    }

    public void shutdown() {
        executor.shutdown();
    }

}
