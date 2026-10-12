package travelsync.co4;

import java.util.ArrayList;
import java.util.List;

/**
 * Directed Residual Flow Network representation.
 * Supports residual forward and backward edges, flow conservation, and adjacency indexing.
 */
public class FlowNetwork {

    public static class Edge {
        public final int u;
        public final int v;
        public final int capacity;
        public int flow;
        public Edge residualReverse;

        public Edge(int u, int v, int capacity) {
            this.u = u;
            this.v = v;
            this.capacity = capacity;
            this.flow = 0;
        }

        public int remainingCapacity() {
            return capacity - flow;
        }

        public void augment(int bottleneck) {
            this.flow += bottleneck;
            this.residualReverse.flow -= bottleneck;
        }

        @Override
        public String toString() {
            return String.format("(%d -> %d | %d/%d)", u, v, flow, capacity);
        }
    }

    private final int vertexCount;
    private final List<List<Edge>> adj;
    private final List<Edge> allEdges;

    public FlowNetwork(int vertexCount) {
        this.vertexCount = vertexCount;
        this.adj = new ArrayList<>(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            this.adj.add(new ArrayList<>());
        }
        this.allEdges = new ArrayList<>();
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public List<List<Edge>> getAdjacencyList() {
        return adj;
    }

    public List<Edge> getAllEdges() {
        return allEdges;
    }

    /**
     * Adds a directed edge with forward capacity and matching reverse residual edge.
     */
    public void addEdge(int u, int v, int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity must be non-negative");
        }
        Edge forward = new Edge(u, v, capacity);
        Edge backward = new Edge(v, u, 0); // Reverse residual edge with 0 initial capacity

        forward.residualReverse = backward;
        backward.residualReverse = forward;

        adj.get(u).add(forward);
        adj.get(v).add(backward);

        allEdges.add(forward);
    }

    /**
     * Resets all flows in the network back to 0.
     */
    public void resetFlows() {
        for (Edge e : allEdges) {
            e.flow = 0;
            if (e.residualReverse != null) {
                e.residualReverse.flow = 0;
            }
        }
    }
}
