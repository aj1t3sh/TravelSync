package travelsync.model;

/**
 * Model representing a traveler sentiment review.
 */
public class Review {
    private final String id;
    private final String destinationId;
    private final String destinationName;
    private final String reviewText;
    private final double rating;
    private final String reviewDate;
    private final String travelerType;

    public Review(String id, String destinationId, String destinationName,
                  String reviewText, double rating, String reviewDate, String travelerType) {
        this.id = id != null ? id.trim() : "";
        this.destinationId = destinationId != null ? destinationId.trim() : "";
        this.destinationName = destinationName != null ? destinationName.trim() : "";
        this.reviewText = reviewText != null ? reviewText.trim() : "";
        this.rating = rating;
        this.reviewDate = reviewDate != null ? reviewDate.trim() : "";
        this.travelerType = travelerType != null ? travelerType.trim() : "General";
    }

    public String getId() { return id; }
    public String getDestinationId() { return destinationId; }
    public String getDestinationName() { return destinationName; }
    public String getReviewText() { return reviewText; }
    public double getRating() { return rating; }
    public String getReviewDate() { return reviewDate; }
    public String getTravelerType() { return travelerType; }

    @Override
    public String toString() {
        String snippet = reviewText.length() > 60 ? reviewText.substring(0, 57) + "..." : reviewText;
        return String.format("[%s] (%s - %.1f★) \"%s\" [%s, %s]",
                id, destinationName, rating, snippet, travelerType, reviewDate);
    }
}
