package travelsync.api.dto;

/**
 * Encapsulates execution metadata for an algorithm demonstrating CO1-CO6.
 */
public class AlgorithmExecutionCard {
    private String name;
    private String courseOutcome; // e.g. "CO1", "CO2", "CO3", "CO4", "CO5", "CO6"
    private String stage;         // e.g. "Classification", "Search & Fuzzy Resolution", "Itinerary Route Optimization", "Transit Flow & Bottleneck Analysis", "Constraint & Budget Approximation", "Parallel Demand & Primality Integrity"
    private String description;
    private String inputSize;
    private String complexity;
    private long executionTimeNs;
    private double executionTimeMs;
    private String result;
    private String limitations;
    private String status;        // "SUCCESS", "APPLIED", "GUARANTEED"

    public AlgorithmExecutionCard() {}

    public AlgorithmExecutionCard(String name, String courseOutcome, String stage,
                                  String description, String inputSize, String complexity,
                                  long executionTimeNs, String result, String limitations, String status) {
        this.name = name;
        this.courseOutcome = courseOutcome;
        this.stage = stage;
        this.description = description;
        this.inputSize = inputSize;
        this.complexity = complexity;
        this.executionTimeNs = executionTimeNs;
        this.executionTimeMs = executionTimeNs / 1_000_000.0;
        this.result = result;
        this.limitations = limitations;
        this.status = status;
    }

    public String getName() { return name; }
    public String getCourseOutcome() { return courseOutcome; }
    public String getStage() { return stage; }
    public String getDescription() { return description; }
    public String getInputSize() { return inputSize; }
    public String getComplexity() { return complexity; }
    public long getExecutionTimeNs() { return executionTimeNs; }
    public double getExecutionTimeMs() { return executionTimeMs; }
    public String getResult() { return result; }
    public String getLimitations() { return limitations; }
    public String getStatus() { return status; }
}
