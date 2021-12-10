/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {

    private Picture p;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        p = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        Picture temp = p;
        p = new Picture(p);    // keep a defensive copy
        return temp;
    }

    // width of current picture
    public int width() {
        return p.width();
    }

    // height of current picture
    public int height() {
        return p.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= p.width() || y < 0 || y >= p.height())
            throw new IllegalArgumentException();
        if (x == 0 || x == p.width() - 1 || y == 0 || y == p.height() - 1)
            return 1000;
        return Math.sqrt(squarex(x, y) + squarey(x, y));
    }

    private double squarex(int x, int y) {
        Color left = p.get(x - 1, y);
        Color right = p.get(x + 1, y);
        return Math.pow(left.getRed() - right.getRed(), 2) + Math
                .pow(left.getBlue() - right.getBlue(), 2) + Math
                .pow(left.getGreen() - right.getGreen(), 2);
    }

    private double squarey(int x, int y) {
        Color up = p.get(x, y - 1);
        Color down = p.get(x, y + 1);
        return Math.pow(up.getRed() - down.getRed(), 2) + Math
                .pow(up.getBlue() - down.getBlue(), 2) + Math
                .pow(up.getGreen() - down.getGreen(), 2);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energy = new double[p.height()][p.width()];
        int[][] edgeTo = new int[p.height()][p.width()];
        double[][] disTo = new double[p.height()][p.width()];
        for (int i = 0; i < p.height(); i++) {
            for (int j = 0; j < p.width(); j++) { // initialize
                if (i == 0) {
                    disTo[i][j] = 1000;
                    energy[i][j] = energy(j, i);
                }
                else {
                    disTo[i][j] = Double.POSITIVE_INFINITY;
                    energy[i][j] = energy(j, i);
                }
            }
        }

        for (int i = 0; i < p.height(); i++) {
            for (int j = 0; j < p.width(); j++) {  // initialize
                if (i + 1 < p.height()) {             // relax edge down
                    if (disTo[i + 1][j] > disTo[i][j] + energy[i + 1][j]) {
                        disTo[i + 1][j] = disTo[i][j] + energy[i + 1][j];
                        edgeTo[i + 1][j] = j;
                    }
                }
                if (i + 1 < p.height() && j + 1 < p.width()) {     // relax edge right down
                    if (disTo[i + 1][j + 1] > disTo[i][j] + energy[i + 1][j + 1]) {
                        disTo[i + 1][j + 1] = disTo[i][j] + energy[i + 1][j + 1];
                        edgeTo[i + 1][j + 1] = j;
                    }
                }
                if (i + 1 < p.height() && j - 1 >= 0) {        // relax edge left down
                    if (disTo[i + 1][j - 1] > disTo[i][j] + energy[i + 1][j - 1]) {
                        disTo[i + 1][j - 1] = disTo[i][j] + energy[i + 1][j - 1];
                        edgeTo[i + 1][j - 1] = j;
                    }
                }
            }
        }
        double smallestd = Double.POSITIVE_INFINITY;
        int lastrowPixel = 0;
        for (int i = 0; i < p.width(); i++) {
            if (smallestd > disTo[p.height() - 1][i]) {
                smallestd = disTo[p.height() - 1][i];
                lastrowPixel = i;
            }
        }
        int[] seam = new int[p.height()];
        int temp = lastrowPixel;
        seam[p.height() - 1] = lastrowPixel;
        for (int i = p.height() - 2; i >= 0; i--) {
            seam[i] = edgeTo[i + 1][temp];
            temp = seam[i];
        }
        return seam;
    }

    private void transpose() {
        Picture temp = new Picture(p.height(), p.width());
        for (int i = 0; i < p.height(); i++) {
            for (int j = 0; j < p.width(); j++) {
                temp.set(i, j, p.get(j, i));
            }
        }
        p = temp;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        for (int i : seam) {
            if (i < 0 || i >= p.width()) throw new IllegalArgumentException();
        }
        if (seam.length != p.height()) throw new IllegalArgumentException();
        if (p.width() <= 1) throw new IllegalArgumentException();
        int last = seam[0];
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i] - last) > 1) throw new IllegalArgumentException();
            last = seam[i];
        }
        Picture newPic = new Picture(p.width() - 1, p.height());
        for (int i = 0; i < newPic.height(); i++) {
            for (int j = 0; j < newPic.width(); j++) {
                int remove = seam[i];
                if (j < remove) newPic.set(j, i, p.get(j, i));
                if (j >= remove) newPic.set(j, i, p.get(j + 1, i));
            }
        }
        p = newPic;
    }

    //  unit testing (optional)
    public static void main(String[] args) {
    }
}
