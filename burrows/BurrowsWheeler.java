/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray c = new CircularSuffixArray(s);
        int index = 0;
        for (int i = 0; i < c.length(); i++) {
            if (c.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < c.length(); i++) {
            if (c.index(i) - 1 >= 0) BinaryStdOut.write(s.charAt(c.index(i) - 1));
            else BinaryStdOut.write(s.charAt(c.length() - 1));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        char[] last = t.toCharArray();
        int[] next = new int[t.length()];
        int[] count = new int[R + 1];
        for (int i = 0; i < t.length(); i++) count[last[i] + 1]++;
        for (int i = 0; i < R; i++) count[i + 1] = count[i + 1] + count[i];
        for (int i = 0; i < t.length(); i++) next[count[last[i]]++] = i;
        int nextIndex = next[first];
        for (int i = 0; i < t.length(); i++) {
            BinaryStdOut.write(last[nextIndex]);
            nextIndex = next[nextIndex];
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        if (args[0].equals("+")) inverseTransform();
    }
}
