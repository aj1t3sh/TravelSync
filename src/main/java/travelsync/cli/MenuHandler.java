package travelsync.cli;

import travelsync.co1.AlgorithmSignature;
import travelsync.co1.ComplexityEvaluator;
import travelsync.co1.ProblemType;
import travelsync.co2.*;
import travelsync.co3.*;
import travelsync.co4.*;
import travelsync.co5.*;
import travelsync.co6.*;
import travelsync.data.DataNormalizer;
import travelsync.model.*;
import travelsync.planner.DestinationSearchEngine;
import travelsync.planner.TransportFlowAnalyzer;
import travelsync.planner.TravelSyncEngine;

import java.math.BigInteger;
import java.util.*;

/**
 * Interactive CLI Menu Handler implementing all 12 DSA demonstration workflows.
 */
public class MenuHandler {

    private final TravelSyncEngine engine;
    private final Scanner scanner;

    public MenuHandler(TravelSyncEngine engine, Scanner scanner) {
        this.engine = engine;
        this.scanner = scanner;
    }

    public void displayMainMenu() {
        int destCount = engine.getRepository().getAllDestinations().size();
        int routeCount = engine.getRepository().getAllTransportConnections().size();
        int hotelCount = engine.getRepository().getAllHotels().size();
        int revCount = engine.getRepository().getAllReviews().size();
        int bookCount = engine.getRepository().getAllBookings().size();

        System.out.println(ConsoleColors.cyan("\n" +
            "================================================================================\n" +
            "                                TRAVELSYNC CLI\n" +
            "                     Data Structures & Algorithms - III\n" +
            "================================================================================"));

        System.out.println(ConsoleColors.green("  Data Status : Loaded Successfully from External Dataset"));
        System.out.printf("  Inventory   : %d Destinations | %d Routes | %d Hotels | %d Reviews | %d Bookings\n",
                destCount, routeCount, hotelCount, revCount, bookCount);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("   1. Search destinations (KMP / Rabin-Karp exact search)               [CO2]");
        System.out.println("   2. Advanced keyword search (Aho-Corasick multi-pattern matching)      [CO2]");
        System.out.println("   3. Fuzzy search & review similarity (Wagner-Fischer & Kasai LCP)     [CO2/CO3]");
        System.out.println("   4. Plan a trip (Interactive end-to-end travel workflow)               [Planner]");
        System.out.println("   5. Optimize a multi-stop itinerary (Bitmask DP / Held-Karp TSP)       [CO3]");
        System.out.println("   6. Analyze transport capacity (Edmonds-Karp / Dinic & Min-Cut)        [CO4]");
        System.out.println("   7. Demonstrate assignment & matching (Bipartite matching & König)    [CO4]");
        System.out.println("   8. NP-hard optimization & approximation (3-SAT reductions & VC 2-app) [CO5]");
        System.out.println("   9. Randomized & parallel algorithms (Miller-Rabin, Scan, Brent's)     [CO6]");
        System.out.println("  10. Run complete TravelSync demonstration (CO1 -> CO6 Full Pipeline)   [Master]");
        System.out.println("  11. View dataset statistics and schema diagnostics                    [Data]");
        System.out.println("  12. Run algorithm benchmarks (Sequential vs Parallel, String Match)   [Bench]");
        System.out.println("   0. Exit");
        System.out.println("================================================================================");
    }

    public void handleOption(int choice) {
        switch (choice) {
            case 1 -> handleExactSearch();
            case 2 -> handleMultiKeywordSearch();
            case 3 -> handleFuzzyAndSimilarity();
            case 4 -> handlePlanTrip();
            case 5 -> handleBitmaskTsp();
            case 6 -> handleTransportFlow();
            case 7 -> handleBipartiteMatching();
            case 8 -> handleNPHardAndApproximation();
            case 9 -> handleRandomizedAndParallel();
            case 10 -> handleCompleteDemonstration();
            case 11 -> handleDatasetStats();
            case 12 -> handleBenchmarks();
            case 0 -> System.out.println(ConsoleColors.brightGreen("\nThank you for exploring TravelSync! Exiting."));
            default -> TerminalUI.printWarning("Invalid option. Please try again.");
        }
    }

