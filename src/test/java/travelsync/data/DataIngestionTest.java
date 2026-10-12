package travelsync.data;

import org.junit.jupiter.api.Test;
import travelsync.model.Destination;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataIngestionTest {

    @Test
    public void testCSVParserEscapesAndCommas() {
        String line = "DEST001,Hyderabad,\"City of Pearls, famous for Nizami heritage\",8.6";
        List<String> tokens = CSVParser.parseLine(line);

        assertEquals(4, tokens.size());
        assertEquals("DEST001", tokens.get(0));
        assertEquals("Hyderabad", tokens.get(1));
        assertEquals("City of Pearls, famous for Nizami heritage", tokens.get(2));
        assertEquals("8.6", tokens.get(3));
    }

    @Test
    public void testDataNormalizerAndHaversine() {
        assertEquals("hyderabad", DataNormalizer.normalizeText("  HyDeRaBaD  "));
        List<String> tags = DataNormalizer.parseTags("history, culture, food ; heritage");
        assertEquals(4, tags.size());
        assertTrue(tags.contains("history"));
        assertTrue(tags.contains("heritage"));

        // Haversine distance between Hyderabad (17.3850, 78.4867) and Bengaluru (12.9716, 77.5946) ~ 500 km
        double dist = DataNormalizer.haversineDistanceKm(17.3850, 78.4867, 12.9716, 77.5946);
        assertTrue(dist > 450 && dist < 550, "Distance should be approximately 500 km");
    }

    @Test
    public void testRepositoryInitialization() {
        DataRepository repo = new DataRepository();
        repo.initialize();

        assertTrue(repo.isLoaded());
        assertFalse(repo.getAllDestinations().isEmpty());
        assertFalse(repo.getAllAttractions().isEmpty());
        assertFalse(repo.getAllHotels().isEmpty());
        assertFalse(repo.getAllTransportConnections().isEmpty());
        assertFalse(repo.getAllReviews().isEmpty());
        assertFalse(repo.getAllBookings().isEmpty());

        Destination hyd = repo.findDestinationByName("Hyderabad");
        assertNotNull(hyd);
        assertEquals("DEST001", hyd.getId());
    }
}
