package travelsync.api.dto;

import travelsync.model.Attraction;

/**
 * An activity or attraction scheduled in a daily time slot.
 */
public class ActivitySlot {
    private String slot; // "Morning", "Afternoon", "Evening"
    private String timeWindow; // "09:00 AM - 12:30 PM"
    private String title;
    private String category;
    private String description;
    private int estimatedCostInr;
    private double durationHours;
    private String attractionId;
    private double latitude;
    private double longitude;
    private String tags;

    public ActivitySlot() {}

    public ActivitySlot(String slot, String timeWindow, String title, String category,
                        String description, int estimatedCostInr, double durationHours,
                        String attractionId, double latitude, double longitude, String tags) {
        this.slot = slot;
        this.timeWindow = timeWindow;
        this.title = title;
        this.category = category;
        this.description = description;
        this.estimatedCostInr = estimatedCostInr;
        this.durationHours = durationHours;
        this.attractionId = attractionId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tags = tags;
    }

    public static ActivitySlot fromAttraction(String slot, String timeWindow, Attraction a) {
        if (a == null) {
            return new ActivitySlot(slot, timeWindow, "Local City Exploration & Culinary Walk", "Leisure",
                    "Stroll around the destination center, explore historic bazaars, sample local delicacies, and enjoy regional ambiance.",
                    0, 2.0, "", 0, 0, "leisure,food,walk");
        }
        return new ActivitySlot(slot, timeWindow, a.getName(), a.getCategory(),
                a.getDescription(), a.getEntryFeeInr(), a.getVisitDurationHours(),
                a.getId(), a.getLatitude(), a.getLongitude(), a.getTags());
    }

    public String getSlot() { return slot; }
    public String getTimeWindow() { return timeWindow; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public int getEstimatedCostInr() { return estimatedCostInr; }
    public double getDurationHours() { return durationHours; }
    public String getAttractionId() { return attractionId; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getTags() { return tags; }
}
