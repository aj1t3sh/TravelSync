package travelsync.model;

/**
 * Model representing a specific point of interest or tourist attraction.
 */
public class Attraction {
    private final String id;
    private final String destinationId;
    private final String name;
    private final String city;
    private final String category;
    private final double rating;
    private final int entryFeeInr;
    private final double visitDurationHours;
    private final String description;
    private final String tags;
    private final double latitude;
    private final double longitude;

    public Attraction(String id, String destinationId, String name, String city, String category,
                      double rating, int entryFeeInr, double visitDurationHours,
                      String description, String tags, double latitude, double longitude) {
        this.id = id != null ? id.trim() : "";
        this.destinationId = destinationId != null ? destinationId.trim() : "";
        this.name = name != null ? name.trim() : "";
        this.city = city != null ? city.trim() : "";
        this.category = category != null ? category.trim() : "General";
        this.rating = rating;
        this.entryFeeInr = entryFeeInr;
        this.visitDurationHours = visitDurationHours;
        this.description = description != null ? description.trim() : "";
        this.tags = tags != null ? tags.trim() : "";
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() { return id; }
    public String getDestinationId() { return destinationId; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getCategory() { return category; }
    public double getRating() { return rating; }
    public int getEntryFeeInr() { return entryFeeInr; }
    public double getVisitDurationHours() { return visitDurationHours; }
    public String getDescription() { return description; }
    public String getTags() { return tags; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - %s | Rating: %.1f/5.0 | Fee: ₹%d | Duration: %.1fh",
                id, name, city, category, rating, entryFeeInr, visitDurationHours);
    }
}
