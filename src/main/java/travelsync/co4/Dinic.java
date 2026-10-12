package travelsync.co4;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * Dinic's Algorithm for Maximum Flow: O(V^2 · E).
 * Employs level graphs (BFS) and blocking flows (DFS with current-arc pointer optimization).
 */
public class Dinic {

    public static class DinicResult {
        private final int maxFlow;
        private final int phasesCount;
        private final long executionTimeNs;

        public DinicResult(int maxFlow, int phasesCount, long executionTimeNs) {
            this.maxFlow = maxFlow;
            this.phasesCount = phasesCount;
            this.executionTimeNs = executionTimeNs;
        }

        public int getMaxFlow() { return maxFlow; }
        public int getPhasesCount() { return phasesCount; }
        public long getExecutionTimeNs() { return executionTimeNs; }
    }

    public static DinicResult computeMaxFlow(FlowNetwork network, int source, int sink) {
        long start = System.nanoTime();
        network.resetFlows();

        int n = network.getVertexCount();
        int[] level = new int[n];
        int[] ptr = new int[n];
        int maxFlow = 0;
        int phases = 0;

        while (bfs(network, source, sink, level)) {
            Arrays.fill(ptr, 0);
            while (true) {
                int pushed = dfs(network, source, sink, Integer.MAX_VALUE, level, ptr);
                if (pushed <= 0) break;
                maxFlow += pushed;
            }
            phases++;
        }

        return new DinicResult(maxFlow, phases, System.nanoTime() - start);
    }

    private static boolean bfs(FlowNetwork network, int source, int sink, int[] level) {
        Arrays.fill(level, -1);
        level[source] = 0;
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(source);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (FlowNetwork.Edge edge : network.getAdjacencyList().get(u)) {
                if (edge.remainingCapacity() > 0 && level[edge.v] == -1) {
                    level[edge.v] = level[u] + 1;
                    queue.add(edge.v);
                }
            }
        }
        return level[sink] != -1;
    }

    private static int dfs(FlowNetwork network, int u, int sink, int pushed, int[] level, int[] ptr) {
        if (pushed == 0 || u == sink) return pushed;

        List<FlowNetwork.Edge> edges = network.getAdjacencyList().get(u);
        for (int cid = ptr[u]; cid < edges.size(); cid++, ptr[u]++) {
            FlowNetwork.Edge edge = edges.get(cid);
            int trg = edge.v;

            if (level[u] + 1 != level[trg] || edge.remainingCapacity() == 0) continue;

            int tr = dfs(network, trg, sink, Math.min(pushed, edge.remainingCapacity()), level, ptr);
            if (tr == 0) continue;

            edge.augment(tr);
            return tr;
        }
        return 0;
    }
}
