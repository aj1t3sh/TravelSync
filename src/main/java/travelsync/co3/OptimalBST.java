package travelsync.co3;

/**
 * Optimal Binary Search Tree (Optimal BST) using Interval Dynamic Programming: O(N^3) / O(N^2).
 * Minimizes expected search cost given key access probabilities and dummy failure probabilities.
 */
public class OptimalBST {

    public static class OBSTResult {
        private final double minCost;
        private final double[][] e;
        private final double[][] w;
        private final int[][] root;

        public OBSTResult(double minCost, double[][] e, double[][] w, int[][] root) {
            this.minCost = minCost;
            this.e = e;
            this.w = w;
            this.root = root;
        }

        public double getMinCost() { return minCost; }
        public int[][] getRoot() { return root; }

        @Override
        public String toString() {
            return String.format("Optimal BST Expected Search Cost: %.4f", minCost);
        }
    }

    /**
     * Solves the Optimal BST problem using standard CLRS tabulation.
     * @param p Array of key probabilities (1-indexed, p[1..n])
     * @param q Array of dummy key probabilities (0-indexed, q[0..n])
     * @param n Number of keys
     */
    public static OBSTResult solve(double[] p, double[] q, int n) {
        double[][] e = new double[n + 2][n + 1];
        double[][] w = new double[n + 2][n + 1];
        int[][] root = new int[n + 1][n + 1];

        for (int i = 1; i <= n + 1; i++) {
            e[i][i - 1] = q[i - 1];
            w[i][i - 1] = q[i - 1];
        }

        for (int l = 1; l <= n; l++) {
            for (int i = 1; i <= n - l + 1; i++) {
                int j = i + l - 1;
                e[i][j] = Double.MAX_VALUE;
                w[i][j] = w[i][j - 1] + p[j] + q[j];

                for (int r = i; r <= j; r++) {
                    double t = e[i][r - 1] + e[r + 1][j] + w[i][j];
                    if (t < e[i][j]) {
                        e[i][j] = t;
                        root[i][j] = r;
                    }
                }
            }
        }

        return new OBSTResult(e[1][n], e, w, root);
    }
}
