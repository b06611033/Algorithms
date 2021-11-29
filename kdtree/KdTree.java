/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private static class Node {
        private Point2D point;      // the point
        private RectHV rect;    // the axis-aligned rectangle split by the point in this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private boolean vertical;
    }

    private int size;
    private Node root;
    private double rectxmin; // the minx coordinate of the rect of the new input
    private double rectymin; // the miny coordinate of the rect of the new input
    private double rectxmax; // the maxx coordinate of the rect of the new input
    private double rectymax; // the maxy coordinate of the rect of the new input


    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }


    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = put(root, p, true);
        size++;
        if (size == 1) {
            root.rect = new RectHV(0, 0, 1, 1);    // insert first node
        }
    }

    private Node put(Node n, Point2D p, boolean vertical) {
        if (n == null) {
            Node newpoint = new Node();
            newpoint.point = p;
            newpoint.lb = null;
            newpoint.rt = null;
            newpoint.rect = new RectHV(rectxmin, rectymin, rectxmax, rectymax);
            newpoint.vertical = vertical;
            return newpoint;
        }
        if (vertical) {
            if (p.x() < n.point.x()) {
                rectxmin = n.rect.xmin();
                rectymin = n.rect.ymin();
                rectxmax = n.point.x();
                rectymax = n.rect.ymax();
                n.lb = put(n.lb, p, false);
            }
            else if (p.x() > n.point.x() || (p.x() == n.point.x() && p.y() != n.point.y())) {
                rectxmin = n.point.x();
                rectymin = n.rect.ymin();
                rectxmax = n.rect.xmax();
                rectymax = n.rect.ymax();
                n.rt = put(n.rt, p, false);
            }
            else if (p.y() == n.point.y()) {  // point already exists
                n.point = p;
                size--;          // contemplate the size++ in insert
            }
            return n;
        }
        else {       // !vertical
            if (p.y() < n.point.y()) {
                rectxmin = n.rect.xmin();
                rectymin = n.rect.ymin();
                rectxmax = n.rect.xmax();
                rectymax = n.point.y();
                n.lb = put(n.lb, p, true);
            }
            else if (p.y() > n.point.y() || (p.y() == n.point.y() && p.x() != n.point.x())) {
                rectxmin = n.rect.xmin();
                rectymin = n.point.y();
                rectxmax = n.rect.xmax();
                rectymax = n.rect.ymax();
                n.rt = put(n.rt, p, true);
            }
            else if (p.x() == n.point.x()) {
                n.point = p;
                size--;
            }
            return n;
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node n = root;
        int track = 0;
        while (n != null) {
            if (track % 2 == 0) {
                if (p.x() < n.point.x()) n = n.lb;
                else if (p.x() > n.point.x() || (p.x() == n.point.x() && p.y() != n.point.y()))
                    n = n.rt;
                else if (p.y() == n.point.y()) return true;
                track++;
            }
            else {
                if (p.y() < n.point.y()) n = n.lb;
                else if (p.y() > n.point.y() || (p.y() == n.point.y() && p.x() != n.point.x()))
                    n = n.rt;
                else if (p.x() == n.point.x()) return true;
                track++;
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node n) {
        if (n == null) {
            throw new IllegalArgumentException();
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.point.draw();
        if (n.vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(n.point.x(), n.rect.ymin(), n.point.x(), n.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(n.rect.xmin(), n.point.y(), n.rect.xmax(), n.point.y());
        }
        if (n.lb != null) {
            draw(n.lb);
        }
        if (n.rt != null) {
            draw(n.rt);
        }
    }
    

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> within = new Stack<Point2D>();
        rangeSearch(root, rect, within);
        return within;
    }

    private void rangeSearch(Node n, RectHV rect, Stack<Point2D> st) {
        if (n == null) return;
        if (rect.contains(n.point)) {
            st.push(n.point);
            rangeSearch(n.lb, rect, st);
            rangeSearch(n.rt, rect, st);
        }
        else {
            if (n.vertical && n.point.x() > rect.xmin() && n.point.x() < rect.xmax()) {
                rangeSearch(n.lb, rect, st);
                rangeSearch(n.rt, rect, st);
            }
            if (!n.vertical && n.point.y() > rect.ymin() && n.point.y() < rect.ymax()) {
                rangeSearch(n.lb, rect, st);
                rangeSearch(n.rt, rect, st);
            }
            if (n.vertical && n.point.x() <= rect.xmin()) {
                rangeSearch(n.rt, rect, st);
            }
            if (n.vertical && n.point.x() >= rect.xmax()) {
                rangeSearch(n.lb, rect, st);
            }
            if (!n.vertical && n.point.y() <= rect.ymin()) {
                rangeSearch(n.rt, rect, st);
            }
            if (!n.vertical && n.point.y() >= rect.ymax()) {
                rangeSearch(n.lb, rect, st);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Point2D champion = null;
        double nearestD = Double.POSITIVE_INFINITY;
        Stack<Node> node = new Stack<Node>();
        node.push(root);
        while (!node.isEmpty()) {
            Node n = node.pop();
            if (n == null) {
                break;
            }
            if (n.point.distanceSquaredTo(p) < nearestD) {
                champion = n.point;
                nearestD = n.point.distanceSquaredTo(p);
            }
            if (n.lb != null) {
                node.push(n.lb);
            }
            if (n.rt != null) {
                node.push(n.rt);
            }
        }

        return champion;
    }


    public static void main(String[] args) {
        KdTree ps = new KdTree();
        // read the n points from a file
        In in = new In(args[0]);
        while (!in.isEmpty()) {
            Point2D point = new Point2D(in.readDouble(), in.readDouble());
            ps.insert(point);
        }
        ps.draw();


    }
}
