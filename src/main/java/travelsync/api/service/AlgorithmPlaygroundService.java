package travelsync.api.service;

import travelsync.co1.AlgorithmSignature;
import travelsync.co1.ProblemClassifier;
import travelsync.co1.ProblemType;
import travelsync.co2.*;
import travelsync.co3.*;
import travelsync.co4.*;
import travelsync.co5.*;
import travelsync.co6.*;
import travelsync.data.DataRepository;
import travelsync.model.Attraction;
import travelsync.model.Booking;
import travelsync.model.Destination;

import java.math.BigInteger;
import java.util.*;

/**
 * Service to execute interactive live DSA algorithm demonstrations (CO1-CO6)
 * for academic verification and the interactive Algorithm Lab UI.
 */
public class AlgorithmPlaygroundService {

    private final DataRepository repository;
    private final ProblemClassifier classifier;

    public AlgorithmPlaygroundService(DataRepository repository) {
        this.repository = repository;
        this.classifier = new ProblemClassifier();
    }

    public Map<String, Object> execute(String algorithmId, Map<String, String> params) {
        Map<String, Object> result = new LinkedHashMap<>();
        long startNs = System.nanoTime();

        try {
            switch (algorithmId.toUpperCase(Locale.ROOT)) {
                // -------------------------------------------------------------
                // CO1: Problem Classification
                // -------------------------------------------------------------
                case "CLASSIFY": {
                    String query = params.getOrDefault("query", "Find shortest round trip visiting Hyderabad, Goa and Mumbai within budget");
                    AlgorithmSignature sig = classifier.classifyQuery(query);
                    result.put("query", query);
                    result.put("classifiedCategory", sig.getProblemType().name());
                    result.put("primaryAlgorithm", sig.getSelectedAlgorithm());
                    result.put("timeComplexity", sig.getTimeComplexity());
                    result.put("spaceComplexity", sig.getSpaceComplexity());
                    result.put("rationale", sig.getSelectionRationale());
                    result.put("courseOutcome", "CO1");
                    break;
                }

                // -------------------------------------------------------------
                // CO2: String Algorithms & Benchmarks
                // -------------------------------------------------------------
                case "STRING_MATCH_COMPARE": {
                    String text = params.getOrDefault("text", "Hyderabad is known for historic Charminar and Golconda Fort architecture in Hyderabad");
                    String pattern = params.getOrDefault("pattern", "Hyderabad");

                    NaiveSearch.MatchResult naive = NaiveSearch.search(text, pattern);
                    KMP.KMPResult kmp = KMP.search(text, pattern);
                    RabinKarp.RKResult rk = RabinKarp.search(text, pattern);

                    result.put("text", text);
                    result.put("pattern", pattern);
                    result.put("matchesFound", kmp.getMatchCount());
                    result.put("matchIndices", kmp.getMatchIndices());
                    result.put("naiveComparisons", naive.getComparisons());
                    result.put("naiveTimeNs", naive.getExecutionTimeNs());
                    result.put("kmpComparisons", kmp.getComparisons());
                    result.put("kmpTimeNs", kmp.getExecutionTimeNs());
                    result.put("kmpLpsArray", kmp.getLpsArray());
                    result.put("rkHashCollisions", rk.getHashCollisions());
                    result.put("rkHashEvaluations", rk.getHashEvaluations());
                    result.put("rkCharComparisons", rk.getCharacterComparisons());
                    result.put("rkTimeNs", rk.getExecutionTimeNs());
                    result.put("speedupKmpVsNaive", naive.getComparisons() > 0 ? (double) naive.getComparisons() / Math.max(1, kmp.getComparisons()) : 1.0);
                    result.put("courseOutcome", "CO2");
                    break;
                }

                case "LCS_REVIEWS": {
                    String text1 = params.getOrDefault("text1", "The historic architecture of the royal fort and palaces left us stunned with incredible heritage views.");
                    String text2 = params.getOrDefault("text2", "We loved the historic architecture of the royal palace courtyards displaying exquisite craftsmanship.");

                    KasaiLCP.CommonSubstringResult lcs = KasaiLCP.findLongestCommonSubstring(text1, text2);
                    result.put("text1Length", text1.length());
                    result.put("text2Length", text2.length());
                    result.put("longestCommonSubstring", lcs.getLongestCommonSubstring());
                    result.put("lcsLength", lcs.getLength());
                    result.put("jaccardSimilarity", lcs.getJaccardSimilarity());
                    result.put("courseOutcome", "CO2");
                    break;
                }

                // -------------------------------------------------------------
                // CO3: Dynamic Programming
                // -------------------------------------------------------------
                case "EDIT_DISTANCE": {
                    String s1 = params.getOrDefault("str1", "Hyderabad");
                    String s2 = params.getOrDefault("str2", "Hyderbad");

                    int lev = WagnerFischer.levenshteinDistance(s1, s2);
                    int dam = WagnerFischer.damerauLevenshteinDistance(s1, s2);

                    result.put("string1", s1);
                    result.put("string2", s2);
                    result.put("levenshteinDistance", lev);
                    result.put("damerauLevenshteinDistance", dam);
                    result.put("courseOutcome", "CO3");
                    break;
                }

                case "BITMASK_TSP": {
                    String citiesParam = params.getOrDefault("cities", "Hyderabad,Bengaluru,Goa,Mumbai");
                    List<String> cities = Arrays.asList(citiesParam.split(","));
                    int n = cities.size();
                    double[][] distMatrix = new double[n][n];

                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            if (i == j) {
                                distMatrix[i][j] = 0;
                            } else {
                                Destination dA = repository.findDestinationByName(cities.get(i).trim());
                                Destination dB = repository.findDestinationByName(cities.get(j).trim());
                                if (dA != null && dB != null) {
                                    distMatrix[i][j] = travelsync.data.DataNormalizer.haversineDistanceKm(dA.getLatitude(), dA.getLongitude(), dB.getLatitude(), dB.getLongitude());
                                } else {
                                    distMatrix[i][j] = 400 + (Math.abs(i - j) * 200);
                                }
                            }
                        }
                    }

                    BitmaskTSP.TSPResult tsp = BitmaskTSP.solveTSP(distMatrix, cities, 0, 0.0);
                    result.put("citiesCount", n);
                    result.put("tourSequence", tsp.getTourCityNames());
                    result.put("totalTourDistanceKm", Math.round(tsp.getMinCost()));
                    result.put("timeComplexity", "O(N^2 · 2^N)");
                    result.put("stateTransitionsEvaluated", n * n * (1 << n));
                    result.put("courseOutcome", "CO3");
                    break;
                }

                // -------------------------------------------------------------
                // CO4: Flow Networks & Matching
                // -------------------------------------------------------------
                case "MAX_FLOW_MIN_CUT": {
                    String from = params.getOrDefault("from", "Hyderabad");
                    String to = params.getOrDefault("to", "Goa");

                    travelsync.planner.TransportFlowAnalyzer.FlowAnalysisReport report =
                            new travelsync.planner.TransportFlowAnalyzer(repository).analyzeFlow(from, to);

                    result.put("origin", report.originCity);
                    result.put("destination", report.destinationCity);
                    result.put("maxDailyPassengerFlow", report.maxDailyPassengerFlow);
                    result.put("minCutCapacity", report.minCutCapacity);
                    result.put("bottlenecks", report.bottleneckCorridors);
                    result.put("maxFlowEqualsMinCut", report.verified);
                    result.put("courseOutcome", "CO4");
                    break;
                }

                // -------------------------------------------------------------
                // CO5: NP-Completeness & Approximations
                // -------------------------------------------------------------
                case "KNAPSACK_APPROX": {
                    int budget = Integer.parseInt(params.getOrDefault("budget", "1500"));
                    List<Attraction> all = repository.getAllAttractions();
                    List<KnapsackApproximation.Item> items = new ArrayList<>();
                    for (int i = 0; i < Math.min(15, all.size()); i++) {
                        Attraction a = all.get(i);
                        items.add(new KnapsackApproximation.Item(a.getName(), Math.max(25, a.getEntryFeeInr()), (int) (a.getRating() * 20)));
                    }

                    KnapsackApproximation.KnapsackResult exact = KnapsackApproximation.solveExactDP(items, budget);
                    KnapsackApproximation.KnapsackResult approx = KnapsackApproximation.solveGreedyApprox(items, budget, exact.totalValue);

                    result.put("budgetCapacity", budget);
                    result.put("exactValue", exact.totalValue);
                    result.put("exactWeight", exact.totalWeight);
                    result.put("exactItemsCount", exact.selectedItems.size());
                    result.put("approxValue", approx.totalValue);
                    result.put("approxWeight", approx.totalWeight);
                    result.put("approxItemsCount", approx.selectedItems.size());
                    result.put("empiricalRatio", exact.totalValue > 0 ? (double) approx.totalValue / exact.totalValue : 1.0);
                    result.put("guaranteedRatioBound", ">= 0.50 (Provable 1/2-Approximation)");
                    result.put("courseOutcome", "CO5");
                    break;
                }

                // -------------------------------------------------------------
                // CO6: Randomized & Parallel Algorithms
                // -------------------------------------------------------------
                case "MILLER_RABIN": {
                    String numStr = params.getOrDefault("number", "1000000007");
                    int rounds = Integer.parseInt(params.getOrDefault("rounds", "10"));
                    BigInteger n = new BigInteger(numStr);

                    MillerRabin.PrimalityResult mr = MillerRabin.isPrime(n, rounds);
                    result.put("candidate", numStr);
                    result.put("roundsTested", rounds);
                    result.put("isProbablePrime", mr.isProbablePrime());
                    result.put("maxErrorProbability", mr.getMaxErrorProbability());
                    result.put("witness", mr.getCompositeWitness() != null ? mr.getCompositeWitness().toString() : "None");
                    result.put("executionTimeNs", mr.getExecutionTimeNs());
                    result.put("courseOutcome", "CO6");
                    break;
                }

                case "PARALLEL_REDUCE": {
                    List<Booking> bookings = repository.getAllBookings();
                    long[] arr = new long[bookings.size()];
                    for (int i = 0; i < bookings.size(); i++) {
                        arr[i] = bookings.get(i).getTotalTransactionValue();
                    }

                    ParallelReduce.ReductionResult red = ParallelReduce.parallelSum(arr);
                    result.put("arraySize", arr.length);
                    result.put("sumResult", red.getResult());
                    result.put("sequentialTimeNs", red.getSequentialTimeNs());
                    result.put("parallelTimeNs", red.getParallelTimeNs());
                    result.put("speedup", red.getSpeedup());
                    result.put("exactMatch", red.isExactMatch());
                    result.put("workSpan", "Work T1 = O(N), Span T_inf = O(log N)");
                    result.put("courseOutcome", "CO6");
                    break;
                }

                case "BLELLOCH_SCAN": {
                    List<Booking> bookings = repository.getAllBookings();
                    long[] weights = new long[bookings.size()];
                    for (int i = 0; i < bookings.size(); i++) {
                        weights[i] = bookings.get(i).getDemandWeight();
                    }

                    BlellochScan.ScanResult scan = BlellochScan.scan(weights);
                    result.put("elements", weights.length);
                    result.put("exclusivePrefixSumSample", Arrays.copyOf(scan.getExclusivePrefixSum(), Math.min(5, scan.getExclusivePrefixSum().length)));
                    result.put("inclusivePrefixSumSample", Arrays.copyOf(scan.getInclusivePrefixSum(), Math.min(5, scan.getInclusivePrefixSum().length)));
                    result.put("sequentialTimeNs", scan.getSequentialTimeNs());
                    result.put("parallelTimeNs", scan.getParallelTimeNs());
                    result.put("exactMatch", scan.isExactMatch());
                    result.put("workSpan", "Work T1 = O(N), Span T_inf = O(log N) via Up-Sweep & Down-Sweep");
                    result.put("courseOutcome", "CO6");
                    break;
                }

                default: {
                    result.put("error", "Unknown algorithm id: " + algorithmId);
                    break;
                }
            }
        } catch (Exception e) {
            result.put("error", e.getMessage());
        }

        result.put("totalPipelineNs", System.nanoTime() - startNs);
        return result;
    }
}
