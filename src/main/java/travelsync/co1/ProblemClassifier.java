package travelsync.co1;

import java.util.*;

/**
 * Problem Classifier implementing Course Outcome 1 (CO1):
 * Evaluates problem-class signatures and systematically selects an optimal algorithmic strategy.
 */
public class ProblemClassifier {

    private final Map<ProblemType, AlgorithmSignature> signatures = new EnumMap<>(ProblemType.class);

    public ProblemClassifier() {
        initSignatures();
    }

    private void initSignatures() {
        signatures.put(ProblemType.EXACT_SUBSTRING_SEARCH, new AlgorithmSignature(
                ProblemType.EXACT_SUBSTRING_SEARCH,
                "String Algorithms",
                "Single pattern search inside destination descriptions and tourist reviews",
                "Knuth-Morris-Pratt (KMP) / Rabin-Karp Rolling Hash",
                "Preprocessing: O(M), Matching: O(N) where N=|Text|, M=|Pattern|",
                "O(M) auxiliary space for LPS failure array / hash state",
                "Guarantees deterministic linear-time matching without backtracking in text stream.",
                "Optimized for 1 pattern; repeated queries against static text benefit more from Suffix Array.",
                "Naïve Brute Force O(N·M)",
                "Naïve exhibits O(N·M) worst-case on repetitive patterns (e.g. 'AAAA'); KMP shifts using LPS in strictly O(N+M)."
        ));

        signatures.put(ProblemType.MULTI_PATTERN_SEARCH, new AlgorithmSignature(
                ProblemType.MULTI_PATTERN_SEARCH,
                "String Algorithms (Multi-Pattern)",
                "Simultaneous filtering of multiple tourist interest tags and keywords",
                "Aho-Corasick Automaton (Trie with Failure Links)",
                "O(N + sum(|P_i|) + Z) where Z is total matches",
                "O(sum(|P_i|) · Sigma) for Trie structure and failure transitions",
                "Processes entire corpus in a single linear pass regardless of number of search patterns.",
                "Higher memory footprint during Trie construction; requires static pattern dictionary.",
                "Running KMP K times: O(K · (N + M))",
                "KMP takes O(K·N) passes; Aho-Corasick matches all K patterns simultaneously in a single O(N) stream pass."
        ));

        signatures.put(ProblemType.SUFFIX_INDEXING_SIMILARITY, new AlgorithmSignature(
                ProblemType.SUFFIX_INDEXING_SIMILARITY,
                "Suffix Structures & Text Analytics",
                "Document similarity, longest common substring, and full-text index on reviews",
                "Suffix Array + Kasai's LCP Array",
                "SA Construction: O(N log^2 N) / SA-IS O(N), Kasai LCP: O(N), Substring Query: O(M log N)",
                "O(N) integer arrays for SA, Rank, and LCP tables",
                "Enables lightning-fast binary search substring queries and linear-time longest common substring analysis.",
                "High initial index construction overhead; unsuited for frequently mutated texts.",
                "Suffix Trees / Inverted Index",
                "Suffix arrays use 4-8x less memory than Suffix Trees while achieving identical asymptotic lookup performance."
        ));

        signatures.put(ProblemType.FUZZY_STRING_MATCHING, new AlgorithmSignature(
                ProblemType.FUZZY_STRING_MATCHING,
                "Dynamic Programming (String Metric)",
                "Typo-tolerant destination search with character edit tolerance (insert/delete/substitute/transpose)",
                "Wagner-Fischer (Levenshtein / Damerau-Levenshtein DP)",
                "O(N · M) where N=|S1|, M=|S2|",
                "O(N · M) table or O(min(N,M)) space-optimized 2-row rolling buffer",
                "Computes true mathematical edit metric ensuring optimal typo recovery for user queries.",
                "Quadratic cost becomes expensive for long documents (mitigated by length-filtering).",
                "N-gram Jaccard Similarity / BK-Tree",
                "N-gram is approximate heuristic; Wagner-Fischer yields exact minimal edit transcript."
        ));

        signatures.put(ProblemType.SEQUENCE_ALIGNMENT, new AlgorithmSignature(
                ProblemType.SEQUENCE_ALIGNMENT,
                "Advanced Dynamic Programming",
                "Global/local traveler profile & route sequence alignment with scoring matrix and gap penalties",
                "Needleman-Wunsch (Global) & Smith-Waterman (Local)",
                "O(N · M) time and O(N · M) space for DP matrix + traceback path",
                "O(N · M) space to reconstruct optimal alignment alignment strings",
                "Finds globally/locally optimal aligned sub-sequences with customized match/mismatch/gap scores.",
                "Requires quadratic memory for traceback unless Hirshberg's divide-and-conquer is applied.",
                "Greedy Alignment",
                "Greedy fails on arbitrary gap penalties; DP guarantees globally optimal alignment score."
        ));

        signatures.put(ProblemType.MULTI_STOP_ITINERARY_TSP, new AlgorithmSignature(
                ProblemType.MULTI_STOP_ITINERARY_TSP,
                "Dynamic Programming (State Compression)",
                "Optimal multi-destination tour route minimizing distance/cost/time for K stops",
                "Bitmask Dynamic Programming (Held-Karp TSP)",
                "O(K^2 · 2^K) where K = number of selected destination stops",
                "O(K · 2^K) memoization table",
                "Guarantees provably optimal tour order and exact cost with full route sequence reconstruction.",
                "Exponential scaling restricts exact bitmask DP to K <= 20 stops; larger instances require approximation.",
                "Brute Force Permutations O(K!)",
                "For K=15 stops, K! = 1.3 trillion operations (intractable), while Bitmask DP takes only ~7.3 million operations."
        ));

        signatures.put(ProblemType.INTERVAL_OPTIMIZATION, new AlgorithmSignature(
                ProblemType.INTERVAL_OPTIMIZATION,
                "Interval Dynamic Programming",
                "Optimal evaluation order for multi-stage trip plans (Matrix Chain) & frequency-weighted search trees (Optimal BST)",
                "Interval DP (Matrix Chain Multiplication / Optimal BST)",
                "O(N^3) standard tabulation (or O(N^2) with Knuth-Yao speedup)",
                "O(N^2) 2D DP and split-point decision table",
                "Optimal substructure on contiguous intervals guarantees minimal multiplication/lookup cost.",
                "O(N^3) time limits input size N to a few hundreds.",
                "Greedy Parenthesization / Balanced BST",
                "Greedy splits fail to achieve minimal scalar multiplications; Optimal BST optimizes for non-uniform access frequencies."
        ));

        signatures.put(ProblemType.TREE_DP_OPTIMIZATION, new AlgorithmSignature(
                ProblemType.TREE_DP_OPTIMIZATION,
                "Dynamic Programming on Trees",
                "Hub-and-spoke tourist circuit optimization, maximum weight independent attraction sets, tree rerooting",
                "Tree DP & Tree Rerooting Technique",
                "O(V) linear time on tree topologies",
                "O(V) call stack / memoization table",
                "Exploits acyclic tree structure to solve NP-hard graph problems (like Independent Set) in linear time.",
                "Applicable exclusively to tree structures; general graphs remain NP-hard.",
                "General Graph Backtracking O(2^V)",
                "Tree DP reduces exponential general graph complexity O(2^V) to strictly linear O(V)."
        ));

        signatures.put(ProblemType.NETWORK_CAPACITY_MAX_FLOW, new AlgorithmSignature(
                ProblemType.NETWORK_CAPACITY_MAX_FLOW,
                "Network Flow & Residual Graphs",
                "Evaluating maximum passenger throughput and transit bottleneck corridors across India",
                "Edmonds-Karp (BFS Augmenting Paths) / Dinic's Algorithm",
                "Edmonds-Karp: O(V · E^2), Dinic: O(V^2 · E)",
                "O(V + E) for residual graph, capacity, and flow tracking matrices",
                "Calculates exact maximum bottleneck-constrained throughput and reveals critical min-cut links.",
                "Requires directed capacity constraints; not suitable for simple unconstrained shortest paths.",
                "Standard Ford-Fulkerson with arbitrary DFS",
                "DFS Ford-Fulkerson can take O(E · MaxFlow) iterations with irrational/large capacities; Edmonds-Karp bounds iterations to O(V·E)."
        ));

        signatures.put(ProblemType.BIPARTITE_MATCHING_ASSIGNMENT, new AlgorithmSignature(
                ProblemType.BIPARTITE_MATCHING_ASSIGNMENT,
                "Flow Reductions & Matching",
                "Assigning tourist groups to available hotel rooms or certified local guides",
                "Hopcroft-Karp / Flow Network Reduction with König's Theorem",
                "O(E · sqrt(V)) via Hopcroft-Karp or O(V · E) via Edmonds-Karp on unit network",
                "O(V + E) adjacency structures",
                "Guarantees maximum cardinality matching and provides minimal vertex cover via König's duality.",
                "Requires strictly bipartite structure (disjoint sets with no internal edges).",
                "Greedy First-Come Assignment",
                "Greedy assignment gets trapped in sub-optimal local choices; augmenting paths guarantee maximum global matches."
        ));

        signatures.put(ProblemType.NP_HARD_VERTEX_COVER, new AlgorithmSignature(
                ProblemType.NP_HARD_VERTEX_COVER,
                "NP-Completeness & Approximation",
                "Placing minimum transit monitoring hubs to cover all active travel corridors",
                "Vertex Cover 2-Approximation (Maximal Matching) vs Exact Solver",
                "Approximation: O(V + E) linear time; Exact: O(2^V · (V+E)) or O(1.2738^k + k·V)",
                "Approximation: O(V) space; Exact: O(V) recursion depth",
                "Guarantees solution size <= 2 · OPT in linear time with provable mathematical bound.",
                "Approximation ratio is 2 (never exceeds 2x optimal, but not guaranteed exact minimal).",
                "Exact Exponential Search",
                "Exact solver becomes intractable for |V| > 30; 2-approximation runs in milliseconds for millions of edges."
        ));

        signatures.put(ProblemType.NP_HARD_SUBSET_SELECTION, new AlgorithmSignature(
                ProblemType.NP_HARD_SUBSET_SELECTION,
                "NP-Hard Combinatorial Optimization",
                "Selecting subset of tourist activities/attractions maximizing value under budget limit",
                "0/1 Knapsack Exact DP vs Greedy 1/2-Approximation / FPTAS",
                "Exact DP: O(N · W), Greedy Approx: O(N log N)",
                "Exact DP: O(N · W) table or O(W) space",
                "Guarantees approximation ratio >= 0.5 in O(N log N) time; exact DP yields optimal value.",
                "Exact DP pseudo-polynomial time depends on magnitude of budget W.",
                "Exhaustive 2^N Subset Search",
                "Exhaustive search requires 2^N states; FPTAS / Greedy provides guaranteed polynomial-time solution."
        ));

        signatures.put(ProblemType.LARGE_STREAM_SAMPLING, new AlgorithmSignature(
                ProblemType.LARGE_STREAM_SAMPLING,
                "Randomized Algorithms (Streaming)",
                "Uniform unbiased sampling of K reviews or bookings from a high-velocity streaming log of unknown total length N",
                "Reservoir Sampling (Algorithm R)",
                "O(N) single-pass streaming time",
                "O(K) auxiliary memory reservoir",
                "Guarantees every item in the stream has identical probability K/N of being selected without knowing N in advance.",
                "Requires true random number generator; items cannot be retrieved in sorted priority order without weights.",
                "Full In-Memory Loading & Shuffling O(N log N)",
                "In-memory shuffling requires loading all N items into RAM (fails on multi-gigabyte streams); Reservoir uses strictly O(K) RAM."
        ));

        signatures.put(ProblemType.PARALLEL_REDUCTION, new AlgorithmSignature(
                ProblemType.PARALLEL_REDUCTION,
                "Parallel Algorithms & ForkJoin",
                "Aggregating large datasets (e.g. total revenue, min/max price, demand weight aggregation)",
                "Parallel Tree Reduction (Java ForkJoin)",
                "Work T_1 = O(N), Span T_infinity = O(log N), Parallel Time T_P = O(N/P + log N)",
                "O(log N) stack frames across ForkJoin threads",
                "Achieves near-linear parallel speedup across multi-core processors on compute-bound aggregations.",
                "Thread creation and fork/join overhead can dominate on small array sizes (N < 10,000).",
                "Sequential Linear Scan O(N)",
                "Sequential scan uses 1 core; Parallel reduction utilizes all available CPU cores for sub-linear span."
        ));

        signatures.put(ProblemType.PARALLEL_PREFIX_SUM, new AlgorithmSignature(
                ProblemType.PARALLEL_PREFIX_SUM,
                "Parallel Algorithms (Scan Primitives)",
                "Cumulative metric computation (running revenue, cumulative guest arrivals, workload balancing)",
                "Blelloch Parallel Scan (Up-Sweep & Down-Sweep)",
                "Work T_1 = O(N), Span T_infinity = O(log N), Total Parallel Steps = 2 log N",
                "O(N) working buffer",
                "Computes all N prefix sums in O(log N) depth using O(N) total operations.",
                "Array size must be padded to power-of-2 for standard tree sweep; requires synchronizing tree barriers.",
                "Sequential Prefix Scan O(N)",
                "Sequential scan is fundamentally serial with span O(N); Blelloch scan parallelizes the dependency chain to span O(log N)."
        ));

        signatures.put(ProblemType.PROBABILISTIC_PRIMALITY_TEST, new AlgorithmSignature(
                ProblemType.PROBABILISTIC_PRIMALITY_TEST,
                "Randomized Algorithms (Monte Carlo)",
                "Cryptographic token verification, booking hash validation, large integer primality testing",
                "Miller-Rabin Probabilistic Primality Test",
                "O(k · log^3 N) where k = number of random witness rounds",
                "O(1) auxiliary space",
                "Monte Carlo algorithm with error probability bounded by (1/4)^k; immune to Carmichael composite vulnerabilities.",
                "Probabilistic: composite numbers could theoretically pass as 'probable prime' with probability <= 4^-k.",
                "Deterministic Trial Division O(sqrt(N))",
                "Trial division on a 100-digit number takes billions of years; Miller-Rabin verifies it in less than 1 millisecond."
        ));
    }

