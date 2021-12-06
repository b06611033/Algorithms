/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph dg;
    private int ancestor;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
        dg = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= dg.V() || w < 0 || w >= dg.V()) {
            throw new IllegalArgumentException();
        }
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(dg, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(dg, w);
        int sp = Integer.MAX_VALUE; // shortest path
        int count = 0;
        for (int i = 0; i < dg.V(); i++) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                int d = bfsv.distTo(i) + bfsw.distTo(i);
                if (d < sp) {
                    sp = d;
                    ancestor = i;
                }
                count++;
            }
        }
        if (count == 0) {
            ancestor = -1;
            return -1;
        }
        return sp;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= dg.V() || w < 0 || w >= dg.V()) {
            throw new IllegalArgumentException();
        }
        length(v, w);
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        for (Integer s : v) {
            if (s == null) throw new IllegalArgumentException();
            if (s < 0 || s >= dg.V()) throw new IllegalArgumentException();
        }
        for (Integer s : w) {
            if (s == null) throw new IllegalArgumentException();
            if (s < 0 || s >= dg.V()) throw new IllegalArgumentException();
        }
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(dg, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(dg, w);
        int sp = Integer.MAX_VALUE; // shortest path
        int count = 0;
        for (int i = 0; i < dg.V(); i++) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                int d = bfsv.distTo(i) + bfsw.distTo(i);
                if (d < sp) {
                    sp = d;
                    ancestor = i;
                }
                count++;
            }
        }
        if (count == 0) {
            ancestor = -1;
            return -1;
        }
        return sp;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        for (Integer i : v) {
            if (i == null) throw new IllegalArgumentException();
        }
        for (Integer i : w) {
            if (i == null) throw new IllegalArgumentException();
        }
        length(v, w);
        return ancestor;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
       /* Stack<Integer> st1 = new Stack<>();
        Stack<Integer> st2 = new Stack<>();
        st1.push(9);
        st2.push(3);
        st2.push(8);

        int length = sap.length(st1, st2);
        int ancestor = sap.ancestor(st1, st2);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);*/
    }
}
