package travelsync.co4;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Minimum s-t Cut Extraction and Max-Flow Min-Cut Duality Verification.
 * Identifies the minimum-capacity bottleneck cut separating source and sink.
 */
public class MinCut {

    public static class CutResult {
        private final int maxFlowValue;
        private final int minCutCapacity;
        private final List<Integer> sourcePartition;
        private final List<Integer> sinkPartition;
        private final List<FlowNetwork.Edge> cutEdges;
        private final boolean theoremVerified;

        public CutResult(int maxFlowValue, int minCutCapacity,
                         List<Integer> sourcePartition, List<Integer> sinkPartition,
                         List<FlowNetwork.Edge> cutEdges, boolean theoremVerified) {
            this.maxFlowValue = maxFlowValue;
            this.minCutCapacity = minCutCapacity;
            this.sourcePartition = sourcePartition;
            this.sinkPartition = sinkPartition;
            this.cutEdges = cutEdges;
            this.theoremVerified = theoremVerified;
        }

        public int getMaxFlowValue() { return maxFlowValue; }
        public int getMinCutCapacity() { return minCutCapacity; }
        public List<Integer> getSourcePartition() { return sourcePartition; }
        public List<Integer> getSinkPartition() { return sinkPartition; }
        public List<FlowNetwork.Edge> getCutEdges() { return cutEdges; }
        public boolean isTheoremVerified() { return theoremVerified; }

        @Override
        public String toString() {
            return String.format("MaxFlow: %d | MinCut Capacity: %d | Cut Edges: %d | Duality Verified: %s",
                    maxFlowValue, minCutCapacity, cutEdges.size(), theoremVerified);
        }
    }

    /**
     * Solves Max-Flow and extracts the Minimum s-t Cut.
     */
    public static CutResult findMinCut(FlowNetwork network, int source, int sink) {
        EdmondsKarp.FlowResult flowRes = EdmondsKarp.computeMaxFlow(network, source, sink);
        int maxFlow = flowRes.getMaxFlow();

        int n = network.getVertexCount();
        boolean[] reachable = new boolean[n];
        Queue<Integer> queue = new ArrayDeque<>();

        reachable[source] = true;
        queue.add(source);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (FlowNetwork.Edge edge : network.getAdjacencyList().get(u)) {
                if (edge.remainingCapacity() > 0 && !reachable[edge.v]) {
                    reachable[edge.v] = true;
                    queue.add(edge.v);
                }
            }
        }

        List<Integer> sourcePart = new ArrayList<>();
        List<Integer> sinkPart = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (reachable[i]) {
                sourcePart.add(i);
            } else {
                sinkPart.add(i);
            }
        }

        List<FlowNetwork.Edge> cutEdges = new ArrayList<>();
        int cutCapacity = 0;

        for (FlowNetwork.Edge edge : network.getAllEdges()) {
            if (reachable[edge.u] && !reachable[edge.v]) {
                cutEdges.add(edge);
                cutCapacity += edge.capacity;
            }
        }

        boolean verified = (maxFlow == cutCapacity);
        return new CutResult(maxFlow, cutCapacity, sourcePart, sinkPart, cutEdges, verified);
    }
}