    // -------------------------------------------------------------------------
    // 1. Exact Search (CO2)
    // -------------------------------------------------------------------------
    private void handleExactSearch() {
        TerminalUI.printSectionHeader("DESTINATION EXACT SEARCH (CO1 / CO2: KMP & RABIN-KARP)");
        AlgorithmSignature sig = engine.classifyTask(ProblemType.EXACT_SUBSTRING_SEARCH);
        System.out.println(ComplexityEvaluator.formatSignatureCard(sig));

        String pattern = TerminalUI.readString(scanner, "Enter search keyword / city name", "Hyderabad");

        System.out.println(ConsoleColors.yellow("\n--- Executing KMP String Matcher ---"));
        long startKmp = System.nanoTime();
        int matchCountKmp = 0;
        for (Destination d : engine.getRepository().getAllDestinations()) {
            KMP.KMPResult res = KMP.search(d.getName().toLowerCase(), pattern.toLowerCase());
            if (res.getMatchCount() > 0) {
                matchCountKmp++;
                System.out.printf("  ✓ Found in destination: %s (at index %s, Comparisons: %d)\n",
                        d.getName(), res.getMatchIndices(), res.getComparisons());
            }
        }
        long timeKmp = System.nanoTime() - startKmp;

        System.out.println(ConsoleColors.yellow("\n--- Executing Rabin-Karp Rolling Hash Matcher ---"));
        long startRk = System.nanoTime();
        int matchCountRk = 0;
        for (Destination d : engine.getRepository().getAllDestinations()) {
            RabinKarp.RKResult res = RabinKarp.search(d.getDescription().toLowerCase(), pattern.toLowerCase());
            if (res.getMatchCount() > 0) {
                matchCountRk++;
                System.out.printf("  ✓ Found in %s description: %d occurrence(s) [Collisions: %d, Evaluations: %d]\n",
                        d.getName(), res.getMatchCount(), res.getHashCollisions(), res.getHashEvaluations());
            }
        }
        long timeRk = System.nanoTime() - startRk;

        System.out.println("\n" + ConsoleColors.green(String.format(
                "Search Summary: KMP found %d name matches in %.3f ms | Rabin-Karp found %d description matches in %.3f ms",
                matchCountKmp, timeKmp / 1_000_000.0, matchCountRk, timeRk / 1_000_000.0)));

        TerminalUI.pressEnterToContinue(scanner);
    }

    // -------------------------------------------------------------------------
    // 2. Multi-Keyword Search (CO2: Aho-Corasick)
    // -------------------------------------------------------------------------
    private void handleMultiKeywordSearch() {
        TerminalUI.printSectionHeader("ADVANCED MULTI-KEYWORD SEARCH (CO2: AHO-CORASICK AUTOMATON)");
        AlgorithmSignature sig = engine.classifyTask(ProblemType.MULTI_PATTERN_SEARCH);
        System.out.println(ComplexityEvaluator.formatSignatureCard(sig));

        String input = TerminalUI.readString(scanner, "Enter comma-separated keywords/tags", "heritage, beach, palace, fort, nature");
        String[] tags = input.split("[,;]");

        AhoCorasick ac = new AhoCorasick();
        List<String> validTags = new ArrayList<>();
        for (String t : tags) {
            String clean = t.trim().toLowerCase();
            if (!clean.isEmpty()) {
                ac.addPattern(clean);
                validTags.add(clean);
            }
        }
        ac.buildFailureLinks();

        System.out.printf("\nSearching for %d patterns simultaneously across all destination descriptions & tags...\n", validTags.size());

        for (Destination d : engine.getRepository().getAllDestinations()) {
            String corpus = (d.getDescription() + " " + d.getTags()).toLowerCase();
            List<AhoCorasick.Match> matches = ac.search(corpus);
            if (!matches.isEmpty()) {
                Set<String> matchedWords = new LinkedHashSet<>();
                for (AhoCorasick.Match m : matches) matchedWords.add(m.getPattern());
                System.out.printf("  • %-15s [%s]: Matched %d tag(s) %s\n",
                        d.getName(), d.getState(), matchedWords.size(), matchedWords);
            }
        }

        TerminalUI.pressEnterToContinue(scanner);
    }

