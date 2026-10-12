package travelsync.co2;

import java.util.HashMap;
import java.util.Map;

/**
 * Suffix Automaton (DAWG): Linear-time O(N) compact automaton representing all substrings of a string.
 * Supports distinct substring counting, fast pattern testing in O(M), and longest common substring.
 */
public class SuffixAutomaton {

    public static class State {
        int len;
        int link;
        Map<Character, Integer> next = new HashMap<>();

        State(int len, int link) {
            this.len = len;
            this.link = link;
        }
    }

    private State[] st;
    private int sz;
    private int last;

    public SuffixAutomaton(String s) {
        if (s == null) s = "";
        int n = s.length();
        st = new State[2 * n + 2];
        st[0] = new State(0, -1);
        sz = 1;
        last = 0;

        for (char c : s.toCharArray()) {
            extend(c);
        }
    }

    private void extend(char c) {
        int cur = sz++;
        st[cur] = new State(st[last].len + 1, 0);

        int p = last;
        while (p != -1 && !st[p].next.containsKey(c)) {
            st[p].next.put(c, cur);
            p = st[p].link;
        }

        if (p == -1) {
            st[cur].link = 0;
        } else {
            int q = st[p].next.get(c);
            if (st[p].len + 1 == st[q].len) {
                st[cur].link = q;
            } else {
                int clone = sz++;
                st[clone] = new State(st[p].len + 1, st[q].link);
                st[clone].next.putAll(st[q].next);

                while (p != -1 && st[p].next.get(c) == q) {
                    st[p].next.put(c, clone);
                    p = st[p].link;
                }
                st[q].link = st[cur].link = clone;
            }
        }
        last = cur;
    }

    /**
     * Checks if a pattern exists as a substring in O(M) time.
     */
    public boolean contains(String pattern) {
        if (pattern == null || pattern.isEmpty()) return true;
        int curr = 0;
        for (char c : pattern.toCharArray()) {
            if (!st[curr].next.containsKey(c)) {
                return false;
            }
            curr = st[curr].next.get(c);
        }
        return true;
    }

    /**
     * Computes the total number of distinct substrings in the text in O(N) time.
     */
    public long countDistinctSubstrings() {
        long total = 0;
        for (int i = 1; i < sz; i++) {
            total += st[i].len - st[st[i].link].len;
        }
        return total;
    }

    public int getStateCount() {
        return sz;
    }
}
