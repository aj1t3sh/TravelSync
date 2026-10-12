package travelsync.co4;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 * Edmonds-Karp Maximum Flow algorithm: O(V · E^2).
 * Uses Breadth-First Search (BFS) to find shortest augmenting paths in the residual graph.
 */
public class EdmondsKarp {

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

        int n = network.getVertexCount();
        int maxFlow = 0;
        int paths = 0;
        FlowNetwork.Edge[] parentEdge = new FlowNetwork.Edge[n];

        while (bfs(network, source, sink, parentEdge)) {
            // Find bottleneck along path
            int bottleneck = Integer.MAX_VALUE;
            for (int curr = sink; curr != source; ) {
                FlowNetwork.Edge edge = parentEdge[curr];
                bottleneck = Math.min(bottleneck, edge.remainingCapacity());
                curr = edge.u;
            }

            // Augment flow
            for (int curr = sink; curr != source; ) {
                FlowNetwork.Edge edge = parentEdge[curr];
                edge.augment(bottleneck);
                curr = edge.u;
            }

            maxFlow += bottleneck;
            paths++;
        }

        return new FlowResult(maxFlow, paths, System.nanoTime() - start);
    }

    private static boolean bfs(FlowNetwork network, int source, int sink, FlowNetwork.Edge[] parentEdge) {
        Arrays.fill(parentEdge, null);
        Queue<Integer> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[network.getVertexCount()];

        visited[source] = true;
        queue.add(source);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            if (u == sink) {
                return true;
            }

            for (FlowNetwork.Edge edge : network.getAdjacencyList().get(u)) {
                if (!visited[edge.v] && edge.remainingCapacity() > 0) {
                    visited[edge.v] = true;
                    parentEdge[edge.v] = edge;
                    queue.add(edge.v);
                }
            }
        }

        return visited[sink];
    }
}
