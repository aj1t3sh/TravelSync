package travelsync.co5;

import java.util.*;

/**
 * 3-SAT (3-Conjunctive Normal Form Boolean Satisfiability) problem solver and verifier.
 * Demonstrates NP certificate verification in O(C) polynomial time and exact exponential solving in O(2^V).
 */
public class ThreeSAT {

    public static class Literal {
        public final int variable; // 1-indexed variable id
        public final boolean isNegated;

        public Literal(int variable, boolean isNegated) {
            this.variable = variable;
            this.isNegated = isNegated;
        }

        public boolean evaluate(boolean[] assignment) {
            boolean val = assignment[variable];
            return isNegated ? !val : val;
        }

        @Override
        public String toString() {
            return isNegated ? ("~x" + variable) : ("x" + variable);
        }
    }

    public static class Clause {
        public final List<Literal> literals;

        public Clause(List<Literal> literals) {
            this.literals = literals != null ? literals : new ArrayList<>();
        }

        public boolean isSatisfied(boolean[] assignment) {
            for (Literal lit : literals) {
                if (lit.evaluate(assignment)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < literals.size(); i++) {
                sb.append(literals.get(i).toString());
                if (i + 1 < literals.size()) sb.append(" v ");
            }
            sb.append(")");
            return sb.toString();
        }
    }

    private final int numVariables;
    private final List<Clause> clauses;

    public ThreeSAT(int numVariables, List<Clause> clauses) {
        this.numVariables = numVariables;
        this.clauses = clauses != null ? clauses : new ArrayList<>();
    }

    public int getNumVariables() { return numVariables; }
    public List<Clause> getClauses() { return clauses; }

    /**
     * Polynomial-time verifier: Verifies if a given assignment certificate satisfies all clauses in O(C).
     */
    public boolean verifyCertificate(boolean[] assignment) {
        if (assignment == null || assignment.length <= numVariables) return false;
        for (Clause clause : clauses) {
            if (!clause.isSatisfied(assignment)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Exact exponential solver: Evaluates 2^N truth assignments via backtracking.
     */
    public boolean[] solveExact() {
        boolean[] assignment = new boolean[numVariables + 1];
        if (backtrack(1, assignment)) {
            return assignment;
        }
        return null;
    }

    private boolean backtrack(int varIndex, boolean[] assignment) {
        if (varIndex > numVariables) {
            return verifyCertificate(assignment);
        }

        assignment[varIndex] = true;
        if (backtrack(varIndex + 1, assignment)) return true;

        assignment[varIndex] = false;
        if (backtrack(varIndex + 1, assignment)) return true;

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < clauses.size(); i++) {
            sb.append(clauses.get(i).toString());
            if (i + 1 < clauses.size()) sb.append(" ^ ");
        }
        return sb.toString();
    }
}