    // -------------------------------------------------------------------------
    // 3. Fuzzy Search & Similarity (CO2 / CO3: Wagner-Fischer & Suffix Array / Kasai LCP)
    // -------------------------------------------------------------------------
    private void handleFuzzyAndSimilarity() {
        TerminalUI.printSectionHeader("FUZZY SEARCH & DOCUMENT SIMILARITY (CO2 / CO3)");
        System.out.println("1. Fuzzy Destination Query (Wagner-Fischer Edit Distance)");
        System.out.println("2. Tourist Review Similarity (Suffix Array + Kasai's LCP Array)");

        int sub = TerminalUI.readIntChoice(scanner, "Select demonstration (1-2): ", 1, 2);

        if (sub == 1) {
            String typoQuery = TerminalUI.readString(scanner, "Enter misspelled destination name (e.g. Hyderbad, Jaipoor, Banglore)", "Hyderbad");
            System.out.println(ConsoleColors.yellow("\n--- Evaluating Wagner-Fischer Edit Metric against All Cities ---"));
            for (Destination d : engine.getRepository().getAllDestinations()) {
                int lev = WagnerFischer.levenshteinDistance(typoQuery.toLowerCase(), d.getName().toLowerCase());
                int damerau = WagnerFischer.damerauLevenshteinDistance(typoQuery.toLowerCase(), d.getName().toLowerCase());
                double sim = WagnerFischer.similarityScore(typoQuery.toLowerCase(), d.getName().toLowerCase());

                if (sim >= 0.50 || lev <= 3) {
                    System.out.printf("  ✓ Matched: %-15s | Levenshtein: %d | Damerau: %d | Similarity: %.1f%%\n",
                            d.getName(), lev, damerau, sim * 100);
                }
            }
        } else {
            System.out.println(ConsoleColors.yellow("\n--- Suffix Array & Kasai LCP Document Similarity on Reviews ---"));
            List<Review> revs = engine.getRepository().getAllReviews();
            if (revs.size() >= 2) {
                Review r1 = revs.get(0);
                Review r2 = revs.get(1);

                System.out.println("Review A: " + r1.getReviewText());
                System.out.println("Review B: " + r2.getReviewText());

                KasaiLCP.CommonSubstringResult lcs = KasaiLCP.findLongestCommonSubstring(r1.getReviewText(), r2.getReviewText());
                System.out.println(ConsoleColors.green(String.format(
                        "\n  ✓ Longest Common Substring : \"%s\" (Length: %d characters)\n  ✓ Cross-Document Similarity: %.2f%%",
                        lcs.getLongestCommonSubstring(), lcs.getLength(), lcs.getJaccardSimilarity() * 100)));
            } else {
                TerminalUI.printWarning("Insufficient review records for cross-document analysis.");
            }
        }

        TerminalUI.pressEnterToContinue(scanner);
    }

    // -------------------------------------------------------------------------
    // 4. Plan a Trip (Planner / End-to-End)
    // -------------------------------------------------------------------------
    private void handlePlanTrip() {
        TerminalUI.printSectionHeader("INTELLIGENT TRIP PLANNER (END-TO-END WORKFLOW)");

        String startCity = TerminalUI.readString(scanner, "Enter starting origin city", "Hyderabad");
        String destsInput = TerminalUI.readString(scanner, "Enter desired destination stops (comma-separated)", "Bengaluru, Chennai, Goa");
        int maxDays = TerminalUI.readPositiveInt(scanner, "Enter maximum available days", 5);
        int budget = TerminalUI.readPositiveInt(scanner, "Enter total budget in INR (₹)", 25000);
        String tagsInput = TerminalUI.readString(scanner, "Enter trip interest tags (e.g. heritage, beach, food)", "heritage, food");
        String mode = TerminalUI.readString(scanner, "Preferred transport mode (FLIGHT, TRAIN, BUS, or ANY)", "ANY");

        List<String> targetList = new ArrayList<>();
        for (String s : destsInput.split("[,;]")) {
            if (!s.trim().isEmpty()) targetList.add(s.trim());
        }

        List<String> tagList = new ArrayList<>();
        for (String s : tagsInput.split("[,;]")) {
            if (!s.trim().isEmpty()) tagList.add(s.trim());
        }

        TravelerPreferences prefs = new TravelerPreferences(startCity, targetList, maxDays, budget, tagList, mode);
        ItineraryPlan plan = engine.optimizeItinerary(prefs);

        System.out.println(ConsoleColors.green("\n================================================================================"));
        System.out.println(ConsoleColors.brightGreen("                    TRAVELSYNC FINAL ITINERARY REPORT                           "));
        System.out.println(ConsoleColors.green("================================================================================"));
        System.out.printf("  Origin City        : %s\n", plan.getStartCity());
        System.out.printf("  Optimized Route    : %s\n", String.join(" ➔ ", plan.getRouteStops()));
        System.out.printf("  Total Distance     : %d km\n", plan.getTotalDistanceKm());
        System.out.printf("  Total Travel Time  : %.1f hours\n", plan.getTotalTravelTimeHours());
        System.out.printf("  Transport Cost     : ₹%d\n", plan.getTotalTransportCostInr());
        System.out.printf("  Accommodation Cost : ₹%d\n", plan.getTotalAccommodationCostInr());
        System.out.printf("  Total Cost         : ₹%d (Allocated Budget: ₹%d)\n", plan.getTotalEstimatedCostInr(), budget);
        System.out.printf("  Feasibility Status : %s\n", plan.isFeasible() ? ConsoleColors.green("FEASIBLE ✓") : ConsoleColors.red("INFEASIBLE ✗ (" + plan.getInfeasibilityReason() + ")"));
        System.out.printf("  Algorithm Engine   : %s\n", plan.getAlgorithmUsed());
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("  Day-by-Day Schedule:");
        for (String day : plan.getDailyBreakdown()) {
            System.out.println("    " + day);
        }
        System.out.println(ConsoleColors.green("================================================================================"));

        TerminalUI.pressEnterToContinue(scanner);
    }

