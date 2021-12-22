/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    private final int len;
    private final String csa;
    private final int[] originalIndex;

    private class CircularSuffix implements Comparable<CircularSuffix> {
        private final int index;

        public CircularSuffix(int i) {
            this.index = i;
        }

        public int compareTo(CircularSuffix cs) {
            for (int i = 0; i < len; i++) {
                if (csa.charAt(this.index + i) > csa.charAt(cs.index + i)) return 1;
                if (csa.charAt(this.index + i) < csa.charAt(cs.index + i)) return -1;
            }
            return 0;
        }
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        len = s.length();
        csa = s + s;
        CircularSuffix[] array = new CircularSuffix[len];
        for (int i = 0; i < len; i++) array[i] = new CircularSuffix(i);
        Arrays.sort(array);
        originalIndex = new int[len];
        for (int i = 0; i < len; i++) originalIndex[i] = array[i].index;
    }

    // length of s
    public int length() {
        return len;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= len) throw new IllegalArgumentException();
        return originalIndex[i];
    }

    public static void main(String[] args) {
        CircularSuffixArray c = new CircularSuffixArray("ABRACADABRA!");
        StdOut.println(c.length());
        for (int i = 0; i < c.length(); i++) StdOut.println(c.index(i));
    }
}
