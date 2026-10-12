package travelsync.co2;

import java.util.*;

/**
 * Aho-Corasick Automaton for simultaneous multi-pattern matching in O(N + sum(|P_i|) + Z).
 * Builds a Trie with BFS failure transitions and dictionary output links.
 */
public class AhoCorasick {

    public static class Match {
        private final int startIndex;
        private final int endIndex;
        private final String pattern;

        public Match(int startIndex, int endIndex, String pattern) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.pattern = pattern;
        }

        public int getStartIndex() { return startIndex; }
        public int getEndIndex() { return endIndex; }
        public String getPattern() { return pattern; }

        @Override
        public String toString() {
            return String.format("[%d..%d]: \"%s\"", startIndex, endIndex, pattern);
        }
    }

    private static class Node {
        final Map<Character, Node> children = new HashMap<>();
        Node fail;
        final List<String> output = new ArrayList<>();
    }

    private final Node root = new Node();
    private boolean built = false;

    public void addPattern(String pattern) {
        if (pattern == null || pattern.isEmpty()) return;
        Node curr = root;
        for (char c : pattern.toCharArray()) {
            curr = curr.children.computeIfAbsent(c, k -> new Node());
        }
        curr.output.add(pattern);
        built = false;
    }

    public void addPatterns(Collection<String> patterns) {
        if (patterns != null) {
            for (String p : patterns) {
                addPattern(p);
            }
        }
    }

    /**
     * Builds failure links using Breadth-First Search (BFS).
     */
    public void buildFailureLinks() {
        Queue<Node> queue = new ArrayDeque<>();
        root.fail = root;

        for (Map.Entry<Character, Node> entry : root.children.entrySet()) {
            Node child = entry.getValue();
            child.fail = root;
            queue.add(child);
        }

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            for (Map.Entry<Character, Node> entry : current.children.entrySet()) {
                char ch = entry.getKey();
                Node child = entry.getValue();

                Node fallback = current.fail;
                while (fallback != root && !fallback.children.containsKey(ch)) {
                    fallback = fallback.fail;
                }

                if (fallback.children.containsKey(ch) && fallback.children.get(ch) != child) {
                    child.fail = fallback.children.get(ch);
                } else {
                    child.fail = root;
                }

                // Copy output patterns from fail state
                child.output.addAll(child.fail.output);
                queue.add(child);
            }
        }
        built = true;
    }

    /**
     * Searches the text for all occurrences of all registered patterns.
     */
    public List<Match> search(String text) {
        if (!built) {
            buildFailureLinks();
        }
        List<Match> matches = new ArrayList<>();
        if (text == null || text.isEmpty()) return matches;

        Node current = root;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            while (current != root && !current.children.containsKey(ch)) {
                current = current.fail;
            }

            current = current.children.getOrDefault(ch, root);

            for (String matchedPattern : current.output) {
                matches.add(new Match(i - matchedPattern.length() + 1, i, matchedPattern));
            }
        }

        return matches;
    }
}