    public AlgorithmSignature getSignature(ProblemType type) {
        return signatures.get(type);
    }

    public Collection<AlgorithmSignature> getAllSignatures() {
        return Collections.unmodifiableCollection(signatures.values());
    }

    /**
     * Classifies a natural language query or user intent into an appropriate problem class.
     */
    public AlgorithmSignature classifyQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            return signatures.get(ProblemType.EXACT_SUBSTRING_SEARCH);
        }
        String q = query.toLowerCase(Locale.ROOT);

        if (q.contains("itinerary") || q.contains("tsp") || q.contains("tour") || q.contains("multi-stop") || q.contains("stops")) {
            return signatures.get(ProblemType.MULTI_STOP_ITINERARY_TSP);
        } else if (q.contains("fuzzy") || q.contains("typo") || q.contains("spelling") || q.contains("edit distance") || q.contains("levenshtein")) {
            return signatures.get(ProblemType.FUZZY_STRING_MATCHING);
        } else if (q.contains("multi") || q.contains("tags") || q.contains("keywords") || q.contains("aho")) {
            return signatures.get(ProblemType.MULTI_PATTERN_SEARCH);
        } else if (q.contains("suffix") || q.contains("lcp") || q.contains("similarity") || q.contains("common substring")) {
            return signatures.get(ProblemType.SUFFIX_INDEXING_SIMILARITY);
        } else if (q.contains("flow") || q.contains("capacity") || q.contains("bottleneck") || q.contains("cut") || q.contains("transit")) {
            return signatures.get(ProblemType.NETWORK_CAPACITY_MAX_FLOW);
        } else if (q.contains("match") || q.contains("assign") || q.contains("guide") || q.contains("hotel room") || q.contains("bipartite")) {
            return signatures.get(ProblemType.BIPARTITE_MATCHING_ASSIGNMENT);
        } else if (q.contains("vertex cover") || q.contains("approx") || q.contains("np-hard") || q.contains("sat") || q.contains("clique")) {
            return signatures.get(ProblemType.NP_HARD_VERTEX_COVER);
        } else if (q.contains("sample") || q.contains("reservoir") || q.contains("stream")) {
            return signatures.get(ProblemType.LARGE_STREAM_SAMPLING);
        } else if (q.contains("prime") || q.contains("miller") || q.contains("rabin") || q.contains("crypto")) {
            return signatures.get(ProblemType.PROBABILISTIC_PRIMALITY_TEST);
        } else if (q.contains("parallel prefix") || q.contains("blelloch") || q.contains("scan")) {
            return signatures.get(ProblemType.PARALLEL_PREFIX_SUM);
        } else if (q.contains("parallel") || q.contains("reduce") || q.contains("forkjoin")) {
            return signatures.get(ProblemType.PARALLEL_REDUCTION);
        } else if (q.contains("align") || q.contains("needleman") || q.contains("smith-waterman")) {
            return signatures.get(ProblemType.SEQUENCE_ALIGNMENT);
        } else if (q.contains("matrix") || q.contains("bst") || q.contains("interval")) {
            return signatures.get(ProblemType.INTERVAL_OPTIMIZATION);
        } else {
            return signatures.get(ProblemType.EXACT_SUBSTRING_SEARCH);
        }
    }
}
