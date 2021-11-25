/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private class Node implements Comparable<Node> {
        private Board board;
        private int moves;
        private Node previous;

        Node(Board board, int moves, Node previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }

        public int priority() {
            int nodePriority = moves + board.manhattan();
            return nodePriority;
        }

        public int compareTo(Node that) {
            if (this.priority() > that.priority()) return +1;
            if (this.priority() < that.priority()) return -1;
            return 0;
        }
    }

    private boolean solvable = false;
    private final Stack<Board> st = new Stack<Board>();   // for iterable


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        if (initial.isGoal()) {
            solvable = true;
            st.push(initial);
            return;
        }
        if (initial.twin().isGoal()) {
            solvable = false;
            return;
        }
        MinPQ<Node> pq = new MinPQ<Node>();
        MinPQ<Node> twinpq = new MinPQ<Node>(); // to check if solvable
        Node node = new Node(initial, 0, null);
        Node nodetwin = new Node(initial.twin(), 0, null);
        pq.insert(node);
        twinpq.insert(nodetwin);
        while (true) {
            node = pq.delMin();
            nodetwin = twinpq.delMin();
            Iterable<Board> neighbors = node.board.neighbors();
            for (Board b : neighbors) {
                if (b.isGoal()) {
                    st.push(b);
                    st.push(node.board);
                    solvable = true;
                    while (node.previous != null) {
                        st.push(node.previous.board);
                        node = node.previous;
                    }
                    return;
                }
                if (node.previous != null && b.equals(node.previous.board)) {
                    continue;
                }
                Node nextnode = new Node(b, node.moves + 1, node);
                pq.insert(nextnode);
            }
            Iterable<Board> neighborstwin = nodetwin.board.neighbors();
            for (Board b : neighborstwin) {
                if (b.isGoal()) {
                    solvable = false;
                    return;
                }
                if (nodetwin.previous != null && b.equals(nodetwin.previous.board)) {
                    continue;
                }
                Node nextnode = new Node(b, nodetwin.moves + 1, nodetwin);
                twinpq.insert(nextnode);
            }
        }

    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!solvable) return -1;
        return st.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable) return null;
        return st;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
