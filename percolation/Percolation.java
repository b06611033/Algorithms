/* *****************************************************************************
 *  Name:              Bryan Hou
 *  Coursera User ID:  Algorithms Part I
 *  Last modified:     11/14/2021
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int[][] id;
    private WeightedQuickUnionUF uf;
    private int size;    // grid size

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        id = new int[n][n];
        size = n;
        uf = new WeightedQuickUnionUF(n * n + 2); // plus two for the top and bottom virtual site
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                id[i][j] = 0;
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row <= 0 || row > size || col <= 0 || col > size) throw new IllegalArgumentException();
        if (id[row - 1][col - 1] == 0) {
            id[row - 1][col - 1] = 1;
            if (row == 1) {
                uf.union(col, 0);    // if any sites in first row is open, connect to top
            }
            if (row == size) {
                uf.union((size - 1) * size + col,
                         size * size + 1);  // last row open, connect to bottom
            }
            if (col > 1 && isOpen(row, col - 1)) {
                uf.union((row - 1) * size + col, (row - 1) * size + col - 1); // union with left
            }
            if (col < size && isOpen(row, col + 1)) {
                uf.union((row - 1) * size + col, (row - 1) * size + col + 1); // union with right
            }
            if (row > 1 && isOpen(row - 1, col)) {
                uf.union((row - 1) * size + col, (row - 2) * size + col); // union with up
            }
            if (row < size && isOpen(row + 1, col)) {
                uf.union((row - 1) * size + col, (row) * size + col); // union with down
            }

        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || row > size || col <= 0 || col > size) throw new IllegalArgumentException();
        boolean open = false;
        if (id[row - 1][col - 1] == 1) {
            open = true;
        }
        return open;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row <= 0 || row > size || col <= 0 || col > size) throw new IllegalArgumentException();
        if (uf.find((row - 1) * size + col) == uf.find(0)) {
            return true;
        }

        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (isOpen(i + 1, j + 1)) {
                    count++;
                }
            }
        }
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        if (uf.find(size * size + 1) == uf.find(0)) {
            return true;
        }
        /* for (int i = 0; i < size; i++) {
            if (isFull(size, i + 1)) {
                return true;                  //method to prevent backwash, but will exceed time limit
            }
        } */

        return false;

    }

    // test client (optional)
    public static void main(String[] args) {

    }


}
