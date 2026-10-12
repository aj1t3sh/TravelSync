package travelsync.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import travelsync.api.dto.TripPlanRequest;
import travelsync.api.dto.TripPlanResponse;
import travelsync.api.service.AlgorithmPlaygroundService;
import travelsync.api.service.DatasetAnalyticsService;
import travelsync.api.service.TripPersistenceService;
import travelsync.api.service.TripPlannerService;
import travelsync.data.DataRepository;
import travelsync.model.*;
import travelsync.planner.DestinationSearchEngine;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;

/**
 * High-performance, zero-disruption Java 17 REST API Server.
 * Exposes all TravelSync DSA engine capabilities over standard HTTP endpoints.
 */
public class ApiServer {

    private final int port;
    private final DataRepository repository;
    private final DestinationSearchEngine searchEngine;
    private final TripPlannerService tripPlannerService;
    private final TripPersistenceService persistenceService;
    private final DatasetAnalyticsService analyticsService;
    private final AlgorithmPlaygroundService playgroundService;
    private final Gson gson;
    private HttpServer server;

    public ApiServer(int port) {
        this.port = port;
        this.repository = new DataRepository();
        this.repository.initialize();
        this.searchEngine = new DestinationSearchEngine(repository);
        this.tripPlannerService = new TripPlannerService(repository);
        this.persistenceService = new TripPersistenceService();
        this.analyticsService = new DatasetAnalyticsService(repository);
        this.playgroundService = new AlgorithmPlaygroundService(repository);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newCachedThreadPool());

        // Register REST handlers
        server.createContext("/api/health", new HealthHandler());
        server.createContext("/health", new HealthHandler());
        server.createContext("/api/destinations/search", new DestinationSearchHandler());
        server.createContext("/api/destinations", new DestinationsHandler());
        server.createContext("/api/hotels", new HotelsHandler());
        server.createContext("/api/attractions", new AttractionsHandler());
        server.createContext("/api/trips/plan", new TripPlanHandler());
        server.createContext("/api/trips/optimize", new TripOptimizeHandler());
        server.createContext("/api/trips/save", new TripSaveHandler());
        server.createContext("/api/trips", new TripsHandler());
        server.createContext("/api/analytics/summary", new AnalyticsHandler());
        server.createContext("/api/engine/algorithms", new AlgorithmsMetadataHandler());
        server.createContext("/api/engine/execute", new AlgorithmExecutionHandler());
        server.createContext("/", new RootHandler());

