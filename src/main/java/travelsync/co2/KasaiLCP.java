package travelsync.co2;

/**
 * Kasai's Algorithm for computing Longest Common Prefix (LCP) array in O(N) linear time.
 * Supports Longest Common Substring (LCS) detection and document similarity analysis.
 */
public class KasaiLCP {

    public static class LCPResult {
        private final int[] lcpArray;
        private final int maxLcp;
        private final int maxLcpIndex;

        public LCPResult(int[] lcpArray, int maxLcp, int maxLcpIndex) {
            this.lcpArray = lcpArray;
            this.maxLcp = maxLcp;
            this.maxLcpIndex = maxLcpIndex;
        }

        public int[] getLcpArray() { return lcpArray; }
        public int getMaxLcp() { return maxLcp; }
        public int getMaxLcpIndex() { return maxLcpIndex; }
    }

    public static class CommonSubstringResult {
        private final String longestCommonSubstring;
        private final int length;
        private final double jaccardSimilarity;

        public CommonSubstringResult(String longestCommonSubstring, int length, double jaccardSimilarity) {
            this.longestCommonSubstring = longestCommonSubstring;
            this.length = length;
            this.jaccardSimilarity = jaccardSimilarity;
        }

        public String getLongestCommonSubstring() { return longestCommonSubstring; }
        public int getLength() { return length; }
        public double getJaccardSimilarity() { return jaccardSimilarity; }
    }

    /**
     * Constructs the LCP array in strictly O(N) time using Kasai's algorithm.
     * @param text Original string
     * @param sa Suffix Array of the text
     * @return LCP array where lcp[i] = LCP(suffix[sa[i]], suffix[sa[i-1]])
     */
    public static int[] computeLCP(String text, int[] sa) {
        if (text == null || sa == null || sa.length == 0) return new int[0];
        int n = text.length();
        int[] lcp = new int[n];
        int[] rank = new int[n];

        for (int i = 0; i < n; i++) {
            rank[sa[i]] = i;
        }

        int h = 0;
        for (int i = 0; i < n; i++) {
            if (rank[i] > 0) {
                int j = sa[rank[i] - 1];
                while (i + h < n && j + h < n && text.charAt(i + h) == text.charAt(j + h)) {
                    h++;
                }
                lcp[rank[i]] = h;
                if (h > 0) {
                    h--;
                }
            } else {
                lcp[0] = 0;
            }
        }
        return lcp;
    }

    /**
     * Finds the Longest Common Substring between two documents using Suffix Array + Kasai's LCP.
     */
    public static CommonSubstringResult findLongestCommonSubstring(String s1, String s2) {
        if (s1 == null || s2 == null || s1.isEmpty() || s2.isEmpty()) {
            return new CommonSubstringResult("", 0, 0.0);
        }

        char delim1 = '#';
        char delim2 = '$';
        String combined = s1 + delim1 + s2 + delim2;
        int len1 = s1.length();

        int[] sa = SuffixArray.buildSuffixArray(combined);
        int[] lcp = computeLCP(combined, sa);

        int maxLen = 0;
        int bestSaIndex = -1;

        for (int i = 1; i < combined.length(); i++) {
            boolean fromS1 = sa[i] < len1;
            boolean prevFromS1 = sa[i - 1] < len1;

            boolean fromS2 = sa[i] > len1 && sa[i] < combined.length() - 1;
            boolean prevFromS2 = sa[i - 1] > len1 && sa[i - 1] < combined.length() - 1;

            if ((fromS1 && prevFromS2) || (fromS2 && prevFromS1)) {
                if (lcp[i] > maxLen) {
                    maxLen = lcp[i];
                    bestSaIndex = sa[i];
                }
            }
        }

        String lcs = (bestSaIndex != -1 && maxLen > 0) ? combined.substring(bestSaIndex, bestSaIndex + maxLen) : "";
        double similarity = (2.0 * maxLen) / (s1.length() + s2.length());

        return new CommonSubstringResult(lcs, maxLen, Math.min(1.0, similarity));
    }
}
