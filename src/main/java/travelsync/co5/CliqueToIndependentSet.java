package travelsync.co5;

/**
 * Polynomial-Time Reduction: CLIQUE <=_p INDEPENDENT SET.
 * A graph G has a clique of size k if and only if its complement graph G_bar has an independent set of size k.
 */
public class CliqueToIndependentSet {

    public static class ISResult {
        public final boolean[][] complementAdjMatrix;
        public final int targetK;
        public final int vertexCount;

        public ISResult(boolean[][] complementAdjMatrix, int targetK, int vertexCount) {
            this.complementAdjMatrix = complementAdjMatrix;
            this.targetK = targetK;
            this.vertexCount = vertexCount;
        }
    }

    /**
     * Constructs the complement graph in O(V^2) polynomial time.
     */
    public static ISResult reduce(boolean[][] cliqueAdjMatrix, int targetK) {
        int n = cliqueAdjMatrix.length;
        boolean[][] complement = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    complement[i][j] = !cliqueAdjMatrix[i][j];
                }
            }
        }

        return new ISResult(complement, targetK, n);
    }
}
