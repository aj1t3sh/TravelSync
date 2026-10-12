package travelsync.model;

/**
 * Model representing hotel accommodation inventory.
 */
public class Hotel {
    private final String id;
    private final String city;
    private final String name;
    private final int pricePerNight;
    private final double rating;
    private final int reviewCount;
    private final String hotelType;
    private final String amenities;
    private int availableRooms;
    private final int totalCapacity;

    public Hotel(String id, String city, String name, int pricePerNight, double rating,
                 int reviewCount, String hotelType, String amenities, int availableRooms, int totalCapacity) {
        this.id = id != null ? id.trim() : "";
        this.city = city != null ? city.trim() : "";
        this.name = name != null ? name.trim() : "";
        this.pricePerNight = pricePerNight;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.hotelType = hotelType != null ? hotelType.trim() : "Comfort";
        this.amenities = amenities != null ? amenities.trim() : "";
        this.availableRooms = availableRooms;
        this.totalCapacity = totalCapacity;
    }

    public String getId() { return id; }
    public String getCity() { return city; }
    public String getName() { return name; }
    public int getPricePerNight() { return pricePerNight; }
    public double getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public String getHotelType() { return hotelType; }
    public String getAmenities() { return amenities; }
    public int getAvailableRooms() { return availableRooms; }
    public int getTotalCapacity() { return totalCapacity; }

    public void setAvailableRooms(int availableRooms) {
        this.availableRooms = availableRooms;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - ₹%d/night | Rating: %.1f/5.0 | Type: %s | Rooms: %d/%d",
                id, name, city, pricePerNight, rating, hotelType, availableRooms, totalCapacity);
    }
}
