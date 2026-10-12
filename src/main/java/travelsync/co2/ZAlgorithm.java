package travelsync.co2;

import java.util.ArrayList;
import java.util.List;

/**
 * Z-Algorithm: Modern linear-time alternative to KMP.
 * Computes the Z-array in O(N) time and performs pattern search in O(N + M).
 */
public class ZAlgorithm {

    public static class ZResult {
        private final List<Integer> matchIndices;
        private final int[] zArray;
        private final long comparisons;
        private final long executionTimeNs;

        public ZResult(List<Integer> matchIndices, int[] zArray, long comparisons, long executionTimeNs) {
            this.matchIndices = matchIndices;
            this.zArray = zArray;
            this.comparisons = comparisons;
            this.executionTimeNs = executionTimeNs;
        }

        public List<Integer> getMatchIndices() { return matchIndices; }
        public int[] getZArray() { return zArray; }
        public long getComparisons() { return comparisons; }
        public long getExecutionTimeNs() { return executionTimeNs; }
        public int getMatchCount() { return matchIndices.size(); }
    }

    /**
     * Computes the Z-array for a string S in O(|S|) linear time.
     * Z[i] is the length of the longest common prefix between S and the suffix starting at S[i].
     */
    public static int[] computeZArray(String s) {
        if (s == null || s.isEmpty()) return new int[0];
        int n = s.length();
        int[] z = new int[n];
        int l = 0, r = 0;

        for (int i = 1; i < n; i++) {
            if (i <= r) {
                z[i] = Math.min(r - i + 1, z[i - l]);
            }
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) {
                z[i]++;
            }
            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }
        return z;
    }

    /**
     * Searches for pattern in text using the Z-algorithm with concatenation S = P + '$' + T.
     */
    public static ZResult search(String text, String pattern) {
        long start = System.nanoTime();
        List<Integer> indices = new ArrayList<>();
        long comparisons = 0;

        if (text == null || pattern == null || pattern.isEmpty() || pattern.length() > text.length()) {
            return new ZResult(indices, new int[0], 0, System.nanoTime() - start);
        }

        char delimiter = '\u0000';
        String concat = pattern + delimiter + text;
        int concatLen = concat.length();
        int patLen = pattern.length();

        int[] z = new int[concatLen];
        int l = 0, r = 0;

        for (int i = 1; i < concatLen; i++) {
            if (i <= r) {
                z[i] = Math.min(r - i + 1, z[i - l]);
            }
            while (i + z[i] < concatLen) {
                comparisons++;
                if (concat.charAt(z[i]) == concat.charAt(i + z[i])) {
                    z[i]++;
                } else {
                    break;
                }
            }
            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }

            if (z[i] == patLen) {
                int textIndex = i - patLen - 1;
                if (textIndex >= 0) {
                    indices.add(textIndex);
                }
            }
        }

        return new ZResult(indices, z, comparisons, System.nanoTime() - start);
    }
}
