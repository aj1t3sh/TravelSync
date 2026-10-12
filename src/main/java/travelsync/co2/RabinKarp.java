package travelsync.co2;

import java.util.ArrayList;
import java.util.List;

/**
 * Rabin-Karp pattern matching algorithm using polynomial rolling hash
 * with modular arithmetic discipline and character-by-character collision verification.
 */
public class RabinKarp {

    private static final long BASE1 = 256;
    private static final long MOD1 = 1_000_000_007L;

    private static final long BASE2 = 257;
    private static final long MOD2 = 1_000_000_009L;

    public static class RKResult {
        private final List<Integer> matchIndices;
        private final int hashCollisions;
        private final long hashEvaluations;
        private final long characterComparisons;
        private final long executionTimeNs;

        public RKResult(List<Integer> matchIndices, int hashCollisions,
                        long hashEvaluations, long characterComparisons, long executionTimeNs) {
            this.matchIndices = matchIndices;
            this.hashCollisions = hashCollisions;
            this.hashEvaluations = hashEvaluations;
            this.characterComparisons = characterComparisons;
            this.executionTimeNs = executionTimeNs;
        }

        public List<Integer> getMatchIndices() { return matchIndices; }
        public int getHashCollisions() { return hashCollisions; }
        public long getHashEvaluations() { return hashEvaluations; }
        public long getCharacterComparisons() { return characterComparisons; }
        public long getExecutionTimeNs() { return executionTimeNs; }
        public int getMatchCount() { return matchIndices.size(); }
    }

    /**
     * Searches for pattern in text using single-hash Rabin-Karp with collision verification.
     */
    public static RKResult search(String text, String pattern) {
        return search(text, pattern, false);
    }

    /**
     * Searches for pattern in text with optional double-hashing.
     */
    public static RKResult search(String text, String pattern, boolean useDoubleHash) {
        long start = System.nanoTime();
        List<Integer> matches = new ArrayList<>();
        int collisions = 0;
        long hashEvaluations = 0;
        long charComparisons = 0;

        if (text == null || pattern == null || pattern.isEmpty() || pattern.length() > text.length()) {
            return new RKResult(matches, 0, 0, 0, System.nanoTime() - start);
        }

        int n = text.length();
        int m = pattern.length();

        long h1 = 1;
        long h2 = 1;
        for (int i = 0; i < m - 1; i++) {
            h1 = (h1 * BASE1) % MOD1;
            if (useDoubleHash) {
                h2 = (h2 * BASE2) % MOD2;
            }
        }

        long patHash1 = 0, textHash1 = 0;
        long patHash2 = 0, textHash2 = 0;

        for (int i = 0; i < m; i++) {
            patHash1 = (BASE1 * patHash1 + pattern.charAt(i)) % MOD1;
            textHash1 = (BASE1 * textHash1 + text.charAt(i)) % MOD1;
            if (useDoubleHash) {
                patHash2 = (BASE2 * patHash2 + pattern.charAt(i)) % MOD2;
                textHash2 = (BASE2 * textHash2 + text.charAt(i)) % MOD2;
            }
        }
        hashEvaluations++;

        for (int i = 0; i <= n - m; i++) {
            boolean hashMatch = (patHash1 == textHash1) && (!useDoubleHash || (patHash2 == textHash2));

            if (hashMatch) {
                // Collision verification phase
                boolean exactMatch = true;
                for (int j = 0; j < m; j++) {
                    charComparisons++;
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        exactMatch = false;
                        collisions++;
                        break;
                    }
                }
                if (exactMatch) {
                    matches.add(i);
                }
            }

            if (i < n - m) {
                textHash1 = (BASE1 * (textHash1 - text.charAt(i) * h1) + text.charAt(i + m)) % MOD1;
                if (textHash1 < 0) textHash1 += MOD1;

                if (useDoubleHash) {
                    textHash2 = (BASE2 * (textHash2 - text.charAt(i) * h2) + text.charAt(i + m)) % MOD2;
                    if (textHash2 < 0) textHash2 += MOD2;
                }
                hashEvaluations++;
            }
        }

        return new RKResult(matches, collisions, hashEvaluations, charComparisons, System.nanoTime() - start);
    }
}
