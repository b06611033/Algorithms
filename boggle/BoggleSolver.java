/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private final TrieST trie = new TrieST();
    private int row;
    private int col;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String s : dictionary) {
            if (s.length() < 3) continue;
            trie.put(s);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Stack<String> st = new Stack<>();
        row = board.rows();
        col = board.cols();
        boolean[][] visited = new boolean[row][col];
        char[][] b = new char[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                b[i][j] = board.getLetter(i, j);
            }
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                String s = "";
                dfs(b, visited, i, j, s, st);
            }
        }
        return st;
    }

    private void dfs(char[][] b, boolean[][] visited, int r, int c, String s,
                     Stack<String> st) {
        if (r >= row || r < 0 || c < 0 || c >= col || !trie.prefixExists(s)) return;
        if (visited[r][c]) return;
        visited[r][c] = true;
        if (b[r][c] == 'Q') s = s + "QU";
        else s = s + b[r][c];
        if (trie.contain(s) && !inStack(st, s)) {
            st.push(s);
        }
        for (int i = r - 1; i <= r + 1; i++) {
            for (int j = c - 1; j <= c + 1; j++) {
                dfs(b, visited, i, j, s, st);
            }
        }
        visited[r][c] = false;
    }

    private boolean inStack(Stack<String> st, String word) {
        for (String s : st) {
            if (s.equals(word)) return true;
        }
        return false;
    }


    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int score = 0;
        if (trie.contain(word)) {
            if (word.length() <= 4) score = 1;
            else if (word.length() == 5) score = 2;
            else if (word.length() == 6) score = 3;
            else if (word.length() == 7) score = 5;
            else score = 11;
        }
        return score;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
