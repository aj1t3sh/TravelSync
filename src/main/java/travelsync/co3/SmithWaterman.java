package travelsync.co3;

/**
 * Smith-Waterman algorithm for Local Sequence Alignment.
 * Identifies the highest-scoring local matching sub-segments between two sequences.
 */
public class SmithWaterman {

    public static class LocalAlignmentResult {
        private final int maxScore;
        private final String alignedSub1;
        private final String alignedSub2;
        private final int start1, end1;
        private final int start2, end2;

        public LocalAlignmentResult(int maxScore, String alignedSub1, String alignedSub2,
                                    int start1, int end1, int start2, int end2) {
            this.maxScore = maxScore;
            this.alignedSub1 = alignedSub1;
            this.alignedSub2 = alignedSub2;
            this.start1 = start1;
            this.end1 = end1;
            this.start2 = start2;
            this.end2 = end2;
        }

        public int getMaxScore() { return maxScore; }
        public String getAlignedSub1() { return alignedSub1; }
        public String getAlignedSub2() { return alignedSub2; }
        public int getStart1() { return start1; }
        public int getEnd1() { return end1; }
        public int getStart2() { return start2; }
        public int getEnd2() { return end2; }

        @Override
        public String toString() {
            return String.format("Local Max Score: %d | Sub1 [%d..%d]: %s | Sub2 [%d..%d]: %s",
                    maxScore, start1, end1, alignedSub1, start2, end2, alignedSub2);
        }
    }

    /**
     * Computes local alignment matrix and backtracks from the maximum scoring cell.
     */
    public static LocalAlignmentResult align(String seq1, String seq2, int matchScore, int mismatchPenalty, int gapPenalty) {
        if (seq1 == null) seq1 = "";
        if (seq2 == null) seq2 = "";

        int n = seq1.length();
        int m = seq2.length();

        int[][] dp = new int[n + 1][m + 1];
        int maxScore = 0;
        int maxI = 0, maxJ = 0;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int scoreMatch = (seq1.charAt(i - 1) == seq2.charAt(j - 1)) ? matchScore : mismatchPenalty;
                dp[i][j] = Math.max(0,
                    Math.max(
                        dp[i - 1][j - 1] + scoreMatch,
                        Math.max(
                            dp[i - 1][j] + gapPenalty,
                            dp[i][j - 1] + gapPenalty
                        )
                    )
                );

                if (dp[i][j] > maxScore) {
                    maxScore = dp[i][j];
                    maxI = i;
                    maxJ = j;
                }
            }
        }

        if (maxScore == 0) {
            return new LocalAlignmentResult(0, "", "", 0, 0, 0, 0);
        }

        // Traceback from maxI, maxJ until reaching a cell with 0
        StringBuilder sub1 = new StringBuilder();
        StringBuilder sub2 = new StringBuilder();
        int i = maxI, j = maxJ;

        while (i > 0 && j > 0 && dp[i][j] > 0) {
            int scoreMatch = (seq1.charAt(i - 1) == seq2.charAt(j - 1)) ? matchScore : mismatchPenalty;
            if (dp[i][j] == dp[i - 1][j - 1] + scoreMatch) {
                sub1.append(seq1.charAt(i - 1));
                sub2.append(seq2.charAt(j - 1));
                i--;
                j--;
            } else if (dp[i][j] == dp[i - 1][j] + gapPenalty) {
                sub1.append(seq1.charAt(i - 1));
                sub2.append('-');
                i--;
            } else if (dp[i][j] == dp[i][j - 1] + gapPenalty) {
                sub1.append('-');
                sub2.append(seq2.charAt(j - 1));
                j--;
            } else {
                break;
            }
        }

        return new LocalAlignmentResult(maxScore, sub1.reverse().toString(), sub2.reverse().toString(),
                i, maxI, j, maxJ);
    }

    private static void alignGap(StringBuilder sub1, StringBuilder sub2, char c) {
        sub1.append('-');
        sub2.append(c);
    }
}
