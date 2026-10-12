package travelsync.co4;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CO4Test {

    @Test
    public void testMaxFlowAndMinCutDuality() {
        // Classic 6-vertex flow network
        // Source 0, Sink 5
        FlowNetwork network = new FlowNetwork(6);
        network.addEdge(0, 1, 16);
        network.addEdge(0, 2, 13);
        network.addEdge(1, 2, 10);
        network.addEdge(1, 3, 12);
        network.addEdge(2, 1, 4);
        network.addEdge(2, 4, 14);
        network.addEdge(3, 2, 9);
        network.addEdge(3, 5, 20);
        network.addEdge(4, 3, 7);
        network.addEdge(4, 5, 4);

        EdmondsKarp.FlowResult ek = EdmondsKarp.computeMaxFlow(network, 0, 5);
        assertEquals(23, ek.getMaxFlow());

        Dinic.DinicResult dinic = Dinic.computeMaxFlow(network, 0, 5);
        assertEquals(23, dinic.getMaxFlow());

        FordFulkerson.FlowResult ff = FordFulkerson.computeMaxFlow(network, 0, 5);
        assertEquals(23, ff.getMaxFlow());

        MinCut.CutResult cut = MinCut.findMinCut(network, 0, 5);
        assertEquals(23, cut.getMaxFlowValue());
        assertEquals(23, cut.getMinCutCapacity());
        assertTrue(cut.isTheoremVerified());
    }

    @Test
    public void testBipartiteMatching() {
        List<String> applicants = Arrays.asList("Tour Group 1", "Tour Group 2", "Tour Group 3");
        List<String> hotelRooms = Arrays.asList("Room 101", "Room 102", "Room 103");

        boolean[][] compat = {
            {true, true, false},
            {false, true, false},
            {false, false, true}
        };

        BipartiteMatching.MatchingResult res = BipartiteMatching.computeMatching(applicants, hotelRooms, compat);
        assertEquals(3, res.getMaxMatches());
        assertEquals(3, res.getKonigMinVertexCoverSize());
        assertEquals(3, res.getMatchedPairs().size());
    }
}
