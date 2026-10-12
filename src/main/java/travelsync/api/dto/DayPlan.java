package travelsync.api.dto;

import travelsync.model.Hotel;
import travelsync.model.TransportConnection;

import java.util.List;

/**
 * Detailed itinerary for a single day.
 */
public class DayPlan {
    private int dayNumber;
    private String date;
    private String city;
    private String theme;
    private ActivitySlot morning;
    private ActivitySlot afternoon;
    private ActivitySlot evening;
    private HotelSummary hotel;
    private TransitLegSummary transitLeg;
    private int dayCostInr;
    private List<String> notes;

    public static class HotelSummary {
        private String id;
        private String name;
        private String hotelType;
        private int pricePerNightInr;
        private double rating;
        private String amenities;
        private int matchedRooms;

        public HotelSummary() {}

        public HotelSummary(Hotel h, int matchedRooms) {
            if (h != null) {
                this.id = h.getId();
                this.name = h.getName();
                this.hotelType = h.getHotelType();
                this.pricePerNightInr = h.getPricePerNight();
                this.rating = h.getRating();
                this.amenities = h.getAmenities();
                this.matchedRooms = matchedRooms;
            } else {
                this.id = "DEFAULT";
                this.name = "Verified Local Hotel Partner";
                this.hotelType = "Comfort";
                this.pricePerNightInr = 2500;
                this.rating = 4.2;
                this.amenities = "WiFi, AC, Breakfast";
                this.matchedRooms = matchedRooms;
            }
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getHotelType() { return hotelType; }
        public int getPricePerNightInr() { return pricePerNightInr; }
        public double getRating() { return rating; }
        public String getAmenities() { return amenities; }
        public int getMatchedRooms() { return matchedRooms; }
    }

    public static class TransitLegSummary {
        private String fromCity;
        private String toCity;
        private String mode;
        private int distanceKm;
        private int costInr;
        private double travelTimeHours;
        private int capacityPaxPerDay;

        public TransitLegSummary() {}

        public TransitLegSummary(TransportConnection t) {
            if (t != null) {
                this.fromCity = t.getFromCity();
                this.toCity = t.getToCity();
                this.mode = t.getMode();
                this.distanceKm = t.getDistanceKm();
                this.costInr = t.getCostInr();
                this.travelTimeHours = t.getTravelTimeHours();
                this.capacityPaxPerDay = t.getCapacity();
            }
        }

        public TransitLegSummary(String from, String to, String mode, int dist, int cost, double hours, int cap) {
            this.fromCity = from;
            this.toCity = to;
            this.mode = mode;
            this.distanceKm = dist;
            this.costInr = cost;
            this.travelTimeHours = hours;
            this.capacityPaxPerDay = cap;
        }

        public String getFromCity() { return fromCity; }
        public String getToCity() { return toCity; }
        public String getMode() { return mode; }
        public int getDistanceKm() { return distanceKm; }
        public int getCostInr() { return costInr; }
        public double getTravelTimeHours() { return travelTimeHours; }
        public int getCapacityPaxPerDay() { return capacityPaxPerDay; }
    }

    public DayPlan() {}

    public DayPlan(int dayNumber, String date, String city, String theme,
                   ActivitySlot morning, ActivitySlot afternoon, ActivitySlot evening,
                   HotelSummary hotel, TransitLegSummary transitLeg, int dayCostInr, List<String> notes) {
        this.dayNumber = dayNumber;
        this.date = date;
        this.city = city;
        this.theme = theme;
        this.morning = morning;
        this.afternoon = afternoon;
        this.evening = evening;
        this.hotel = hotel;
        this.transitLeg = transitLeg;
        this.dayCostInr = dayCostInr;
        this.notes = notes;
    }

    public int getDayNumber() { return dayNumber; }
    public String getDate() { return date; }
    public String getCity() { return city; }
    public String getTheme() { return theme; }
    public ActivitySlot getMorning() { return morning; }
    public ActivitySlot getAfternoon() { return afternoon; }
    public ActivitySlot getEvening() { return evening; }
    public HotelSummary getHotel() { return hotel; }
    public TransitLegSummary getTransitLeg() { return transitLeg; }
    public int getDayCostInr() { return dayCostInr; }
    public List<String> getNotes() { return notes; }
}
