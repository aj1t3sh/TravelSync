package travelsync.co4;

import java.util.List;

/**
 * Ford-Fulkerson Method for Maximum Flow using DFS augmenting paths.
 * Complexity: O(E · |f*|) where f* is the maximum flow.
 */
public class FordFulkerson {

    public static class FlowResult {
        private final int maxFlow;
        private final int augmentingPathsCount;
        private final long executionTimeNs;

        public FlowResult(int maxFlow, int augmentingPathsCount, long executionTimeNs) {
            this.maxFlow = maxFlow;
            this.augmentingPathsCount = augmentingPathsCount;
            this.executionTimeNs = executionTimeNs;
        }

        public int getMaxFlow() { return maxFlow; }
        public int getAugmentingPathsCount() { return augmentingPathsCount; }
        public long getExecutionTimeNs() { return executionTimeNs; }
    }

    public static FlowResult computeMaxFlow(FlowNetwork network, int source, int sink) {
        long start = System.nanoTime();
        network.resetFlows();

        int maxFlow = 0;
        int paths = 0;
        int n = network.getVertexCount();
        boolean[] visited = new boolean[n];

        while (true) {
            java.util.Arrays.fill(visited, false);
            int bottleneck = dfs(network, source, sink, Integer.MAX_VALUE, visited);
            if (bottleneck <= 0) {
                break;
            }
            maxFlow += bottleneck;
            paths++;
        }

        return new FlowResult(maxFlow, paths, System.nanoTime() - start);
    }

    private static int dfs(FlowNetwork network, int u, int sink, int flowSoFar, boolean[] visited) {
        if (u == sink) return flowSoFar;
        visited[u] = true;

        List<FlowNetwork.Edge> edges = network.getAdjacencyList().get(u);
        for (FlowNetwork.Edge edge : edges) {
            if (!visited[edge.v] && edge.remainingCapacity() > 0) {
                int bottleneck = dfs(network, edge.v, sink, Math.min(flowSoFar, edge.remainingCapacity()), visited);
                if (bottleneck > 0) {
                    edge.augment(bottleneck);
                    return bottleneck;
                }
            }
        }
        return 0;
    }
}
