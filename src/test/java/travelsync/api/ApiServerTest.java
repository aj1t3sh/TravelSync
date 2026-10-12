package travelsync.api;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import travelsync.api.dto.TripPlanRequest;
import travelsync.api.dto.TripPlanResponse;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ApiServerTest {

    private static ApiServer server;
    private static int port;
    private static final Gson gson = new Gson();

    @BeforeAll
    public static void setUp() throws Exception {
        // Find a random available port
        try (ServerSocket socket = new ServerSocket(0)) {
            port = socket.getLocalPort();
        }
        server = new ApiServer(port);
        server.start();
    }

    @AfterAll
    public static void tearDown() {
        if (server != null) {
            server.stop();
        }
    }

    private String get(String path) throws Exception {
        URL url = new URL("http://localhost:" + port + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        assertEquals(200, conn.getResponseCode(), "Path failed: " + path);
        try (InputStream is = conn.getInputStream()) {
            return new Scanner(is, StandardCharsets.UTF_8).useDelimiter("\\A").next();
        }
    }

    private String post(String path, Object body) throws Exception {
        URL url = new URL("http://localhost:" + port + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        String json = gson.toJson(body);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        assertEquals(200, conn.getResponseCode(), "POST failed: " + path);
        try (InputStream is = conn.getInputStream()) {
            return new Scanner(is, StandardCharsets.UTF_8).useDelimiter("\\A").next();
        }
    }

    @Test
    public void testHealthEndpoint() throws Exception {
        String resp = get("/api/health");
        assertTrue(resp.contains("\"status\": \"UP\""));
        assertTrue(resp.contains("\"dataLoaded\": true"));

        // Alternative health endpoint
        String respAlt = get("/health");
        assertTrue(respAlt.contains("\"status\": \"UP\""));

        // Root information endpoint
        String respRoot = get("/");
        assertTrue(respRoot.contains("\"service\": \"TravelSync REST API Server\""));
    }

    @Test
    public void testDestinationsEndpoints() throws Exception {
        String listJson = get("/api/destinations");
        assertTrue(listJson.contains("Hyderabad"));
        assertTrue(listJson.contains("Goa"));

        String detailJson = get("/api/destinations/DEST001");
        assertTrue(detailJson.contains("Charminar"));
        assertTrue(detailJson.contains("Taj Falaknuma Palace"));
    }

    @Test
    public void testDestinationSearch() throws Exception {
        String searchJson = get("/api/destinations/search?q=Goa");
        assertTrue(searchJson.contains("Goa"));

        // Typo search test (Wagner-Fischer fuzzy matching)
        String fuzzyJson = get("/api/destinations/search?q=Hyderbad");
        assertTrue(fuzzyJson.contains("Hyderabad"));
    }

    @Test
    public void testHotelsAndAttractions() throws Exception {
        String hotels = get("/api/hotels?city=Hyderabad");
        assertTrue(hotels.contains("Taj Falaknuma Palace"));

        String attractions = get("/api/attractions?city=Hyderabad");
        assertTrue(attractions.contains("Charminar"));
    }

    @Test
    public void testPlanTripFeasible() throws Exception {
        TripPlanRequest req = new TripPlanRequest();
        req.setOrigin("Hyderabad");
        req.setDestinations(Arrays.asList("Goa", "Mumbai"));
        req.setDays(4);
        req.setTravelers(2);
        req.setBudgetInr(60000);
        req.setInterests(Arrays.asList("nature", "beaches", "history"));
        req.setTransportPreference("ANY");
        req.setAccommodationPreference("Comfort");

        String respJson = post("/api/trips/plan", req);
        TripPlanResponse resp = gson.fromJson(respJson, TripPlanResponse.class);

        assertNotNull(resp);
        assertNotNull(resp.getTripId());
        assertTrue(resp.isFeasible());
        assertEquals("Hyderabad", resp.getOrigin());
        assertFalse(resp.getRouteStops().isEmpty());
        assertEquals(4, resp.getDailyPlans().size());
        assertFalse(resp.getAlgorithmTransparency().isEmpty());

        // Verify CO1 to CO6 traces exist
        boolean hasCO1 = resp.getAlgorithmTransparency().stream().anyMatch(c -> c.getCourseOutcome().contains("CO1"));
        boolean hasCO2or3 = resp.getAlgorithmTransparency().stream().anyMatch(c -> c.getCourseOutcome().contains("CO2") || c.getCourseOutcome().contains("CO3"));
        boolean hasCO4 = resp.getAlgorithmTransparency().stream().anyMatch(c -> c.getCourseOutcome().contains("CO4"));
        boolean hasCO5 = resp.getAlgorithmTransparency().stream().anyMatch(c -> c.getCourseOutcome().contains("CO5"));
        boolean hasCO6 = resp.getAlgorithmTransparency().stream().anyMatch(c -> c.getCourseOutcome().contains("CO6"));

        assertTrue(hasCO1, "Must contain CO1 Problem Classification");
        assertTrue(hasCO2or3, "Must contain CO2/CO3 Search or DP");
        assertTrue(hasCO4, "Must contain CO4 Max Flow or Matching");
        assertTrue(hasCO5, "Must contain CO5 Knapsack Approximation");
        assertTrue(hasCO6, "Must contain CO6 Parallel Reduce / Miller-Rabin");

        // Verify persistence
        String savedTrip = get("/api/trips/" + resp.getTripId());
        assertTrue(savedTrip.contains(resp.getTripId()));
    }

    @Test
    public void testPlanTripInfeasibleBudgetExplainsReason() throws Exception {
        TripPlanRequest req = new TripPlanRequest();
        req.setOrigin("Hyderabad");
        req.setDestinations(Collections.singletonList("New Delhi"));
        req.setDays(5);
        req.setTravelers(4);
        req.setBudgetInr(2000); // Infeasible for 4 travelers over 5 days
        req.setTransportPreference("FLIGHT");

        String respJson = post("/api/trips/plan", req);
        TripPlanResponse resp = gson.fromJson(respJson, TripPlanResponse.class);

        assertNotNull(resp);
        assertFalse(resp.isFeasible(), "Plan must be marked infeasible when budget is violated");
        assertNotNull(resp.getInfeasibilityReason());
        assertTrue(resp.getInfeasibilityReason().contains("exceeds your target budget"));
        assertNotNull(resp.getClosestFeasibleAlternative());
    }

    @Test
    public void testAnalyticsSummary() throws Exception {
        String analytics = get("/api/analytics/summary");
        assertTrue(analytics.contains("inventoryCounts"));
        assertTrue(analytics.contains("attractionsByCategory"));
        assertTrue(analytics.contains("co6ParallelMetrics"));
    }

    @Test
    public void testAlgorithmPlayground() throws Exception {
        Map<String, Object> req = new HashMap<>();
        req.put("algorithmId", "STRING_MATCH_COMPARE");
        Map<String, String> params = new HashMap<>();
        params.put("text", "Charminar in Hyderabad is historic in Hyderabad");
        params.put("pattern", "Hyderabad");
        req.put("params", params);

        String execJson = post("/api/engine/execute", req);
        assertTrue(execJson.contains("kmpComparisons"));
        assertTrue(execJson.contains("naiveComparisons"));
    }
}
