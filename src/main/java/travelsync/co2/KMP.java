package travelsync.co2;

import java.util.ArrayList;
import java.util.List;

/**
 * Knuth-Morris-Pratt (KMP) linear-time pattern matching algorithm: O(N + M).
 * Computes the Longest Proper Prefix which is also Suffix (LPS / failure function).
 */
public class KMP {

    public static class KMPResult {
        private final List<Integer> matchIndices;
        private final int[] lpsArray;
        private final long comparisons;
        private final long executionTimeNs;

        public KMPResult(List<Integer> matchIndices, int[] lpsArray, long comparisons, long executionTimeNs) {
            this.matchIndices = matchIndices;
            this.lpsArray = lpsArray;
            this.comparisons = comparisons;
            this.executionTimeNs = executionTimeNs;
        }

        public List<Integer> getMatchIndices() { return matchIndices; }
        public int[] getLpsArray() { return lpsArray; }
        public long getComparisons() { return comparisons; }
        public long getExecutionTimeNs() { return executionTimeNs; }
        public int getMatchCount() { return matchIndices.size(); }
    }

    /**
     * Computes the LPS (Longest Prefix Suffix) failure array in O(M) time.
     */
    public static int[] computeLPS(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            return new int[0];
        }
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    /**
     * Searches for all occurrences of pattern in text using KMP.
     */
    public static KMPResult search(String text, String pattern) {
        long start = System.nanoTime();
        List<Integer> indices = new ArrayList<>();
        long comparisons = 0;

        if (text == null || pattern == null || pattern.isEmpty() || pattern.length() > text.length()) {
            return new KMPResult(indices, new int[0], 0, System.nanoTime() - start);
        }

        int[] lps = computeLPS(pattern);
        int n = text.length();
        int m = pattern.length();
        int i = 0; // index for text
        int j = 0; // index for pattern

        while (i < n) {
            comparisons++;
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }

            if (j == m) {
                indices.add(i - j);
                j = lps[j - 1];
            } else if (i < n && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        return new KMPResult(indices, lps, comparisons, System.nanoTime() - start);
    }
}
