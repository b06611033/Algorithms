/* *****************************************************************************
 *  Name:              Bryan Hou
 *  Coursera User ID:  Algorithms Part I
 *  Last modified:     11/14/2021
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] probability;   // save the fraction of open sites of every trial

    private int t;
    private double confidence = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("N <= 0 or T <= 0");
        }
        probability = new double[trials];
        t = trials;
        for (int i = 0; i < trials; i++) {
            int num = 0;                    // to track the number of open site each trial
            Percolation per = new Percolation(n);
            while (!per.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                if (!per.isOpen(row, col)) {
                    per.open(row, col);
                    num++;
                }
            }
            probability[i] = (double) num / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(probability);
    }


    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(probability);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return (mean() - confidence * stddev() / Math.sqrt(t));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (mean() + confidence * stddev() / Math.sqrt(t));
    }

    // test client (see below)
    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);
        int tries = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(size, tries);

        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println(
                "95% confidence interval = " + "[" + ps.confidenceLo() + ", " + ps.confidenceHi()
                        + "]");

    }
}

