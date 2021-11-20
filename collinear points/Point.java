/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        if (this.y == that.y && this.x != that.x) {
            return +0.0;                         // horizontal
        }
        if (this.x == that.x && this.y != that.y) {
            return Double.POSITIVE_INFINITY;      // vertical
        }
        if (this.x == that.x && this.y == that.y) {
            return Double.NEGATIVE_INFINITY;      // same point
        }
        return (double) (that.y - this.y) / (that.x - this.x);
    }


    public int compareTo(Point that) {
        if (this.y > that.y || (this.y == that.y && this.x > that.x)) {
            return 1;
        }
        if (this.y < that.y || (this.y == that.y && this.x < that.x)) {
            return -1;
        }
        return 0;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        /* YOUR CODE HERE */
        return new BySlope();
    }

    private class BySlope implements Comparator<Point> {
        public int compare(Point v, Point w) {
            double slp1 = slopeTo(v);
            double slp2 = slopeTo(w);
            return Double.compare(slp1, slp2);
        }
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        Point p1 = new Point(1, 2);
        Point p2 = new Point(2, 4);
        Point p3 = new Point(2, 100);
        StdOut.println(p1.compareTo(p2));
        int i = p1.slopeOrder().compare(p2, p3);
        StdOut.println(i);
    }
}
