package data;


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


    class Node {
        boolean item;
        Node[] children = new Node[26];
    }

}
