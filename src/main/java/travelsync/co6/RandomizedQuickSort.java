package travelsync.co6;

import java.util.Random;

/**
 * Las Vegas Algorithm: Randomized QuickSort.
 * Always produces a 100% correct sorted output, with running time being a random variable
 * having expected value E[T(N)] = O(N log N).
 */
public class RandomizedQuickSort {

    public static class SortStats {
        private final long comparisons;
        private final int maxRecursionDepth;
        private final long executionTimeNs;

        public SortStats(long comparisons, int maxRecursionDepth, long executionTimeNs) {
            this.comparisons = comparisons;
            this.maxRecursionDepth = maxRecursionDepth;
            this.executionTimeNs = executionTimeNs;
        }

        public long getComparisons() { return comparisons; }
        public int getMaxRecursionDepth() { return maxRecursionDepth; }
        public long getExecutionTimeNs() { return executionTimeNs; }

        @Override
        public String toString() {
            return String.format("Comparisons: %d | Max Depth: %d | Time: %.3f ms",
                    comparisons, maxRecursionDepth, executionTimeNs / 1_000_000.0);
        }
    }

    private static class Counter {
        long comparisons = 0;
        int maxDepth = 0;
    }

    /**
     * Sorts an array using randomized pivot selection.
     */
    public static SortStats sort(int[] arr) {
        return sort(arr, new Random());
    }

    public static SortStats sort(int[] arr, Random rng) {
        long start = System.nanoTime();
        if (arr == null || arr.length <= 1) {
            return new SortStats(0, 0, System.nanoTime() - start);
        }
        Counter counter = new Counter();
        quickSort(arr, 0, arr.length - 1, 1, rng, counter);
        return new SortStats(counter.comparisons, counter.maxDepth, System.nanoTime() - start);
    }

    private static void quickSort(int[] arr, int low, int high, int depth, Random rng, Counter counter) {
        if (depth > counter.maxDepth) {
            counter.maxDepth = depth;
        }
        if (low < high) {
            int pi = randomizedPartition(arr, low, high, rng, counter);
            quickSort(arr, low, pi - 1, depth + 1, rng, counter);
            quickSort(arr, pi + 1, high, depth + 1, rng, counter);
        }
    }

    private static int randomizedPartition(int[] arr, int low, int high, Random rng, Counter counter) {
        int randomPivotIdx = low + rng.nextInt(high - low + 1);
        swap(arr, randomPivotIdx, high);

        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            counter.comparisons++;
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
