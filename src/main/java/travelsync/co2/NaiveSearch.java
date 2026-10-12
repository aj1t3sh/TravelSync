package travelsync.co2;

import java.util.ArrayList;
import java.util.List;

/**
 * Naïve Pattern Matching algorithm: O(N · M) worst-case baseline.
 * Counts character comparisons to demonstrate the need for linear-time algorithms.
 */
public class NaiveSearch {

    public static class MatchResult {
        private final List<Integer> matchIndices;
        private final long comparisons;
        private final long executionTimeNs;

        public MatchResult(List<Integer> matchIndices, long comparisons, long executionTimeNs) {
            this.matchIndices = matchIndices;
            this.comparisons = comparisons;
            this.executionTimeNs = executionTimeNs;
        }

        public List<Integer> getMatchIndices() { return matchIndices; }
        public long getComparisons() { return comparisons; }
        public long getExecutionTimeNs() { return executionTimeNs; }
        public int getMatchCount() { return matchIndices.size(); }
    }

    /**
     * Finds all occurrences of pattern inside text using Naïve brute force.
     */
    public static MatchResult search(String text, String pattern) {
        long start = System.nanoTime();
        List<Integer> indices = new ArrayList<>();
        long comparisons = 0;

        if (text == null || pattern == null || pattern.isEmpty() || pattern.length() > text.length()) {
            return new MatchResult(indices, 0, System.nanoTime() - start);
        }

        int n = text.length();
        int m = pattern.length();

        for (int i = 0; i <= n - m; i++) {
            int j = 0;
            while (j < m) {
                comparisons++;
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
                j++;
            }
            if (j == m) {
                indices.add(i);
            }
        }

        return new MatchResult(indices, comparisons, System.nanoTime() - start);
    }
}
