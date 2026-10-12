package travelsync.co3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Bitmask Dynamic Programming for Multi-Stop Itinerary / Traveling Salesperson Problem (TSP).
 * Asymptotic Complexity: O(N^2 · 2^N) time, O(N · 2^N) space.
 * Includes complete tour reconstruction and budget / duration constraint verification.
 */
public class BitmaskTSP {

    public static final double INF = 1e9;

    public static class TSPResult {
        private final double minCost;
        private final List<Integer> tourIndices;
        private final List<String> tourCityNames;
        private final boolean feasible;
        private final String infeasibilityReason;

        public TSPResult(double minCost, List<Integer> tourIndices, List<String> tourCityNames,
                         boolean feasible, String infeasibilityReason) {
            this.minCost = minCost;
            this.tourIndices = tourIndices;
            this.tourCityNames = tourCityNames;
            this.feasible = feasible;
            this.infeasibilityReason = infeasibilityReason;
        }

        public double getMinCost() { return minCost; }
        public List<Integer> getTourIndices() { return tourIndices; }
        public List<String> getTourCityNames() { return tourCityNames; }
        public boolean isFeasible() { return feasible; }
        public String getInfeasibilityReason() { return infeasibilityReason; }

        @Override
        public String toString() {
            if (!feasible) {
                return "Infeasible Tour: " + infeasibilityReason;
            }
            return String.format("Optimal Tour: %s | Total Cost/Dist: %.1f",
                    String.join(" -> ", tourCityNames), minCost);
        }
    }

    /**
     * Solves symmetric/asymmetric TSP with full round-trip tour using Bitmask DP.
     * @param distMatrix n x n distance / cost matrix
     * @param cityNames list of n city names
     * @param startCityIndex index of the originating hub (usually 0)
     * @param budgetConstraint maximum allowed budget/distance (<=0 for unconstrained)
     */
    public static TSPResult solveTSP(double[][] distMatrix, List<String> cityNames, int startCityIndex, double budgetConstraint) {
        int n = distMatrix.length;
        if (n == 0) {
            return new TSPResult(0, Collections.emptyList(), Collections.emptyList(), true, "");
        }
        if (n == 1) {
            List<Integer> idx = Collections.singletonList(0);
            List<String> names = Collections.singletonList(cityNames.get(0));
            return new TSPResult(0, idx, names, true, "");
        }
        if (n > 20) {
            return new TSPResult(INF, Collections.emptyList(), Collections.emptyList(), false,
                    "Input city count (" + n + ") exceeds Bitmask DP exact limit (N <= 20). Use approximation instead.");
        }

        int totalMasks = 1 << n;
        double[][] dp = new double[totalMasks][n];
        int[][] parent = new int[totalMasks][n];

        for (int i = 0; i < totalMasks; i++) {
            Arrays.fill(dp[i], INF);
            Arrays.fill(parent[i], -1);
        }

        int startMask = 1 << startCityIndex;
        dp[startMask][startCityIndex] = 0.0;

        for (int mask = 1; mask < totalMasks; mask++) {
            if ((mask & startMask) == 0) continue; // must include start city

            for (int u = 0; u < n; u++) {
                if ((mask & (1 << u)) == 0 || dp[mask][u] >= INF) continue;

                for (int v = 0; v < n; v++) {
                    if ((mask & (1 << v)) != 0) continue; // v already visited

                    int nextMask = mask | (1 << v);
                    double newDist = dp[mask][u] + distMatrix[u][v];

                    if (newDist < dp[nextMask][v]) {
                        dp[nextMask][v] = newDist;
                        parent[nextMask][v] = u;
                    }
                }
            }
        }

        // Complete the tour back to startCityIndex
        int allVisitedMask = (1 << n) - 1;
        double minTotalCost = INF;
        int lastCity = -1;

        for (int u = 0; u < n; u++) {
            if (u == startCityIndex) continue;
            double totalCost = dp[allVisitedMask][u] + distMatrix[u][startCityIndex];
            if (totalCost < minTotalCost) {
                minTotalCost = totalCost;
                lastCity = u;
            }
        }

        if (minTotalCost >= INF || lastCity == -1) {
            return new TSPResult(INF, Collections.emptyList(), Collections.emptyList(), false, "No valid Hamiltonian cycle exists among the selected destinations.");
        }

        // Reconstruct the tour path
        List<Integer> tour = new ArrayList<>();
        int currMask = allVisitedMask;
        int currCity = lastCity;

        while (currCity != -1 && currMask > 0) {
            tour.add(currCity);
            int prevCity = parent[currMask][currCity];
            currMask ^= (1 << currCity);
            currCity = prevCity;
        }

        Collections.reverse(tour);
        tour.add(startCityIndex); // close the cycle back at origin

        List<String> tourNames = new ArrayList<>();
        for (int idx : tour) {
            tourNames.add(cityNames.get(idx));
        }

        boolean withinBudget = budgetConstraint <= 0 || minTotalCost <= budgetConstraint;
        String reason = withinBudget ? "" : String.format("Computed cost (₹%.1f) exceeds allocated budget constraint (₹%.1f)", minTotalCost, budgetConstraint);

        return new TSPResult(minTotalCost, tour, tourNames, withinBudget, reason);
    }
}
