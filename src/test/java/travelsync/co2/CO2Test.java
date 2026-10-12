package travelsync.co2;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CO2Test {

    @Test
    public void testNaiveAndKMPPatternMatching() {
        String text = "ABABDABACDABABCABAB";
        String pattern = "ABABCABAB";

        NaiveSearch.MatchResult naive = NaiveSearch.search(text, pattern);
        KMP.KMPResult kmp = KMP.search(text, pattern);

        assertEquals(1, naive.getMatchCount());
        assertEquals(1, kmp.getMatchCount());
        assertEquals(10, (int) naive.getMatchIndices().get(0));
        assertEquals(10, (int) kmp.getMatchIndices().get(0));

        // Verify LPS array computation
        int[] lps = KMP.computeLPS("AABAACAABAA");
        assertNotNull(lps);
        assertEquals(11, lps.length);
        assertEquals(5, lps[10]);
    }

    @Test
    public void testZAlgorithm() {
        String text = "GEEKS FOR GEEKS";
        String pattern = "GEEKS";

        ZAlgorithm.ZResult zRes = ZAlgorithm.search(text, pattern);
        assertEquals(2, zRes.getMatchCount());
        assertEquals(Arrays.asList(0, 10), zRes.getMatchIndices());
    }

    @Test
    public void testRabinKarpCollisionVerification() {
        String text = "HYDERABAD IS FAMOUS FOR CHARMINAR AND ROYAL HERITAGE";
        String pattern = "CHARMINAR";

        RabinKarp.RKResult rk = RabinKarp.search(text, pattern, true);
        assertEquals(1, rk.getMatchCount());
        assertEquals(24, (int) rk.getMatchIndices().get(0));

        // Pattern not present
        RabinKarp.RKResult rkAbsent = RabinKarp.search(text, "NONEXISTENT", false);
        assertEquals(0, rkAbsent.getMatchCount());
    }

    @Test
    public void testSuffixArrayAndKasaiLCP() {
        String s = "banana";
        SuffixArray sa = new SuffixArray(s);
        int[] saArr = sa.getSuffixArray();
        assertEquals(6, saArr.length);

        // Binary search on suffix array
        List<Integer> matches = sa.search("ana");
        assertEquals(2, matches.size());
        assertTrue(matches.contains(1));
        assertTrue(matches.contains(3));

        // Kasai LCP
        int[] lcp = KasaiLCP.computeLCP(s, saArr);
        assertEquals(6, lcp.length);

        // Longest Common Substring
        KasaiLCP.CommonSubstringResult lcs = KasaiLCP.findLongestCommonSubstring("visiting hyderabad charminar", "exploring hyderabad monument");
        assertTrue(lcs.getLongestCommonSubstring().contains("hyderabad"));
        assertEquals(14, lcs.getLength());
    }

    @Test
    public void testAhoCorasickMultiPattern() {
        AhoCorasick ac = new AhoCorasick();
        ac.addPatterns(Arrays.asList("heritage", "beach", "fort", "palace"));
        ac.buildFailureLinks();

        String text = "Exploring heritage fort and ancient palace in Rajasthan";
        List<AhoCorasick.Match> matches = ac.search(text);

        assertEquals(3, matches.size());
        List<String> matchedPatterns = matches.stream().map(AhoCorasick.Match::getPattern).toList();
        assertTrue(matchedPatterns.contains("heritage"));
        assertTrue(matchedPatterns.contains("fort"));
        assertTrue(matchedPatterns.contains("palace"));
    }

    @Test
    public void testSuffixAutomaton() {
        SuffixAutomaton sam = new SuffixAutomaton("abacaba");
        assertTrue(sam.contains("bac"));
        assertTrue(sam.contains("caba"));
        assertTrue(sam.contains("a"));
        assertFalse(sam.contains("xyz"));
        assertTrue(sam.countDistinctSubstrings() > 0);
    }
}
