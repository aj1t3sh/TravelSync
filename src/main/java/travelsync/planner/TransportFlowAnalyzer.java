package travelsync.planner;

import travelsync.co4.Dinic;
import travelsync.co4.EdmondsKarp;
import travelsync.co4.FlowNetwork;
import travelsync.co4.MinCut;
import travelsync.data.DataRepository;
import travelsync.model.TransportConnection;

import java.util.*;

/**
 * Transportation Network Flow & Bottleneck Analyzer.
 * Models passenger flow between Indian cities and isolates minimum-cut transit bottlenecks.
 */
public class TransportFlowAnalyzer {

    private final DataRepository repository;

    public static class FlowAnalysisReport {
        public final String originCity;
        public final String destinationCity;
        public final int maxDailyPassengerFlow;
        public final int minCutCapacity;
        public final List<String> bottleneckCorridors;
        public final long executionTimeNs;
        public final boolean verified;

        public FlowAnalysisReport(String originCity, String destinationCity,
                                  int maxDailyPassengerFlow, int minCutCapacity,
                                  List<String> bottleneckCorridors, long executionTimeNs, boolean verified) {
            this.originCity = originCity;
            this.destinationCity = destinationCity;
            this.maxDailyPassengerFlow = maxDailyPassengerFlow;
            this.minCutCapacity = minCutCapacity;
            this.bottleneckCorridors = bottleneckCorridors;
            this.executionTimeNs = executionTimeNs;
            this.verified = verified;
        }

        @Override
        public String toString() {
            return String.format("Transit Flow [%s -> %s]: Max Flow = %d pax/day | Min-Cut = %d pax/day | Bottlenecks: %s",
                    originCity, destinationCity, maxDailyPassengerFlow, minCutCapacity, bottleneckCorridors);
        }
    }

    public TransportFlowAnalyzer(DataRepository repository) {
        this.repository = repository;
    }

    /**
     * Analyzes max passenger transit capacity and bottlenecks between two Indian cities.
     */
    public FlowAnalysisReport analyzeFlow(String origin, String destination) {
        long start = System.nanoTime();
        List<TransportConnection> allTrans = repository.getAllTransportConnections();

        // Index unique city names
        Set<String> citiesSet = new LinkedHashSet<>();
        citiesSet.add(origin);
        for (TransportConnection t : allTrans) {
            citiesSet.add(t.getFromCity());
            citiesSet.add(t.getToCity());
        }
        citiesSet.add(destination);

        List<String> cityList = new ArrayList<>(citiesSet);
        Map<String, Integer> cityToId = new HashMap<>();
        for (int i = 0; i < cityList.size(); i++) {
            cityToId.put(cityList.get(i).toLowerCase(Locale.ROOT), i);
        }

        Integer sourceId = cityToId.get(origin.toLowerCase(Locale.ROOT));
        Integer sinkId = cityToId.get(destination.toLowerCase(Locale.ROOT));

        if (sourceId == null || sinkId == null) {
            return new FlowAnalysisReport(origin, destination, 0, 0,
                    Collections.singletonList("Origin or Destination city not found in transport network."),
                    System.nanoTime() - start, false);
        }

        FlowNetwork network = new FlowNetwork(cityList.size());
        for (TransportConnection t : allTrans) {
            Integer u = cityToId.get(t.getFromCity().toLowerCase(Locale.ROOT));
            Integer v = cityToId.get(t.getToCity().toLowerCase(Locale.ROOT));
            if (u != null && v != null) {
                network.addEdge(u, v, t.getCapacity());
            }
        }

        MinCut.CutResult cutRes = MinCut.findMinCut(network, sourceId, sinkId);

        List<String> bottlenecks = new ArrayList<>();
        for (FlowNetwork.Edge edge : cutRes.getCutEdges()) {
            bottlenecks.add(String.format("%s -> %s (Capacity: %d pax/day)",
                    cityList.get(edge.u), cityList.get(edge.v), edge.capacity));
        }

        if (bottlenecks.isEmpty() && cutRes.getMaxFlowValue() == 0) {
            bottlenecks.add("No connected transit path exists between " + origin + " and " + destination);
        }

        return new FlowAnalysisReport(origin, destination, cutRes.getMaxFlowValue(),
                cutRes.getMinCutCapacity(), bottlenecks, System.nanoTime() - start, cutRes.isTheoremVerified());
    }
}
