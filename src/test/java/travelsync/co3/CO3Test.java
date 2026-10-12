package travelsync.co3;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CO3Test {

    @Test
    public void testWagnerFischerEditDistances() {
        assertEquals(3, WagnerFischer.levenshteinDistance("kitten", "sitting"));
        assertEquals(0, WagnerFischer.levenshteinDistance("hyderabad", "hyderabad"));
        assertEquals(1, WagnerFischer.levenshteinDistance("hyderbad", "hyderabad"));

        // Damerau-Levenshtein transposition
        assertEquals(1, WagnerFischer.damerauLevenshteinDistance("ca", "ac"));
        assertEquals(2, WagnerFischer.levenshteinDistance("ca", "ac")); // standard Levenshtein is 2

        // Weighted Edit Distance (delete 'a' (2) + insert 'u' (2) = 4 < sub (5))
        int weighted = WagnerFischer.weightedEditDistance("cat", "cut", 2, 2, 5);
        assertEquals(4, weighted);
    }

    @Test
    public void testNeedlemanWunschAndSmithWaterman() {
        // Global alignment
        NeedlemanWunsch.AlignmentResult global = NeedlemanWunsch.align("GCATGCG", "GATTACA", 1, -1, -1);
        assertNotNull(global.getAlignedSeq1());
        assertNotNull(global.getAlignedSeq2());
        assertEquals(global.getAlignedSeq1().length(), global.getAlignedSeq2().length());

        // Local alignment
        SmithWaterman.LocalAlignmentResult local = SmithWaterman.align("AGCACACA", "ACACACTA", 2, -1, -1);
        assertTrue(local.getMaxScore() > 0);
        assertTrue(local.getAlignedSub1().length() > 0);
    }

    @Test
    public void testBitmaskTSP() {
        double[][] dist = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };
        List<String> cities = Arrays.asList("City0", "City1", "City2", "City3");

        BitmaskTSP.TSPResult res = BitmaskTSP.solveTSP(dist, cities, 0, 100);
        assertTrue(res.isFeasible());
        assertEquals(80.0, res.getMinCost(), 0.001);
        assertEquals(5, res.getTourCityNames().size()); // 0 -> 1 -> 3 -> 2 -> 0
        assertEquals("City0", res.getTourCityNames().get(0));
        assertEquals("City0", res.getTourCityNames().get(4));
    }

    @Test
    public void testMatrixChainMultiplication() {
        int[] arr = {10, 20, 30, 40, 30};
        MatrixChainMultiplication.MCMResult res = MatrixChainMultiplication.solve(arr);
        assertEquals(30000, res.getMinMultiplications());
        assertNotNull(res.getOptimalParenthesization());
    }

    @Test
    public void testOptimalBST() {
        double[] p = {0, 0.15, 0.10, 0.05, 0.10, 0.20};
        double[] q = {0.05, 0.10, 0.05, 0.05, 0.05, 0.10};
        OptimalBST.OBSTResult res = OptimalBST.solve(p, q, 5);
        assertEquals(2.75, res.getMinCost(), 0.01);
    }

    @Test
    public void testTreeDPAndSOS() {
        // Tree MWIS
        int n = 4;
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        adj.get(0).add(1); adj.get(1).add(0);
        adj.get(0).add(2); adj.get(2).add(0);
        adj.get(0).add(3); adj.get(3).add(0);

        long[] weights = {10, 5, 5, 5};
        TreeDP.MWISResult mwis = TreeDP.solveMWIS(n, adj, weights);
        assertEquals(15, mwis.getMaxWeight()); // pick 1, 2, 3 (sum=15) > pick 0 (10)

        // Tree Rerooting
        long[] distSums = TreeDP.computeAllNodesDistanceSum(n, adj);
        assertEquals(3, distSums[0]); // distance to 1, 2, 3 is 1+1+1 = 3

        // SOS DP
        long[] a = {1, 2, 4, 8};
        long[] f = SOSDynamicProgramming.computeSOS(2, a);
        assertEquals(1, f[0]);
        assertEquals(1 + 2, f[1]);
        assertEquals(1 + 4, f[2]);
        assertEquals(1 + 2 + 4 + 8, f[3]);
    }
}
