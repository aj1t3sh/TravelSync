package travelsync.co5;

/**
 * Polynomial-Time Reduction: INDEPENDENT SET <=_p VERTEX COVER.
 * In any graph G = (V, E), S is an Independent Set iff V \\ S is a Vertex Cover.
 * Therefore, G has an Independent Set of size k iff G has a Vertex Cover of size |V| - k.
 */
public class IndependentSetToVertexCover {

    public static class VCResult {
        public final boolean[][] adjMatrix;
        public final int targetVCSize;
        public final int totalVertices;

        public VCResult(boolean[][] adjMatrix, int targetVCSize, int totalVertices) {
            this.adjMatrix = adjMatrix;
            this.targetVCSize = targetVCSize;
            this.totalVertices = totalVertices;
        }
    }

    /**
     * Translates the target Independent Set parameter k to target Vertex Cover size (|V| - k).
     */
    public static VCResult reduce(boolean[][] adjMatrix, int targetISSize) {
        int n = adjMatrix.length;
        int targetVC = n - targetISSize;
        return new VCResult(adjMatrix, targetVC, n);
    }
}
