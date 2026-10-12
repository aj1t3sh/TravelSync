package travelsync.co1;

/**
 * Utility for formatting algorithmic complexity evaluations, asymptotic comparisons,
 * and work-span analysis reports.
 */
public class ComplexityEvaluator {

    /**
     * Generates a detailed complexity breakdown card for an algorithm signature.
     */
    public static String formatSignatureCard(AlgorithmSignature sig) {
        StringBuilder sb = new StringBuilder();
        String border = "═".repeat(78);
        String div = "─".repeat(78);

        sb.append("╔").append(border).append("╗\n");
        sb.append(String.format("║  %-76s║\n", "PROBLEM CLASSIFICATION & ALGORITHM SELECTION (CO1)"));
        sb.append("╠").append(border).append("╣\n");
        sb.append(String.format("║  Problem Category : %-55s║\n", sig.getProblemCategory()));
        sb.append(String.format("║  Problem Scope    : %-55s║\n", sig.getProblemDescription()));
        sb.append("╟").append(div).append("╢\n");
        sb.append(String.format("║  Selected Strategy: %-55s║\n", sig.getSelectedAlgorithm()));
        sb.append(String.format("║  Time Complexity  : %-55s║\n", sig.getTimeComplexity()));
        sb.append(String.format("║  Space Complexity : %-55s║\n", sig.getSpaceComplexity()));
        sb.append("╟").append(div).append("╢\n");
        sb.append(String.format("║  Selection Reason : %-55s║\n", truncate(sig.getSelectionRationale(), 55)));
        if (sig.getSelectionRationale().length() > 55) {
            sb.append(String.format("║                     %-55s║\n", sig.getSelectionRationale().substring(55)));
        }
        sb.append(String.format("║  Key Limitations  : %-55s║\n", truncate(sig.getLimitations(), 55)));
        if (sig.getLimitations().length() > 55) {
            sb.append(String.format("║                     %-55s║\n", sig.getLimitations().substring(55)));
        }
        sb.append("╟").append(div).append("╢\n");
        sb.append(String.format("║  Alternative      : %-55s║\n", sig.getAlternativeAlgorithm()));
        sb.append(String.format("║  Why Superior     : %-55s║\n", truncate(sig.getAlternativeComparison(), 55)));
        if (sig.getAlternativeComparison().length() > 55) {
            sb.append(String.format("║                     %-55s║\n", sig.getAlternativeComparison().substring(55)));
        }
        sb.append("╚").append(border).append("╝\n");
        return sb.toString();
    }

    private static String truncate(String text, int max) {
        if (text == null) return "";
        return text.length() <= max ? text : text.substring(0, max);
    }
}
