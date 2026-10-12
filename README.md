# TravelSync: Intelligent Travel Planning & Information Platform
**A Modular, Production-Grade Java CLI & DSA Engine for Eclipse**  
*Aligned with KL Deemed to be University — Department of CSE (Course Code: 25CS2103E)*

---

## 1. Executive Summary & Project Overview

**TravelSync** is an intelligent travel planning, search, and transit optimization platform implemented purely in **Java** as an interactive **Command-Line Interface (CLI)** application tailored for Eclipse. 

It solves real-world travel optimization challenges — including multi-stop itinerary routing, fuzzy destination search, transportation network bottleneck isolation, room/guide allocation, and streaming demand aggregation — by applying the advanced algorithmic canon taught in **Data Structures and Algorithms - 3 (25CS2103E)**.

---

## 2. Course Outcomes (CO1 to CO6) Implementation Matrix

| Course Outcome | Handout Requirement | Implemented Java Class(es) | Primary Asymptotic Complexity | CLI Menu Option | Automated Test Suite |
|---|---|---|---|---|---|
| **CO1: Problem Classification** | Problem-class signatures, algorithmic paradigms, asymptotic selection | [`ProblemClassifier`](src/main/java/travelsync/co1/ProblemClassifier.java), [`ComplexityEvaluator`](src/main/java/travelsync/co1/ComplexityEvaluator.java) | $O(1)$ decision tree | Option 1, 10 | [`CO1Test.java`](src/test/java/travelsync/co1/CO1Test.java) |
| **CO2: Linear String Algorithms** | Naïve, KMP ($\pi$/LPS array), Z-Function, Rabin-Karp (rolling hash & collision check) | [`NaiveSearch`](src/main/java/travelsync/co2/NaiveSearch.java), [`KMP`](src/main/java/travelsync/co2/KMP.java), [`ZAlgorithm`](src/main/java/travelsync/co2/ZAlgorithm.java), [`RabinKarp`](src/main/java/travelsync/co2/RabinKarp.java) | $O(N + M)$ time, $O(M)$ space | Option 1, 12 | [`CO2Test.java`](src/test/java/travelsync/co2/CO2Test.java) |
| **CO2: Suffix Structures** | Suffix Arrays ($O(N \log^2 N)$), Kasai's LCP Array ($O(N)$), Suffix Automaton, Aho-Corasick | [`SuffixArray`](src/main/java/travelsync/co2/SuffixArray.java), [`KasaiLCP`](src/main/java/travelsync/co2/KasaiLCP.java), [`AhoCorasick`](src/main/java/travelsync/co2/AhoCorasick.java), [`SuffixAutomaton`](src/main/java/travelsync/co2/SuffixAutomaton.java) | SA: $O(N \log^2 N)$, LCP: $O(N)$, Aho: $O(N + \sum |P_i| + Z)$ | Option 2, 3 | [`CO2Test.java`](src/test/java/travelsync/co2/CO2Test.java) |
| **CO3: Dynamic Programming** | Wagner-Fischer edit distance, Damerau-Levenshtein, Weighted edit distance | [`WagnerFischer`](src/main/java/travelsync/co3/WagnerFischer.java) | $O(N \cdot M)$ time, $O(\min(N,M))$ space | Option 3, 4 | [`CO3Test.java`](src/test/java/travelsync/co3/CO3Test.java) |
| **CO3: Sequence Alignment** | Needleman-Wunsch (Global) & Smith-Waterman (Local) with gap penalties | [`NeedlemanWunsch`](src/main/java/travelsync/co3/NeedlemanWunsch.java), [`SmithWaterman`](src/main/java/travelsync/co3/SmithWaterman.java) | $O(N \cdot M)$ time, $O(N \cdot M)$ space | Option 3 | [`CO3Test.java`](src/test/java/travelsync/co3/CO3Test.java) |
| **CO3: Combinatorial & Tree DP** | Held-Karp Bitmask DP TSP, Matrix Chain (MCM), Optimal BST, Tree DP, SOS DP | [`BitmaskTSP`](src/main/java/travelsync/co3/BitmaskTSP.java), [`MatrixChainMultiplication`](src/main/java/travelsync/co3/MatrixChainMultiplication.java), [`OptimalBST`](src/main/java/travelsync/co3/OptimalBST.java), [`TreeDP`](src/main/java/travelsync/co3/TreeDP.java), [`SOSDynamicProgramming`](src/main/java/travelsync/co3/SOSDynamicProgramming.java) | TSP: $O(K^2 \cdot 2^K)$, MCM/OBST: $O(N^3)$, Tree: $O(V)$, SOS: $O(N \cdot 2^N)$ | Option 4, 5 | [`CO3Test.java`](src/test/java/travelsync/co3/CO3Test.java) |
| **CO4: Network Flow & Min-Cut** | Directed residual graph, Ford-Fulkerson, Edmonds-Karp (BFS), Dinic (level graph), Min-Cut duality | [`FlowNetwork`](src/main/java/travelsync/co4/FlowNetwork.java), [`FordFulkerson`](src/main/java/travelsync/co4/FordFulkerson.java), [`EdmondsKarp`](src/main/java/travelsync/co4/EdmondsKarp.java), [`Dinic`](src/main/java/travelsync/co4/Dinic.java), [`MinCut`](src/main/java/travelsync/co4/MinCut.java) | EK: $O(V \cdot E^2)$, Dinic: $O(V^2 \cdot E)$, MinCut: MaxFlow == MinCut | Option 6 | [`CO4Test.java`](src/test/java/travelsync/co4/CO4Test.java) |
| **CO4: Matching & Assignment** | Bipartite Matching via Flow Reduction, König's Minimum Vertex Cover duality | [`BipartiteMatching`](src/main/java/travelsync/co4/BipartiteMatching.java) | $O(V \cdot E)$ on unit network | Option 7 | [`CO4Test.java`](src/test/java/travelsync/co4/CO4Test.java) |
| **CO5: NP-Completeness** | P, NP, co-NP, NP-Complete, 3-SAT Exact Solver, Reduction: 3-SAT $\le_p$ CLIQUE $\le_p$ IS $\le_p$ VC | [`ComplexityClassesGuide`](src/main/java/travelsync/co5/ComplexityClassesGuide.java), [`ThreeSAT`](src/main/java/travelsync/co5/ThreeSAT.java), [`ThreeSATToClique`](src/main/java/travelsync/co5/ThreeSATToClique.java), [`CliqueToIndependentSet`](src/main/java/travelsync/co5/CliqueToIndependentSet.java), [`IndependentSetToVertexCover`](src/main/java/travelsync/co5/IndependentSetToVertexCover.java) | Verification: $O(C)$, Exact: $O(2^V \cdot C)$, Reduction: $O(V^2)$ | Option 8 | [`CO5Test.java`](src/test/java/travelsync/co5/CO5Test.java) |
| **CO5: Approximation Algorithms** | Vertex Cover 2-Approximation (Maximal Matching), Knapsack 1/2-Approximation | [`ExactVertexCover`](src/main/java/travelsync/co5/ExactVertexCover.java), [`VertexCoverApproximation`](src/main/java/travelsync/co5/VertexCoverApproximation.java), [`KnapsackApproximation`](src/main/java/travelsync/co5/KnapsackApproximation.java) | VC 2-Approx: $O(V+E)$, Ratio $\le 2.0$; Knapsack: $O(N \log N)$, Ratio $\ge 0.5$ | Option 8 | [`CO5Test.java`](src/test/java/travelsync/co5/CO5Test.java) |
| **CO6: Randomized Algorithms** | Las Vegas QuickSort, Monte Carlo Miller-Rabin (Carmichael immunity), Universal Hashing, Reservoir Sampling | [`RandomizedQuickSort`](src/main/java/travelsync/co6/RandomizedQuickSort.java), [`MillerRabin`](src/main/java/travelsync/co6/MillerRabin.java), [`RandomizedHashing`](src/main/java/travelsync/co6/RandomizedHashing.java), [`ReservoirSampling`](src/main/java/travelsync/co6/ReservoirSampling.java) | Sort: $E[T] = O(N \log N)$, MR: $O(k \log^3 N)$, Res: $O(N)$ time, $O(K)$ space | Option 9 | [`CO6Test.java`](src/test/java/travelsync/co6/CO6Test.java) |
| **CO6: Parallel Primitives & Brent's** | Parallel Tree Reduce, Blelloch Parallel Scan (Up-Sweep/Down-Sweep), Work-Span ($T_1, T_\infty$), Brent's Theorem | [`ParallelReduce`](src/main/java/travelsync/co6/ParallelReduce.java), [`BlellochScan`](src/main/java/travelsync/co6/BlellochScan.java), [`BrentsTheoremAnalysis`](src/main/java/travelsync/co6/BrentsTheoremAnalysis.java) | Work $T_1 = O(N)$, Span $T_\infty = O(\log N)$, Speedup $S_p \le \frac{T_1}{T_p}$ | Option 9, 12 | [`CO6Test.java`](src/test/java/travelsync/co6/CO6Test.java) |

