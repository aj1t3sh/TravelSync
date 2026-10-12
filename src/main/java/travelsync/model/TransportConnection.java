package travelsync.model;

/**
 * Model representing a directed transportation network corridor between Indian cities.
 */
public class TransportConnection {
    private final String fromCity;
    private final String toCity;
    private final String mode;
    private final int capacity;
    private final int distanceKm;
    private final int costInr;
    private final double travelTimeHours;

    public TransportConnection(String fromCity, String toCity, String mode,
                               int capacity, int distanceKm, int costInr, double travelTimeHours) {
        this.fromCity = fromCity != null ? fromCity.trim() : "";
        this.toCity = toCity != null ? toCity.trim() : "";
        this.mode = mode != null ? mode.trim().toUpperCase() : "HIGHWAY";
        this.capacity = capacity;
        this.distanceKm = distanceKm;
        this.costInr = costInr;
        this.travelTimeHours = travelTimeHours;
    }

    public String getFromCity() { return fromCity; }
    public String getToCity() { return toCity; }
    public String getMode() { return mode; }
    public int getCapacity() { return capacity; }
    public int getDistanceKm() { return distanceKm; }
    public int getCostInr() { return costInr; }
    public double getTravelTimeHours() { return travelTimeHours; }

    @Override
    public String toString() {
        return String.format("%s -> %s via %s [%d km, ₹%d, %.1fh, Capacity: %d pax/day]",
                fromCity, toCity, mode, distanceKm, costInr, travelTimeHours, capacity);
    }
}
