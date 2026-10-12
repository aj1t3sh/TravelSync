package travelsync.co6;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class CO6Test {

    @Test
    public void testRandomizedQuickSort() {
        int[] data = {9, 3, 7, 5, 6, 4, 8, 2};
        RandomizedQuickSort.SortStats stats = RandomizedQuickSort.sort(data, new Random(12345));

        int[] expected = {2, 3, 4, 5, 6, 7, 8, 9};
        assertArrayEquals(expected, data);
        assertTrue(stats.getComparisons() > 0);
        assertTrue(stats.getMaxRecursionDepth() > 0);
    }

    @Test
    public void testMillerRabinPrimality() {
        // Known primes
        assertTrue(MillerRabin.isPrime(BigInteger.valueOf(1000000007L), 20).isProbablePrime());
        assertTrue(MillerRabin.isPrime(BigInteger.valueOf(997), 20).isProbablePrime());
        assertTrue(MillerRabin.isPrime(BigInteger.valueOf(2), 20).isProbablePrime());

        // Known composites
        assertFalse(MillerRabin.isPrime(BigInteger.valueOf(100), 20).isProbablePrime());
        assertFalse(MillerRabin.isPrime(BigInteger.valueOf(1), 20).isProbablePrime());

        // Carmichael numbers (must be recognized as composite)
        assertFalse(MillerRabin.isPrime(BigInteger.valueOf(561), 20).isProbablePrime());
        assertFalse(MillerRabin.isPrime(BigInteger.valueOf(1105), 20).isProbablePrime());
        assertFalse(MillerRabin.isPrime(BigInteger.valueOf(1729), 20).isProbablePrime());
    }

    @Test
    public void testReservoirSampling() {
        List<Integer> stream = new ArrayList<>();
        for (int i = 0; i < 100; i++) stream.add(i);

        int k = 10;
        ReservoirSampling.SampleResult<Integer> res = ReservoirSampling.sample(stream, k, new Random(42));
        assertEquals(k, res.getReservoir().size());
        assertEquals(100, res.getStreamItemsProcessed());
        assertEquals(0.10, res.getSelectionProbability(), 0.001);
    }

    @Test
    public void testParallelReduce() {
        long[] data = new long[20000];
        long expectedSum = 0;
        for (int i = 0; i < data.length; i++) {
            data[i] = i + 1;
            expectedSum += (i + 1);
        }

        ParallelReduce.ReductionResult res = ParallelReduce.parallelSum(data);
        assertEquals(expectedSum, res.getResult());
        assertTrue(res.isExactMatch());
    }

    @Test
    public void testBlellochScan() {
        long[] input = {3, 1, 7, 0, 4, 1, 6, 3};
        long[] expectedInclusive = {3, 4, 11, 11, 15, 16, 22, 25};
        long[] expectedExclusive = {0, 3, 4, 11, 11, 15, 16, 22};

        BlellochScan.ScanResult res = BlellochScan.scan(input);
        assertArrayEquals(expectedInclusive, res.getInclusivePrefixSum());
        assertArrayEquals(expectedExclusive, res.getExclusivePrefixSum());
        assertTrue(res.isExactMatch());
    }

    @Test
    public void testBrentsTheoremAnalysis() {
        BrentsTheoremAnalysis.WorkSpanMetrics metrics = BrentsTheoremAnalysis.evaluateWorkSpan(10000, 4);
        assertEquals(20000, metrics.workT1);
        assertTrue(metrics.spanTInfinity > 0);
        assertTrue(metrics.maxTheoreticalSpeedup > 1.0);
    }
}
