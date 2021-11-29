/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        set = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        double xmin = rect.xmin();
        double xmax = rect.xmax();
        double ymin = rect.ymin();
        double ymax = rect.ymax();
        Stack<Point2D> st = new Stack<Point2D>();
        for (Point2D p : set) {
            if (p.x() >= xmin && p.x() <= xmax && p.y() >= ymin && p.y() <= ymax) {
                st.push(p);
            }
        }
        return st;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (set.isEmpty()) return null;
        Point2D champion = set.first();
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (Point2D thisp : set) {
            double distance = thisp.distanceSquaredTo(p);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                champion = thisp;
            }
        }
        return champion;
    }

    public static void main(String[] args) {
        PointSET ps = new PointSET();
        StdOut.println(ps.isEmpty());
        Point2D p1 = new Point2D(0.1, 0.2);
        Point2D p2 = new Point2D(0.2, 0.4);
        Point2D p3 = new Point2D(0.2, 0.4);
        Point2D p4 = new Point2D(0.6, 0.6);
        Point2D p5 = new Point2D(0.2, 0.5);
        ps.insert(p1);
        ps.insert(p2);
        ps.insert(p5);
        StdOut.println(ps.isEmpty());
        StdOut.println(ps.contains(p3));
        StdOut.println(ps.contains(p4));
        StdOut.println(ps.size());
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(0.05);
        p4.draw();
        ps.draw();
        RectHV rect = new RectHV(0.0, 0.0, 0.5, 0.5);
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius(0.01);
        rect.draw();
        Iterable<Point2D> i = ps.range(rect);
        for (Point2D p : i) {
            StdOut.println(p);
        }
        StdOut.println(ps.nearest(p4));
    }
}
