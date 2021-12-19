/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class TrieST {
    private static final int A = 'A';
    private static final int R = 26;
    private Node root = new Node();

    private static class Node {
        private Node[] next = new Node[R];
        private String word = null;
    }

    public void put(String word) {
        put(root, word);
    }

    private void put(Node x, String word) {
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (x.next[c - A] == null) x.next[c - A] = new Node();
            x = x.next[c - A];
        }
        x.word = word;
    }

    public boolean contain(String word) {
        Node x = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (x.next[c - A] == null) return false;
            x = x.next[c - A];
        }
        if (x.word != null) return true;
        return false;
    }

    public boolean prefixExists(String prefix) {
        Node x = root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (x.next[c - A] == null) return false;
            x = x.next[c - A];
        }
        return true;
    }

    public static void main(String[] args) {

    }
}
