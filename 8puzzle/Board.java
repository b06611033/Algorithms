/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private final int N;
    private final int[][] board;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        N = tiles.length;
        board = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder represent = new StringBuilder();
        represent.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                represent.append(String.format("%2d ", board[i][j]));
            }
            represent.append("\n");
        }
        return represent.toString();
    }

    // board dimension n
    public int dimension() {
        return N;
    }

    // number of tiles out of place
    public int hamming() {
        int ham = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int value = board[i][j];
                if (value != 0 && value != j + 1 + i * N) ham++;
            }
        }
        return ham;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int man = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int value = board[i][j];
                if (value != 0 && value != j + 1 + i * N) {
                    int row = (value - 1) / N;  // row = correct row the element should be at
                    int col = (value - 1) % N;  // col = correct column
                    man = man + Math.abs(i - row) + Math.abs(j - col);
                }
            }
        }
        return man;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int value = board[i][j];
                if (value != 0 && value != j + 1 + i * N) {
                    return false;
                }
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;
        Board that = (Board) y;
        if (that.dimension() != this.dimension()) return false;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.board[i][j] != that.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int row0 = 0;
        int col0 = 0;
        boolean found0 = false;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == 0) {
                    row0 = i;
                    col0 = j;
                    found0 = true;
                    break;
                }
            }
            if (found0) break;
        }
        Stack<Board> st = new Stack<Board>();
        Board n1 = new Board(board);
        boolean hasn1 = exchange(n1, row0, col0, row0 - 1, col0);
        if (hasn1) st.push(n1);
        Board n2 = new Board(board);
        boolean hasn2 = exchange(n2, row0, col0, row0 + 1, col0);
        if (hasn2) st.push(n2);
        Board n3 = new Board(board);
        boolean hasn3 = exchange(n3, row0, col0, row0, col0 + 1);
        if (hasn3) st.push(n3);
        Board n4 = new Board(board);
        boolean hasn4 = exchange(n4, row0, col0, row0, col0 - 1);
        if (hasn4) st.push(n4);
        return st;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = new Board(board);
        if (twin.board[0][0] != 0 && twin.board[0][1] != 0) {
            exchange(twin, 0, 0, 0, 1);
        }
        else {
            exchange(twin, 1, 0, 1, 1);
        }
        return twin;
    }

    private boolean exchange(Board b, int i, int j, int v, int w) {
        if (i < 0 || j < 0 || v < 0 || w < 0) return false;
        if (i >= N || j >= N || v >= N || w >= N) return false;
        int temp = b.board[i][j];
        b.board[i][j] = b.board[v][w];
        b.board[v][w] = temp;
        return true;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        StdOut.print(initial.toString());
        StdOut.println(initial.dimension());
        StdOut.println(initial.hamming());
        StdOut.println(initial.manhattan());
        StdOut.println(initial.isGoal());
        int[][] test = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                test[i][j] = j + 1 + i * 10;
            }
        }
        test[9][9] = 8;
        Board second = new Board(test);
        StdOut.println(initial.equals(second));
        Board third = initial.twin();
        StdOut.print(third.toString());
        StdOut.println(initial.neighbors().toString());
    }

}