---

## 3. Project Architecture & Directory Layout

```
TravelSync2/
├── .classpath                       <- Eclipse Java Build Path Configuration
├── .project                         <- Eclipse Project Descriptor
├── pom.xml                          <- Standard Maven Build & JUnit 5 Dependencies
├── dataset/                         <- External Authentic Datasets
│   ├── destinations.csv             <- Indian Destination Hubs (Coordinates, Ratings, Tags)
│   ├── attractions.csv              <- Historical & Cultural Monuments (Fees, Durations)
│   ├── hotels.csv                   <- Accommodation Inventory (Rooms, Capacities, Tariffs)
│   ├── transport_network.csv        <- Inter-City Flight/Train/Bus Multi-graph
│   ├── reviews.csv                  <- Tourist Reviews Sentiment Corpus
│   ├── bookings.csv                 <- Demand Transaction Log Stream
│   └── README.md                    <- Dataset Sources, Schema & Preprocessing
├── src/
│   ├── main/java/travelsync/
│   │   ├── Main.java                <- Primary CLI Application Entry Point
│   │   ├── model/                   <- Domain Data Entities
│   │   ├── data/                    <- RFC 4180 CSV Parsing, Haversine, In-Memory Repository
│   │   ├── co1/                     <- Problem Classifier & Complexity Evaluator
│   │   ├── co2/                     <- String Algorithms & Suffix Structures
│   │   ├── co3/                     <- Dynamic Programming, Alignment, Bitmask TSP, Tree DP
│   │   ├── co4/                     <- Residual Flow Networks, Edmonds-Karp, Dinic, Matching
│   │   ├── co5/                     <- NP-Completeness, 3-SAT Reductions, 2-Approximation
│   │   ├── co6/                     <- Randomized QuickSort, Miller-Rabin, Blelloch Scan, Brent's
│   │   ├── planner/                 <- TravelSync Search, Flow Analyzer, Itinerary Optimizer
│   │   └── cli/                     <- Terminal UI Widgets, ANSI Colors, Menu Dispatcher
│   └── test/java/travelsync/
│       ├── co1/CO1Test.java         <- Problem Classifier Verification
│       ├── co2/CO2Test.java         <- String Search, LPS, Suffix Array & LCP Tests
│       ├── co3/CO3Test.java         <- Wagner-Fischer, Sequence Alignment & Bitmask TSP Tests
│       ├── co4/CO4Test.java         <- Max-Flow / Min-Cut Duality & Bipartite Matching Tests
│       ├── co5/CO5Test.java         <- 3-SAT Reductions & 2-Approximation Guarantee Tests
│       ├── co6/CO6Test.java         <- Miller-Rabin, Blelloch Scan & Parallel Reduction Tests
│       ├── planner/PlannerTest.java <- End-to-End Travel Planner Verification
│       └── data/DataIngestionTest.java <- CSV Parser & Repository Integrity Tests
├── README.md                        <- Project Manual & Algorithmic Documentation
└── CO_VERIFICATION.md               <- Comprehensive Course Outcome Verification Table
```

