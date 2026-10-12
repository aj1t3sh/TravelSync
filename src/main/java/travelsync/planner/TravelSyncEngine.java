package travelsync.planner;

import travelsync.co1.AlgorithmSignature;
import travelsync.co1.ProblemClassifier;
import travelsync.co1.ProblemType;
import travelsync.co2.*;
import travelsync.co3.*;
import travelsync.co4.*;
import travelsync.co5.*;
import travelsync.co6.*;
import travelsync.data.DataRepository;
import travelsync.model.*;

import java.math.BigInteger;
import java.util.*;

/**
 * Main TravelSync Engine: Coordinates all DSA Course Outcome modules (CO1 to CO6)
 * and powers the intelligent travel planning workflows.
 */
public class TravelSyncEngine {

    private final DataRepository repository;
    private final ProblemClassifier classifier;
    private final DestinationSearchEngine searchEngine;
    private final ItineraryOptimizer itineraryOptimizer;
    private final TransportFlowAnalyzer flowAnalyzer;

    public TravelSyncEngine() {
        this.repository = new DataRepository();
        this.repository.initialize();
        this.classifier = new ProblemClassifier();
        this.searchEngine = new DestinationSearchEngine(repository);
        this.itineraryOptimizer = new ItineraryOptimizer(repository);
        this.flowAnalyzer = new TransportFlowAnalyzer(repository);
    }

    public DataRepository getRepository() { return repository; }
    public ProblemClassifier getClassifier() { return classifier; }
    public DestinationSearchEngine getSearchEngine() { return searchEngine; }
    public ItineraryOptimizer getItineraryOptimizer() { return itineraryOptimizer; }
    public TransportFlowAnalyzer getFlowAnalyzer() { return flowAnalyzer; }

    // -------------------------------------------------------------
    // CO1: Problem Classification & Algorithm Selection
    // -------------------------------------------------------------
    public AlgorithmSignature classifyTask(ProblemType type) {
        return classifier.getSignature(type);
    }

    public AlgorithmSignature classifyUserQuery(String query) {
        return classifier.classifyQuery(query);
    }

    // -------------------------------------------------------------
    // CO2: String Matching & Suffix Processing
    // -------------------------------------------------------------
    public List<DestinationSearchEngine.SearchResult> searchDestinations(String query) {
        return searchEngine.search(query);
    }

    public KasaiLCP.CommonSubstringResult analyzeReviewSimilarity(String reviewText1, String reviewText2) {
        return KasaiLCP.findLongestCommonSubstring(reviewText1, reviewText2);
    }

    // -------------------------------------------------------------
    // CO3: Dynamic Programming & Itinerary Optimization
    // -------------------------------------------------------------
    public ItineraryPlan optimizeItinerary(TravelerPreferences prefs) {
        return itineraryOptimizer.planItinerary(prefs);
    }

    // -------------------------------------------------------------
    // CO4: Network Flow, Min Cut & Matching
    // -------------------------------------------------------------
    public TransportFlowAnalyzer.FlowAnalysisReport analyzeTransitFlow(String fromCity, String toCity) {
        return flowAnalyzer.analyzeFlow(fromCity, toCity);
    }

    public BipartiteMatching.MatchingResult matchHotelRooms(String city, List<String> groupNames) {
        List<Hotel> cityHotels = repository.getHotelsForCity(city);
        List<String> hotelNames = new ArrayList<>();
        for (Hotel h : cityHotels) {
            for (int r = 1; r <= Math.min(h.getAvailableRooms(), 3); r++) {
                hotelNames.add(h.getName() + " [Room #" + r + " - ₹" + h.getPricePerNight() + "]");
            }
        }

        boolean[][] compat = new boolean[groupNames.size()][hotelNames.size()];
        for (int i = 0; i < groupNames.size(); i++) {
            for (int j = 0; j < hotelNames.size(); j++) {
                compat[i][j] = true; // Compatible
            }
        }

        return BipartiteMatching.computeMatching(groupNames, hotelNames, compat);
    }

    // -------------------------------------------------------------
    // CO5: NP-Hard Reductions & Approximation
    // -------------------------------------------------------------
    public VertexCoverApproximation.ApproxResult solveTransitMonitoringVertexCover(int numHubs, boolean[][] connectionGrid) {
        return VertexCoverApproximation.solve(numHubs, connectionGrid);
    }

    // -------------------------------------------------------------
    // CO6: Randomized & Parallel Algorithms
    // -------------------------------------------------------------
    public ReservoirSampling.SampleResult<Booking> sampleRecentBookings(int sampleSize) {
        return ReservoirSampling.sample(repository.getAllBookings(), sampleSize);
    }

    public MillerRabin.PrimalityResult verifyBookingSecurityToken(BigInteger token, int rounds) {
        return MillerRabin.isPrime(token, rounds);
    }

    public ParallelReduce.ReductionResult aggregateBookingRevenuesParallel() {
        List<Booking> bookings = repository.getAllBookings();
        long[] revenues = new long[bookings.size()];
        for (int i = 0; i < bookings.size(); i++) {
            revenues[i] = bookings.get(i).getTotalTransactionValue();
        }
        return ParallelReduce.parallelSum(revenues);
    }

    public BlellochScan.ScanResult computeCumulativeBookingDemandParallel() {
        List<Booking> bookings = repository.getAllBookings();
        long[] demands = new long[bookings.size()];
        for (int i = 0; i < bookings.size(); i++) {
            demands[i] = bookings.get(i).getDemandWeight();
        }
        return BlellochScan.scan(demands);
    }
}
