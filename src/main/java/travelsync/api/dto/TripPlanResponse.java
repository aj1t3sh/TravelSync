package travelsync.api.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Complete structured response for an optimized trip plan.
 */
public class TripPlanResponse {
    private String tripId;
    private String origin;
    private String destination;
    private List<String> routeStops;
    private String startDate;
    private String endDate;
    private int travelers;
    private int days;
    private boolean feasible;
    private String infeasibilityReason;
    private String closestFeasibleAlternative;
    private int totalDistanceKm;
    private int totalEstimatedCostInr;
    private int totalTransportCostInr;
    private int totalAccommodationCostInr;
    private int totalAttractionFeesInr;
    private int userBudgetInr;
    private int budgetRemainingInr;
    private double totalTravelTimeHours;
    private List<DayPlan> dailyPlans = new ArrayList<>();
    private List<AlgorithmExecutionCard> algorithmTransparency = new ArrayList<>();
    private FlowAnalysisSummary transitFlowReport;
    private List<AlternativeOption> alternativePlans = new ArrayList<>();
    private List<String> warnings = new ArrayList<>();
    private String securityVerificationToken;
    private long executionTimestampMs;

    public static class FlowAnalysisSummary {
        private String originCity;
        private String destinationCity;
        private int maxDailyPassengerFlow;
        private int minCutCapacity;
        private List<String> bottleneckCorridors;
        private boolean flowBalanced;

        public FlowAnalysisSummary() {}

        public FlowAnalysisSummary(String originCity, String destinationCity,
                                   int maxDailyPassengerFlow, int minCutCapacity,
                                   List<String> bottleneckCorridors, boolean flowBalanced) {
            this.originCity = originCity;
            this.destinationCity = destinationCity;
            this.maxDailyPassengerFlow = maxDailyPassengerFlow;
            this.minCutCapacity = minCutCapacity;
            this.bottleneckCorridors = bottleneckCorridors;
            this.flowBalanced = flowBalanced;
        }

        public String getOriginCity() { return originCity; }
        public String getDestinationCity() { return destinationCity; }
        public int getMaxDailyPassengerFlow() { return maxDailyPassengerFlow; }
        public int getMinCutCapacity() { return minCutCapacity; }
        public List<String> getBottleneckCorridors() { return bottleneckCorridors; }
        public boolean isFlowBalanced() { return flowBalanced; }
    }

    public static class AlternativeOption {
        private String name;
        private String description;
        private int estimatedCostInr;
        private String accommodationType;
        private String transportMode;
        private String tradeoff;

        public AlternativeOption() {}

        public AlternativeOption(String name, String description, int estimatedCostInr,
                                 String accommodationType, String transportMode, String tradeoff) {
            this.name = name;
            this.description = description;
            this.estimatedCostInr = estimatedCostInr;
            this.accommodationType = accommodationType;
            this.transportMode = transportMode;
            this.tradeoff = tradeoff;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getEstimatedCostInr() { return estimatedCostInr; }
        public String getAccommodationType() { return accommodationType; }
        public String getTransportMode() { return transportMode; }
        public String getTradeoff() { return tradeoff; }
    }

    public TripPlanResponse() {}

    // Getters and Setters
    public String getTripId() { return tripId; }
    public void setTripId(String tripId) { this.tripId = tripId; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public List<String> getRouteStops() { return routeStops; }
    public void setRouteStops(List<String> routeStops) { this.routeStops = routeStops; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public int getTravelers() { return travelers; }
    public void setTravelers(int travelers) { this.travelers = travelers; }

    public int getDays() { return days; }
    public void setDays(int days) { this.days = days; }

    public boolean isFeasible() { return feasible; }
    public void setFeasible(boolean feasible) { this.feasible = feasible; }

    public String getInfeasibilityReason() { return infeasibilityReason; }
    public void setInfeasibilityReason(String infeasibilityReason) { this.infeasibilityReason = infeasibilityReason; }

    public String getClosestFeasibleAlternative() { return closestFeasibleAlternative; }
    public void setClosestFeasibleAlternative(String closestFeasibleAlternative) { this.closestFeasibleAlternative = closestFeasibleAlternative; }

    public int getTotalDistanceKm() { return totalDistanceKm; }
    public void setTotalDistanceKm(int totalDistanceKm) { this.totalDistanceKm = totalDistanceKm; }

    public int getTotalEstimatedCostInr() { return totalEstimatedCostInr; }
    public void setTotalEstimatedCostInr(int totalEstimatedCostInr) { this.totalEstimatedCostInr = totalEstimatedCostInr; }

    public int getTotalTransportCostInr() { return totalTransportCostInr; }
    public void setTotalTransportCostInr(int totalTransportCostInr) { this.totalTransportCostInr = totalTransportCostInr; }

    public int getTotalAccommodationCostInr() { return totalAccommodationCostInr; }
    public void setTotalAccommodationCostInr(int totalAccommodationCostInr) { this.totalAccommodationCostInr = totalAccommodationCostInr; }

    public int getTotalAttractionFeesInr() { return totalAttractionFeesInr; }
    public void setTotalAttractionFeesInr(int totalAttractionFeesInr) { this.totalAttractionFeesInr = totalAttractionFeesInr; }

    public int getUserBudgetInr() { return userBudgetInr; }
    public void setUserBudgetInr(int userBudgetInr) { this.userBudgetInr = userBudgetInr; }

    public int getBudgetRemainingInr() { return budgetRemainingInr; }
    public void setBudgetRemainingInr(int budgetRemainingInr) { this.budgetRemainingInr = budgetRemainingInr; }

    public double getTotalTravelTimeHours() { return totalTravelTimeHours; }
    public void setTotalTravelTimeHours(double totalTravelTimeHours) { this.totalTravelTimeHours = totalTravelTimeHours; }

    public List<DayPlan> getDailyPlans() { return dailyPlans; }
    public void setDailyPlans(List<DayPlan> dailyPlans) { this.dailyPlans = dailyPlans; }

    public List<AlgorithmExecutionCard> getAlgorithmTransparency() { return algorithmTransparency; }
    public void setAlgorithmTransparency(List<AlgorithmExecutionCard> algorithmTransparency) { this.algorithmTransparency = algorithmTransparency; }

    public FlowAnalysisSummary getTransitFlowReport() { return transitFlowReport; }
    public void setTransitFlowReport(FlowAnalysisSummary transitFlowReport) { this.transitFlowReport = transitFlowReport; }

    public List<AlternativeOption> getAlternativePlans() { return alternativePlans; }
    public void setAlternativePlans(List<AlternativeOption> alternativePlans) { this.alternativePlans = alternativePlans; }

    public List<String> getWarnings() { return warnings; }
    public void setWarnings(List<String> warnings) { this.warnings = warnings; }

    public String getSecurityVerificationToken() { return securityVerificationToken; }
    public void setSecurityVerificationToken(String securityVerificationToken) { this.securityVerificationToken = securityVerificationToken; }

    public long getExecutionTimestampMs() { return executionTimestampMs; }
    public void setExecutionTimestampMs(long executionTimestampMs) { this.executionTimestampMs = executionTimestampMs; }
}
