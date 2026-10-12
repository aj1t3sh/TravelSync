package travelsync.planner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import travelsync.model.ItineraryPlan;
import travelsync.model.TravelerPreferences;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlannerTest {

    private TravelSyncEngine engine;

    @BeforeEach
    public void setUp() {
        engine = new TravelSyncEngine();
    }

    @Test
    public void testDestinationSearch() {
        List<DestinationSearchEngine.SearchResult> res = engine.searchDestinations("Hyderabad");
        assertFalse(res.isEmpty());
        assertEquals("DEST001", res.get(0).getDestination().getId());

        // Fuzzy search
        List<DestinationSearchEngine.SearchResult> fuzzyRes = engine.searchDestinations("Hyderbad");
        assertFalse(fuzzyRes.isEmpty());
        assertEquals("DEST001", fuzzyRes.get(0).getDestination().getId());
    }

    @Test
    public void testItineraryOptimization() {
        TravelerPreferences prefs = new TravelerPreferences("Hyderabad", Arrays.asList("Bengaluru", "Chennai"), 5, 30000, Collections.singletonList("history"), "ANY");
        ItineraryPlan plan = engine.optimizeItinerary(prefs);

        assertNotNull(plan);
        assertTrue(plan.isFeasible());
        assertEquals("Hyderabad", plan.getStartCity());
        assertTrue(plan.getTotalDistanceKm() > 0);
        assertTrue(plan.getTotalEstimatedCostInr() > 0);
        assertFalse(plan.getDailyBreakdown().isEmpty());
    }

    @Test
    public void testTransportFlowAnalysis() {
        TransportFlowAnalyzer.FlowAnalysisReport report = engine.analyzeTransitFlow("Hyderabad", "Bengaluru");
        assertNotNull(report);
        assertTrue(report.maxDailyPassengerFlow > 0);
        assertTrue(report.verified);
    }
}
