package data;


import java.util.HashSet;
import java.util.Set;

public class TrieWordData {

    Node root = new Node();

    public void addWord(String w) {
        Node node = root;

        for (char c : w.toCharArray()) {
            if (node.children[c - 'A'] == null) {
                node.children[c - 'A'] = new Node();
            }
            node = node.children[c - 'A'];
        }

        node.item = true;
    }

    public boolean findWord(String w) {
        Node node = root;
        for (char c : w.toCharArray()) {
            if (node.children[c - 'A'] == null) {
                return false;
            }
            node = node.children[c - 'A'];
        }

        if (node.item) {
            return true;
        }
        return false;
    }

    public boolean findPrefix(String pref) {
        Node node = root;
        for (char c : pref.toCharArray()) {
            if (node.children[c - 'A'] == null) {
                return false;
            }
            node = node.children[c - 'A'];
        }

        return true;
    }

    static public Set countWords(char[] grid, TrieWordData trie) {

        boolean[] notReachedIndex;
        Set<String> wordCounted = new HashSet<>();

        // i is a starting index.
        for (int i = 0; i < 16; i++) {
            notReachedIndex = new boolean[]{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};
            helperCountWords(grid, notReachedIndex, "", i, trie, wordCounted);

        }

        return wordCounted;
    }

    static public void helperCountWords(char[] grid, boolean[] notReachedIndex, String prefix, int index, TrieWordData trie, Set<String> table) {
        notReachedIndex[index] = false;

        int[] pathValues = {-5, -4, -3, -1, 1, 3, 4, 5};
        for (int path : pathValues) {
            if (0 <= index + path && index + path < 16
                    && notReachedIndex[index + path]) {

                if ((index % 4 != 0 || (index + path) % 4 != 3) && (index % 4 != 3 || (index + path) % 4 != 0) ){

                    if (trie.findPrefix(prefix + grid[index + path])) {
                        helperCountWords(grid, notReachedIndex, prefix + grid[index + path], index + path, trie, table);
                    }

                    if (trie.findWord(prefix + grid[index + path])) {

                        if (!table.contains(prefix + grid[index + path])) {
                            table.add(prefix + grid[index + path]);
                        }

                    }
                }

            }
        }

    }

    class Node {
        boolean item;
        Node[] children = new Node[26];
    }

}
