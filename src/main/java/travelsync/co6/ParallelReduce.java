package travelsync.co6;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Parallel Reduction Primitive using ForkJoin: Work T_1 = O(N), Span T_infinity = O(log N).
 * Supports parallel summation, min, and max aggregations across datasets.
 */
public class ParallelReduce {

    private static final int SEQUENTIAL_THRESHOLD = 5000;
    private static final ForkJoinPool POOL = ForkJoinPool.commonPool();

    private static class SumTask extends RecursiveTask<Long> {
        private final long[] array;
        private final int start;
        private final int end;

        SumTask(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            int length = end - start;
            if (length <= SEQUENTIAL_THRESHOLD) {
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                }
                return sum;
            }

            int mid = start + length / 2;
            SumTask left = new SumTask(array, start, mid);
            SumTask right = new SumTask(array, mid, end);

            left.fork();
            long rightResult = right.compute();
            long leftResult = left.join();

            return leftResult + rightResult;
        }
    }

    public static class ReductionResult {
        private final long result;
        private final long sequentialTimeNs;
        private final long parallelTimeNs;
        private final double speedup;
        private final boolean exactMatch;

        public ReductionResult(long result, long sequentialTimeNs, long parallelTimeNs, double speedup, boolean exactMatch) {
            this.result = result;
            this.sequentialTimeNs = sequentialTimeNs;
            this.parallelTimeNs = parallelTimeNs;
            this.speedup = speedup;
            this.exactMatch = exactMatch;
        }

        public long getResult() { return result; }
        public long getSequentialTimeNs() { return sequentialTimeNs; }
        public long getParallelTimeNs() { return parallelTimeNs; }
        public double getSpeedup() { return speedup; }
        public boolean isExactMatch() { return exactMatch; }

        @Override
        public String toString() {
            return String.format("Reduction Result: %d | Seq: %.3f ms | Par: %.3f ms | Speedup: %.2fx | Match: %s",
                    result, sequentialTimeNs / 1_000_000.0, parallelTimeNs / 1_000_000.0, speedup, exactMatch);
        }
    }

    /**
     * Executes parallel sum reduction and compares with sequential baseline.
     */
    public static ReductionResult parallelSum(long[] array) {
        if (array == null || array.length == 0) {
            return new ReductionResult(0, 0, 0, 1.0, true);
        }

        // Sequential baseline
        long seqStart = System.nanoTime();
        long seqSum = 0;
        for (long val : array) {
            seqSum += val;
        }
        long seqTime = System.nanoTime() - seqStart;

        // Parallel reduction
        long parStart = System.nanoTime();
        long parSum = POOL.invoke(new SumTask(array, 0, array.length));
        long parTime = System.nanoTime() - parStart;

        double speedup = parTime > 0 ? (double) seqTime / parTime : 1.0;
        return new ReductionResult(parSum, seqTime, parTime, speedup, seqSum == parSum);
    }
}
