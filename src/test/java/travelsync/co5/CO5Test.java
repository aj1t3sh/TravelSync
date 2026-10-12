package travelsync.co5;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CO5Test {

    @Test
    public void testThreeSATSolverAndCertificateVerifier() {
        List<ThreeSAT.Clause> clauses = new ArrayList<>();
        // (x1 v x2 v ~x3) ^ (~x1 v ~x2 v x3)
        clauses.add(new ThreeSAT.Clause(Arrays.asList(
                new ThreeSAT.Literal(1, false), new ThreeSAT.Literal(2, false), new ThreeSAT.Literal(3, true))));
        clauses.add(new ThreeSAT.Clause(Arrays.asList(
                new ThreeSAT.Literal(1, true), new ThreeSAT.Literal(2, true), new ThreeSAT.Literal(3, false))));

        ThreeSAT sat = new ThreeSAT(3, clauses);
        boolean[] assignment = sat.solveExact();
        assertNotNull(assignment);
        assertTrue(sat.verifyCertificate(assignment));
    }

    @Test
    public void testPolynomialReductionChain() {
        List<ThreeSAT.Clause> clauses = new ArrayList<>();
        clauses.add(new ThreeSAT.Clause(Arrays.asList(
                new ThreeSAT.Literal(1, false), new ThreeSAT.Literal(2, false), new ThreeSAT.Literal(3, false))));

        ThreeSAT sat = new ThreeSAT(3, clauses);

        // 1. 3-SAT -> CLIQUE
        ThreeSATToClique.ReductionResult clique = ThreeSATToClique.reduce(sat);
        assertEquals(3, clique.nodes.size());
        assertEquals(1, clique.targetK);

        // 2. CLIQUE -> IS
        CliqueToIndependentSet.ISResult is = CliqueToIndependentSet.reduce(clique.adjMatrix, clique.targetK);
        assertEquals(1, is.targetK);

        // 3. IS -> VC
        IndependentSetToVertexCover.VCResult vc = IndependentSetToVertexCover.reduce(is.complementAdjMatrix, is.targetK);
        assertEquals(3 - 1, vc.targetVCSize);
    }

    @Test
    public void testVertexCoverApproximationGuarantee() {
        int n = 6;
        boolean[][] adj = new boolean[n][n];
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 0}}; // 6-cycle
        for (int[] e : edges) {
            adj[e[0]][e[1]] = true;
            adj[e[1]][e[0]] = true;
        }

        ExactVertexCover.ExactVCResult exact = ExactVertexCover.solveExact(n, adj);
        VertexCoverApproximation.ApproxResult approx = VertexCoverApproximation.solve(n, adj);

        assertEquals(3, exact.getMinCoverSize()); // OPT for 6-cycle is 3
        assertTrue(approx.getApproxCoverSize() <= 2 * exact.getMinCoverSize(), "Approximation size must be <= 2 * OPT");
        assertTrue(approx.getApproxRatio() <= 2.0);
    }

    @Test
    public void testKnapsackApproximation() {
        List<KnapsackApproximation.Item> items = Arrays.asList(
                new KnapsackApproximation.Item("Item1", 10, 60),
                new KnapsackApproximation.Item("Item2", 20, 100),
                new KnapsackApproximation.Item("Item3", 30, 120)
        );

        KnapsackApproximation.KnapsackResult exact = KnapsackApproximation.solveExactDP(items, 50);
        assertEquals(220, exact.totalValue);

        KnapsackApproximation.KnapsackResult approx = KnapsackApproximation.solveGreedyApprox(items, 50, exact.totalValue);
        assertTrue(approx.totalValue >= exact.totalValue / 2, "1/2-Approx must achieve >= 0.5 * OPT");
    }
}
