package travelsync.co3;

import java.util.ArrayList;
import java.util.List;

/**
 * Dynamic Programming on Trees:
 * 1. Maximum Weight Independent Set (MWIS) on Tree in O(V) linear time.
 * 2. Tree Rerooting Technique for all-nodes distance aggregation in O(V) time.
 */
public class TreeDP {

    public static class MWISResult {
        private final long maxWeight;
        private final List<Integer> selectedNodes;

        public MWISResult(long maxWeight, List<Integer> selectedNodes) {
            this.maxWeight = maxWeight;
            this.selectedNodes = selectedNodes;
        }

        public long getMaxWeight() { return maxWeight; }
        public List<Integer> getSelectedNodes() { return selectedNodes; }
    }

    /**
     * Solves Maximum Weight Independent Set on a tree graph.
     * @param n Number of vertices (0 to n-1)
     * @param adj Adjacency list of the tree
     * @param weights Node weights (e.g. tourist appeal scores)
     */
    public static MWISResult solveMWIS(int n, List<List<Integer>> adj, long[] weights) {
        if (n == 0) return new MWISResult(0, new ArrayList<>());

        long[][] dp = new long[n][2];
        dfsMWIS(0, -1, adj, weights, dp);

        long maxWeight = Math.max(dp[0][0], dp[0][1]);
        List<Integer> selected = new ArrayList<>();
        boolean takeRoot = dp[0][1] > dp[0][0];
        reconstructMWIS(0, -1, takeRoot, adj, dp, selected);

        return new MWISResult(maxWeight, selected);
    }

    private static void dfsMWIS(int u, int p, List<List<Integer>> adj, long[] weights, long[][] dp) {
        dp[u][0] = 0;
        dp[u][1] = weights[u];

        for (int v : adj.get(u)) {
            if (v == p) continue;
            dfsMWIS(v, u, adj, weights, dp);
            dp[u][0] += Math.max(dp[v][0], dp[v][1]);
            dp[u][1] += dp[v][0];
        }
    }

    private static void reconstructMWIS(int u, int p, boolean taken, List<List<Integer>> adj, long[][] dp, List<Integer> selected) {
        if (taken) {
            selected.add(u);
            for (int v : adj.get(u)) {
                if (v == p) continue;
                reconstructMWIS(v, u, false, adj, dp, selected);
            }
        } else {
            for (int v : adj.get(u)) {
                if (v == p) continue;
                boolean takeChild = dp[v][1] > dp[v][0];
                reconstructMWIS(v, u, takeChild, adj, dp, selected);
            }
        }
    }

    /**
     * Tree Rerooting: Computes total distance sum from every node to all other nodes in O(V) time.
     */
    public static long[] computeAllNodesDistanceSum(int n, List<List<Integer>> adj) {
        if (n <= 0) return new long[0];
        long[] count = new long[n];
        long[] ans = new long[n];

        // First DFS: Subtree counts and root (0) distance sum
        dfsDown(0, -1, adj, count, ans);

        // Second DFS: Rerooting to parent
        dfsReroot(0, -1, n, adj, count, ans);

        return ans;
    }

    private static void dfsDown(int u, int p, List<List<Integer>> adj, long[] count, long[] ans) {
        count[u] = 1;
        for (int v : adj.get(u)) {
            if (v == p) continue;
            dfsDown(v, u, adj, count, ans);
            count[u] += count[v];
            ans[u] += ans[v] + count[v];
        }
    }

    private static void dfsReroot(int u, int p, int n, List<List<Integer>> adj, long[] count, long[] ans) {
        for (int v : adj.get(u)) {
            if (v == p) continue;
            ans[v] = ans[u] - count[v] + (n - count[v]);
            dfsReroot(v, u, n, adj, count, ans);
        }
    }
}
