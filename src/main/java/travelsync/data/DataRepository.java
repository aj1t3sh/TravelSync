package travelsync.data;

import travelsync.model.*;

import java.util.*;

/**
 * In-memory repository providing fast indexed lookups, graph adjacency representations,
 * and statistical aggregations for all TravelSync entities.
 */
public class DataRepository {

    private final List<Destination> destinations = new ArrayList<>();
    private final Map<String, Destination> destinationById = new HashMap<>();
    private final Map<String, Destination> destinationByName = new HashMap<>();

    private final List<Attraction> attractions = new ArrayList<>();
    private final Map<String, List<Attraction>> attractionsByDestId = new HashMap<>();
    private final Map<String, List<Attraction>> attractionsByCity = new HashMap<>();

    private final List<Hotel> hotels = new ArrayList<>();
    private final Map<String, List<Hotel>> hotelsByCity = new HashMap<>();

    private final List<TransportConnection> transportConnections = new ArrayList<>();
    private final Map<String, List<TransportConnection>> transportAdjacency = new HashMap<>();

    private final List<Review> reviews = new ArrayList<>();
    private final Map<String, List<Review>> reviewsByDestId = new HashMap<>();

    private final List<Booking> bookings = new ArrayList<>();

    private final DatasetLoader loader = new DatasetLoader();
    private boolean loaded = false;

    /**
     * Initializes and loads the datasets from the discovered directory.
     */
    public synchronized void initialize() {
        if (loaded) return;
        String dataDir = loader.resolveDatasetDirectory();

        List<Destination> destList = loader.loadDestinations(dataDir);
        for (Destination d : destList) {
            destinations.add(d);
            destinationById.put(d.getId(), d);
            destinationByName.put(d.getName().toLowerCase(Locale.ROOT), d);
        }

        List<Attraction> attrList = loader.loadAttractions(dataDir);
        for (Attraction a : attrList) {
            attractions.add(a);
            attractionsByDestId.computeIfAbsent(a.getDestinationId(), k -> new ArrayList<>()).add(a);
            attractionsByCity.computeIfAbsent(a.getCity().toLowerCase(Locale.ROOT), k -> new ArrayList<>()).add(a);
        }

        List<Hotel> hotelList = loader.loadHotels(dataDir);
        for (Hotel h : hotelList) {
            hotels.add(h);
            hotelsByCity.computeIfAbsent(h.getCity().toLowerCase(Locale.ROOT), k -> new ArrayList<>()).add(h);
        }

        List<TransportConnection> transList = loader.loadTransport(dataDir);
        for (TransportConnection t : transList) {
            transportConnections.add(t);
            transportAdjacency.computeIfAbsent(t.getFromCity().toLowerCase(Locale.ROOT), k -> new ArrayList<>()).add(t);
        }

        List<Review> revList = loader.loadReviews(dataDir);
        for (Review r : revList) {
            reviews.add(r);
            reviewsByDestId.computeIfAbsent(r.getDestinationId(), k -> new ArrayList<>()).add(r);
        }

        List<Booking> bookList = loader.loadBookings(dataDir);
        bookings.addAll(bookList);

        loaded = true;
    }

    public boolean isLoaded() { return loaded; }
    public DatasetLoader getLoader() { return loader; }

    public List<Destination> getAllDestinations() { return Collections.unmodifiableList(destinations); }
    public List<Attraction> getAllAttractions() { return Collections.unmodifiableList(attractions); }
    public List<Hotel> getAllHotels() { return Collections.unmodifiableList(hotels); }
    public List<TransportConnection> getAllTransportConnections() { return Collections.unmodifiableList(transportConnections); }
    public List<Review> getAllReviews() { return Collections.unmodifiableList(reviews); }
    public List<Booking> getAllBookings() { return Collections.unmodifiableList(bookings); }

    public Destination findDestinationById(String id) {
        return destinationById.get(id);
    }

    public Destination findDestinationByName(String name) {
        if (name == null) return null;
        return destinationByName.get(name.trim().toLowerCase(Locale.ROOT));
    }

    public List<Attraction> getAttractionsForDestination(String destinationId) {
        return attractionsByDestId.getOrDefault(destinationId, Collections.emptyList());
    }

    public List<Attraction> getAttractionsForCity(String city) {
        if (city == null) return Collections.emptyList();
        return attractionsByCity.getOrDefault(city.trim().toLowerCase(Locale.ROOT), Collections.emptyList());
    }

    public List<Hotel> getHotelsForCity(String city) {
        if (city == null) return Collections.emptyList();
        return hotelsByCity.getOrDefault(city.trim().toLowerCase(Locale.ROOT), Collections.emptyList());
    }

    public List<TransportConnection> getOutboundTransport(String city) {
        if (city == null) return Collections.emptyList();
        return transportAdjacency.getOrDefault(city.trim().toLowerCase(Locale.ROOT), Collections.emptyList());
    }

    public List<Review> getReviewsForDestination(String destinationId) {
        return reviewsByDestId.getOrDefault(destinationId, Collections.emptyList());
    }

    public Set<String> getAllCityNames() {
        Set<String> set = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (Destination d : destinations) {
            set.add(d.getName());
        }
        for (TransportConnection t : transportConnections) {
            set.add(t.getFromCity());
            set.add(t.getToCity());
        }
        return set;
    }
}
