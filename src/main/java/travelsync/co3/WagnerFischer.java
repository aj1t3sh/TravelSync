package travelsync.co3;

/**
 * Wagner-Fischer Dynamic Programming algorithm for string metrics:
 * 1. Levenshtein Distance (Insert, Delete, Substitute)
 * 2. Damerau-Levenshtein Distance (Insert, Delete, Substitute, Transposition)
 * 3. Weighted Edit Distance with custom operation costs.
 */
public class WagnerFischer {

    /**
     * Standard Levenshtein Distance in O(N · M) time and O(min(N,M)) space.
     */
    public static int levenshteinDistance(String s1, String s2) {
        if (s1 == null) s1 = "";
        if (s2 == null) s2 = "";

        int n = s1.length();
        int m = s2.length();

        if (n == 0) return m;
        if (m == 0) return n;

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int j = 0; j <= m; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            for (int j = 1; j <= m; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(
                    curr[j - 1] + 1, // Insertion
                    Math.min(
                        prev[j] + 1, // Deletion
                        prev[j - 1] + cost // Substitution
                    )
                );
            }
            System.arraycopy(curr, 0, prev, 0, m + 1);
        }

        return prev[m];
    }

    /**
     * Damerau-Levenshtein Distance supporting adjacent character transpositions.
     */
    public static int damerauLevenshteinDistance(String s1, String s2) {
        if (s1 == null) s1 = "";
        if (s2 == null) s2 = "";

        int n = s1.length();
        int m = s2.length();

        if (n == 0) return m;
        if (m == 0) return n;

        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;

                dp[i][j] = Math.min(
                    dp[i - 1][j] + 1, // Deletion
                    Math.min(
                        dp[i][j - 1] + 1, // Insertion
                        dp[i - 1][j - 1] + cost // Substitution
                    )
                );

                // Check transposition
                if (i > 1 && j > 1 &&
                    s1.charAt(i - 1) == s2.charAt(j - 2) &&
                    s1.charAt(i - 2) == s2.charAt(j - 1)) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 2][j - 2] + 1);
                }
            }
        }

        return dp[n][m];
    }

    /**
     * Weighted Edit Distance with custom costs for Insert, Delete, and Substitute.
     */
    public static int weightedEditDistance(String s1, String s2, int insertCost, int deleteCost, int subCost) {
        if (s1 == null) s1 = "";
        if (s2 == null) s2 = "";

        int n = s1.length();
        int m = s2.length();

        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i * deleteCost;
        for (int j = 0; j <= m; j++) dp[0][j] = j * insertCost;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : subCost;
                dp[i][j] = Math.min(
                    dp[i - 1][j] + deleteCost,
                    Math.min(
                        dp[i][j - 1] + insertCost,
                        dp[i - 1][j - 1] + cost
                    )
                );
            }
        }

        return dp[n][m];
    }

    /**
     * Computes similarity score between 0.0 and 1.0 based on Levenshtein distance.
     */
    public static double similarityScore(String s1, String s2) {
        int maxLen = Math.max(s1.length(), s2.length());
        if (maxLen == 0) return 1.0;
        int dist = levenshteinDistance(s1, s2);
        return 1.0 - ((double) dist / maxLen);
    }
}