---

## 4. How to Import and Run in Eclipse

### Step 1: Open Eclipse IDE
- Launch Eclipse IDE for Java Developers (Eclipse 2022-09 or later recommended; JDK 17+).

### Step 2: Import Project into Workspace
- Select **File** ➔ **Import...**
- Choose **Maven** ➔ **Existing Maven Projects** (or **General** ➔ **Existing Projects into Workspace**).
- Click **Browse...** and select `/Users/ajiteshkaran/TravelSync2`.
- Click **Finish**. Eclipse will resolve the build path, `src/main/java`, and JUnit 5 libraries.

### Step 3: Run the Application
- In the **Package Explorer**, expand `src/main/java` ➔ `travelsync`.
- Right-click `Main.java` ➔ **Run As** ➔ **Java Application**.
- TravelSync launches the REST API Server on port 8080 (or interactive CLI when `--cli` is passed).

### Step 4: Run Automated Tests
- Right-click the project root or `src/test/java` ➔ **Run As** ➔ **JUnit Test**.
- All **41 test cases** across all modules pass with 100% green status.

---

## 5. Web Platform & CLI Execution

### Running the Full Web Platform (Frontend + Backend)

1. **Start the Java REST API Server (Port 8080)**:
   ```bash
   mvn exec:java
   # Or using root npm script:
   npm run backend
   ```

2. **Start the React + Vite Frontend (Port 5173)**:
   ```bash
   cd frontend
   npm install
   npm run dev
   # Or from root:
   npm run dev
   ```

