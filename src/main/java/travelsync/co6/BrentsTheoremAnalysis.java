package travelsync.co6;

/**
 * Work-Span Analysis & Brent's Theorem Evaluation Framework.
 * Computes theoretical bounds and empirical multi-core speedup statistics.
 */
public class BrentsTheoremAnalysis {

    public static class WorkSpanMetrics {
        public final long workT1;         // Total sequential operations
        public final long spanTInfinity;   // Critical path length
        public final int numProcessors;    // Available CPU cores
        public final double maxTheoreticalSpeedup; // T1 / T_infinity
        public final double brentEstimatedTime;    // (T1 - T_inf) / p + T_inf
        public final double brentEstimatedSpeedup; // T1 / brentTime

        public WorkSpanMetrics(long workT1, long spanTInfinity, int numProcessors) {
            this.workT1 = workT1;
            this.spanTInfinity = spanTInfinity;
            this.numProcessors = numProcessors;
            this.maxTheoreticalSpeedup = spanTInfinity > 0 ? (double) workT1 / spanTInfinity : 1.0;
            this.brentEstimatedTime = ((double) (workT1 - spanTInfinity) / numProcessors) + spanTInfinity;
            this.brentEstimatedSpeedup = brentEstimatedTime > 0 ? (double) workT1 / brentEstimatedTime : 1.0;
        }

        @Override
        public String toString() {
            return String.format("Work T1: %d | Span T_inf: %d | Cores: %d | Max Speedup: %.2fx | Brent Est Speedup: %.2fx",
                    workT1, spanTInfinity, numProcessors, maxTheoreticalSpeedup, brentEstimatedSpeedup);
        }
    }

    public static WorkSpanMetrics evaluateWorkSpan(long n, int processors) {
        // For balanced binary tree parallel reduction / scan:
        // Work T1 = 2 * N (linear)
        // Span T_inf = 2 * ceil(log2(N))
        long work = 2 * n;
        long span = (long) (2 * Math.ceil(Math.log(Math.max(2, n)) / Math.log(2)));
        return new WorkSpanMetrics(work, span, processors);
    }

    public static class BenchmarkSummary {
        public final int size;
        public final double avgSeqMs;
        public final double avgParMs;
        public final double empiricalSpeedup;

        public BenchmarkSummary(int size, double avgSeqMs, double avgParMs, double empiricalSpeedup) {
            this.size = size;
            this.avgSeqMs = avgSeqMs;
            this.avgParMs = avgParMs;
            this.empiricalSpeedup = empiricalSpeedup;
        }

        @Override
        public String toString() {
            return String.format("N = %-8d | Sequential: %7.3f ms | Parallel: %7.3f ms | Speedup: %5.2fx",
                    size, avgSeqMs, avgParMs, empiricalSpeedup);
        }
    }

    /**
     * Runs sequential vs parallel reduction benchmarks across multiple iterations.
     */
    public static BenchmarkSummary runBenchmark(int size, int iterations) {
        long[] data = new long[size];
        for (int i = 0; i < size; i++) {
            data[i] = (i % 100) + 1;
        }

        // Warmup
        for (int i = 0; i < 3; i++) {
            ParallelReduce.parallelSum(data);
        }

        long totalSeqNs = 0;
        long totalParNs = 0;

        for (int i = 0; i < iterations; i++) {
            ParallelReduce.ReductionResult res = ParallelReduce.parallelSum(data);
            totalSeqNs += res.getSequentialTimeNs();
            totalParNs += res.getParallelTimeNs();
        }

        double avgSeqMs = (totalSeqNs / (double) iterations) / 1_000_000.0;
        double avgParMs = (totalParNs / (double) iterations) / 1_000_000.0;
        double speedup = avgParMs > 0 ? avgSeqMs / avgParMs : 1.0;

        return new BenchmarkSummary(size, avgSeqMs, avgParMs, speedup);
    }
}
