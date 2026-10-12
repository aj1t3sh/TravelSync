package travelsync.co1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CO1Test {

    private ProblemClassifier classifier;

    @BeforeEach
    public void setUp() {
        classifier = new ProblemClassifier();
    }

    @Test
    public void testSignatureRetrievalForAllProblemTypes() {
        for (ProblemType type : ProblemType.values()) {
            AlgorithmSignature sig = classifier.getSignature(type);
            assertNotNull(sig, "Signature must not be null for " + type);
            assertNotNull(sig.getSelectedAlgorithm());
            assertNotNull(sig.getTimeComplexity());
            assertNotNull(sig.getSpaceComplexity());
            assertNotNull(sig.getSelectionRationale());
            assertNotNull(sig.getAlternativeAlgorithm());
        }
    }

    @Test
    public void testQueryClassification() {
        assertEquals(ProblemType.FUZZY_STRING_MATCHING, classifier.classifyQuery("fuzzy search with typo").getProblemType());
        assertEquals(ProblemType.MULTI_PATTERN_SEARCH, classifier.classifyQuery("search multi keywords and tags").getProblemType());
        assertEquals(ProblemType.MULTI_STOP_ITINERARY_TSP, classifier.classifyQuery("optimize multi-stop itinerary tour").getProblemType());
        assertEquals(ProblemType.NETWORK_CAPACITY_MAX_FLOW, classifier.classifyQuery("analyze network capacity and bottleneck flow").getProblemType());
        assertEquals(ProblemType.NP_HARD_VERTEX_COVER, classifier.classifyQuery("vertex cover approx problem").getProblemType());
        assertEquals(ProblemType.PROBABILISTIC_PRIMALITY_TEST, classifier.classifyQuery("miller rabin prime testing").getProblemType());
        assertEquals(ProblemType.PARALLEL_PREFIX_SUM, classifier.classifyQuery("parallel prefix scan").getProblemType());
    }

    @Test
    public void testComplexityEvaluatorFormatting() {
        AlgorithmSignature sig = classifier.getSignature(ProblemType.MULTI_STOP_ITINERARY_TSP);
        String card = ComplexityEvaluator.formatSignatureCard(sig);
        assertNotNull(card);
        assertTrue(card.contains("Held-Karp"));
        assertTrue(card.contains("O(K^2 · 2^K)"));
    }
}
