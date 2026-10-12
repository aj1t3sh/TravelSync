package travelsync.co5;

/**
 * Theoretical framework explaining Complexity Classes: P, NP, co-NP, NP-Complete, NP-Hard,
 * the Cook-Levin Theorem, and Polynomial-Time Reductions.
 */
public class ComplexityClassesGuide {

    public static String getComplexityOverview() {
        return """
        ================================================================================
                          COMPUTATIONAL COMPLEXITY THEORY (CO5)
        ================================================================================
        1. CLASS P (Deterministic Polynomial Time):
           - Problems solvable by a Deterministic Turing Machine in O(n^c) time.
           - Examples: Shortest Path (Dijkstra), Maximum Flow (Edmonds-Karp),
             String Matching (KMP), Minimum Spanning Tree (Kruskal).

        2. CLASS NP (Nondeterministic Polynomial Time):
           - Decision problems whose candidate solutions ('certificates' or 'witnesses')
             can be VERIFIED in polynomial time by a deterministic algorithm.
           - Examples: 3-SAT, Hamiltonian Cycle, Vertex Cover, Clique, Subset Sum.

        3. CLASS co-NP:
           - Decision problems whose 'NO'-instances have polynomial-time verifiable proofs.
           - Example: TAUTOLOGY, UNSAT.

        4. NP-HARD:
           - A problem H is NP-Hard if EVERY problem in NP can be reduced to H in
             polynomial time (L <=_p H for all L in NP).
           - NP-Hard problems do not need to be in NP (e.g. Halting Problem, TSP Optimization).

        5. NP-COMPLETE (NPC):
           - A problem C is NP-Complete if:
             (a) C in NP (polynomial-time certificate verification), AND
             (b) C is NP-Hard (every problem in NP reduces to C in polynomial time).
           - Cook-Levin Theorem (1971): Proved that Boolean Satisfiability (SAT) is NP-Complete.

        6. THE CANONICAL REDUCTION ZOO:
           3-SAT ───(poly-time)───> CLIQUE ───(poly-time)───> INDEPENDENT SET ───(poly-time)───> VERTEX COVER
        ================================================================================
        """;
    }
}
