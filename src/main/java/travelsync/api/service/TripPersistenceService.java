package travelsync.api.service;

import travelsync.api.dto.TripPlanResponse;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe service for persisting and retrieving itineraries.
 */
public class TripPersistenceService {

    private final Map<String, TripPlanResponse> savedTrips = new ConcurrentHashMap<>();

    public synchronized void saveTrip(TripPlanResponse response) {
        if (response != null && response.getTripId() != null) {
            savedTrips.put(response.getTripId(), response);
        }
    }

    public TripPlanResponse getTrip(String tripId) {
        if (tripId == null) return null;
        return savedTrips.get(tripId);
    }

    public List<TripPlanResponse> getAllSavedTrips() {
        List<TripPlanResponse> list = new ArrayList<>(savedTrips.values());
        list.sort((a, b) -> Long.compare(b.getExecutionTimestampMs(), a.getExecutionTimestampMs()));
        return list;
    }

    public boolean deleteTrip(String tripId) {
        return savedTrips.remove(tripId) != null;
    }
}