        server.start();
        System.out.println("================================================================================");
        System.out.println("   ✓ TravelSync REST API Server successfully started on port " + port);
        System.out.println("   ✓ Ingested " + repository.getAllDestinations().size() + " Destinations, "
                + repository.getAllHotels().size() + " Hotels, "
                + repository.getAllAttractions().size() + " Attractions, "
                + repository.getAllTransportConnections().size() + " Transport Routes.");
        System.out.println("   ✓ Endpoints available at: http://localhost:" + port + "/api/health");
        System.out.println("================================================================================");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 8080;
        String envPort = System.getenv("PORT");
        if (envPort != null && !envPort.isEmpty()) {
            try {
                port = Integer.parseInt(envPort);
            } catch (NumberFormatException ignored) {}
        }
        ApiServer server = new ApiServer(port);
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down TravelSync REST Server...");
            server.stop();
        }));
    }

    // -------------------------------------------------------------------------
    // HTTP Helpers (CORS, JSON, Parsing)
    // -------------------------------------------------------------------------
    private void setCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
    }

    private boolean handlePreflight(HttpExchange exchange) throws IOException {
        setCorsHeaders(exchange);
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return true;
        }
        return false;
    }

    private void sendJsonResponse(HttpExchange exchange, int statusCode, Object data) throws IOException {
        setCorsHeaders(exchange);
        byte[] bytes = gson.toJson(data).getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        Map<String, Object> err = new LinkedHashMap<>();
        err.put("error", true);
        err.put("statusCode", statusCode);
        err.put("message", message);
        sendJsonResponse(exchange, statusCode, err);
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null || query.isEmpty()) return map;
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx > 0) {
                String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                String val = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                map.put(key, val);
            }
        }
        return map;
    }

    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            Scanner s = new Scanner(is, StandardCharsets.UTF_8).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }

    // -------------------------------------------------------------------------
    // Route Handlers
    // -------------------------------------------------------------------------

    private class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handlePreflight(exchange)) return;
            Map<String, Object> resp = new LinkedHashMap<>();
            resp.put("status", "UP");
            resp.put("application", "TravelSync - Intelligent Travel Planning & Optimization Platform");
            resp.put("version", "1.0.0");
            resp.put("dataLoaded", repository.isLoaded());
            resp.put("timestamp", System.currentTimeMillis());
            sendJsonResponse(exchange, 200, resp);
        }
    }

    private class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handlePreflight(exchange)) return;
            String path = exchange.getRequestURI().getPath();
            if (!"/".equals(path)) {
                sendErrorResponse(exchange, 404, "Endpoint not found: " + path);
                return;
            }
            Map<String, Object> resp = new LinkedHashMap<>();
            resp.put("status", "UP");
            resp.put("service", "TravelSync REST API Server");
            resp.put("version", "1.0.0");
            resp.put("health", "/api/health");
            resp.put("destinations", "/api/destinations");
            resp.put("analytics", "/api/analytics/summary");
            resp.put("algorithms", "/api/engine/algorithms");
            resp.put("timestamp", System.currentTimeMillis());
            sendJsonResponse(exchange, 200, resp);
        }
    }


    private class DestinationSearchHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handlePreflight(exchange)) return;
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());
            String query = params.getOrDefault("q", "");
            List<DestinationSearchEngine.SearchResult> results = searchEngine.search(query);
            sendJsonResponse(exchange, 200, results);
        }
    }

    private class DestinationsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handlePreflight(exchange)) return;
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            String path = exchange.getRequestURI().getPath();
            // Check for /api/destinations/{id}
            if (path.length() > "/api/destinations/".length()) {
                String destId = path.substring("/api/destinations/".length()).trim();
                Destination d = repository.findDestinationById(destId);
                if (d == null) {
                    d = repository.findDestinationByName(destId);
                }
                if (d == null) {
                    sendErrorResponse(exchange, 404, "Destination not found for id/name: " + destId);
                    return;
                }

                Map<String, Object> detail = new LinkedHashMap<>();
                detail.put("destination", d);
                detail.put("attractions", repository.getAttractionsForDestination(d.getId()));
                detail.put("hotels", repository.getHotelsForCity(d.getCity()));
                detail.put("reviews", repository.getReviewsForDestination(d.getId()));
                detail.put("outboundTransport", repository.getOutboundTransport(d.getCity()));

                // Inbound transport
                List<TransportConnection> inbound = new ArrayList<>();
                for (TransportConnection t : repository.getAllTransportConnections()) {
                    if (t.getToCity().equalsIgnoreCase(d.getCity())) {
                        inbound.add(t);
                    }
                }
                detail.put("inboundTransport", inbound);

                sendJsonResponse(exchange, 200, detail);
                return;
            }

            // List destinations with optional filters
            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());
            String season = params.get("season");
            String price = params.get("priceRange");
            String sort = params.get("sort");

            List<Destination> list = new ArrayList<>(repository.getAllDestinations());
            if (season != null && !season.isEmpty()) {
                list.removeIf(d -> !d.getBestSeason().toLowerCase().contains(season.toLowerCase()));
            }
            if (price != null && !price.isEmpty()) {
                list.removeIf(d -> !d.getPriceRange().equalsIgnoreCase(price));
            }

            if ("rating".equalsIgnoreCase(sort)) {
                list.sort((a, b) -> Double.compare(b.getRating(), a.getRating()));
            } else if ("reviews".equalsIgnoreCase(sort)) {
                list.sort((a, b) -> Integer.compare(b.getReviewCount(), a.getReviewCount()));
            } else if ("name".equalsIgnoreCase(sort)) {
                list.sort(Comparator.comparing(Destination::getName));
            }

            sendJsonResponse(exchange, 200, list);
        }
    }

    private class HotelsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handlePreflight(exchange)) return;
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());
            String city = params.get("city");
            String type = params.get("type");

            List<Hotel> list = city != null && !city.isEmpty()
                    ? new ArrayList<>(repository.getHotelsForCity(city))
                    : new ArrayList<>(repository.getAllHotels());

            if (type != null && !type.isEmpty()) {
                list.removeIf(h -> !h.getHotelType().equalsIgnoreCase(type));
            }

            sendJsonResponse(exchange, 200, list);
        }
    }

    private class AttractionsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handlePreflight(exchange)) return;
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());
            String city = params.get("city");
            String destId = params.get("destinationId");
            String cat = params.get("category");

            List<Attraction> list;
            if (destId != null && !destId.isEmpty()) {
                list = new ArrayList<>(repository.getAttractionsForDestination(destId));
            } else if (city != null && !city.isEmpty()) {
                list = new ArrayList<>(repository.getAttractionsForCity(city));
            } else {
                list = new ArrayList<>(repository.getAllAttractions());
            }

            if (cat != null && !cat.isEmpty()) {
                list.removeIf(a -> !a.getCategory().equalsIgnoreCase(cat));
            }

            sendJsonResponse(exchange, 200, list);
        }
    }

    private class TripPlanHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handlePreflight(exchange)) return;
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            try {
                String body = readRequestBody(exchange);
                TripPlanRequest request = gson.fromJson(body, TripPlanRequest.class);
                if (request == null) {
                    request = new TripPlanRequest();
                }

                TripPlanResponse plan = tripPlannerService.planTrip(request);
                persistenceService.saveTrip(plan);
                sendJsonResponse(exchange, 200, plan);
            } catch (Exception e) {
                e.printStackTrace();
                sendErrorResponse(exchange, 500, "Error generating travel plan: " + e.getMessage());
            }
        }
    }

    private class TripOptimizeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handlePreflight(exchange)) return;
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            try {
                String body = readRequestBody(exchange);
                TripPlanRequest request = gson.fromJson(body, TripPlanRequest.class);
                if (request == null) request = new TripPlanRequest();

                TripPlanResponse plan = tripPlannerService.planTrip(request);
                sendJsonResponse(exchange, 200, plan);
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, "Optimization failure: " + e.getMessage());
            }
        }
    }

    private class TripSaveHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handlePreflight(exchange)) return;
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            try {
                String body = readRequestBody(exchange);
                TripPlanResponse trip = gson.fromJson(body, TripPlanResponse.class);
                if (trip != null && trip.getTripId() != null) {
                    persistenceService.saveTrip(trip);
                    Map<String, Object> resp = new HashMap<>();
                    resp.put("saved", true);
                    resp.put("tripId", trip.getTripId());
                    sendJsonResponse(exchange, 200, resp);
                } else {
                    sendErrorResponse(exchange, 400, "Invalid trip payload");
                }
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, e.getMessage());
            }
        }
    }

    private class TripsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handlePreflight(exchange)) return;

            String path = exchange.getRequestURI().getPath();
            if (path.length() > "/api/trips/".length()) {
                String tripId = path.substring("/api/trips/".length()).trim();
                TripPlanResponse trip = persistenceService.getTrip(tripId);
                if (trip != null) {
                    sendJsonResponse(exchange, 200, trip);
                } else {
                    sendErrorResponse(exchange, 404, "Trip not found with id: " + tripId);
                }
                return;
            }

            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 200, persistenceService.getAllSavedTrips());
            } else {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
            }
        }
    }

    private class AnalyticsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handlePreflight(exchange)) return;
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            Map<String, Object> data = analyticsService.getAnalyticsSummary();
            sendJsonResponse(exchange, 200, data);
        }
    }

    private class AlgorithmsMetadataHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handlePreflight(exchange)) return;
            List<Map<String, Object>> list = new ArrayList<>();

            addAlgo(list, "CO1", "ProblemClassifier & ComplexityEvaluator", "Decision Tree & Dynamic Signature Matching", "O(1)", "O(1)", "Classifies travel queries into exact computational complexity classes.");
            addAlgo(list, "CO2", "Knuth-Morris-Pratt (KMP)", "Exact Linear String Pattern Matcher via Prefix/LPS Array", "O(N + M)", "O(M)", "Searches destination names and descriptions in strictly linear time without backtracking.");
            addAlgo(list, "CO2", "Rabin-Karp Rolling Hash", "Polynomial Rolling Hash with Double Hashing & Collision Verification", "O(N + M) exp", "O(1)", "Fast multi-keyword and phrase searching using modular polynomial hashing.");
            addAlgo(list, "CO2", "Aho-Corasick Multi-Pattern Search", "Automaton Trie with BFS Failure & Output Links", "O(N + sum|Pi| + Z)", "O(sum|Pi|)", "Simultaneously scans traveler interest tags against entire tourist catalog in a single linear pass.");
            addAlgo(list, "CO2", "Suffix Array & Kasai LCP", "Doubling Construction O(N log^2 N) & Kasai Linear LCP O(N)", "O(N log^2 N) / O(N)", "O(N)", "Computes longest common substrings and sentiment review similarity.");
            addAlgo(list, "CO3", "Wagner-Fischer (Levenshtein & Damerau)", "Dynamic Programming Matrix with Character Transpositions", "O(N · M)", "O(min(N,M))", "Fuzzy destination search and typo tolerance with distance penalty calculations.");
            addAlgo(list, "CO3", "Held-Karp Bitmask DP (TSP)", "State Compression DP over 2^K Bitmasks for Tour Scheduling", "O(K^2 · 2^K)", "O(K · 2^K)", "Finds provably optimal multi-stop itinerary tour sequence minimizing transit distance.");
            addAlgo(list, "CO4", "Dinic & Edmonds-Karp Residual Flow Networks", "Breadth-First Shortest Augmenting Paths & Level Graphs", "O(V^2 · E)", "O(V + E)", "Evaluates passenger transit capacity and Max-Flow Min-Cut bottleneck isolation.");
            addAlgo(list, "CO4", "Bipartite Matching & König's Duality", "Unit Flow Reduction Matching & Minimum Vertex Cover", "O(V · E)", "O(V + E)", "Assigns traveler party room requirements to available destination hotel units.");
            addAlgo(list, "CO5", "Cook-Levin Polynomial Reduction Chain", "3-SAT <=p CLIQUE <=p INDEPENDENT SET <=p VERTEX COVER", "O(V^2)", "O(V^2)", "Formal complexity demonstration and conflict mapping.");
            addAlgo(list, "CO5", "Vertex Cover 2-Approximation", "Maximal Matching with Provable 2.0x Performance Guarantee", "O(V + E)", "O(V + E)", "Isolates critical transit monitoring hubs in linear time with provable bounds.");
            addAlgo(list, "CO5", "Knapsack 1/2-Approximation vs Exact DP", "Density Greedy Selection with Provable >= 0.50x Guarantee", "O(N log N)", "O(1)", "Maximizes tourist attraction yield within entry fee budget constraints.");
            addAlgo(list, "CO6", "Miller-Rabin Probabilistic Primality Test", "Monte Carlo Witness Rounds with Carmichael Immunity", "O(k log^3 N)", "O(1)", "Generates and validates cryptographic booking verification tokens with error bound <= 4^-k.");
            addAlgo(list, "CO6", "ForkJoin Parallel Tree Reduction", "Divide-and-Conquer Work-Span Model on Multi-Core CPU", "Work T1=O(N), Span Tinf=O(log N)", "O(log N)", "Aggregates revenue and passenger transit demand across transaction logs in parallel.");
            addAlgo(list, "CO6", "Blelloch Parallel Prefix Sum (Scan)", "Two-Phase Up-Sweep (Reduce) & Down-Sweep Tree Passes", "Work T1=O(N), Span Tinf=O(log N)", "O(N)", "Computes cumulative demand curves and streaming transaction windows in parallel.");

            sendJsonResponse(exchange, 200, list);
        }

        private void addAlgo(List<Map<String, Object>> list, String co, String name, String paradigm, String time, String space, String desc) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("courseOutcome", co);
            m.put("name", name);
            m.put("paradigm", paradigm);
            m.put("timeComplexity", time);
            m.put("spaceComplexity", space);
            m.put("description", desc);
            list.add(m);
        }
    }

    private class AlgorithmExecutionHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handlePreflight(exchange)) return;
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            try {
                String body = readRequestBody(exchange);
                @SuppressWarnings("unchecked")
                Map<String, Object> req = gson.fromJson(body, Map.class);
                String algoId = (String) req.getOrDefault("algorithmId", "CLASSIFY");

                @SuppressWarnings("unchecked")
                Map<String, String> params = (Map<String, String>) req.getOrDefault("params", Collections.emptyMap());
                if (params == null) params = Collections.emptyMap();

                Map<String, Object> res = playgroundService.execute(algoId, params);
                sendJsonResponse(exchange, 200, res);
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, "Algorithm execution failed: " + e.getMessage());
            }
        }
    }
}
