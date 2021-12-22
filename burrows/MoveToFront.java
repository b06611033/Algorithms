/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        int[] charValue = new int[R];
        for (int i = 0; i < R; i++) {
            charValue[i] = i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            BinaryStdOut.write((char) charValue[c]);
            for (int i = 0; i < R; i++) {
                if (charValue[i] < charValue[c]) charValue[i]++;
            }
            charValue[c] = 0;
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        int[] charValue = new int[R];
        for (int i = 0; i < R; i++) {
            charValue[i] = i;
        }
        while (!BinaryStdIn.isEmpty()) {
            int encode = BinaryStdIn.readChar();
            int arrayIndex = 0;
            for (int i = 0; i < R; i++) {
                if (charValue[i] == encode) {
                    arrayIndex = i;  // use index to find the character (index never changes)
                    BinaryStdOut.write((char) i);
                    break;
                }
            }
            for (int i = 0; i < R; i++) {
                if (charValue[i] < charValue[arrayIndex]) charValue[i]++;
            }
            charValue[arrayIndex] = 0;
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }
}
