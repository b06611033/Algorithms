/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {

    private final int num;
    private final Queue<String> teams;
    private final int[] win;
    private final int[] loss;
    private final int[] remain;
    private final int[][] matchup;
    private int s; // source vertex
    private int t;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In file = new In(filename);
        num = file.readInt();
        teams = new Queue<>();
        win = new int[num];
        loss = new int[num];
        remain = new int[num];
        matchup = new int[num][num];
        s = 0;   // source vertex
        t = 2 + (num - 1) + (num - 1) * (num - 2) / 2 - 1; // goal vertex
        for (int i = 0; i < num; i++) {
            teams.enqueue(file.readString());
            win[i] = file.readInt();
            loss[i] = file.readInt();
            remain[i] = file.readInt();
            for (int j = 0; j < num; j++) {
                matchup[i][j] = file.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return num;
    }

    // all teams
    public Iterable<String> teams() {
        return teams;
    }

    private boolean hasteam(String team) {
        boolean b = false;
        for (String s : teams) {
            if (s.equals(team)) b = true;
        }
        return b;
    }

    // number of wins for given team
    public int wins(String team) {
        if (!hasteam(team)) throw new IllegalArgumentException();
        int count = 0;
        for (String w : teams) {
            if (w.equals(team)) break;
            count++;
        }
        return win[count];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!hasteam(team)) throw new IllegalArgumentException();
        int count = 0;
        for (String lose : teams) {
            if (lose.equals(team)) break;
            count++;
        }
        return loss[count];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!hasteam(team)) throw new IllegalArgumentException();
        int count = 0;
        for (String r : teams) {
            if (r.equals(team)) break;
            count++;
        }
        return remain[count];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!hasteam(team1) || !hasteam(team2)) throw new IllegalArgumentException();
        int count1 = 0;
        int count2 = 0;
        for (String a : teams) {
            if (a.equals(team1)) break;
            count1++;
        }
        for (String b : teams) {
            if (b.equals(team2)) break;
            count2++;
        }
        return matchup[count1][count2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!hasteam(team)) throw new IllegalArgumentException();
        for (int w : win) {
            if (wins(team) + remaining(team) < w) return true; // trivial eliminate
        }
        int teamToEliminate = 0;
        for (String e : teams) {
            if (e.equals(team)) break;
            teamToEliminate++;
        }
        FlowNetwork fn = buildMaxFlow(teamToEliminate);
        String g = fn.toString();
        // StdOut.println(g);
        FordFulkerson ff = new FordFulkerson(fn, s, t);
        int rightVStart = (num - 1) * (num - 2) / 2 + 1;
        for (int i = rightVStart; i < rightVStart + num - 1; i++) {
            if (ff.inCut(i)) return true;
        }
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!hasteam(team)) throw new IllegalArgumentException();
        if (!isEliminated(team)) return null;
        Queue<String> teamR1 = new Queue<>();
        int teamNum = 0;
        boolean[] teamNumArray = new boolean[num];
        for (int w : win) {
            if (wins(team) + remaining(team) < w)
                teamNumArray[teamNum] = true;
            else teamNumArray[teamNum] = false;
            teamNum++;
        }
        int track = 0;
        for (String s : teams) {
            if (teamNumArray[track]) teamR1.enqueue(s);
            track++;
        }
        if (!teamR1.isEmpty()) return teamR1;  // trivial eliminate

        int teamToEliminate = 0;
        for (String e : teams) {
            if (e.equals(team)) break;
            teamToEliminate++;
        }
        String[] copy = new String[num - 1]; // array without the eliminated team
        int co = 0;
        for (String e : teams) {
            if (!e.equals(team)) {
                copy[co] = e;
                co++;
            }
        }
        Queue<Integer> r = new Queue<>();
        Queue<String> teamR2 = new Queue<>();
        FlowNetwork fn = buildMaxFlow(teamToEliminate);
        FordFulkerson ff = new FordFulkerson(fn, s, t);
        int rightVStart = (num - 1) * (num - 2) / 2 + 1;
        for (int i = rightVStart; i < rightVStart + num - 1; i++) {
            if (ff.inCut(i)) r.enqueue(i - rightVStart);
        }
        for (int n : r) {
            for (int i = 0; i < copy.length; i++) {
                if (n == i) teamR2.enqueue(copy[i]);
            }
        }
        return teamR2;
    }

    private FlowNetwork buildMaxFlow(int eliminate) {
        int numofV = 2 + (num - 1) + (num - 1) * (num - 2) / 2;
        FlowNetwork fn = new FlowNetwork(numofV);
        int vertexNo = 1; // since s is 0, start from 1
        for (int i = 0; i < num; i++) {
            if (i == eliminate) continue;
            for (int j = i; j < num; j++) {
                if (j != i && j != eliminate) {
                    FlowEdge e = new FlowEdge(s, vertexNo, matchup[i][j]);
                    fn.addEdge(e);
                    vertexNo++;
                }
            }
        }
        for (int i = 0; i < num; i++) {
            if (i != eliminate) {
                FlowEdge e = new FlowEdge(vertexNo, t, win[eliminate] + remain[eliminate] - win[i]);
                fn.addEdge(e);
                vertexNo++;
            }
        }
        int leftVNum = (num - 1) * (num - 2) / 2;   // total number of left vertices
        int rightVStart = (num - 1) * (num - 2) / 2 + 1;  // No. of first vertex on the right
        int vGrouped = num - 2;  // process left vertices by group
        int currentGroupV = num - 2; // vertices in current group
        int groupNum = 0; // No. of group
        int numInGroup = 1; // vertex number in current group, ex: first v is 1, sec is 2
        for (int i = 1; i <= leftVNum; i++) {
            if (i <= vGrouped) {
                FlowEdge e1 = new FlowEdge(i, rightVStart + groupNum, Double.POSITIVE_INFINITY);
                fn.addEdge(e1);
                FlowEdge e2 = new FlowEdge(i, rightVStart + groupNum + numInGroup,
                                           Double.POSITIVE_INFINITY);
                fn.addEdge(e2);
                numInGroup++;
            }
            if (i > vGrouped) {
                groupNum++;
                currentGroupV--;
                vGrouped = vGrouped + currentGroupV;
                numInGroup = 1;
                i--;
            }
        }
        return fn;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        StdOut.println(division.numberOfTeams());
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
