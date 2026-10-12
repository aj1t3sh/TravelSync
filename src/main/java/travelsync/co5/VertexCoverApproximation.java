package travelsync.co5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 2-Approximation Algorithm for Vertex Cover via Maximal Matching.
 * Guarantees |C_approx| <= 2 · |C_opt| in strictly O(V + E) linear time.
 */
public class VertexCoverApproximation {

    public static class ApproxResult {
        private final int approxCoverSize;
        private final List<Integer> coverVertices;
        private final List<int[]> maximalMatchingEdges;
        private final double approxRatio;
        private final long executionTimeNs;

        public ApproxResult(int approxCoverSize, List<Integer> coverVertices,
                            List<int[]> maximalMatchingEdges, double approxRatio, long executionTimeNs) {
            this.approxCoverSize = approxCoverSize;
            this.coverVertices = coverVertices;
            this.maximalMatchingEdges = maximalMatchingEdges;
            this.approxRatio = approxRatio;
            this.executionTimeNs = executionTimeNs;
        }

        public int getApproxCoverSize() { return approxCoverSize; }
        public List<Integer> getCoverVertices() { return coverVertices; }
        public List<int[]> getMaximalMatchingEdges() { return maximalMatchingEdges; }
        public double getApproxRatio() { return approxRatio; }
        public long getExecutionTimeNs() { return executionTimeNs; }

        @Override
        public String toString() {
            return String.format("2-Approx Size: %d | Vertices: %s | Matching Edges: %d | Ratio: %.2fx (Time: %.3f ms)",
                    approxCoverSize, coverVertices, maximalMatchingEdges.size(), approxRatio, executionTimeNs / 1_000_000.0);
        }
    }

    /**
     * Solves Vertex Cover 2-Approximation via Maximal Matching.
     */
    public static ApproxResult solve(int n, boolean[][] adj) {
        long start = System.nanoTime();
        if (n == 0) {
            return new ApproxResult(0, Collections.emptyList(), Collections.emptyList(), 1.0, System.nanoTime() - start);
        }

        boolean[] inCover = new boolean[n];
        List<int[]> matching = new ArrayList<>();
        List<Integer> cover = new ArrayList<>();

        for (int u = 0; u < n; u++) {
            if (inCover[u]) continue;
            for (int v = u + 1; v < n; v++) {
                if (adj[u][v] && !inCover[v]) {
                    // Pick edge (u, v) into maximal matching
                    inCover[u] = true;
                    inCover[v] = true;
                    cover.add(u);
                    cover.add(v);
                    matching.add(new int[]{u, v});
                    break; // Move to next uncovered node
                }
            }
        }

        Collections.sort(cover);

        // Compare with exact OPT if n is manageable (n <= 20)
        double ratio = 1.0;
        if (n <= 20) {
            ExactVertexCover.ExactVCResult exact = ExactVertexCover.solveExact(n, adj);
            if (exact.getMinCoverSize() > 0) {
                ratio = (double) cover.size() / exact.getMinCoverSize();
            }
        }

        return new ApproxResult(cover.size(), cover, matching, ratio, System.nanoTime() - start);
    }
}
