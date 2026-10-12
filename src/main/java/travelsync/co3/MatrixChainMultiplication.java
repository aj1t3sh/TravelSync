package travelsync.co3;

/**
 * Matrix Chain Multiplication using Interval Dynamic Programming: O(N^3) time, O(N^2) space.
 * Computes minimum scalar multiplications and optimal parenthesization order.
 */
public class MatrixChainMultiplication {

    public static class MCMResult {
        private final int minMultiplications;
        private final String optimalParenthesization;
        private final int[][] dpTable;
        private final int[][] splitTable;

        public MCMResult(int minMultiplications, String optimalParenthesization, int[][] dpTable, int[][] splitTable) {
            this.minMultiplications = minMultiplications;
            this.optimalParenthesization = optimalParenthesization;
            this.dpTable = dpTable;
            this.splitTable = splitTable;
        }

        public int getMinMultiplications() { return minMultiplications; }
        public String getOptimalParenthesization() { return optimalParenthesization; }
        public int[][] getDpTable() { return dpTable; }
        public int[][] getSplitTable() { return splitTable; }

        @Override
        public String toString() {
            return String.format("Min Multiplications: %d | Order: %s", minMultiplications, optimalParenthesization);
        }
    }

    /**
     * Solves Matrix Chain Multiplication.
     * @param p Array of dimensions such that Matrix i has dimension p[i-1] x p[i].
     */
    public static MCMResult solve(int[] p) {
        if (p == null || p.length < 2) {
            return new MCMResult(0, "", new int[0][0], new int[0][0]);
        }

        int n = p.length - 1; // Number of matrices
        int[][] m = new int[n + 1][n + 1];
        int[][] s = new int[n + 1][n + 1];

        for (int len = 2; len <= n; len++) {
            for (int i = 1; i <= n - len + 1; i++) {
                int j = i + len - 1;
                m[i][j] = Integer.MAX_VALUE;

                for (int k = i; k < j; k++) {
                    int q = m[i][k] + m[k + 1][j] + p[i - 1] * p[k] * p[j];
                    if (q < m[i][j]) {
                        m[i][j] = q;
                        s[i][j] = k;
                    }
                }
            }
        }

        String order = reconstructOrder(s, 1, n);
        return new MCMResult(m[1][n], order, m, s);
    }

    private static String reconstructOrder(int[][] s, int i, int j) {
        if (i == j) {
            return "M" + i;
        }
        return "(" + reconstructOrder(s, i, s[i][j]) + " x " + reconstructOrder(s, s[i][j] + 1, j) + ")";
    }
}
