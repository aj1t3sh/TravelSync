package travelsync.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model representing an optimized multi-stop travel itinerary plan.
 */
public class ItineraryPlan {
    private final String startCity;
    private final List<String> routeStops;
    private final int totalDistanceKm;
    private final int totalTransportCostInr;
    private final int totalAccommodationCostInr;
    private final double totalTravelTimeHours;
    private final List<String> dailyBreakdown;
    private final boolean feasible;
    private final String infeasibilityReason;
    private final String algorithmUsed;

    public ItineraryPlan(String startCity, List<String> routeStops, int totalDistanceKm,
                         int totalTransportCostInr, int totalAccommodationCostInr,
                         double totalTravelTimeHours, List<String> dailyBreakdown,
                         boolean feasible, String infeasibilityReason, String algorithmUsed) {
        this.startCity = startCity;
        this.routeStops = routeStops != null ? routeStops : Collections.emptyList();
        this.totalDistanceKm = totalDistanceKm;
        this.totalTransportCostInr = totalTransportCostInr;
        this.totalAccommodationCostInr = totalAccommodationCostInr;
        this.totalTravelTimeHours = totalTravelTimeHours;
        this.dailyBreakdown = dailyBreakdown != null ? dailyBreakdown : new ArrayList<>();
        this.feasible = feasible;
        this.infeasibilityReason = infeasibilityReason != null ? infeasibilityReason : "";
        this.algorithmUsed = algorithmUsed != null ? algorithmUsed : "Bitmask Dynamic Programming";
    }

    public static ItineraryPlan infeasible(String startCity, String reason, String algorithm) {
        return new ItineraryPlan(startCity, Collections.emptyList(), 0, 0, 0, 0,
                Collections.emptyList(), false, reason, algorithm);
    }

    public String getStartCity() { return startCity; }
    public List<String> getRouteStops() { return routeStops; }
    public int getTotalDistanceKm() { return totalDistanceKm; }
    public int getTotalTransportCostInr() { return totalTransportCostInr; }
    public int getTotalAccommodationCostInr() { return totalAccommodationCostInr; }
    public int getTotalEstimatedCostInr() { return totalTransportCostInr + totalAccommodationCostInr; }
    public double getTotalTravelTimeHours() { return totalTravelTimeHours; }
    public List<String> getDailyBreakdown() { return dailyBreakdown; }
    public boolean isFeasible() { return feasible; }
    public String getInfeasibilityReason() { return infeasibilityReason; }
    public String getAlgorithmUsed() { return algorithmUsed; }

    @Override
    public String toString() {
        if (!feasible) {
            return String.format("Infeasible Plan (%s): %s", algorithmUsed, infeasibilityReason);
        }
        return String.format("Itinerary: %s -> Total Distance: %d km | Cost: ₹%d (Transport ₹%d + Hotel ₹%d) | Time: %.1fh | [%s]",
                String.join(" -> ", routeStops), totalDistanceKm, getTotalEstimatedCostInr(),
                totalTransportCostInr, totalAccommodationCostInr, totalTravelTimeHours, algorithmUsed);
    }
}
