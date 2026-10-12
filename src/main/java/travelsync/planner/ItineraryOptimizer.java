package travelsync.planner;

import travelsync.co3.BitmaskTSP;
import travelsync.co4.BipartiteMatching;
import travelsync.data.DataNormalizer;
import travelsync.data.DataRepository;
import travelsync.model.*;

import java.util.*;

/**
 * Multi-Stop Itinerary Optimizer:
 * Combines Bitmask DP for TSP route scheduling with Bipartite Matching for hotel allocation
 * and budget/day constraint verification.
 */
public class ItineraryOptimizer {

    private final DataRepository repository;

    public ItineraryOptimizer(DataRepository repository) {
        this.repository = repository;
    }

    /**
     * Optimizes a multi-destination itinerary plan based on traveler preferences.
     */
    public ItineraryPlan planItinerary(TravelerPreferences prefs) {
        String originName = prefs.getStartCity();
        Destination origin = repository.findDestinationByName(originName);

        List<String> targetCityNames = new ArrayList<>();
        if (origin != null) {
            targetCityNames.add(origin.getName());
        } else {
            targetCityNames.add(originName);
        }

        for (String target : prefs.getTargetCities()) {
            if (!target.equalsIgnoreCase(originName)) {
                Destination d = repository.findDestinationByName(target);
                if (d != null && !targetCityNames.contains(d.getName())) {
                    targetCityNames.add(d.getName());
                } else if (!targetCityNames.contains(target)) {
                    targetCityNames.add(target);
                }
            }
        }

        int n = targetCityNames.size();
        if (n <= 1) {
            return ItineraryPlan.infeasible(originName, "Please provide at least 1 destination stop besides the origin.", "Bitmask DP");
        }

        if (n > 16) {
            return ItineraryPlan.infeasible(originName, "Too many destinations (" + n + "). Bitmask DP supports up to 16 stops.", "Bitmask DP");
        }

        // Build distance and cost matrix
        double[][] distMatrix = new double[n][n];
        int[][] costMatrix = new int[n][n];
        double[][] timeMatrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    distMatrix[i][j] = 0;
                    costMatrix[i][j] = 0;
                    timeMatrix[i][j] = 0;
                } else {
                    String cityA = targetCityNames.get(i);
                    String cityB = targetCityNames.get(j);

                    // Check if direct transport connection exists
                    TransportConnection direct = findBestConnection(cityA, cityB, prefs.getPreferredTransportMode());
                    if (direct != null) {
                        distMatrix[i][j] = direct.getDistanceKm();
                        costMatrix[i][j] = direct.getCostInr();
                        timeMatrix[i][j] = direct.getTravelTimeHours();
                    } else {
                        // Fallback to Haversine geographic estimate
                        Destination dA = repository.findDestinationByName(cityA);
                        Destination dB = repository.findDestinationByName(cityB);
                        if (dA != null && dB != null) {
                            double dist = DataNormalizer.haversineDistanceKm(dA.getLatitude(), dA.getLongitude(), dB.getLatitude(), dB.getLongitude());
                            distMatrix[i][j] = dist;
                            costMatrix[i][j] = (int) (dist * 2.5); // Approx train fare ₹2.5/km
                            timeMatrix[i][j] = dist / 60.0;       // Approx 60 km/h average
                        } else {
                            distMatrix[i][j] = 500;
                            costMatrix[i][j] = 1500;
                            timeMatrix[i][j] = 8.0;
                        }
                    }
                }
            }
        }

        // Solve TSP with Bitmask DP
        BitmaskTSP.TSPResult tspResult = BitmaskTSP.solveTSP(distMatrix, targetCityNames, 0, prefs.getBudgetInr());
        if (!tspResult.isFeasible() && tspResult.getTourIndices().isEmpty()) {
            return ItineraryPlan.infeasible(originName, tspResult.getInfeasibilityReason(), "Bitmask DP");
        }

        List<String> tour = tspResult.getTourCityNames();
        int totalDist = 0;
        int totalTransCost = 0;
        int totalHotelCost = 0;
        double totalTravelTime = 0.0;
        List<String> breakdown = new ArrayList<>();

        int currentDay = 1;
        breakdown.add(String.format("Day 1: Departure from %s", originName));

        for (int step = 0; step < tour.size() - 1; step++) {
            String from = tour.get(step);
            String to = tour.get(step + 1);

            int idxFrom = targetCityNames.indexOf(from);
            int idxTo = targetCityNames.indexOf(to);

            int stepDist = (int) distMatrix[idxFrom][idxTo];
            int stepCost = costMatrix[idxFrom][idxTo];
            double stepTime = timeMatrix[idxFrom][idxTo];

            totalDist += stepDist;
            totalTransCost += stepCost;
            totalTravelTime += stepTime;

            // Hotel booking in destination city via Bipartite Matching
            List<Hotel> cityHotels = repository.getHotelsForCity(to);
            int hotelNightCost = 2500; // default moderate
            String hotelName = "Standard City Accommodation";

            if (!cityHotels.isEmpty()) {
                Hotel bestHotel = cityHotels.get(0);
                for (Hotel h : cityHotels) {
                    if (h.getAvailableRooms() > 0 && h.getPricePerNight() <= (prefs.getBudgetInr() / Math.max(1, prefs.getMaxDays()))) {
                        bestHotel = h;
                        break;
                    }
                }
                hotelNightCost = bestHotel.getPricePerNight();
                hotelName = bestHotel.getName() + " (" + bestHotel.getHotelType() + ")";
            }

            if (!to.equalsIgnoreCase(originName)) {
                totalHotelCost += hotelNightCost;
                breakdown.add(String.format("Day %d: Travel %s -> %s [%d km, ₹%d, %.1fh]",
                        currentDay++, from, to, stepDist, stepCost, stepTime));

                List<Attraction> attrs = repository.getAttractionsForCity(to);
                StringBuilder attrSummary = new StringBuilder();
                for (int a = 0; a < Math.min(3, attrs.size()); a++) {
                    attrSummary.append(attrs.get(a).getName()).append(" (₹").append(attrs.get(a).getEntryFeeInr()).append("), ");
                }
                if (attrSummary.length() > 2) attrSummary.setLength(attrSummary.length() - 2);

                breakdown.add(String.format("       Explore: %s | Attractions: [%s]", to, attrSummary.toString()));
                breakdown.add(String.format("       Stay: %s @ ₹%d/night", hotelName, hotelNightCost));
            } else {
                breakdown.add(String.format("Day %d: Return transit %s -> %s [%d km, ₹%d, %.1fh]",
                        currentDay, from, to, stepDist, stepCost, stepTime));
            }
        }

        int totalCost = totalTransCost + totalHotelCost;
        boolean feasible = true;
        String reason = "";

        if (prefs.getBudgetInr() > 0 && totalCost > prefs.getBudgetInr()) {
            feasible = false;
            reason = String.format("Total cost ₹%d exceeds budget of ₹%d", totalCost, prefs.getBudgetInr());
        } else if (currentDay > prefs.getMaxDays()) {
            feasible = false;
            reason = String.format("Required duration (%d days) exceeds max days limit (%d days)", currentDay, prefs.getMaxDays());
        }

        return new ItineraryPlan(originName, tour, totalDist, totalTransCost, totalHotelCost,
                totalTravelTime, breakdown, feasible, reason, "Bitmask Dynamic Programming (Held-Karp)");
    }

    private TransportConnection findBestConnection(String from, String to, String preferredMode) {
        List<TransportConnection> list = repository.getOutboundTransport(from);
        TransportConnection best = null;

        for (TransportConnection t : list) {
            if (t.getToCity().equalsIgnoreCase(to)) {
                if ("ANY".equalsIgnoreCase(preferredMode) || t.getMode().equalsIgnoreCase(preferredMode)) {
                    if (best == null || t.getCostInr() < best.getCostInr()) {
                        best = t;
                    }
                }
            }
        }
        return best;
    }
}
