package travelsync.co5;

import java.util.ArrayList;
import java.util.List;

/**
 * Polynomial-Time Reduction: 3-SAT <=_p CLIQUE.
 * Constructs graph G where a clique of size k = |Clauses| exists iff 3-SAT formula is satisfiable.
 */
public class ThreeSATToClique {

    public static class GraphNode {
        public final int nodeId;
        public final int clauseIndex;
        public final ThreeSAT.Literal literal;

        public GraphNode(int nodeId, int clauseIndex, ThreeSAT.Literal literal) {
            this.nodeId = nodeId;
            this.clauseIndex = clauseIndex;
            this.literal = literal;
        }

        @Override
        public String toString() {
            return String.format("Node %d [Clause %d: %s]", nodeId, clauseIndex, literal);
        }
    }

    public static class ReductionResult {
        public final List<GraphNode> nodes;
        public final boolean[][] adjMatrix;
        public final int targetK;

        public ReductionResult(List<GraphNode> nodes, boolean[][] adjMatrix, int targetK) {
            this.nodes = nodes;
            this.adjMatrix = adjMatrix;
            this.targetK = targetK;
        }
    }

    /**
     * Executes the polynomial-time reduction in O((3C)^2) time.
     */
    public static ReductionResult reduce(ThreeSAT formula) {
        List<GraphNode> nodes = new ArrayList<>();
        List<ThreeSAT.Clause> clauses = formula.getClauses();
        int nodeId = 0;

        for (int cIdx = 0; cIdx < clauses.size(); cIdx++) {
            ThreeSAT.Clause clause = clauses.get(cIdx);
            for (ThreeSAT.Literal lit : clause.literals) {
                nodes.add(new GraphNode(nodeId++, cIdx, lit));
            }
        }

        int totalNodes = nodes.size();
        boolean[][] adj = new boolean[totalNodes][totalNodes];

        for (int i = 0; i < totalNodes; i++) {
            for (int j = i + 1; j < totalNodes; j++) {
                GraphNode u = nodes.get(i);
                GraphNode v = nodes.get(j);

                // Edge exists if different clauses and non-contradictory literals
                if (u.clauseIndex != v.clauseIndex) {
                    boolean contradictory = (u.literal.variable == v.literal.variable &&
                                            u.literal.isNegated != v.literal.isNegated);
                    if (!contradictory) {
                        adj[i][j] = true;
                        adj[j][i] = true;
                    }
                }
            }
        }

        return new ReductionResult(nodes, adj, clauses.size());
    }
}
