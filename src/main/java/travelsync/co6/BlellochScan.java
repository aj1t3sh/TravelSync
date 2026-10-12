package travelsync.co6;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Blelloch Parallel Prefix Sum (Scan) Algorithm:
 * Work T_1 = O(N), Span T_infinity = O(log N).
 * Operates in two parallel tree traversal phases: Up-Sweep (Reduce) and Down-Sweep.
 */
public class BlellochScan {

    private static final ForkJoinPool POOL = ForkJoinPool.commonPool();
    private static final int PARALLEL_THRESHOLD = 2048;

    public static class ScanResult {
        private final long[] exclusivePrefixSum;
        private final long[] inclusivePrefixSum;
        private final long sequentialTimeNs;
        private final long parallelTimeNs;
        private final boolean exactMatch;

        public ScanResult(long[] exclusivePrefixSum, long[] inclusivePrefixSum,
                          long sequentialTimeNs, long parallelTimeNs, boolean exactMatch) {
            this.exclusivePrefixSum = exclusivePrefixSum;
            this.inclusivePrefixSum = inclusivePrefixSum;
            this.sequentialTimeNs = sequentialTimeNs;
            this.parallelTimeNs = parallelTimeNs;
            this.exactMatch = exactMatch;
        }

        public long[] getExclusivePrefixSum() { return exclusivePrefixSum; }
        public long[] getInclusivePrefixSum() { return inclusivePrefixSum; }
        public long getSequentialTimeNs() { return sequentialTimeNs; }
        public long getParallelTimeNs() { return parallelTimeNs; }
        public boolean isExactMatch() { return exactMatch; }

        @Override
        public String toString() {
            return String.format("Scan Elements: %d | Seq: %.3f ms | Par: %.3f ms | Match: %s",
                    inclusivePrefixSum.length, sequentialTimeNs / 1_000_000.0,
                    parallelTimeNs / 1_000_000.0, exactMatch);
        }
    }

    /**
     * Executes Blelloch Parallel Scan and verifies element-by-element against sequential scan.
     */
    public static ScanResult scan(long[] input) {
        if (input == null || input.length == 0) {
            return new ScanResult(new long[0], new long[0], 0, 0, true);
        }

        int origLen = input.length;

        // Sequential baseline
        long seqStart = System.nanoTime();
        long[] seqInclusive = new long[origLen];
        long running = 0;
        for (int i = 0; i < origLen; i++) {
            running += input[i];
            seqInclusive[i] = running;
        }
        long seqTime = System.nanoTime() - seqStart;

        // Parallel Blelloch Scan
        long parStart = System.nanoTime();
        int n = nextPowerOfTwo(origLen);
        long[] tree = new long[n];
        System.arraycopy(input, 0, tree, 0, origLen);

        // Phase 1: Up-Sweep (Parallel Reduce)
        for (int d = 0; (1 << d) < n; d++) {
            int step = 1 << (d + 1);
            int k = 1 << d;
            int count = n / step;
            POOL.invoke(new UpSweepTask(tree, 0, count, step, k));
        }

        // Set root to zero for exclusive scan
        tree[n - 1] = 0;

        // Phase 2: Down-Sweep
        for (int d = log2(n) - 1; d >= 0; d--) {
            int step = 1 << (d + 1);
            int k = 1 << d;
            int count = n / step;
            POOL.invoke(new DownSweepTask(tree, 0, count, step, k));
        }

        long[] exclusive = new long[origLen];
        long[] inclusive = new long[origLen];
        for (int i = 0; i < origLen; i++) {
            exclusive[i] = tree[i];
            inclusive[i] = tree[i] + input[i];
        }
        long parTime = System.nanoTime() - parStart;

        boolean matches = Arrays.equals(seqInclusive, inclusive);
        return new ScanResult(exclusive, inclusive, seqTime, parTime, matches);
    }

    private static class UpSweepTask extends RecursiveAction {
        private final long[] tree;
        private final int startIdx;
        private final int endIdx;
        private final int step;
        private final int k;

        UpSweepTask(long[] tree, int startIdx, int endIdx, int step, int k) {
            this.tree = tree;
            this.startIdx = startIdx;
            this.endIdx = endIdx;
            this.step = step;
            this.k = k;
        }

        @Override
        protected void compute() {
            if (endIdx - startIdx <= PARALLEL_THRESHOLD) {
                for (int i = startIdx; i < endIdx; i++) {
                    int idx = (i + 1) * step - 1;
                    tree[idx] += tree[idx - k];
                }
            } else {
                int mid = startIdx + (endIdx - startIdx) / 2;
                invokeAll(new UpSweepTask(tree, startIdx, mid, step, k),
                          new UpSweepTask(tree, mid, endIdx, step, k));
            }
        }
    }

    private static class DownSweepTask extends RecursiveAction {
        private final long[] tree;
        private final int startIdx;
        private final int endIdx;
        private final int step;
        private final int k;

        DownSweepTask(long[] tree, int startIdx, int endIdx, int step, int k) {
            this.tree = tree;
            this.startIdx = startIdx;
            this.endIdx = endIdx;
            this.step = step;
            this.k = k;
        }

        @Override
        protected void compute() {
            if (endIdx - startIdx <= PARALLEL_THRESHOLD) {
                for (int i = startIdx; i < endIdx; i++) {
                    int right = (i + 1) * step - 1;
                    int left = right - k;
                    long t = tree[left];
                    tree[left] = tree[right];
                    tree[right] += t;
                }
            } else {
                int mid = startIdx + (endIdx - startIdx) / 2;
                invokeAll(new DownSweepTask(tree, startIdx, mid, step, k),
                          new DownSweepTask(tree, mid, endIdx, step, k));
            }
        }
    }

    private static int nextPowerOfTwo(int x) {
        int highest = Integer.highestOneBit(x);
        return (x == highest) ? x : (highest << 1);
    }

    private static int log2(int x) {
        return 31 - Integer.numberOfLeadingZeros(x);
    }
}
