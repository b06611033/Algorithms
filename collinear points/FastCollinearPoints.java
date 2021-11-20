/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> lineSegments = new ArrayList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        if (hasnull(points)) throw new IllegalArgumentException();
        if (hasduplicate(points)) throw new IllegalArgumentException();
        int length = points.length;
        for (int i = 0; i < length - 3; i++) {
            Point first = points[i];
            Point[] temparray = new Point[length - i - 1];
            for (int j = i + 1; j < length; j++) {
                temparray[j - i - 1] = points[j];   // store points after first point into an array
            }
            Arrays.sort(temparray, 0, temparray.length,
                        first.slopeOrder()); // sort the array by slope to first point
            int numOfSame = 1;  // to track num of points with same slope
            for (int w = 0; w < temparray.length; w++) {
                if (w == temparray.length - 1 || temparray[w].slopeTo(first) != temparray[w + 1]
                        .slopeTo(first)) {
                    if (numOfSame >= 3) {
                        Point[] temparray2 = new Point[numOfSame
                                + 1];  // extra space for first point
                        for (int v = 0; v < numOfSame; v++) {
                            temparray2[v] = temparray[w + 1 - numOfSame + v];
                        }
                        temparray2[numOfSame] = first;   // put first point in
                        lineSegments.add(segmentPoints(temparray2));
                    }
                    numOfSame = 1;
                }
                else {
                    numOfSame++;
                }

            }
        /* for (int i = 0; i < length; i++) {
            Point first = points[i];
            Point[] temp = points;
            Arrays.sort(temp, 0, points.length,
                        first.slopeOrder()); // sort the array by slope to first point
            int numOfSame = 1;  // to track num of points with same slope
            for (int w = 0; w < points.length; w++) {
                if (w == points.length - 1 || temp[w].slopeTo(first) != temp[w + 1]
                        .slopeTo(first)) {
                    if (numOfSame >= 3) {
                        Point[] temparray2 = new Point[numOfSame
                                + 1];  // extra space for first point
                        for (int v = 0; v < numOfSame; v++) {
                            temparray2[v] = temp[w + 1 - numOfSame + v];
                        }
                        temparray2[numOfSame] = first;   // put first point in
                        if (newsegment(segmentPoints(temparray2)))
                            lineSegments.add(segmentPoints(temparray2));
                    }
                    numOfSame = 1;
                }
                else {
                    numOfSame++;
                }
            }*/
        }
    }

    private LineSegment segmentPoints(Point[] collinearpoints) {
        Point high = collinearpoints[0];
        Point low = collinearpoints[0];
        for (int i = 0; i < collinearpoints.length; i++) {
            if (high.compareTo(collinearpoints[i]) == -1) high = collinearpoints[i];
            if (low.compareTo(collinearpoints[i]) == 1) low = collinearpoints[i];
        }
        LineSegment ls = new LineSegment(high, low);
        return ls;
    }

    /* private boolean newsegment(LineSegment ls) {
        for (int i = 0; i < lineSegments.size(); i++) {
            if (ls.toString().equals(lineSegments.get(i).toString())) {
                return false;
            }
        }
        return true;
    }*/

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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
