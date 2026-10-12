package travelsync.model;

/**
 * Model representing an Indian tourism destination hub.
 */
public class Destination {
    private final String id;
    private final String name;
    private final String city;
    private final String state;
    private final String country;
    private final String description;
    private final double rating;
    private final int reviewCount;
    private final String priceRange;
    private final String tags;
    private final String bestSeason;
    private final double latitude;
    private final double longitude;

    public Destination(String id, String name, String city, String state, String country,
                       String description, double rating, int reviewCount, String priceRange,
                       String tags, String bestSeason, double latitude, double longitude) {
        this.id = id != null ? id.trim() : "";
        this.name = name != null ? name.trim() : "";
        this.city = city != null ? city.trim() : "";
        this.state = state != null ? state.trim() : "";
        this.country = country != null ? country.trim() : "India";
        this.description = description != null ? description.trim() : "";
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.priceRange = priceRange != null ? priceRange.trim() : "MODERATE";
        this.tags = tags != null ? tags.trim() : "";
        this.bestSeason = bestSeason != null ? bestSeason.trim() : "All Year";
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getCountry() { return country; }
    public String getDescription() { return description; }
    public double getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public String getPriceRange() { return priceRange; }
    public String getTags() { return tags; }
    public String getBestSeason() { return bestSeason; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s, %s) - Rating: %.1f/10.0 | %s | %s",
                id, name, city, state, rating, priceRange, bestSeason);
    }
}
