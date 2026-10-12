package travelsync.api.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Request payload for trip planning and itinerary optimization.
 */
public class TripPlanRequest {
    private String origin;
    private String destination;
    private List<String> destinations;
    private String startDate;
    private String endDate;
    private int travelers = 1;
    private int days = 3;
    private int budgetInr = 30000;
    private int minAccommodationBudget = 0;
    private int maxAccommodationBudget = 0;
    private List<String> interests;
    private String travelStyle = "balanced"; // "relaxed", "balanced", "packed"
    private String accommodationPreference = "Comfort"; // "Budget", "Comfort", "Premium", "Luxury", "Heritage", "Any"
    private String transportPreference = "ANY"; // "FLIGHT", "TRAIN", "BUS", "ANY"
    private int maxAttractionsPerDay = 3;
    private List<String> mustVisitAttractions;
    private List<String> excludedAttractions;

    public TripPlanRequest() {}

    public String getOrigin() {
        return origin != null && !origin.trim().isEmpty() ? origin.trim() : "Hyderabad";
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        if (destination != null && !destination.trim().isEmpty()) {
            return destination.trim();
        }
        if (destinations != null && !destinations.isEmpty()) {
            return destinations.get(0).trim();
        }
        return "Goa";
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<String> getDestinations() {
        if (destinations != null && !destinations.isEmpty()) {
            return destinations;
        }
        List<String> list = new ArrayList<>();
        if (destination != null && !destination.trim().isEmpty()) {
            list.add(destination.trim());
        } else {
            list.add("Goa");
        }
        return list;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }

    public String getStartDate() {
        return startDate != null ? startDate : "2025-04-10";
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate != null ? endDate : "2025-04-14";
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getTravelers() {
        return travelers > 0 ? travelers : 1;
    }

    public void setTravelers(int travelers) {
        this.travelers = travelers;
    }

    public int getDays() {
        return days > 0 ? days : 3;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getBudgetInr() {
        return budgetInr > 0 ? budgetInr : 30000;
    }

    public void setBudgetInr(int budgetInr) {
        this.budgetInr = budgetInr;
    }

    public int getMinAccommodationBudget() {
        return minAccommodationBudget;
    }

    public void setMinAccommodationBudget(int minAccommodationBudget) {
        this.minAccommodationBudget = minAccommodationBudget;
    }

    public int getMaxAccommodationBudget() {
        return maxAccommodationBudget;
    }

    public void setMaxAccommodationBudget(int maxAccommodationBudget) {
        this.maxAccommodationBudget = maxAccommodationBudget;
    }

    public List<String> getInterests() {
        return interests != null ? interests : new ArrayList<>();
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public String getTravelStyle() {
        return travelStyle != null ? travelStyle.toLowerCase() : "balanced";
    }

    public void setTravelStyle(String travelStyle) {
        this.travelStyle = travelStyle;
    }

    public String getAccommodationPreference() {
        return accommodationPreference != null ? accommodationPreference : "Comfort";
    }

    public void setAccommodationPreference(String accommodationPreference) {
        this.accommodationPreference = accommodationPreference;
    }

    public String getTransportPreference() {
        return transportPreference != null ? transportPreference.toUpperCase() : "ANY";
    }

    public void setTransportPreference(String transportPreference) {
        this.transportPreference = transportPreference;
    }

    public int getMaxAttractionsPerDay() {
        return maxAttractionsPerDay > 0 ? Math.min(5, maxAttractionsPerDay) : 3;
    }

    public void setMaxAttractionsPerDay(int maxAttractionsPerDay) {
        this.maxAttractionsPerDay = maxAttractionsPerDay;
    }

    public List<String> getMustVisitAttractions() {
        return mustVisitAttractions != null ? mustVisitAttractions : new ArrayList<>();
    }

    public void setMustVisitAttractions(List<String> mustVisitAttractions) {
        this.mustVisitAttractions = mustVisitAttractions;
    }

    public List<String> getExcludedAttractions() {
        return excludedAttractions != null ? excludedAttractions : new ArrayList<>();
    }

    public void setExcludedAttractions(List<String> excludedAttractions) {
        this.excludedAttractions = excludedAttractions;
    }
}
