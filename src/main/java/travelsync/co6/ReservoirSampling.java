package travelsync.co6;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Reservoir Sampling (Algorithm R): Uniform random sampling of K items from a streaming dataset
 * of unknown or unbounded length N in strictly O(N) time and O(K) space.
 */
public class ReservoirSampling {

    public static class SampleResult<T> {
        private final List<T> reservoir;
        private final int streamItemsProcessed;
        private final double selectionProbability;
        private final long executionTimeNs;

        public SampleResult(List<T> reservoir, int streamItemsProcessed, double selectionProbability, long executionTimeNs) {
            this.reservoir = reservoir;
            this.streamItemsProcessed = streamItemsProcessed;
            this.selectionProbability = selectionProbability;
            this.executionTimeNs = executionTimeNs;
        }

        public List<T> getReservoir() { return reservoir; }
        public int getStreamItemsProcessed() { return streamItemsProcessed; }
        public double getSelectionProbability() { return selectionProbability; }
        public long getExecutionTimeNs() { return executionTimeNs; }

        @Override
        public String toString() {
            return String.format("Reservoir Size: %d / %d processed (Uniform Prob: %.4f, Time: %.3f ms)",
                    reservoir.size(), streamItemsProcessed, selectionProbability, executionTimeNs / 1_000_000.0);
        }
    }

    /**
     * Performs reservoir sampling on an iterable stream of items.
     */
    public static <T> SampleResult<T> sample(Iterable<T> stream, int k) {
        return sample(stream, k, new Random());
    }

    public static <T> SampleResult<T> sample(Iterable<T> stream, int k, Random rng) {
        long start = System.nanoTime();
        List<T> reservoir = new ArrayList<>(k);
        int count = 0;

        for (T item : stream) {
            count++;
            if (reservoir.size() < k) {
                reservoir.add(item);
            } else {
                int j = rng.nextInt(count); // Random integer in [0, count - 1]
                if (j < k) {
                    reservoir.set(j, item);
                }
            }
        }

        double prob = count > 0 ? Math.min(1.0, (double) k / count) : 0.0;
        return new SampleResult<>(reservoir, count, prob, System.nanoTime() - start);
    }
}
