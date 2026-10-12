package travelsync.planner;

import travelsync.co2.AhoCorasick;
import travelsync.co2.KMP;
import travelsync.co2.RabinKarp;
import travelsync.co3.WagnerFischer;
import travelsync.data.DataNormalizer;
import travelsync.data.DataRepository;
import travelsync.model.Attraction;
import travelsync.model.Destination;

import java.util.*;

/**
 * Hybrid Destination Search Engine combining:
 * 1. KMP / Rabin-Karp exact substring matching on destination descriptions
 * 2. Aho-Corasick multi-keyword tagging
 * 3. Wagner-Fischer Levenshtein fuzzy distance for typo tolerance.
 */
public class DestinationSearchEngine {

    private final DataRepository repository;

    public static class SearchResult {
        private final Destination destination;
        private final double score;
        private final String matchType; // EXACT, FUZZY, TAG_MATCH
        private final List<String> matchedKeywords;
        private final List<Attraction> topAttractions;

        public SearchResult(Destination destination, double score, String matchType,
                            List<String> matchedKeywords, List<Attraction> topAttractions) {
            this.destination = destination;
            this.score = score;
            this.matchType = matchType;
            this.matchedKeywords = matchedKeywords != null ? matchedKeywords : Collections.emptyList();
            this.topAttractions = topAttractions != null ? topAttractions : Collections.emptyList();
        }

        public Destination getDestination() { return destination; }
        public double getScore() { return score; }
        public String getMatchType() { return matchType; }
        public List<String> getMatchedKeywords() { return matchedKeywords; }
        public List<Attraction> getTopAttractions() { return topAttractions; }

        @Override
        public String toString() {
            return String.format("[%s] %s (Score: %.2f) - Match: %s | Tags: %s",
                    destination.getId(), destination.getName(), score, matchType, matchedKeywords);
        }
    }

    public DestinationSearchEngine(DataRepository repository) {
        this.repository = repository;
    }

    /**
     * Searches destinations using exact matching (KMP / Rabin-Karp), tag matching, and fuzzy matching.
     */
    public List<SearchResult> search(String query) {
        List<SearchResult> results = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            // Return top destinations by default rating
            for (Destination d : repository.getAllDestinations()) {
                results.add(new SearchResult(d, d.getRating() / 10.0, "DEFAULT_RANKING",
                        Collections.emptyList(), repository.getAttractionsForDestination(d.getId())));
            }
            results.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
            return results;
        }

        String normalizedQuery = DataNormalizer.normalizeText(query);
        List<String> queryTokens = DataNormalizer.parseTags(query);

        // Build Aho-Corasick for multi-keyword matching
        AhoCorasick ac = new AhoCorasick();
        ac.addPatterns(queryTokens);
        ac.addPattern(normalizedQuery);
        ac.buildFailureLinks();

        for (Destination d : repository.getAllDestinations()) {
            String normName = DataNormalizer.normalizeText(d.getName());
            String normDesc = DataNormalizer.normalizeText(d.getDescription());
            String normTags = DataNormalizer.normalizeText(d.getTags());

            double score = 0.0;
            String matchType = "NONE";
            List<String> matchedWords = new ArrayList<>();

            // 1. Exact Substring Search via KMP on destination name
            KMP.KMPResult kmpName = KMP.search(normName, normalizedQuery);
            if (kmpName.getMatchCount() > 0) {
                score += 5.0;
                matchType = "EXACT_NAME";
                matchedWords.add(normalizedQuery);
            }

            // 2. Exact Substring Search via Rabin-Karp on description
            RabinKarp.RKResult rkDesc = RabinKarp.search(normDesc, normalizedQuery);
            if (rkDesc.getMatchCount() > 0) {
                score += 2.0 * rkDesc.getMatchCount();
                if ("NONE".equals(matchType)) matchType = "EXACT_DESCRIPTION";
                matchedWords.add(normalizedQuery);
            }

            // 3. Multi-Pattern Aho-Corasick search on tags & description
            List<AhoCorasick.Match> acMatches = ac.search(normDesc + " " + normTags);
            for (AhoCorasick.Match m : acMatches) {
                score += 1.5;
                if (!matchedWords.contains(m.getPattern())) {
                    matchedWords.add(m.getPattern());
                }
                if ("NONE".equals(matchType)) matchType = "TAG_MATCH";
            }

            // 4. Fuzzy Wagner-Fischer matching if no exact name match
            if (!"EXACT_NAME".equals(matchType)) {
                double sim = WagnerFischer.similarityScore(normName, normalizedQuery);
                if (sim >= 0.60) {
                    score += sim * 3.5;
                    matchType = "FUZZY_NAME (" + String.format("%.0f%% sim", sim * 100) + ")";
                    matchedWords.add(d.getName());
                }
            }

            // Boost score by destination rating (1.0 to 10.0 scale)
            score += (d.getRating() / 10.0) * 0.5;

            if (score > 0.5) {
                List<Attraction> attrs = repository.getAttractionsForDestination(d.getId());
                results.add(new SearchResult(d, score, matchType, matchedWords, attrs));
            }
        }

        results.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
        return results;
    }
}