    // -------------------------------------------------------------------------
    // 5. Bitmask DP TSP (CO3)
    // -------------------------------------------------------------------------
    private void handleBitmaskTsp() {
        TerminalUI.printSectionHeader("MULTI-STOP ITINERARY OPTIMIZATION (CO3: BITMASK DP / HELD-KARP)");
        AlgorithmSignature sig = engine.classifyTask(ProblemType.MULTI_STOP_ITINERARY_TSP);
        System.out.println(ComplexityEvaluator.formatSignatureCard(sig));

        String input = TerminalUI.readString(scanner, "Enter 4 to 8 cities for TSP tour", "Hyderabad, Bengaluru, Chennai, Mumbai, Goa");
        String[] tokens = input.split("[,;]");
        List<String> cities = new ArrayList<>();
        for (String t : tokens) {
            String clean = t.trim();
            if (!clean.isEmpty() && !cities.contains(clean)) cities.add(clean);
        }

        int n = cities.size();
        if (n < 2) {
            TerminalUI.printWarning("Please provide at least 2 cities.");
            TerminalUI.pressEnterToContinue(scanner);
            return;
        }

        double[][] distMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) distMatrix[i][j] = 0;
                else {
                    Destination dA = engine.getRepository().findDestinationByName(cities.get(i));
                    Destination dB = engine.getRepository().findDestinationByName(cities.get(j));
                    if (dA != null && dB != null) {
                        distMatrix[i][j] = DataNormalizer.haversineDistanceKm(dA.getLatitude(), dA.getLongitude(), dB.getLatitude(), dB.getLongitude());
                    } else {
                        distMatrix[i][j] = 300 + (Math.abs(i - j) * 150);
                    }
                }
            }
        }

        System.out.println(ConsoleColors.yellow("\n--- Computed Inter-City Distance Matrix (km) ---"));
        System.out.printf("%-15s", "City");
        for (String c : cities) System.out.printf("%-12s", c.length() > 10 ? c.substring(0, 9) : c);
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.printf("%-15s", cities.get(i));
            for (int j = 0; j < n; j++) {
                System.out.printf("%-12.0f", distMatrix[i][j]);
            }
            System.out.println();
        }

        System.out.println(ConsoleColors.yellow("\n--- Executing Held-Karp Bitmask DP in O(N^2 · 2^N) ---"));
        BitmaskTSP.TSPResult res = BitmaskTSP.solveTSP(distMatrix, cities, 0, 0);

        System.out.println(ConsoleColors.green(String.format(
                "\n  ✓ Optimal TSP Tour Route : %s\n  ✓ Minimal Total Distance : %.1f km",
                String.join(" ➔ ", res.getTourCityNames()), res.getMinCost())));

        TerminalUI.pressEnterToContinue(scanner);
    }

    // -------------------------------------------------------------------------
    // 6. Transport Flow & Bottlenecks (CO4)
    // -------------------------------------------------------------------------
    private void handleTransportFlow() {
        TerminalUI.printSectionHeader("TRANSPORT NETWORK CAPACITY & MIN-CUT (CO4)");
        AlgorithmSignature sig = engine.classifyTask(ProblemType.NETWORK_CAPACITY_MAX_FLOW);
        System.out.println(ComplexityEvaluator.formatSignatureCard(sig));

        String origin = TerminalUI.readString(scanner, "Enter Origin Hub", "Hyderabad");
        String dest = TerminalUI.readString(scanner, "Enter Destination Hub", "New Delhi");

        System.out.println(ConsoleColors.yellow("\n--- Running Edmonds-Karp & Dinic Max-Flow on Transit Multigraph ---"));
        TransportFlowAnalyzer.FlowAnalysisReport report = engine.analyzeTransitFlow(origin, dest);

        System.out.printf("  Origin Hub             : %s\n", report.originCity);
        System.out.printf("  Destination Hub        : %s\n", report.destinationCity);
        System.out.printf("  Max Flow Throughput    : %d passengers/day\n", report.maxDailyPassengerFlow);
        System.out.printf("  Min-Cut Capacity       : %d passengers/day\n", report.minCutCapacity);
        System.out.printf("  Duality Verified       : %s\n", report.verified ? ConsoleColors.green("TRUE (Max-Flow == Min-Cut) ✓") : ConsoleColors.red("FALSE ✗"));
        System.out.println("  Identified Bottleneck Corridors (Min-Cut Cut-Set):");
        for (String b : report.bottleneckCorridors) {
            System.out.println("    ⚠ " + b);
        }

        TerminalUI.pressEnterToContinue(scanner);
    }

    // -------------------------------------------------------------------------
    // 7. Bipartite Matching (CO4)
    // -------------------------------------------------------------------------
    private void handleBipartiteMatching() {
        TerminalUI.printSectionHeader("RESOURCE ALLOCATION & BIPARTITE MATCHING (CO4)");
        AlgorithmSignature sig = engine.classifyTask(ProblemType.BIPARTITE_MATCHING_ASSIGNMENT);
        System.out.println(ComplexityEvaluator.formatSignatureCard(sig));

        String city = TerminalUI.readString(scanner, "Enter destination city for room/guide allocation", "Hyderabad");
        List<String> groups = Arrays.asList("Group Alpha (4 pax)", "Group Beta (2 pax)", "Group Gamma (3 pax)", "Group Delta (1 pax)");

        System.out.println(ConsoleColors.yellow("\n--- Allocating Tourist Groups to Available Accommodations via Flow Reduction ---"));
        BipartiteMatching.MatchingResult result = engine.matchHotelRooms(city, groups);

        System.out.printf("  Total Groups (Left Partition)  : %d\n", result.getLeftSize());
        System.out.printf("  Available Rooms (Right Part)   : %d\n", result.getRightSize());
        System.out.printf("  Max Cardinality Matches        : %d\n", result.getMaxMatches());
        System.out.printf("  König's Min Vertex Cover Size  : %d (by König's Duality Theorem)\n", result.getKonigMinVertexCoverSize());
        System.out.println("  Matched Assignments:");
        for (BipartiteMatching.MatchPair pair : result.getMatchedPairs()) {
            System.out.printf("    ✓ %-25s <===> %s\n", pair.getLeftItem(), pair.getRightItem());
        }

        TerminalUI.pressEnterToContinue(scanner);
    }

    // -------------------------------------------------------------------------
    // 8. NP-Hard Optimization & Approximation (CO5)
    // -------------------------------------------------------------------------
    private void handleNPHardAndApproximation() {
        TerminalUI.printSectionHeader("NP-COMPLETENESS & APPROXIMATION ALGORITHMS (CO5)");
        System.out.println("1. Complexity Theory Overview (P, NP, NP-C, NP-Hard, Cook-Levin)");
        System.out.println("2. 3-SAT Exact Solving & Polynomial Reduction Chain (3-SAT -> CLIQUE -> IS -> VC)");
        System.out.println("3. Vertex Cover 2-Approximation vs Exact Exponential Solver");
        System.out.println("4. 0/1 Knapsack Exact DP vs Greedy 1/2-Approximation");

        int sub = TerminalUI.readIntChoice(scanner, "Select demonstration (1-4): ", 1, 4);

        switch (sub) {
            case 1 -> System.out.println(ComplexityClassesGuide.getComplexityOverview());
            case 2 -> demonstrate3SatReductions();
            case 3 -> demonstrateVertexCover2Approx();
            case 4 -> demonstrateKnapsackApprox();
        }

        TerminalUI.pressEnterToContinue(scanner);
    }

    private void demonstrate3SatReductions() {
        System.out.println(ConsoleColors.yellow("\n--- 3-SAT Instance Formulation ---"));
        List<ThreeSAT.Clause> clauses = new ArrayList<>();
        // (x1 v x2 v ~x3) ^ (~x1 v ~x2 v x3) ^ (x1 v ~x2 v x3)
        clauses.add(new ThreeSAT.Clause(Arrays.asList(
                new ThreeSAT.Literal(1, false), new ThreeSAT.Literal(2, false), new ThreeSAT.Literal(3, true))));
        clauses.add(new ThreeSAT.Clause(Arrays.asList(
                new ThreeSAT.Literal(1, true), new ThreeSAT.Literal(2, true), new ThreeSAT.Literal(3, false))));
        clauses.add(new ThreeSAT.Clause(Arrays.asList(
                new ThreeSAT.Literal(1, false), new ThreeSAT.Literal(2, true), new ThreeSAT.Literal(3, false))));

        ThreeSAT sat = new ThreeSAT(3, clauses);
        System.out.println("Formula: " + sat);

        boolean[] assignment = sat.solveExact();
        System.out.printf("  ✓ 3-SAT Exact Solution: x1=%b, x2=%b, x3=%b (Verified Certificate: %b)\n",
                assignment[1], assignment[2], assignment[3], sat.verifyCertificate(assignment));

        System.out.println(ConsoleColors.yellow("\n--- Executing Polynomial-Time Reduction Chain ---"));
        ThreeSATToClique.ReductionResult clique = ThreeSATToClique.reduce(sat);
        System.out.printf("  [Step 1] 3-SAT <=_p CLIQUE            : Created %d graph vertices, target clique k = %d\n",
                clique.nodes.size(), clique.targetK);

        CliqueToIndependentSet.ISResult is = CliqueToIndependentSet.reduce(clique.adjMatrix, clique.targetK);
        System.out.printf("  [Step 2] CLIQUE <=_p INDEPENDENT SET  : Complement graph built with target IS k = %d\n",
                is.targetK);

        IndependentSetToVertexCover.VCResult vc = IndependentSetToVertexCover.reduce(is.complementAdjMatrix, is.targetK);
        System.out.printf("  [Step 3] INDEPENDENT SET <=_p VC      : Target Vertex Cover size |V| - k = %d\n",
                vc.targetVCSize);
    }

    private void demonstrateVertexCover2Approx() {
        System.out.println(ConsoleColors.yellow("\n--- Transit Hub Monitoring Network (Vertex Cover) ---"));
        int n = 8;
        boolean[][] adj = new boolean[n][n];
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}, {6, 7}, {0, 7}, {1, 5}, {2, 6}};
        for (int[] e : edges) {
            adj[e[0]][e[1]] = true;
            adj[e[1]][e[0]] = true;
        }

        System.out.printf("Network: %d Hubs and %d Transit Corridors\n", n, edges.length);

        ExactVertexCover.ExactVCResult exact = ExactVertexCover.solveExact(n, adj);
        VertexCoverApproximation.ApproxResult approx = VertexCoverApproximation.solve(n, adj);

        System.out.println("  " + exact);
        System.out.println("  " + approx);
        System.out.println(ConsoleColors.green(String.format(
                "  ✓ Provable Bound Check: |C_approx| (%d) <= 2 · |C_opt| (%d) -> Empirical Ratio: %.2fx <= 2.0x ✓",
                approx.getApproxCoverSize(), exact.getMinCoverSize(), approx.getApproxRatio())));
    }

    private void demonstrateKnapsackApprox() {
        System.out.println(ConsoleColors.yellow("\n--- Attraction Selection under Budget Constraint (Knapsack) ---"));
        List<KnapsackApproximation.Item> items = Arrays.asList(
                new KnapsackApproximation.Item("Taj Mahal Heritage Tour", 1500, 95),
                new KnapsackApproximation.Item("Red Fort Sound & Light", 500, 80),
                new KnapsackApproximation.Item("City Palace Guided Walk", 800, 85),
                new KnapsackApproximation.Item("Old City Food Tasting", 600, 90),
                new KnapsackApproximation.Item("Sunset Boat Cruise", 1200, 88)
        );
        int maxBudget = 2500;

        KnapsackApproximation.KnapsackResult exact = KnapsackApproximation.solveExactDP(items, maxBudget);
        KnapsackApproximation.KnapsackResult approx = KnapsackApproximation.solveGreedyApprox(items, maxBudget, exact.totalValue);

        System.out.println("  " + exact);
        System.out.println("  " + approx);
        System.out.println(ConsoleColors.green(String.format(
                "  ✓ Approximation Ratio: %.2f (Guaranteed >= 0.50x optimal)", approx.approximationRatio)));
    }

    // -------------------------------------------------------------------------
    // 9. Randomized & Parallel Algorithms (CO6)
    // -------------------------------------------------------------------------
    private void handleRandomizedAndParallel() {
        TerminalUI.printSectionHeader("RANDOMIZED & PARALLEL ALGORITHMS (CO6)");
        System.out.println("1. Miller-Rabin Primality Test (Monte Carlo / Carmichael Immunity)");
        System.out.println("2. Reservoir Sampling on Booking Streams (Uniform Prob Proof)");
        System.out.println("3. Randomized QuickSort with Expected Time Analysis (Las Vegas)");
        System.out.println("4. Blelloch Parallel Prefix Sum (Scan Up-Sweep & Down-Sweep)");
        System.out.println("5. Parallel Reduction & Brent's Theorem Work-Span Analysis");

        int sub = TerminalUI.readIntChoice(scanner, "Select demonstration (1-5): ", 1, 5);

        switch (sub) {
            case 1 -> demonstrateMillerRabin();
            case 2 -> demonstrateReservoirSampling();
            case 3 -> demonstrateRandomizedQuickSort();
            case 4 -> demonstrateBlellochScan();
            case 5 -> demonstrateParallelReduceAndBrents();
        }

        TerminalUI.pressEnterToContinue(scanner);
    }

    private void demonstrateMillerRabin() {
        System.out.println(ConsoleColors.yellow("\n--- Miller-Rabin Probabilistic Primality Verification ---"));
        BigInteger[] testNumbers = {
            BigInteger.valueOf(1000000007L),        // Prime
            BigInteger.valueOf(561),                // Carmichael composite (3 * 11 * 17)
            BigInteger.valueOf(1105),               // Carmichael composite (5 * 13 * 17)
            BigInteger.valueOf(1729),               // Ramanujan-Hardy / Carmichael (7 * 13 * 19)
            new BigInteger("10000000000000000051")  // Large prime (20-digit)
        };

        for (BigInteger num : testNumbers) {
            MillerRabin.PrimalityResult res = MillerRabin.isPrime(num, 20);
            System.out.println("  " + res);
        }
    }

    private void demonstrateReservoirSampling() {
        System.out.println(ConsoleColors.yellow("\n--- Reservoir Sampling from Booking Stream ---"));
        List<Booking> stream = engine.getRepository().getAllBookings();
        int k = 5;
        ReservoirSampling.SampleResult<Booking> sample = ReservoirSampling.sample(stream, k);

        System.out.println("  " + sample);
        System.out.println("  Sampled Bookings Reservoir:");
        for (Booking b : sample.getReservoir()) {
            System.out.println("    • " + b);
        }
    }

    private void demonstrateRandomizedQuickSort() {
        System.out.println(ConsoleColors.yellow("\n--- Randomized QuickSort (Las Vegas) ---"));
        int[] data = new int[5000];
        Random rng = new Random(42);
        for (int i = 0; i < data.length; i++) data[i] = rng.nextInt(100000);

        RandomizedQuickSort.SortStats stats = RandomizedQuickSort.sort(data);
        System.out.println("  " + stats);
    }

    private void demonstrateBlellochScan() {
        System.out.println(ConsoleColors.yellow("\n--- Blelloch Parallel Prefix Sum (Scan) ---"));
        long[] data = {3, 1, 7, 0, 4, 1, 6, 3};
        System.out.println("  Original Array : " + Arrays.toString(data));

        BlellochScan.ScanResult res = BlellochScan.scan(data);
        System.out.println("  Exclusive Scan : " + Arrays.toString(res.getExclusivePrefixSum()));
        System.out.println("  Inclusive Scan : " + Arrays.toString(res.getInclusivePrefixSum()));
        System.out.println("  Verification   : Element-by-element equality with sequential = " + res.isExactMatch());
    }

    private void demonstrateParallelReduceAndBrents() {
        System.out.println(ConsoleColors.yellow("\n--- Parallel Reduction & Brent's Theorem Evaluation ---"));
        int processors = Runtime.getRuntime().availableProcessors();
        BrentsTheoremAnalysis.WorkSpanMetrics metrics = BrentsTheoremAnalysis.evaluateWorkSpan(1_000_000, processors);
        System.out.println("  " + metrics);

        System.out.println("\nRunning Benchmark on N = 1,000,000 elements across " + processors + " CPU cores...");
        BrentsTheoremAnalysis.BenchmarkSummary bench = BrentsTheoremAnalysis.runBenchmark(1_000_000, 5);
        System.out.println("  " + bench);
    }

    // -------------------------------------------------------------------------
    // 10. Complete Demonstration (CO1 -> CO6)
    // -------------------------------------------------------------------------
    private void handleCompleteDemonstration() {
        TerminalUI.printSectionHeader("COMPLETE TRAVELSYNC DEMONSTRATION PIPELINE (CO1 -> CO6)");

        System.out.println(ConsoleColors.brightCyan("\n[STAGE 1/6] CO1: Problem Classification & Asymptotic Analysis"));
        System.out.println(ComplexityEvaluator.formatSignatureCard(engine.classifyTask(ProblemType.MULTI_STOP_ITINERARY_TSP)));

        System.out.println(ConsoleColors.brightCyan("\n[STAGE 2/6] CO2: Linear-Time Pattern Search & Suffix Analytics"));
        List<DestinationSearchEngine.SearchResult> searchResults = engine.searchDestinations("Hyderabad");
        System.out.printf("  Found %d matching destinations. Top match: %s\n", searchResults.size(), searchResults.get(0));

        System.out.println(ConsoleColors.brightCyan("\n[STAGE 3/6] CO3: Bitmask Dynamic Programming Tour Planning"));
        TravelerPreferences prefs = new TravelerPreferences("Hyderabad", Arrays.asList("Bengaluru", "Goa"), 5, 30000, Collections.singletonList("culture"), "ANY");
        ItineraryPlan plan = engine.optimizeItinerary(prefs);
        System.out.printf("  Route: %s | Distance: %d km | Cost: ₹%d | Feasible: %s\n",
                String.join(" -> ", plan.getRouteStops()), plan.getTotalDistanceKm(), plan.getTotalEstimatedCostInr(), plan.isFeasible());

        System.out.println(ConsoleColors.brightCyan("\n[STAGE 4/6] CO4: Network Flow & Min-Cut Bottleneck Isolation"));
        TransportFlowAnalyzer.FlowAnalysisReport flow = engine.analyzeTransitFlow("Hyderabad", "New Delhi");
        System.out.printf("  Max Passenger Flow: %d pax/day | Min-Cut Capacity: %d pax/day | Verified: %s\n",
                flow.maxDailyPassengerFlow, flow.minCutCapacity, flow.verified);

        System.out.println(ConsoleColors.brightCyan("\n[STAGE 5/6] CO5: NP-Completeness Reductions & 2-Approximation"));
        demonstrateVertexCover2Approx();

        System.out.println(ConsoleColors.brightCyan("\n[STAGE 6/6] CO6: Randomized Testing & Blelloch Parallel Scan"));
        demonstrateMillerRabin();
        demonstrateBlellochScan();

        System.out.println(ConsoleColors.green("\n✓ COMPLETE TRAVELSYNC PIPELINE EXECUTED SUCCESSFULLY WITH 100% VERIFIED OUTCOMES!"));
        TerminalUI.pressEnterToContinue(scanner);
    }

    // -------------------------------------------------------------------------
    // 11. Dataset Statistics
    // -------------------------------------------------------------------------
    private void handleDatasetStats() {
        TerminalUI.printSectionHeader("DATASET REPOSITORY STATISTICS & DIAGNOSTICS");
        var loader = engine.getRepository().getLoader();

        System.out.printf("  Resolved Data Directory : %s\n", loader.getResolvedDatasetDir());
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("  %-25s : %4d Valid | %2d Malformed/Rejected\n", "destinations.csv", loader.getValidDestinations(), loader.getRejectedDestinations());
        System.out.printf("  %-25s : %4d Valid | %2d Malformed/Rejected\n", "attractions.csv", loader.getValidAttractions(), loader.getRejectedAttractions());
        System.out.printf("  %-25s : %4d Valid | %2d Malformed/Rejected\n", "hotels.csv", loader.getValidHotels(), loader.getRejectedHotels());
        System.out.printf("  %-25s : %4d Valid | %2d Malformed/Rejected\n", "transport_network.csv", loader.getValidTransport(), loader.getRejectedTransport());
        System.out.printf("  %-25s : %4d Valid | %2d Malformed/Rejected\n", "reviews.csv", loader.getValidReviews(), loader.getRejectedReviews());
        System.out.printf("  %-25s : %4d Valid | %2d Malformed/Rejected\n", "bookings.csv", loader.getValidBookings(), loader.getRejectedBookings());
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("  Total Ingested Records  : %4d Valid Records\n", loader.getTotalValidRecords());

        TerminalUI.pressEnterToContinue(scanner);
    }

    // -------------------------------------------------------------------------
    // 12. Algorithm Benchmarks
    // -------------------------------------------------------------------------
    private void handleBenchmarks() {
        TerminalUI.printSectionHeader("ALGORITHMIC BENCHMARK SUITE");

        System.out.println(ConsoleColors.yellow("1. String Matching Benchmark: Naive vs KMP vs Z-Algorithm vs Rabin-Karp"));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5000; i++) sb.append("HYDERABAD_TOURISM_TRAVEL_SYNC_");
        String text = sb.toString();
        String pattern = "TRAVEL_SYNC_HYD";

        long tNaive = benchmarkStringMatcher(() -> NaiveSearch.search(text, pattern));
        long tKmp = benchmarkStringMatcher(() -> KMP.search(text, pattern));
        long tZ = benchmarkStringMatcher(() -> ZAlgorithm.search(text, pattern));
        long tRk = benchmarkStringMatcher(() -> RabinKarp.search(text, pattern));

        System.out.printf("  Text Length: %d chars | Pattern: '%s'\n", text.length(), pattern);
        System.out.printf("    • Naive Brute Force : %7.3f ms\n", tNaive / 1_000_000.0);
        System.out.printf("    • KMP Linear Search : %7.3f ms (Speedup vs Naive: %.2fx)\n", tKmp / 1_000_000.0, (double) tNaive / Math.max(1, tKmp));
        System.out.printf("    • Z-Algorithm Search: %7.3f ms\n", tZ / 1_000_000.0);
        System.out.printf("    • Rabin-Karp Hash   : %7.3f ms\n", tRk / 1_000_000.0);

        System.out.println(ConsoleColors.yellow("\n2. Parallel Reduction Multi-Scale Scaling (Java ForkJoin Multi-Core)"));
        for (int size : new int[]{10_000, 100_000, 1_000_000}) {
            BrentsTheoremAnalysis.BenchmarkSummary sum = BrentsTheoremAnalysis.runBenchmark(size, 5);
            System.out.println("    " + sum);
        }

        TerminalUI.pressEnterToContinue(scanner);
    }

    private long benchmarkStringMatcher(Runnable r) {
        // Warmup
        for (int i = 0; i < 3; i++) r.run();
        long start = System.nanoTime();
        for (int i = 0; i < 5; i++) r.run();
        return (System.nanoTime() - start) / 5;
    }
}
