package travelsync.co5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Knapsack Optimization: Exact Dynamic Programming vs Greedy 1/2-Approximation.
 * Models budget allocation for selecting tourist attractions.
 */
public class KnapsackApproximation {

    public static class Item {
        public final String name;
        public final int weight; // e.g. entry fee or duration
        public final int value;  // e.g. rating or tourist appeal

        public Item(String name, int weight, int value) {
            this.name = name;
            this.weight = weight;
            this.value = value;
        }

        public double getDensity() {
            return weight > 0 ? (double) value / weight : value;
        }
    }

    public static class KnapsackResult {
        public final int totalValue;
        public final int totalWeight;
        public final List<Item> selectedItems;
        public final String method;
        public final double approximationRatio;

        public KnapsackResult(int totalValue, int totalWeight, List<Item> selectedItems, String method, double approximationRatio) {
            this.totalValue = totalValue;
            this.totalWeight = totalWeight;
            this.selectedItems = selectedItems;
            this.method = method;
            this.approximationRatio = approximationRatio;
        }

        @Override
        public String toString() {
            return String.format("[%s] Total Value: %d | Total Weight: %d | Items: %d | Ratio: %.2f",
                    method, totalValue, totalWeight, selectedItems.size(), approximationRatio);
        }
    }

    /**
     * Exact 0/1 Knapsack DP in O(N · W) pseudo-polynomial time.
     */
    public static KnapsackResult solveExactDP(List<Item> items, int maxWeight) {
        int n = items.size();
        int[][] dp = new int[n + 1][maxWeight + 1];

        for (int i = 1; i <= n; i++) {
            Item item = items.get(i - 1);
            for (int w = 0; w <= maxWeight; w++) {
                dp[i][w] = dp[i - 1][w];
                if (item.weight <= w) {
                    dp[i][w] = Math.max(dp[i][w], dp[i - 1][w - item.weight] + item.value);
                }
            }
        }

        List<Item> selected = new ArrayList<>();
        int w = maxWeight;
        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                Item item = items.get(i - 1);
                selected.add(item);
                w -= item.weight;
            }
        }
        Collections.reverse(selected);

        int totalW = maxWeight - w;
        return new KnapsackResult(dp[n][maxWeight], totalW, selected, "Exact DP", 1.0);
    }

    /**
     * Greedy 1/2-Approximation algorithm in O(N log N) time.
     */
    public static KnapsackResult solveGreedyApprox(List<Item> items, int maxWeight, int exactOptValue) {
        List<Item> sorted = new ArrayList<>(items);
        sorted.sort((a, b) -> Double.compare(b.getDensity(), a.getDensity()));

        List<Item> greedyList = new ArrayList<>();
        int greedyVal = 0;
        int currentWeight = 0;

        for (Item item : sorted) {
            if (currentWeight + item.weight <= maxWeight) {
                greedyList.add(item);
                greedyVal += item.value;
                currentWeight += item.weight;
            }
        }

        // Find single item with max value that fits
        Item bestSingle = null;
        int maxSingleVal = 0;
        for (Item item : items) {
            if (item.weight <= maxWeight && item.value > maxSingleVal) {
                maxSingleVal = item.value;
                bestSingle = item;
            }
        }

        List<Item> resultItems;
        int resultVal;
        int resultWeight;

        if (maxSingleVal > greedyVal && bestSingle != null) {
            resultItems = Collections.singletonList(bestSingle);
            resultVal = bestSingle.value;
            resultWeight = bestSingle.weight;
        } else {
            resultItems = greedyList;
            resultVal = greedyVal;
            resultWeight = currentWeight;
        }

        double ratio = exactOptValue > 0 ? (double) resultVal / exactOptValue : 1.0;
        return new KnapsackResult(resultVal, resultWeight, resultItems, "Greedy 1/2-Approx", ratio);
    }
}
