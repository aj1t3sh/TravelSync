package travelsync.co4;

import java.util.ArrayList;
import java.util.List;

/**
 * Maximum Bipartite Matching via Flow Network Reduction and König's Duality.
 * Solves assignment problems (e.g., traveler groups to hotel rooms or local tour guides).
 */
public class BipartiteMatching {

    public static class MatchPair {
        private final String leftItem;
        private final String rightItem;

        public MatchPair(String leftItem, String rightItem) {
            this.leftItem = leftItem;
            this.rightItem = rightItem;
        }

        public String getLeftItem() { return leftItem; }
        public String getRightItem() { return rightItem; }

        @Override
        public String toString() {
            return String.format("%s <===> %s", leftItem, rightItem);
        }
    }

    public static class MatchingResult {
        private final int maxMatches;
        private final List<MatchPair> matchedPairs;
        private final int leftSize;
        private final int rightSize;
        private final int konigMinVertexCoverSize;

        public MatchingResult(int maxMatches, List<MatchPair> matchedPairs, int leftSize, int rightSize) {
            this.maxMatches = maxMatches;
            this.matchedPairs = matchedPairs;
            this.leftSize = leftSize;
            this.rightSize = rightSize;
            this.konigMinVertexCoverSize = maxMatches; // By König's Theorem: |Max Matching| == |Min Vertex Cover|
        }

        public int getMaxMatches() { return maxMatches; }
        public List<MatchPair> getMatchedPairs() { return matchedPairs; }
        public int getLeftSize() { return leftSize; }
        public int getRightSize() { return rightSize; }
        public int getKonigMinVertexCoverSize() { return konigMinVertexCoverSize; }

        @Override
        public String toString() {
            return String.format("Max Matches: %d (Left: %d, Right: %d) | König's Min Vertex Cover: %d",
                    maxMatches, leftSize, rightSize, konigMinVertexCoverSize);
        }
    }

    /**
     * Computes Maximum Bipartite Matching.
     * @param leftNames Labels for left partition (e.g., Tourist Groups)
     * @param rightNames Labels for right partition (e.g., Hotel Rooms / Guides)
     * @param compatibility Matrix where compat[i][j] is true if left i is compatible with right j
     */
    public static MatchingResult computeMatching(List<String> leftNames, List<String> rightNames, boolean[][] compatibility) {
        int nL = leftNames.size();
        int nR = rightNames.size();

        if (nL == 0 || nR == 0) {
            return new MatchingResult(0, new ArrayList<>(), nL, nR);
        }

        int source = 0;
        int sink = nL + nR + 1;
        FlowNetwork network = new FlowNetwork(sink + 1);

        // Connect source to left partition
        for (int i = 0; i < nL; i++) {
            network.addEdge(source, i + 1, 1);
        }

        // Connect left partition to compatible right partition
        for (int i = 0; i < nL; i++) {
            for (int j = 0; j < nR; j++) {
                if (compatibility[i][j]) {
                    network.addEdge(i + 1, nL + 1 + j, 1);
                }
            }
        }

        // Connect right partition to sink
        for (int j = 0; j < nR; j++) {
            network.addEdge(nL + 1 + j, sink, 1);
        }

        EdmondsKarp.FlowResult flowRes = EdmondsKarp.computeMaxFlow(network, source, sink);
        int maxMatches = flowRes.getMaxFlow();

        List<MatchPair> pairs = new ArrayList<>();
        for (int i = 0; i < nL; i++) {
            int u = i + 1;
            for (FlowNetwork.Edge edge : network.getAdjacencyList().get(u)) {
                if (edge.v > nL && edge.v <= nL + nR && edge.flow == 1) {
                    int rightIndex = edge.v - (nL + 1);
                    pairs.add(new MatchPair(leftNames.get(i), rightNames.get(rightIndex)));
                }
            }
        }

        return new MatchingResult(maxMatches, pairs, nL, nR);
    }
}