3. **Open the Web Application**:
   Navigate to [http://localhost:5173](http://localhost:5173) in your browser.

### Running CLI / Automated Demonstration

```bash
# Run interactive CLI console:
mvn exec:java -Dexec.args="--cli"

# Run automated full pipeline demonstration:
mvn exec:java -Dexec.args="--demo"

# Run complete Maven test suite (41 tests):
mvn clean test
```

### REST API Endpoints Overview

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/health` | Backend status & data inventory health check |
| `GET` | `/api/destinations` | All destinations with season, price tier, and sort filters |
| `GET` | `/api/destinations/{id}` | Detailed hub view (attractions, hotels, reviews, transport) |
| `GET` | `/api/destinations/search?q=...` | KMP exact & Wagner-Fischer fuzzy autocorrection search |
| `GET` | `/api/hotels` | Hotel catalog with city and tier filtering |
| `GET` | `/api/attractions` | Monuments and attractions catalog |
| `POST` | `/api/trips/plan` | Core planning engine (CO1 to CO6 full optimization pipeline) |
| `POST` | `/api/trips/optimize` | Stop order re-optimization |
| `GET` | `/api/trips` | Persisted itineraries list |
| `GET` | `/api/trips/{id}` | Retrieve saved trip by ID |
| `POST` | `/api/trips/save` | Explicitly save itinerary |
| `GET` | `/api/analytics/summary` | Real dataset statistics & CO6 multi-core parallel benchmarks |
| `GET` | `/api/engine/algorithms` | Comprehensive DSA CO1-CO6 syllabus metadata |
| `POST` | `/api/engine/execute` | Live interactive algorithm runner for student/faculty evaluation |

---

## 6. Algorithmic Highlights & Rigorous Details

### Dynamic Programming Itinerary TSP (CO3)
- Solves the Traveling Salesperson Problem on $K$ destination stops in $O(K^2 \cdot 2^K)$ time using Held-Karp state compression:
  $$\text{dp}(\text{mask}, u) = \min_{v \notin \text{mask}} \{\text{dp}(\text{mask} \mid (1 \ll v), v) + \text{dist}(u, v)\}$$
- Reconstructs the exact sequence of city hops from origin back to origin.
- Checks budget limits and maximum day constraints, providing clear infeasibility diagnostics.

### Maximum Flow & Minimum Cut Bottleneck Analysis (CO4)
- Implements directed residual networks with forward and backward edges.
- Solves maximum daily passenger flow using both **Edmonds-Karp** ($O(V \cdot E^2)$) and **Dinic's Algorithm** ($O(V^2 \cdot E)$).
- Extracts the $S$-$T$ minimum cut by performing a reachability search on the residual graph.
- Verifies the **Max-Flow Min-Cut Theorem**: $\text{Capacity}(S, T) = \text{MaxFlow}(s, t)$.

### NP-Hard Reductions & 2-Approximation (CO5)
- Implements the Cook-Levin reduction chain:
  $$\text{3-SAT} \le_p \text{CLIQUE} \le_p \text{INDEPENDENT SET} \le_p \text{VERTEX COVER}$$
- Implements a **Vertex Cover 2-Approximation** via maximal matching running in $O(V + E)$ linear time:
  $$|C_{\text{approx}}| = 2 \cdot |M| \le 2 \cdot |C_{\text{opt}}|$$
- Compares approximate cover with exact branch-and-bound exponential solver ($O(2^V)$) on graph instances, demonstrating the approximation ratio $\le 2.0$.

### Randomized Algorithms & Parallel Primitives (CO6)
- **Miller-Rabin Primality Test**: Decomposes $n - 1 = 2^s \cdot d$, tests $k$ random witness bases, and achieves error probability $\le 4^{-k}$. Resistant to Carmichael composites (561, 1105, 1729).
- **Reservoir Sampling (Algorithm R)**: Unbiased uniform sampling of $k$ items from a streaming dataset of unknown length $N$, with selection probability $\frac{k}{N}$ and strictly $O(k)$ memory.
- **Blelloch Parallel Scan**: Up-Sweep (reduce) and Down-Sweep tree sweeps computing exclusive and inclusive prefix sums with Work $T_1 = O(N)$ and Span $T_\infty = O(\log N)$.
- **Brent's Theorem**: Validates multi-core speedup $S_p = \frac{T_1}{T_p} \le \min(p, \frac{T_1}{T_\infty})$ on $p$ available CPU cores.

---

## 7. Real-World Limitations & Unavailable Data Notice

1. **Live Transport Fares & Flight Schedules**: Transport network data is derived from static public transit schedules and average seasonal ticket tariffs. Real-time dynamic pricing, seat availability, and live railway delay feeds are not integrated.
2. **Exact Road Topologies**: City-to-city distances use authentic highway corridor distances and Haversine geodesic coordinates, not turn-by-turn routing geometries.
3. **NP-Hard Input Threshold**: Exact Bitmask DP TSP is limited to $K \le 16$ stops to prevent exponential memory consumption. Larger tours should utilize approximation heuristics.
