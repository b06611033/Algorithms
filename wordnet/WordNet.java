/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordNet {
    private final Digraph dg;
    private final Map<Integer, String> synset;
    private final Map<String, Set<Integer>> nounId;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        synset = new HashMap<>();
        nounId = new HashMap<>();
        In file = new In(synsets);
        while (file.hasNextLine()) {
            String[] line = file.readLine().split(",");
            Integer id = Integer.valueOf(line[0]);
            String n = line[1];
            synset.put(id, n);
            String[] noun = n.split(" ");
            for (String s : noun) {
                Set<Integer> ids = nounId.get(s);
                if (ids == null) {
                    ids = new HashSet<>();
                }
                ids.add(id);
                nounId.put(s, ids);
            }
        }
        dg = new Digraph(synset.size());
        graphConstruct(hypernyms);
        DirectedCycle dc = new DirectedCycle(dg);
        if (dc.hasCycle()) throw new IllegalArgumentException();
        int root = 0;
        for (int i = 0; i < dg.V(); i++) {
            if (dg.outdegree(i) == 0) root++;
            if (root > 1) throw new IllegalArgumentException();
        }
    }

    private void graphConstruct(String filename) {
        In file = new In(filename);
        while (file.hasNextLine()) {
            String[] line = file.readLine().split(",");
            Integer id = Integer.valueOf(line[0]);
            for (int i = 1; i < line.length; i++) {
                Integer hypernym = Integer.valueOf(line[i]);
                dg.addEdge(id, hypernym);
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounId.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        Set<Integer> ids = nounId.get(word);
        if (ids == null) return false;
        return true;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        SAP sap = new SAP(dg);
        return sap.length(nounId.get(nounA), nounId.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        SAP sap = new SAP(dg);
        int ancestor = sap.ancestor(nounId.get(nounA), nounId.get(nounB));
        return synset.get(ancestor);
    }

    public static void main(String[] args) {
       /* WordNet wordNet = new WordNet(args[0], args[1]);
        while (!StdIn.isEmpty()) {
            String v = StdIn.readString();
            String w = StdIn.readString();
            int distance = wordNet.distance(v, w);   // test distance and ancestor
            String ancestor = wordNet.sap(v, w);
            StdOut.printf("distance = %d, ancestor = %s\n", distance, ancestor);
        }
        while (!StdIn.isEmpty()) {
            String v = StdIn.readString();
            boolean isnoun = wordNet.isNoun(v);
            StdOut.println(isnoun);   // test isNoun
        }
        int count = 0;
        for (String s : wordNet.nouns()) {
            count++;               // test nouns
        }
        StdOut.println(count); */
    }
}


