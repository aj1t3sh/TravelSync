package travelsync.co2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Suffix Array construction using prefix-doubling algorithm: O(N log^2 N) / O(N log N).
 * Supports binary search pattern matching in O(M log N).
 */
public class SuffixArray {

    private final String text;
    private final int[] suffixArray;

    private static class SuffixRank implements Comparable<SuffixRank> {
        int index;
        int rankPrimary;
        int rankSecondary;

        @Override
        public int compareTo(SuffixRank o) {
            if (this.rankPrimary != o.rankPrimary) {
                return Integer.compare(this.rankPrimary, o.rankPrimary);
            }
            return Integer.compare(this.rankSecondary, o.rankSecondary);
        }
    }

    public SuffixArray(String text) {
        this.text = text != null ? text : "";
        this.suffixArray = buildSuffixArray(this.text);
    }

    /**
     * Builds Suffix Array using prefix doubling in O(N log^2 N) time.
     */
    public static int[] buildSuffixArray(String s) {
        if (s == null || s.isEmpty()) return new int[0];
        int n = s.length();
        SuffixRank[] suffixes = new SuffixRank[n];

        for (int i = 0; i < n; i++) {
            suffixes[i] = new SuffixRank();
            suffixes[i].index = i;
            suffixes[i].rankPrimary = s.charAt(i);
            suffixes[i].rankSecondary = (i + 1 < n) ? s.charAt(i + 1) : -1;
        }

        Arrays.sort(suffixes);

        int[] ind = new int[n];
        for (int length = 4; length < 2 * n; length <<= 1) {
            int rank = 0;
            int prevRank = suffixes[0].rankPrimary;
            suffixes[0].rankPrimary = rank;
            ind[suffixes[0].index] = 0;

            for (int i = 1; i < n; i++) {
                if (suffixes[i].rankPrimary == prevRank &&
                    suffixes[i].rankSecondary == suffixes[i - 1].rankSecondary) {
                    prevRank = suffixes[i].rankPrimary;
                    suffixes[i].rankPrimary = rank;
                } else {
                    prevRank = suffixes[i].rankPrimary;
                    suffixes[i].rankPrimary = ++rank;
                }
                ind[suffixes[i].index] = i;
            }

            for (int i = 0; i < n; i++) {
                int nextIndex = suffixes[i].index + length / 2;
                suffixes[i].rankSecondary = (nextIndex < n) ? suffixes[ind[nextIndex]].rankPrimary : -1;
            }

            Arrays.sort(suffixes);
        }

        int[] sa = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = suffixes[i].index;
        }
        return sa;
    }

    public String getText() { return text; }
    public int[] getSuffixArray() { return suffixArray; }

    /**
     * Binary search for pattern occurrences in text using Suffix Array in O(M log N).
     */
    public List<Integer> search(String pattern) {
        List<Integer> result = new ArrayList<>();
        if (pattern == null || pattern.isEmpty() || suffixArray.length == 0) {
            return result;
        }

        int n = text.length();
        int m = pattern.length();

        // Find lower bound
        int low = 0, high = n - 1;
        int lowerBound = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int suffixStart = suffixArray[mid];
            int cmp = comparePrefix(text, suffixStart, pattern);
            if (cmp >= 0) {
                if (cmp == 0) lowerBound = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        if (lowerBound == -1) return result;

        // Find upper bound
        low = lowerBound;
        high = n - 1;
        int upperBound = lowerBound;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int suffixStart = suffixArray[mid];
            int cmp = comparePrefix(text, suffixStart, pattern);
            if (cmp == 0) {
                upperBound = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        for (int i = lowerBound; i <= upperBound; i++) {
            result.add(suffixArray[i]);
        }
        return result;
    }

    private static int comparePrefix(String text, int suffixStart, String pattern) {
        int m = pattern.length();
        int remaining = text.length() - suffixStart;
        int len = Math.min(m, remaining);

        for (int i = 0; i < len; i++) {
            char tc = text.charAt(suffixStart + i);
            char pc = pattern.charAt(i);
            if (tc != pc) {
                return Character.compare(tc, pc);
            }
        }
        if (remaining < m) {
            return -1; // text suffix is shorter than pattern
        }
        return 0; // matches pattern prefix
    }
}
