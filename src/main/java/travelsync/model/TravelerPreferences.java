package travelsync.model;

import java.util.Collections;
import java.util.List;

/**
 * Model capturing user constraints and preferences for itinerary generation.
 */
public class TravelerPreferences {
    private final String startCity;
    private final List<String> targetCities;
    private final int maxDays;
    private final int budgetInr;
    private final List<String> interestTags;
    private final String preferredTransportMode;

    public TravelerPreferences(String startCity, List<String> targetCities, int maxDays,
                               int budgetInr, List<String> interestTags, String preferredTransportMode) {
        this.startCity = startCity != null ? startCity.trim() : "Hyderabad";
        this.targetCities = targetCities != null ? targetCities : Collections.emptyList();
        this.maxDays = maxDays > 0 ? maxDays : 5;
        this.budgetInr = budgetInr > 0 ? budgetInr : 25000;
        this.interestTags = interestTags != null ? interestTags : Collections.emptyList();
        this.preferredTransportMode = preferredTransportMode != null ? preferredTransportMode.toUpperCase() : "ANY";
    }

    public String getStartCity() { return startCity; }
    public List<String> getTargetCities() { return targetCities; }
    public int getMaxDays() { return maxDays; }
    public int getBudgetInr() { return budgetInr; }
    public List<String> getInterestTags() { return interestTags; }
    public String getPreferredTransportMode() { return preferredTransportMode; }

    @Override
    public String toString() {
        return String.format("Origin: %s | Destinations: %s | Days: %d | Budget: ₹%d | Tags: %s | Mode: %s",
                startCity, targetCities, maxDays, budgetInr, interestTags, preferredTransportMode);
    }
}
