package travelsync.model;

/**
 * Model representing a booking transaction in the hospitality demand stream.
 */
public class Booking {
    private final String bookingId;
    private final String hotelId;
    private final String hotelName;
    private final String city;
    private final String bookingDate;
    private final String checkinDate;
    private final String checkoutDate;
    private final int guests;
    private final int rooms;
    private final int dailyRateInr;
    private final String status;
    private final int demandWeight;

    public Booking(String bookingId, String hotelId, String hotelName, String city,
                   String bookingDate, String checkinDate, String checkoutDate,
                   int guests, int rooms, int dailyRateInr, String status, int demandWeight) {
        this.bookingId = bookingId != null ? bookingId.trim() : "";
        this.hotelId = hotelId != null ? hotelId.trim() : "";
        this.hotelName = hotelName != null ? hotelName.trim() : "";
        this.city = city != null ? city.trim() : "";
        this.bookingDate = bookingDate != null ? bookingDate.trim() : "";
        this.checkinDate = checkinDate != null ? checkinDate.trim() : "";
        this.checkoutDate = checkoutDate != null ? checkoutDate.trim() : "";
        this.guests = guests;
        this.rooms = rooms;
        this.dailyRateInr = dailyRateInr;
        this.status = status != null ? status.trim() : "CONFIRMED";
        this.demandWeight = demandWeight;
    }

    public String getBookingId() { return bookingId; }
    public String getHotelId() { return hotelId; }
    public String getHotelName() { return hotelName; }
    public String getCity() { return city; }
    public String getBookingDate() { return bookingDate; }
    public String getCheckinDate() { return checkinDate; }
    public String getCheckoutDate() { return checkoutDate; }
    public int getGuests() { return guests; }
    public int getRooms() { return rooms; }
    public int getDailyRateInr() { return dailyRateInr; }
    public String getStatus() { return status; }
    public int getDemandWeight() { return demandWeight; }

    public int getTotalTransactionValue() {
        return dailyRateInr * rooms;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - %d guests, %d rooms @ ₹%d/day | %s (Weight: %d)",
                bookingId, hotelName, city, guests, rooms, dailyRateInr, status, demandWeight);
    }
}
