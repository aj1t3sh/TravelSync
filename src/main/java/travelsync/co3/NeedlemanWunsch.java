package travelsync.co3;

/**
 * Needleman-Wunsch algorithm for Global Sequence Alignment with gap penalty.
 * Reconstructs the optimal full alignment strings.
 */
public class NeedlemanWunsch {

    public static class AlignmentResult {
        private final int score;
        private final String alignedSeq1;
        private final String alignedSeq2;

        public AlignmentResult(int score, String alignedSeq1, String alignedSeq2) {
            this.score = score;
            this.alignedSeq1 = alignedSeq1;
            this.alignedSeq2 = alignedSeq2;
        }

        public int getScore() { return score; }
        public String getAlignedSeq1() { return alignedSeq1; }
        public String getAlignedSeq2() { return alignedSeq2; }

        @Override
        public String toString() {
            return String.format("Score: %d\nSeq1: %s\nSeq2: %s", score, alignedSeq1, alignedSeq2);
        }
    }

    /**
     * Performs global sequence alignment with linear gap penalty.
     */
    public static AlignmentResult align(String seq1, String seq2, int matchScore, int mismatchPenalty, int gapPenalty) {
        if (seq1 == null) seq1 = "";
        if (seq2 == null) seq2 = "";

        int n = seq1.length();
        int m = seq2.length();

        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i * gapPenalty;
        for (int j = 0; j <= m; j++) dp[0][j] = j * gapPenalty;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int scoreMatch = (seq1.charAt(i - 1) == seq2.charAt(j - 1)) ? matchScore : mismatchPenalty;
                dp[i][j] = Math.max(
                    dp[i - 1][j - 1] + scoreMatch,
                    Math.max(
                        dp[i - 1][j] + gapPenalty,
                        dp[i][j - 1] + gapPenalty
                    )
                );
            }
        }

        // Traceback to reconstruct alignment
        StringBuilder align1 = new StringBuilder();
        StringBuilder align2 = new StringBuilder();
        int i = n, j = m;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + (seq1.charAt(i - 1) == seq2.charAt(j - 1) ? matchScore : mismatchPenalty)) {
                align1.append(seq1.charAt(i - 1));
                align2.append(seq2.charAt(j - 1));
                i--;
                j--;
            } else if (i > 0 && dp[i][j] == dp[i - 1][j] + gapPenalty) {
                align1.append(seq1.charAt(i - 1));
                align2.append('-');
                i--;
            } else {
                align1.append('-');
                align2.append(seq2.charAt(j - 1));
                j--;
            }
        }

        return new AlignmentResult(dp[n][m], align1.reverse().toString(), align2.reverse().toString());
    }
}
