/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> lineSegments = new ArrayList<>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        if (hasnull(points)) throw new IllegalArgumentException();
        if (hasduplicate(points)) throw new IllegalArgumentException();
        int length = points.length;
        for (int i = 0; i < length - 3; i++) {
            Point first = points[i];
            for (int j = i + 1; j < length - 2; j++) {
                Point second = points[j];
                double firSec = first.slopeTo(second);
                for (int v = j + 1; v < length - 1; v++) {
                    Point third = points[v];
                    double firThir = first.slopeTo(third);
                    if (firSec == firThir) {    // only examine forth if first three is collinear
                        for (int w = v + 1; w < length; w++) {
                            Point fourth = points[w];
                            double firFor = first.slopeTo(fourth);
                            if (firSec == firFor) {
                                lineSegments.add(segmentPoints(first, second, third, fourth));
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean hasduplicate(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) return true;
            }
        }
        return false;
    }

    private boolean hasnull(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) return true;
        }
        return false;
    }

    private LineSegment segmentPoints(Point point1, Point point2, Point point3, Point point4) {
        Point high = point1;
        Point low = point1;
        if (high.compareTo(point2) == -1) high = point2;
        if (low.compareTo(point2) == 1) low = point2;
        if (high.compareTo(point3) == -1) high = point3;
        if (low.compareTo(point3) == 1) low = point3;
        if (high.compareTo(point4) == -1) high = point4;
        if (low.compareTo(point4) == 1) low = point4;
        LineSegment ls = new LineSegment(high, low);
        return ls;
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
