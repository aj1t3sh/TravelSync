package travelsync.co5;

import java.util.ArrayList;
import java.util.List;

/**
 * Exact Exponential Solver for Minimum Vertex Cover.
 * Enumerates vertex subsets or uses branch-and-bound to find the mathematically optimal vertex cover.
 */
public class ExactVertexCover {

    public static class ExactVCResult {
        private final int minCoverSize;
        private final List<Integer> coverVertices;
        private final long statesExplored;
        private final long executionTimeNs;

        public ExactVCResult(int minCoverSize, List<Integer> coverVertices, long statesExplored, long executionTimeNs) {
            this.minCoverSize = minCoverSize;
            this.coverVertices = coverVertices;
            this.statesExplored = statesExplored;
            this.executionTimeNs = executionTimeNs;
        }

        public int getMinCoverSize() { return minCoverSize; }
        public List<Integer> getCoverVertices() { return coverVertices; }
        public long getStatesExplored() { return statesExplored; }
        public long getExecutionTimeNs() { return executionTimeNs; }

        @Override
        public String toString() {
            return String.format("Exact OPT Size: %d | Vertices: %s (Explored %d states in %.2f ms)",
                    minCoverSize, coverVertices, statesExplored, executionTimeNs / 1_000_000.0);
        }
    }

    /**
     * Finds the minimum vertex cover via subset search for N <= 25.
     */
    public static ExactVCResult solveExact(int n, boolean[][] adj) {
        long start = System.nanoTime();
        if (n == 0) return new ExactVCResult(0, new ArrayList<>(), 0, System.nanoTime() - start);

        List<int[]> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (adj[i][j]) {
                    edges.add(new int[]{i, j});
                }
            }
        }

        if (edges.isEmpty()) {
            return new ExactVCResult(0, new ArrayList<>(), 1, System.nanoTime() - start);
        }

        long states = 0;
        int limit = 1 << n;
        int bestSize = n;
        int bestMask = (1 << n) - 1;

        for (int mask = 0; mask < limit; mask++) {
            states++;
            int size = Integer.bitCount(mask);
            if (size >= bestSize) continue;

            boolean coversAll = true;
            for (int[] edge : edges) {
                int u = edge[0];
                int v = edge[1];
                if ((mask & (1 << u)) == 0 && (mask & (1 << v)) == 0) {
                    coversAll = false;
                    break;
                }
            }

            if (coversAll) {
                bestSize = size;
                bestMask = mask;
            }
        }

        List<Integer> cover = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if ((bestMask & (1 << i)) != 0) {
                cover.add(i);
            }
        }

        return new ExactVCResult(bestSize, cover, states, System.nanoTime() - start);
    }
}
