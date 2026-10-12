package travelsync.api.service;

import travelsync.co6.BlellochScan;
import travelsync.co6.ParallelReduce;
import travelsync.co6.ReservoirSampling;
import travelsync.data.DataRepository;
import travelsync.model.*;

import java.util.*;

/**
 * Service to calculate real aggregated analytics and graph topology metrics
 * directly from the authentic TravelSync datasets.
 */
public class DatasetAnalyticsService {

    private final DataRepository repository;

    public DatasetAnalyticsService(DataRepository repository) {
        this.repository = repository;
    }

    public Map<String, Object> getAnalyticsSummary() {
        Map<String, Object> data = new LinkedHashMap<>();

        List<Destination> destinations = repository.getAllDestinations();
        List<Attraction> attractions = repository.getAllAttractions();
        List<Hotel> hotels = repository.getAllHotels();
        List<TransportConnection> routes = repository.getAllTransportConnections();
        List<Booking> bookings = repository.getAllBookings();
        List<Review> reviews = repository.getAllReviews();

        // 1. Overview counts
        Map<String, Object> counts = new LinkedHashMap<>();
        counts.put("destinations", destinations.size());
        counts.put("attractions", attractions.size());
        counts.put("hotels", hotels.size());
        counts.put("transportRoutes", routes.size());
        counts.put("bookings", bookings.size());
        counts.put("reviews", reviews.size());
        data.put("inventoryCounts", counts);

        // 2. Attractions by category
        Map<String, Integer> categoryCounts = new LinkedHashMap<>();
        for (Attraction a : attractions) {
            categoryCounts.put(a.getCategory(), categoryCounts.getOrDefault(a.getCategory(), 0) + 1);
        }
        data.put("attractionsByCategory", categoryCounts);

        // 3. Destinations by price range & average rating
        Map<String, Integer> destPriceRanges = new LinkedHashMap<>();
        double totalRating = 0;
        for (Destination d : destinations) {
            destPriceRanges.put(d.getPriceRange(), destPriceRanges.getOrDefault(d.getPriceRange(), 0) + 1);
            totalRating += d.getRating();
        }
        data.put("destinationPriceRanges", destPriceRanges);
        data.put("averageDestinationRating", destinations.isEmpty() ? 0 : Math.round((totalRating / destinations.size()) * 10.0) / 10.0);

        // 4. Hotel stats: distribution by type & price tiers
        Map<String, Integer> hotelTypes = new LinkedHashMap<>();
        int totalHotelCapacity = 0;
        int totalAvailableRooms = 0;
        for (Hotel h : hotels) {
            hotelTypes.put(h.getHotelType(), hotelTypes.getOrDefault(h.getHotelType(), 0) + 1);
            totalHotelCapacity += h.getTotalCapacity();
            totalAvailableRooms += h.getAvailableRooms();
        }
        data.put("hotelsByType", hotelTypes);
        data.put("totalHotelCapacity", totalHotelCapacity);
        data.put("totalAvailableRooms", totalAvailableRooms);

        // 5. Transport network stats
        Map<String, Integer> transportModes = new LinkedHashMap<>();
        int totalCorridorCapacity = 0;
        for (TransportConnection t : routes) {
            transportModes.put(t.getMode(), transportModes.getOrDefault(t.getMode(), 0) + 1);
            totalCorridorCapacity += t.getCapacity();
        }
        data.put("transportByMode", transportModes);
        data.put("totalNetworkDailyPaxCapacity", totalCorridorCapacity);

        // Top capacity transit corridors
        List<TransportConnection> sortedCorridors = new ArrayList<>(routes);
        sortedCorridors.sort((a, b) -> Integer.compare(b.getCapacity(), a.getCapacity()));
        List<Map<String, Object>> topCorridors = new ArrayList<>();
        for (int i = 0; i < Math.min(5, sortedCorridors.size()); i++) {
            TransportConnection t = sortedCorridors.get(i);
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("corridor", t.getFromCity() + " ➔ " + t.getToCity());
            map.put("mode", t.getMode());
            map.put("capacity", t.getCapacity());
            map.put("fare", t.getCostInr());
            topCorridors.add(map);
        }
        data.put("topCorridors", topCorridors);

        // 6. CO6 Parallel aggregations on live booking transactions
        long[] revenues = new long[bookings.size()];
        long[] demands = new long[bookings.size()];
        for (int i = 0; i < bookings.size(); i++) {
            revenues[i] = bookings.get(i).getTotalTransactionValue();
            demands[i] = bookings.get(i).getDemandWeight();
        }

        ParallelReduce.ReductionResult revRed = ParallelReduce.parallelSum(revenues);
        BlellochScan.ScanResult demandScan = BlellochScan.scan(demands);
        ReservoirSampling.SampleResult<Booking> sample = ReservoirSampling.sample(bookings, 5);

        Map<String, Object> co6Stats = new LinkedHashMap<>();
        co6Stats.put("totalTransactionRevenueInr", revRed.getResult());
        co6Stats.put("reductionSequentialMs", revRed.getSequentialTimeNs() / 1_000_000.0);
        co6Stats.put("reductionParallelMs", revRed.getParallelTimeNs() / 1_000_000.0);
        co6Stats.put("parallelSpeedup", revRed.getSpeedup());
        co6Stats.put("scanSequentialMs", demandScan.getSequentialTimeNs() / 1_000_000.0);
        co6Stats.put("scanParallelMs", demandScan.getParallelTimeNs() / 1_000_000.0);
        co6Stats.put("reservoirSampleCount", sample.getReservoir().size());
        data.put("co6ParallelMetrics", co6Stats);

        return data;
    }
}
