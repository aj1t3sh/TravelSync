package travelsync.co1;

/**
 * Encapsulates the algorithmic strategy, theoretical bounds, rationale,
 * and alternative comparison for a given computational problem class.
 */
public class AlgorithmSignature {
    private final ProblemType problemType;
    private final String problemCategory;
    private final String problemDescription;
    private final String selectedAlgorithm;
    private final String timeComplexity;
    private final String spaceComplexity;
    private final String selectionRationale;
    private final String limitations;
    private final String alternativeAlgorithm;
    private final String alternativeComparison;

    public AlgorithmSignature(ProblemType problemType, String problemCategory,
                              String problemDescription, String selectedAlgorithm,
                              String timeComplexity, String spaceComplexity,
                              String selectionRationale, String limitations,
                              String alternativeAlgorithm, String alternativeComparison) {
        this.problemType = problemType;
        this.problemCategory = problemCategory;
        this.problemDescription = problemDescription;
        this.selectedAlgorithm = selectedAlgorithm;
        this.timeComplexity = timeComplexity;
        this.spaceComplexity = spaceComplexity;
        this.selectionRationale = selectionRationale;
        this.limitations = limitations;
        this.alternativeAlgorithm = alternativeAlgorithm;
        this.alternativeComparison = alternativeComparison;
    }

    public ProblemType getProblemType() { return problemType; }
    public String getProblemCategory() { return problemCategory; }
    public String getProblemDescription() { return problemDescription; }
    public String getSelectedAlgorithm() { return selectedAlgorithm; }
    public String getTimeComplexity() { return timeComplexity; }
    public String getSpaceComplexity() { return spaceComplexity; }
    public String getSelectionRationale() { return selectionRationale; }
    public String getLimitations() { return limitations; }
    public String getAlternativeAlgorithm() { return alternativeAlgorithm; }
    public String getAlternativeComparison() { return alternativeComparison; }

    @Override
    public String toString() {
        return String.format("[%s] %s -> Strategy: %s (Time: %s, Space: %s)",
                problemCategory, problemDescription, selectedAlgorithm, timeComplexity, spaceComplexity);
    }
}
