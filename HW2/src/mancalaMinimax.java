
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.FileOutputStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * @author Snehal
 */
public class mancalaMinimax {

    public static int Task;
    public static int Player;
    public static int Opponent;
    public static int CutOffDepth;
    public static int Board[][];
    public static int dummyBoard[][];
    public static int BoardSize;
    public static int Mancala2;
    public static int Mancala1;
    public static int evalFunction[];
    public static int INFINITY = Integer.MAX_VALUE;
    public static mancala dummy;

    public static class Move {

        String node;
        int depth;
        int value;

        Move(String node, int depth, int value) {
            this.node = node;
            this.depth = depth;
            this.value = value;
        }

        public void printMove() {
            String printValue = "{" + this.node + "," + this.depth + "," + this.value + "}\t";
            print(printValue);
        }

    }

    public static class ABNode {

        int alpha;
        int beta;
        int board[][];
        int value;
        String oper;
        int depth;

        ABNode(int a, int b, int[][] retBoard, String op, int val, int dep) {
            this.alpha = a;
            this.beta = b;
            this.board = initDummy(retBoard);
            this.value = val;
            this.oper = op;
            this.depth = dep;
        }

        ABNode(ABNode c) {
            this.alpha = c.alpha;
            this.beta = c.beta;
            this.board = initDummy(c.board);
            this.value = c.value;
            this.oper = c.oper;
        }

        public String printABNode() {
            String printValue = utility(this.board) + "," + this.alpha + "," + this.beta;
            println(printValue);
            return printValue;
        }
    }

    public static class MNode {

        int board[][];
        int value;
        int depth;
        String oper;

        MNode(int[][] retBoard, int dep, String op, int val) {
            this.board = initDummy(retBoard);
            this.depth = dep;
            this.value = val;
            this.oper = op;
        }

        MNode(MNode m) {
            this.board = initDummy(m.board);
            this.depth = m.depth;
            this.value = m.value;
            this.oper = m.oper;
        }

        public String printMNode() {
            String printValue = "val " + utility(this.board) + ", depth " + this.depth + "," + this.oper;
            println(printValue);
            return printValue;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String inputFile = "C:\\Users\\Snehal\\Documents\\NetBeansProjects\\mancala\\src\\input.txt";
        boolean a = false;

        try {
            if (args.length > 0) {
                args[0] = "-i";
                inputFile = args[1];
                
                try {
                    File f = new File("verbose_" + inputFile.substring(inputFile.lastIndexOf("input")));
                    PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(f)), true);
                    System.setOut(ps);
                } catch (Exception e) {
                    print("Not able to redirect the o/p to verbose.txt");
                }
            }

            System.out.println("path " + inputFile);
            a = readInputFile(inputFile);
            System.out.println("Task" + Task + " Player" + Player + " Opponent" + Opponent);
            printBoard();
            if (Task == 1) {
                try {
                    List<List<Move>> playAt = new ArrayList<List<Move>>();
                    Move m = new Move("X", 0, -INFINITY);
                    List<Move> move = new ArrayList<>();
                    move.add(m);
                    playAt.add(move);
                    playAt = chooseMove(Player, 1, Board, playAt.get(0));
                    print("=>");
                    for (int i = 0; i < playAt.get(0).size(); i++) {
                        playAt.get(0).get(i).printMove();
                    }
                    println("");
                    printBoard();
                    for (int i = 0; i < playAt.get(0).size(); i++) {
                        Move play = playAt.get(0).get(i);
                        int index = Integer.parseInt(playAt.get(0).get(i).node.substring(1));
                        println("index: " + index + "");
                        Board = playMove(index - 1, Board);
                        List<String> str = printBoard();
                        writeTextFile("next_state.txt", str);
                    }
                } catch (Exception e) {
                    System.out.println("error task 1");
                }
            }
            if (Task == 2) {
                try {
                    List<String> playAt = new ArrayList<>();
                    Board = minimax_decision(Player, 0, Board);
                    println("===========\nbest board\n");
                    List<String> str = printBoard();
                    writeTextFile("next_state.txt", str);
                } catch (Exception e) {
                    System.out.println("error task 2");
                }
            }
            if (Task == 3) {
                try {
                    List<String> playAt = new ArrayList<>();
                    Board = alphaBeta_decision(Player, 0, Board);
                    println("===========\nbest board\n");
                    List<String> str = printBoard();
                    writeTextFile("next_state.txt", str);
                } catch (Exception e) {
                    System.out.println("error task 3");
                }
            }
            if (Task == 4) {
                try {
                    print("HW2 Competition Not coded");
                } catch (Exception e) {
                    System.out.println("error task 3");
                }
            }

        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            if (!a) {
                readInputFile(inputFile);
            }
        }
    }

    public static void writeTextFile(String aFileName, List<String> aLines) {
        Path path = Paths.get(aFileName);
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            {
                for (String line : aLines) {
                    writer.append(line);
                }
            }
            writer.close();
        } catch (Exception e) {
            e.toString();
        }
    }

    public static void writeTraverseFile(String aFileName, String line, boolean createFile) {
        Path path = Paths.get(aFileName);
        try {
          FileWriter writer = new FileWriter(aFileName, createFile);
            {
                // System.out.println(line);
                writer.append(line + "\n");

            }
            writer.close();
        } catch (Exception e) {
            e.toString();
        }
    }

    public static List<String> printBoard() {
        List<String> str = new ArrayList<>();
        int size = BoardSize;
        for (int i = 1; i >= 0; i--) {
            for (int j = 1; j <= size; j++) {
                str.add(Board[i][j] + " ");
            }
            str.add("\n");
        }
        str.add(Board[1][0] + "\n");
        str.add(Board[0][size + 1] + "\n");
//        for (String line : str) {
//            print(line);
//        }
        return str;
    }

    public static void printDummyBoard(int dummyBoard[][], int n) {
      /*  int size = BoardSize;
        String str = "";
        for (int i = 0; i < n; i++) {
            str = str + "\t";
        }
        for (int i = 1; i >= 0; i--) {
            System.out.print("D\t" + str);
            for (int j = 1; j <= size; j++) {
                System.out.print(dummyBoard[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println(dummyBoard[1][0]);
        System.out.println(dummyBoard[0][size + 1]);
              */
    }

    public static boolean readInputFile(String inputFile) {
        try {
            Path path = Paths.get(inputFile);
            String line = null;
            BufferedReader reader = Files.newBufferedReader(path);
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                lineCount++;

                if (lineCount == 1) {
                    Task = Integer.parseInt(line);
                } else if (lineCount == 2) {
                    Player = Integer.parseInt(line);
                    Opponent = (Player == 1) ? 2 : 1;
                } else if (lineCount == 3) {
                    CutOffDepth = Integer.parseInt(line);
                } else if (lineCount == 4) {
                    String temp[] = line.split(" ");
                    BoardSize = temp.length;
                    Board = new int[2][BoardSize + 2];
                    Board[1] = new int[BoardSize + 2];
                    for (int i = 1; i <= temp.length; i++) {
                        Board[1][i] = Integer.parseInt(temp[i - 1]);
                    }
                } else if (lineCount == 5) {
                    String temp[] = line.split(" ");
                    Board[0] = new int[BoardSize + 2];
                    for (int i = 1; i <= temp.length; i++) {
                        Board[0][i] = Integer.parseInt(temp[i - 1]);
                    }
                } else if (lineCount == 6) {
                    Mancala2 = Integer.parseInt(line);
                    Board[0][0] = Integer.parseInt(line);
                    Board[1][0] = Integer.parseInt(line);
                } else if (lineCount == 7) {
                    Mancala1 = Integer.parseInt(line);
                    Board[0][BoardSize + 1] = Integer.parseInt(line);
                    Board[1][BoardSize + 1] = Integer.parseInt(line);
                }
            }
            printBoard();
            dummyBoard = new int[2][BoardSize + 2];
            evalFunction = new int[BoardSize];
        } catch (Exception e) {
            System.out.println("Error readInputFile");
            e.printStackTrace();
        }
        return false;
    }

    public static int[][] initDummy(int Board[][]) {
        int dummyBoard[][] = new int[2][BoardSize + 2];
        for (int i = 0; i < 2; i++) {
            System.arraycopy(Board[i], 0, dummyBoard[i], 0, BoardSize + 2);
        }
        return dummyBoard;
    }

    public static List<List<Move>> chooseMove(int Player, int turn, int Board[][], List<Move> Traverse) {
        int k = 1;
        int cond = BoardSize + 1;
        String next = "";
        int pos = k + 1;
        int maxEvalFn = -INFINITY;
        int maxEvalFnPos = -1;
        Move maxNode = new Move("X", 0, -INFINITY);;
        List<List<Move>> TraverseNode = new ArrayList<List<Move>>();

        System.out.println("Initial Player " + Player + " pit " + pos + " turn" + turn);
        try {
            int dummyBoard[][] = new int[2][BoardSize + 2];
            String tempTraverse = "";
            for (; k < cond; k++) {
                List<Move> tempTraverseNode = new ArrayList<>();
                List<List<Move>> childTraverseNode = new ArrayList<List<Move>>();
                pos = k + 1;
                int childEvalValue = -INFINITY;
                dummyBoard = initDummy(Board);
                int seeds = dummyBoard[Player - 1][k];
                dummyBoard[Player - 1][k] = 0;
                //  printDummyBoard(dummyBoard, turn);
                if (seeds > 0) {
                    try {
                        System.out.println(" Before " + seeds + "{" + (Player == 1 ? "B" : "A") + pos + "," + turn + ",X}");
                        System.out.println("seeds " + seeds + " pit " + pos);
                        String thisNode = (Player == 1 ? "B" : "A") + k;
                        next = sowSeeds(seeds, Player, k, dummyBoard);
                        int index = Integer.parseInt(next.substring(1));
                        boolean captureSeeds = (Player == 2 && next.startsWith("A") && index > 0) || (Player == 1 && next.startsWith("B")
                                && index < BoardSize + 1)
                                && (Board[Player - 1][index] == 0 || (next.equals(thisNode) && dummyBoard[Player - 1][k] == 1));
                        println(captureSeeds + "Next " + next);
                        if (captureSeeds) {
                            if (dummyBoard[Player - 1][index] == 1) {
                                println("capture index " + index);
                                int capture = dummyBoard[Opponent - 1][index] + 1;
                                dummyBoard[Opponent - 1][index] = 0;
                                dummyBoard[Player - 1][index] = 0;
                                if (Player == 2) {
                                    dummyBoard[Player - 1][0] += capture;
                                } else {
                                    dummyBoard[Player - 1][BoardSize + 1] += capture;
                                }
                            }
                        }
                        int mIdx = BoardSize + 1;
                        if ((Player == 2 && next.equals("A0")) || (Player == 1 && next.equals("B" + (mIdx)))) {
                            println("new turn" + turn);
                            printDummyBoard(dummyBoard, turn);
                            turn++;
                            childTraverseNode = chooseMove(Player, turn, dummyBoard, Traverse);
                            Move maxChildMove = new Move("X", turn, -INFINITY);
                            int maxchildpos = -1;
                            for (int i = 0; i < childTraverseNode.size(); i++) {
                                List<Move> tempString = childTraverseNode.get(i);
                                if (tempString != null) {
                                    for (int j = 0; j < tempString.size(); j++) {
                                        print("Child move::" + j);
                                        tempString.get(j).printMove();
                                    }
                                    if (tempString.get(tempString.size() - 1).value > maxChildMove.value) {
                                        maxChildMove = tempString.get(tempString.size() - 1);
                                        childEvalValue = tempString.get(tempString.size() - 1).value;
                                        maxchildpos = i;
                                        print("Found max at" + childEvalValue);
                                        maxChildMove.printMove();

                                    }
                                }
                            }
                            try {
                                for (int j = 0; j < childTraverseNode.get(maxchildpos).size(); j++) {
                                    tempTraverseNode.add(j, childTraverseNode.get(maxchildpos).get(j));
                                }
                            } catch (Exception e) {
                                print(e.toString());
                            }
                            turn--;
                        }
                        //check game end
                        int countEmpty = 0;
                        int seedsPlayer = 0, seedsOpponent = 0;
                        for (int i = 1; i < BoardSize + 1; i++) {
                            if (dummyBoard[Player - 1][i] == 0) {
                                countEmpty++;
                            }
                            seedsPlayer = seedsPlayer + dummyBoard[Player - 1][i];
                            seedsOpponent = seedsOpponent + dummyBoard[Opponent - 1][i];
                        }
                        if (seedsOpponent == 0 || seedsPlayer == 0) {
                            if (Player == 1) {
                                dummyBoard[Opponent - 1][0] += seedsOpponent;
                                dummyBoard[Player - 1][BoardSize + 1] += seedsPlayer;
                                print(dummyBoard[Opponent - 1][0] + " Opponent seeds\t");
                                print(dummyBoard[Player - 1][BoardSize + 1] + " Player seeds");
                            } else {
                                dummyBoard[Player - 1][0] += seedsPlayer;
                                dummyBoard[Opponent - 1][BoardSize + 1] += seedsOpponent;
                                print(dummyBoard[Player - 1][0] + " Opponent seeds");
                                print(dummyBoard[Opponent - 1][BoardSize + 1] + " Player seeds");

                            }
                            for (int i = 1; i < BoardSize + 1; i++) {
                                dummyBoard[Player - 1][i] = 0;
                                dummyBoard[Opponent - 1][i] = 0;
                            }
                        }
                        if (Player == 1) {
                            evalFunction[k - 1] = dummyBoard[Player - 1][BoardSize + 1] - dummyBoard[Opponent - 1][0];
                        } else {
                            evalFunction[k - 1] = dummyBoard[Player - 1][0] - dummyBoard[Opponent - 1][BoardSize + 1];
                        }
                        if (childEvalValue > -INFINITY) {
                            evalFunction[k - 1] = childEvalValue;
                        }
                        tempTraverse = "{" + (Player == 1 ? "B" : "A") + pos + "," + turn + "," + evalFunction[k - 1] + "}";

                        if (maxEvalFn < evalFunction[k - 1]) {
                            maxEvalFn = evalFunction[k - 1];
                            maxEvalFnPos = k;
                            // "{" + (Player == 1 ? "B" : "A") + pos + "," + turn + "," + evalFunction[k - 1] + "}";
                            Move move = new Move((Player == 1 ? "B" : "A") + pos, turn, evalFunction[k - 1]);
                            maxNode = move;
                        }

                        println("=============" + tempTraverse + "\n");
                        printDummyBoard(dummyBoard, turn);
                        print(tempTraverse + "=============\n");
                        Move move = new Move((Player == 1 ? "B" : "A") + pos, turn, evalFunction[k - 1]);

                        tempTraverseNode.add(0, move);
                        for (int i = 0; i < tempTraverseNode.size(); i++) {
                            print("\t" + turn + "max" + maxNode.value + "=" + maxEvalFn + "at" + maxEvalFnPos + "\t TempNode " + i + tempTraverseNode.get(i).node + "," + turn + "," + tempTraverseNode.get(i).value);
                        }

                        print("\n============= child " + maxNode.value + "=============\n");
                        TraverseNode.add(k - 1, tempTraverseNode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Move move = new Move((Player == 1 ? "B" : "A") + pos, turn, -INFINITY);
                    tempTraverseNode.add(0, move);
                    TraverseNode.add(k - 1, tempTraverseNode);
                }

            }//for loop
            for (int i = 0; i < TraverseNode.size(); i++) {
                print("TraverseNode " + i + "/" + TraverseNode.size());
                for (int j = 0; j < TraverseNode.get(i).size(); j++) {
                    print("\t\tchildTraverseNode " + j + "/" + TraverseNode.get(i).size());
                    TraverseNode.get(i).get(j).printMove();

                }
                if (!maxNode.node.equals(TraverseNode.get(i).get(0).node)) {
                    TraverseNode.remove(i);
                    i--;
                }
                println("\n");
            }
            print("\nMAX " + maxNode.node + "," + maxEvalFn + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TraverseNode;
    }

    public static int[][] playMove(int idx, int Board[][]) {
        int k = idx;
        String next;
        int dummyBoard[][] = initDummy(Board);
        //System.out.println("Initial Player " + Player + " pit " + k + " index" + idx);
        //System.out.println("Player " + Player + " pit " + k);
        int seeds = Board[Player - 1][k];
        Board[Player - 1][k] = 0;
        String thisNode = (Player == 1 ? "B" : "A") + k;
        //System.out.println("seeds " + seeds + " pit " + k);
        next = sowSeeds(seeds, Player, k, Board);
        //print("Playing");
        // printDummyBoard(Board, k);
        int index = Integer.parseInt(next.substring(1));
        boolean captureSeeds = (Player == 2 && next.startsWith("A") && index > 0) || (Player == 1 && next.startsWith("B")
                && index < BoardSize + 1)
                && (dummyBoard[Player - 1][index] == 0 || (next.equals(thisNode) && Board[Player - 1][k] == 1));
        //println(captureSeeds + "Next " + next);
        if (captureSeeds) {
            if (Board[Player - 1][index] == 1) {
                //      println("capture index " + index);
                int capture = Board[Opponent - 1][index] + 1;
                Board[Opponent - 1][index] = 0;
                Board[Player - 1][index] = 0;
                if (Player == 2) {
                    Board[Player - 1][0] += capture;
                } else {
                    Board[Player - 1][BoardSize + 1] += capture;
                }
            }
        }
        int countEmpty = 0;
        int seedsPlayer = 0, seedsOpponent = 0;
        for (int i = 1; i < BoardSize + 1; i++) {
            if (Board[Player - 1][i] == 0) {
                countEmpty++;
            }
            seedsPlayer = seedsPlayer + Board[Player - 1][i];
            seedsOpponent = seedsOpponent + Board[Opponent - 1][i];
        }
        if (seedsOpponent == 0 || seedsPlayer == 0) {
            if (Player == 1) {
                Board[Opponent - 1][0] += seedsOpponent;
                Board[Player - 1][BoardSize + 1] += seedsPlayer;
                //    print(Board[Opponent - 1][0] + " Opponent seeds\t");
                //    print(Board[Player - 1][BoardSize + 1] + " Player seeds");
            } else {
                Board[Player - 1][0] += seedsPlayer;
                Board[Opponent - 1][BoardSize + 1] += seedsOpponent;
                //  print(Board[Player - 1][0] + " Opponent seeds");
//                print(Board[Opponent - 1][BoardSize + 1] + " Player seeds");

            }
            for (int i = 1; i < BoardSize + 1; i++) {
                Board[Player - 1][i] = 0;
                Board[Opponent - 1][i] = 0;
            }
        }
        //      print("PlayAfter" + idx + "\n");
        // printDummyBoard(Board, k);
        print("=============\n");

        return Board;
    }

    public static String sowSeeds(int seeds, int player, int pit, int dummyBoard[][]) {
        int i = player - 1;
        int j = pit;
        String position = (player == 2 ? "A" : "B") + pit;
        if (player == 2) {
            j = pit - 1;
        } else {
            j = pit + 1;
        }
        while (seeds > 0) {
            if (player == 2) {
                for (; i < 2 && seeds > 0; i++) {
                    //println(seeds + "(" + i + " " + j + ")");
                    for (; j < BoardSize + 1 && j >= 0 && seeds > 0;) {
                        //   println("\t" + seeds + "(" + i + " " + j + ")");
                        if (i == 0 && j == BoardSize + 1) {
                        } else {
                            seeds--;
                            dummyBoard[i][j]++;
                        }
                        position = (i == 1 ? "A" : "B") + j;
                        //printDummyBoard();
                        if (i == 0) {
                            j++;
                        } else {
                            j--;
                        }
                    }
                    if (j == BoardSize + 1) {
                        j = j - 1;
                    }
                }
                //printDummyBoard(dummyBoard,1);
                if (i == 2) {
                    i = i % 2;
                }
                if (j == -1) {
                    if (i == 0) {
                        j = j + 2;
                    } else {
                        j = 0;
                    }
                }
                //println(seeds + " seeds " + i + " " + j);
            } else {
                for (; i < 2 && seeds > 0; i++) {
                    // println(seeds + "(" + i + " " + j + ")");
                    for (; j < BoardSize + 2 && j >= 0 && seeds > 0;) {
                        //   println("\t" + seeds + "(" + i + " " + j + ")");
                        if (j == 0 && i == 1) {

                        } else {
                            seeds--;
                            dummyBoard[i][j]++;
                        }
                        position = (i == 1 ? "A" : "B") + j;
                        //printDummyBoard();
                        if (i == 0) {
                            j++;
                        } else {
                            j--;
                        }
                    }
                    if (j == BoardSize + 2) {
                        j = j - 2;
                    }
                }
                //printDummyBoard(dummyBoard,1);
                if (i == 2) {
                    i = i % 2;
                }
                if (j == -1) {
                    if (i == 0) {
                        j = j + 2;
                    } else {
                        j = 0;
                    }
                }
                //println(seeds + " seeds " + i + " " + j);
            }
        }
        return position;
    }

    public static MNode playMinMaxMove(int idx, int Player, int depth, MNode succ) {
        println(" call in playMinMaxMove Player" + Player + " at " + idx + depth);
        succ.printMNode();
        int k = idx;
        String next;
        MNode mNode = new MNode(succ);
        mNode.depth = depth + 1;
        int Board[][] = initDummy(mNode.board);
        int bestBoard[][] = initDummy(Board);
        int seeds = Board[Player - 1][k];
        Board[Player - 1][k] = 0;
        int Opponent = Player % 2 + 1;
        if (seeds > 0) {
            int pos1 = k;
            String thisNode = (Player == 1 ? "B" : "A") + pos1;
            next = sowSeeds(seeds, Player, k, Board);
            int index = Integer.parseInt(next.substring(1));
            boolean captureSeeds = (Player == 2 && next.startsWith("A") && index > 0)
                    || (Player == 1 && next.startsWith("B") && index < BoardSize + 1)
                    && (succ.board[Player - 1][index] == 0 || (next.equals(thisNode) && Board[Player - 1][k] == 1));// check index upper bound
            if (captureSeeds) {
                if (Board[Player - 1][index] == 1) {
                    int capture = Board[Opponent - 1][index] + 1;
                    Board[Opponent - 1][index] = 0;
                    Board[Player - 1][index] = 0;
                    if (Player == 2) {
                        Board[Player - 1][0] += capture;
                    } else {
                        Board[Player - 1][BoardSize + 1] += capture;
                    }
                }
            }
            int countEmpty = 0;
            int seedsPlayer = 0, seedsOpponent = 0;
            for (int i = 1; i < BoardSize + 1; i++) {
                if (Board[Player - 1][i] == 0) {
                    countEmpty++;
                }
                seedsPlayer = seedsPlayer + Board[Player - 1][i];
                seedsOpponent = seedsOpponent + Board[Opponent - 1][i];
            }
            if (seedsOpponent == 0 || seedsPlayer == 0) {
                if (Player == 1) {
                    Board[Opponent - 1][0] += seedsOpponent;
                    Board[Player - 1][BoardSize + 1] += seedsPlayer;
                } else {
                    Board[Player - 1][0] += seedsPlayer;
                    Board[Opponent - 1][BoardSize + 1] += seedsOpponent;
                }
                for (int i = 1; i < BoardSize + 1; i++) {
                    Board[Player - 1][i] = 0;
                    Board[Opponent - 1][i] = 0;
                }
                countEmpty = -1;
            }
            int mIdx = BoardSize + 1;
            if (((Player == 2 && next.equals("A0")) || (Player == 1 && next.equals("B" + (mIdx)))) && countEmpty >= 0) {
                String node = (Player == 1 ? "B" : "A");
                int pos = k + 1;
                mNode.board = initDummy(Board);
                bestBoard = initDummy(Board);
                String operChild = mNode.oper;
                mNode.oper = (mNode.oper.equals("MAX") ? "MIN" : "MAX");
                int value = mNode.oper.equals("MAX") ? -INFINITY : INFINITY;
                mNode.value = value;
                println(" Parent \t\tminimax_value" + node + pos + "," + depth + "," + value + "," + mNode.oper);
                writeTraverseFile("traverse_log.txt", node + pos + "," + depth + "," + printValue(value) //+ " \t\t NewTurn Parent " + mNode.oper
                        , true);
                printDummyBoard(mNode.board, depth);
                for (int ki = 1; ki < BoardSize + 1; ki++) {
                    if (Board[Player - 1][ki] > 0) {
                        println("\t\t+" + next + " Extra chance" + ki);
                        //int dummyBoard1[][] = initDummy(Board);
                        value = operChild.equals("MAX") ? -INFINITY : INFINITY;
                        MNode mnodeChild = new MNode(Board, depth, operChild, value);
                        mnodeChild.oper = operChild;
                        mnodeChild = minimax_value(ki, Player, depth, mnodeChild);
                        // mNode.depth=depth+1;
                        if (mNode.oper.equals("MAX")) {
                            int valueParent = mNode.value;
                            int valueChild = mnodeChild.value;
                            if (valueChild > valueParent) {
                                println(mNode.oper + "Parent=" + valueParent + " child=" + valueChild);
                                bestBoard = initDummy(mnodeChild.board);
                                mNode.value = mnodeChild.value;
                                mNode.board = initDummy(mnodeChild.board);
                                print("PlayMinMaxChildren-MAX");
                                printDummyBoard(bestBoard, depth + 1);
                            }
                        } else {
                            int valueParent = mNode.value;
                            int valueChild = mnodeChild.value;
                            if (valueChild < valueParent) {
                                println(mNode.oper + "Parent=" + valueParent + " child=" + valueChild);
                                mNode.value = mnodeChild.value;
                                bestBoard = initDummy(mnodeChild.board);
                                mNode.board = initDummy(mnodeChild.board);
                                print("PlayMinMaxChildren-MIN");
                                printDummyBoard(bestBoard, depth + 1);
                            }

                        }
                        if (ki < BoardSize + 1) {
                            println(" ============Parent \t\tminimax_value" + node + pos + "," + depth + "," + mNode.value + "," + mNode.oper);
                            writeTraverseFile("traverse_log.txt", node + pos + "," + depth + "," + printValue(mNode.value) //+ " \t PARENT " + mNode.oper
                                    , true);
                            mNode.depth = depth + 1;
                        }

                    }

                }
                mNode.oper = "DONE";
            } else {
                String node = (Player == 1 ? "B" : "A");
                int pos = k + 1;
                int value = utility(Board);
                bestBoard = initDummy(Board);
                mNode.board = initDummy(Board);
                if (depth < CutOffDepth) {
                    value = mNode.oper.equals("MAX") ? -INFINITY : INFINITY;
                    mNode.value = value;
                    println(" NoChild \t\tminimax_value" + node + pos + "," + depth + "," + mNode.value + "," + mNode.oper);
                    writeTraverseFile("traverse_log.txt", node + pos + "," + depth + "," + printValue(mNode.value) //+ " \t\t NoChild Not Cut off " + mNode.oper
                            , true);

                    if (countEmpty == -1) {
                        value = utility(Board);
                        mNode.value = value;
                        println(" NoChild Empty \t\tminimax_value" + node + pos + "," + depth + "," + mNode.value + "," + mNode.oper);
                        writeTraverseFile("traverse_log.txt", node + pos + "," + depth + "," + printValue(mNode.value) //+ " \t\t NoChild Not Cut off " + mNode.oper
                                , true);
                    }
                } else {
                    mNode.value = value;
                    String printValue = (value == INFINITY ? "Infinity" : (value == -INFINITY ? "-Infinity" : "" + value));
                    println(" NoChild Cut off\t\tminimax_value" + node + pos + "," + depth + "," + mNode.value + "," + mNode.oper);
                    writeTraverseFile("traverse_log.txt", node + pos + "," + depth + "," + printValue(mNode.value) //+ " \t\t NoChild Not Cut off " + mNode.oper
                            , true);

                }

            }
            print(mNode.oper + "\tPlayAfter" + Player + idx + "\n");
            mNode.board = initDummy(Board);
            print("=======best========\n");
            printDummyBoard(mNode.board, depth);

            print("=======original========" + mNode.value + "\n");
            printDummyBoard(succ.board, depth);

            mNode.board = initDummy(bestBoard);
        }
        return mNode;
    }

    public static ABNode playAlphaBeta(int idx, int Player, int depth, ABNode succ, int alpha, int beta) {
        println(" call in playAlphaBeta Player" + Player + " at " + idx + depth);
        int k = idx;
        String next;
        ABNode mNode = new ABNode(succ);
        mNode.depth = depth + 1;
        int Board[][] = initDummy(mNode.board);
        int bestBoard[][] = initDummy(Board);
        int seeds = Board[Player - 1][k];
        Board[Player - 1][k] = 0;
        int Opponent = Player % 2 + 1;
        if (seeds > 0) {
            String thisNode = (Player == 1 ? "B" : "A") + k;
            next = sowSeeds(seeds, Player, k, Board);
            int index = Integer.parseInt(next.substring(1));
            boolean captureSeeds = (Player == 2 && next.startsWith("A") && index > 0)
                    || (Player == 1 && next.startsWith("B") && index < BoardSize + 1)
                    && (succ.board[Player - 1][index] == 0 || (next.equals(thisNode) && Board[Player - 1][k] == 1));// check index upper bound
            if (captureSeeds) {
                if (Board[Player - 1][index] == 1) {
                    int capture = Board[Opponent - 1][index] + 1;
                    Board[Opponent - 1][index] = 0;
                    Board[Player - 1][index] = 0;
                    if (Player == 2) {
                        Board[Player - 1][0] += capture;
                    } else {
                        Board[Player - 1][BoardSize + 1] += capture;
                    }
                }
            }
            int countEmpty = 0;
            int seedsPlayer = 0, seedsOpponent = 0;
            for (int i = 1; i < BoardSize + 1; i++) {
                if (Board[Player - 1][i] == 0) {
                    countEmpty++;
                }
                seedsPlayer = seedsPlayer + Board[Player - 1][i];
                seedsOpponent = seedsOpponent + Board[Opponent - 1][i];
            }
            if (seedsOpponent == 0 || seedsPlayer == 0) {
                if (Player == 1) {
                    Board[Opponent - 1][0] += seedsOpponent;
                    Board[Player - 1][BoardSize + 1] += seedsPlayer;
                } else {
                    Board[Player - 1][0] += seedsPlayer;
                    Board[Opponent - 1][BoardSize + 1] += seedsOpponent;
                }
                for (int i = 1; i < BoardSize + 1; i++) {
                    Board[Player - 1][i] = 0;
                    Board[Opponent - 1][i] = 0;
                }
                countEmpty = -1;
            }

            int mIdx = BoardSize + 1;
            if (((Player == 2 && next.equals("A0")) || (Player == 1 && next.equals("B" + (mIdx)))) && countEmpty >= 0) {
                String node = (Player == 1 ? "B" : "A");
                int pos = k + 1;
                mNode.board = initDummy(Board);
                bestBoard = initDummy(Board);
                String operChild = mNode.oper;
                mNode.oper = (mNode.oper.equals("MAX") ? "MIN" : "MAX");
                int value = mNode.oper.equals("MAX") ? -INFINITY : INFINITY;
                mNode.value = value;
                String printValue = (value == INFINITY ? "Infinity" : (value == -INFINITY ? "-Infinity" : "" + value));
                println(" Parent \t\tminimax_value" + node + pos + "," + depth + "," + value + "," + alpha + "," + beta + "," + mNode.oper);
                writeTraverseFile("traverse_log.txt", node + pos + "," + depth + "," + printValue(value) + "," + printValue(alpha) + "," + printValue(beta) //+ " \t\t NewTurn Parent " + mNode.oper
                        , true);
                printDummyBoard(mNode.board, depth);
                for (int ki = 1; ki < BoardSize + 1; ki++) {
                    if (Board[Player - 1][ki] > 0) {
                        println("\t\t" + next + "Extra chance" + ki);
                        //int dummyBoard1[][] = initDummy(Board);
                        value = operChild.equals("MAX") ? -INFINITY : INFINITY;
                        ABNode mnodeChild = new ABNode(alpha, beta, Board, operChild, value, depth);
                        mnodeChild.oper = operChild;
                        mnodeChild = alphaBeta(ki, Player, depth, mnodeChild, alpha, beta);
                        // mNode.depth=depth+1;
                        if (mNode.oper.equals("MAX")) {
                            int valueParent = mNode.value;
                            int valueChild = mnodeChild.value;
                            value = valueChild;
                            if (valueChild > valueParent) {
                                println(mNode.oper + "Parent=" + valueParent + " child=" + valueChild);
                                bestBoard = initDummy(mnodeChild.board);
                                mNode.value = mnodeChild.value;
                                mNode.board = initDummy(mnodeChild.board);
                            }
                            print("PlayAlphaBetaChildren-MAX");
                            printDummyBoard(bestBoard, depth + 1);
                            if (value >= beta) {
                                println(" ============Parent \t\tminimax_value" + node + pos + "," + depth + "," + mNode.value + "," + alpha + "," + beta + "," + mNode.oper);
                                writeTraverseFile("traverse_log.txt", node + pos + "," + depth + "," + printValue(mNode.value) + "," + printValue(alpha) + "," + printValue(beta) //+ " \t PARENT " + mNode.oper
                                        , true);
                                mNode.depth = depth + 1;
                                break;
                            }

                            alpha = Math.max(alpha, valueChild);

                        } else {
                            int valueParent = mNode.value;
                            int valueChild = mnodeChild.value;
                            value = valueChild;
                            if (valueChild < valueParent) {
                                println(mNode.oper + "Parent=" + valueParent + " child=" + valueChild);
                                mNode.value = mnodeChild.value;
                                bestBoard = initDummy(mnodeChild.board);
                                mNode.board = initDummy(mnodeChild.board);
                            }
                            print("PlayAlphaBetaChildren-MIN");
                            printDummyBoard(bestBoard, depth + 1);
                            if (value <= alpha) {
                                println(" ============Parent \t\tminimax_value" + node + pos + "," + depth + "," + mNode.value + "," + alpha + "," + beta + "," + mNode.oper);
                                writeTraverseFile("traverse_log.txt", node + pos + "," + depth + "," + printValue(mNode.value) + "," + printValue(alpha) + "," + printValue(beta) //+ " \t PARENT " + mNode.oper
                                        , true);
                                mNode.depth = depth + 1;
                                break;
                            }

                            beta = Math.min(beta, valueChild);

                        }
                        if (ki < BoardSize + 1) {
                            println(" ============Parent \t\tminimax_value" + node + pos + "," + depth + "," + mNode.value + "," + alpha + "," + beta + "," + mNode.oper);
                            writeTraverseFile("traverse_log.txt", node + pos + "," + depth + "," + printValue(mNode.value) + "," + printValue(alpha) + "," + printValue(beta) //+ " \t PARENT " + mNode.oper
                                    , true);
                            mNode.depth = depth + 1;
                        }

                    }

                }
                mNode.oper = "DONE";
            } else {
                String node = (Player == 1 ? "B" : "A");
                int pos = k + 1;
                int value = utility(Board);
                mNode.value = value;
                bestBoard = initDummy(Board);
                mNode.board = initDummy(Board);
                if (depth < CutOffDepth) {

                    value = mNode.oper.equals("MAX") ? -INFINITY : INFINITY;
                    String printValue = (value == INFINITY ? "Infinity" : (value == -INFINITY ? "-Infinity" : "" + value));
                    mNode.value = value;
                    println(" Parent \t\tminimax_value" + node + pos + "," + depth + "," + mNode.value + "," + alpha + "," + beta + "," + mNode.oper);
                    writeTraverseFile("traverse_log.txt", node + pos + "," + depth + "," + printValue(mNode.value) + "," + printValue(alpha) + "," + printValue(beta) //+ " \t\t NoChild Not Cut off " + mNode.oper
                            , true);
                    //mNode.value = value;
                    if (countEmpty == -1) {
                        value = utility(Board);
                        mNode.value = value;
                        println(" Parent \t\tminimax_value" + node + pos + "," + depth + "," + mNode.value + "," + alpha + "," + beta + "," + mNode.oper);
                        writeTraverseFile("traverse_log.txt", node + pos + "," + depth + "," + printValue(mNode.value) + "," + printValue(alpha) + "," + printValue(beta) //+ " \t\t NoChild Not Cut off " + mNode.oper
                                , true);

                    }
                } else {
                    mNode.value = value;
                    String printValue = (value == INFINITY ? "Infinity" : (value == -INFINITY ? "-Infinity" : "" + value));
                    println(" Parent cut off\t\tminimax_value" + node + pos + "," + depth + "," + mNode.value + "," + alpha + "," + beta + "," + mNode.oper);
                    writeTraverseFile("traverse_log.txt", node + pos + "," + depth + "," + printValue(mNode.value) + "," + printValue(alpha) + "," + printValue(beta) //+ " \t\t NoChild Not Cut off " + mNode.oper
                            , true);

                }
            }

            print(mNode.oper + "\tPlayAfter" + Player + idx + "\n");
            mNode.board = initDummy(Board);
            print("=======best========\n");
            printDummyBoard(mNode.board, depth);

            print("=======original========" + mNode.value + "\n");
            printDummyBoard(succ.board, depth);

            mNode.board = initDummy(bestBoard);
        }
        return mNode;
    }

    public static int[][] minimax_decision(int Player, int depth, int[][] Board) {
        int dummyBoard[][] = initDummy(Board);
        int bestBoard[][] = initDummy(Board);
        String oper = "MAX";
        int values[] = new int[BoardSize];
        int maxValue = -INFINITY;
        writeTraverseFile("traverse_log.txt", "Node,Depth,Value", false);
        writeTraverseFile("traverse_log.txt", "root,0,-Infinity" //+ " \t\t" + oper
                , true);
        MNode succ = new MNode(dummyBoard, depth, oper, -INFINITY);
        int maxPos = 1;
        for (int k = 1; k < BoardSize + 1; k++) {
            //   for (int k = 2; k < 3; k++) {
            String node = (Player == 1 ? "B" : "A");
            int pos = k + 1;
            if (dummyBoard[Player - 1][k] > 0) {
                MNode mNode = new MNode(succ);
                mNode.oper = "MIN";
                mNode.value = INFINITY;
                mNode.depth = 1;
                MNode mNodeRet = minimax_value(k, Player, depth + 1, mNode);
                values[k - 1] = mNodeRet.value;
                println("Root max " + maxValue + " val" + values[k - 1]);
                if (values[k - 1] > maxValue) {
                    maxValue = values[k - 1];
                    maxPos = k;
                    bestBoard = initDummy(mNodeRet.board);
                    printDummyBoard(bestBoard, 5);
                }
                println(oper + "-" + node + pos + "," + values[k - 1]);
                println(" Update \t\tminimax_value root,0" + "," + maxValue + "," + mNodeRet.oper);
                writeTraverseFile("traverse_log.txt", "root,0," + printValue(maxValue) //+ " \t\t" + mNodeRet.oper
                        , true);
            }
        }
        for (int i = 1; i < BoardSize + 1; i++) {
            oper = "MAX";
            String node = (Player == 1 ? "B" : "A");
            int pos = i + 1;
            if (maxPos == i) {
                // MNode mNode=  playAllMoves(maxPos,Player,0,succ);
                //bestBoard=mNode.board;
            }
            println(oper + "-" + node + pos + "," + values[i - 1]);
        }
        // printDummyBoard(bestBoard, 5);
        return bestBoard;
    }

    public static int[][] alphaBeta_decision(int Player, int depth, int[][] Board) {
        int dummyBoard[][] = initDummy(Board);
        int bestBoard[][] = initDummy(Board);
        String oper = "MAX";
        int values[] = new int[BoardSize];
        int maxValue = -INFINITY;
        int alpha = -INFINITY;
        int beta = INFINITY;
        writeTraverseFile("traverse_log.txt", "Node,Depth,Value,Alpha,Beta", false);
        writeTraverseFile("traverse_log.txt", "root,0,-Infinity" + "," + printValue(alpha) + "," + printValue(beta) //+ " \t\t" + oper
                , true);
        ABNode succ = new ABNode(alpha, beta, dummyBoard, oper, maxValue, depth);
        //(int a, int b, int[][] retBoard, String op, int val, int dep)
        int maxPos = 1;
        //alpha=43;
        for (int k = 1; k < BoardSize + 1; k++) {
            //  for (int k = 7; k < 8; k++) {
            String node = (Player == 1 ? "B" : "A");
            int pos = k + 1;
            if (dummyBoard[Player - 1][k] > 0) {
                ABNode mNode = new ABNode(succ);
                mNode.oper = "MIN";
                mNode.value = INFINITY;
                mNode.depth = 1;
                ABNode mNodeRet = alphaBeta(k, Player, depth + 1, mNode, alpha, beta);
                values[k - 1] = mNodeRet.value;
                println("Root max " + maxValue + " val" + values[k - 1]);
                if (values[k - 1] > maxValue) {
                    maxValue = values[k - 1];
                    maxPos = k;
                    bestBoard = initDummy(mNodeRet.board);
                    printDummyBoard(bestBoard, 5);
                }
                alpha = Math.max(alpha, maxValue);
                println(oper + "-" + node + pos + "," + values[k - 1]);
                println(" Update \t\tminimax_value root,0" + "," + maxValue + "," + alpha + "," + beta + "," + mNodeRet.oper);
                writeTraverseFile("traverse_log.txt", "root,0," + printValue(maxValue) + "," + printValue(alpha) + "," + printValue(beta) //+ " \t\t" + mNodeRet.oper
                        , true);
            }
        }
        for (int i = 1; i < BoardSize + 1; i++) {
            oper = "MAX";
            String node = (Player == 1 ? "B" : "A");
            int pos = i + 1;
            if (maxPos == i) {
                // MNode mNode=  playAllMoves(maxPos,Player,0,succ);
                //bestBoard=mNode.board;
            }
            println(oper + "-" + node + pos + "," + values[i - 1]);
        }
        // printDummyBoard(bestBoard, 5);
        return bestBoard;
    }

    public static MNode minimax_value(int index, int Player, int depth, MNode succ) {
        println("minimax_value");
        int dummyBoard[][] = initDummy(succ.board);
        int bestBoard[][] = initDummy(dummyBoard);
        int Opponent = Player % 2 + 1;
        int pos = index + 1;
        String node = (Player == 1 ? "B" : "A");
        int value = -INFINITY;
        MNode mNode = new MNode(succ);
        mNode.depth = (mNode.depth > depth ? depth : mNode.depth);
        if (dummyBoard[Player - 1][index] > 0) {
            if (depth == CutOffDepth) {
                mNode = playMinMaxMove(index, Player, depth, mNode);
                bestBoard = initDummy(mNode.board);
                value = mNode.value;
            } else {
                dummyBoard = initDummy(mNode.board);
                mNode = playMinMaxMove(index, Player, depth, mNode);
                println(" ===\nNode " + node + pos + " value" + mNode.value + mNode.oper + "\n===");
                printDummyBoard(mNode.board, depth);
                MNode mNodeSucc = new MNode(mNode);
                bestBoard = initDummy(mNode.board);

                if (!isEmptyBoard(mNode.board, Player)) {
                    if (mNode.oper.equals("MAX")) {
                        mNodeSucc.oper = "MIN";
                        mNodeSucc.depth = depth + 1;
                        value = -INFINITY;

                        for (int k = 1; k < BoardSize + 1; k++) {
                            if (mNode.board[Opponent - 1][k] > 0) {
                                mNode.oper = (mNode.oper == "MIN" ? "MAX" : "MIN");
                                mNodeSucc = minimax_value(k, Opponent, depth + 1, mNode);
                                mNode.oper = (mNode.oper == "MIN" ? "MAX" : "MIN");
                                println(" MAX value" + mNode.value + " child" + mNodeSucc.value);
                                if (mNodeSucc.value > value) {
                                    value = mNodeSucc.value;
                                    bestBoard = initDummy(mNodeSucc.board);
                                    mNode.value = value;
                                }
                                println(" Update MAX \tminimax_value" + node + pos + "," + depth + "," + value + "," + mNode.oper);
                                writeTraverseFile("traverse_log.txt", node + pos + "," + depth + "," + printValue(value) //+ " \tmax\t" + mNode.oper
                                        , true);
                            }
                        }
                    } else if (mNode.oper.equals("MIN")) {
                        mNodeSucc.oper = "MAX";
                        mNodeSucc.depth = depth + 1;
                        value = INFINITY;

                        for (int k = 1; k < BoardSize + 1; k++) {
                            if (mNode.board[Opponent - 1][k] > 0) {
                                mNode.oper = (mNode.oper == "MIN" ? "MAX" : "MIN");
                                mNodeSucc = minimax_value(k, Opponent, depth + 1, mNode);
                                mNode.oper = (mNode.oper == "MIN" ? "MAX" : "MIN");
                                println(" MIN value" + value + " child" + mNodeSucc.value);
                                if (mNodeSucc.value < value) {
                                    value = mNodeSucc.value;
                                    bestBoard = initDummy(mNodeSucc.board);
                                    mNode.value = value;
                                }
                                println(" Update MIN \tminimax_value" + node + pos + "," + depth + "," + value + "," + mNode.oper);
                                writeTraverseFile("traverse_log.txt", node + pos + "," + depth + "," + printValue(value) //+ " \tmin\t" + mNode.oper
                                        , true);
                            }
                        }
                    } else {
                        // DONE with children
                        print("DONE " + mNode.value);
                        //mNode = playMinMaxMove(index, Player, depth, mNode);
                        print("DONE " + mNode.value);
                    }
                }//countEmpty

            }
        }

        //  mNode = new MNode(bestBoard, mNode.depth, mNode.oper, mNode.value);
        return mNode;
    }

    public static ABNode alphaBeta(int index, int Player, int depth, ABNode succ, int alpha, int beta) {
        println("minimax_value");
        int dummyBoard[][] = initDummy(succ.board);
        int bestBoard[][] = initDummy(dummyBoard);
        int Opponent = Player % 2 + 1;
        int pos = index + 1;
        String node = (Player == 1 ? "B" : "A");
        int value = -INFINITY;
        ABNode mNode = new ABNode(succ);
        mNode.depth = (mNode.depth > depth ? depth : mNode.depth);
        if (dummyBoard[Player - 1][index] > 0) {
            if (depth == CutOffDepth) {
                mNode = playAlphaBeta(index, Player, depth, mNode, alpha, beta);
                bestBoard = initDummy(mNode.board);
                value = mNode.value;
            } else {
                dummyBoard = initDummy(mNode.board);
                mNode = playAlphaBeta(index, Player, depth, mNode, alpha, beta);
                println(" ===\nNode " + node + pos + " value" + mNode.value + mNode.oper + "\n===");
                printDummyBoard(mNode.board, depth);
                ABNode mNodeSucc = new ABNode(mNode);
                bestBoard = initDummy(mNode.board);

                if (!isEmptyBoard(mNode.board, Player)) {

                    if (mNode.oper.equals("MAX")) {
                        mNodeSucc.oper = "MIN";
                        mNodeSucc.depth = depth + 1;
                        value = -INFINITY;

                        for (int k = 1; k < BoardSize + 1; k++) {
                            if (mNode.board[Opponent - 1][k] > 0) {
                                mNode.oper = (mNode.oper == "MIN" ? "MAX" : "MIN");
                                mNodeSucc = alphaBeta(k, Opponent, depth + 1, mNode, alpha, beta);
                                mNode.oper = (mNode.oper == "MIN" ? "MAX" : "MIN");
                                println(" MAX value" + mNode.value + " child" + mNodeSucc.value);

                                if (mNodeSucc.value > value) {
                                    value = Math.max(value, mNodeSucc.value);
                                    bestBoard = initDummy(mNodeSucc.board);
                                    mNode.value = value;

                                }
                                if (value >= beta) {
                                    println(" Update MIN pruned \tminimax_value" + node + pos + "," + depth + "," + value + "," + alpha + "," + beta + "," + mNode.oper);
                                    writeTraverseFile("traverse_log.txt", node + pos + "," + depth + "," + printValue(value) + "," + printValue(alpha) + "," + printValue(beta) //+ " \tmin\t" + mNode.oper
                                            , true);
                                    break;
                                }
                                alpha = Math.max(alpha, value);
                                println(" Update MIN \tminimax_value" + node + pos + "," + depth + "," + value + "," + alpha + "," + beta + "," + mNode.oper);
                                writeTraverseFile("traverse_log.txt", node + pos + "," + depth + "," + printValue(value) + "," + printValue(alpha) + "," + printValue(beta) //+ " \tmin\t" + mNode.oper
                                        , true);
                            }
                        }
                    } else if (mNode.oper.equals("MIN")) {
                        mNodeSucc.oper = "MAX";
                        mNodeSucc.depth = depth + 1;
                        value = INFINITY;
                        for (int k = 1; k < BoardSize + 1; k++) {
                            if (mNode.board[Opponent - 1][k] > 0) {
                                mNode.oper = (mNode.oper == "MIN" ? "MAX" : "MIN");
                                mNodeSucc = alphaBeta(k, Opponent, depth + 1, mNode, alpha, beta);
                                mNode.oper = (mNode.oper == "MIN" ? "MAX" : "MIN");
                                println(" MIN value" + value + " child" + mNodeSucc.value);
                                if (mNodeSucc.value < value) {
                                    value = Math.min(value, mNodeSucc.value);
                                    bestBoard = initDummy(mNodeSucc.board);
                                    mNode.value = value;
                                }

                                if (value <= alpha) {
                                    println(" Update MIN  pruned \tminimax_value" + node + pos + "," + depth + "," + value + "," + alpha + "," + beta + "," + mNode.oper);
                                    writeTraverseFile("traverse_log.txt", node + pos + "," + depth + "," + printValue(value) + "," + printValue(alpha) + "," + printValue(beta) //+ " \tmin\t" + mNode.oper
                                            , true);
                                    break;
                                }
                                beta = Math.min(beta, value);
                                println(" Update MIN \tminimax_value" + node + pos + "," + depth + "," + value + "," + alpha + "," + beta + "," + mNode.oper);
                                writeTraverseFile("traverse_log.txt", node + pos + "," + depth + "," + printValue(value) + "," + printValue(alpha) + "," + printValue(beta) //+ " \tmin\t" + mNode.oper
                                        , true);
                            }
                        }
                    } else {
                        // DONE with children
                        print("DONE " + mNode.value);
                        //mNode = playMinMaxMove(index, Player, depth, mNode);
                        print("DONE " + mNode.value);

                    }
                }//countEmpty
            }
        }

        return mNode;

    }

    public static int utility(int[][] Board) {
        if (Player == 1) {
            return Board[0][BoardSize + 1] - Board[1][0];
        } else {
            return -Board[0][BoardSize + 1] + Board[1][0];
        }
    }

    public static boolean isEmptyBoard(int[][] Board, int Player) {
        int countEmpty = 0;
        int seedsPlayer = 0, seedsOpponent = 0;
        int Opponent = Player % 2 + 1;
        for (int i = 1; i < BoardSize + 1; i++) {
            if (Board[Player - 1][i] == 0) {
                countEmpty++;
            }
            seedsPlayer = seedsPlayer + Board[Player - 1][i];
            seedsOpponent = seedsOpponent + Board[Opponent - 1][i];
        }
        if (seedsOpponent == 0 && seedsPlayer == 0) {
            return true;
        }
        return false;
    }

    public static void print(String s) {
        //System.out.print(s);
    }

    public static void println(String s) {
        //System.out.println(s);
    }

    public static String printValue(int value) {
        String printValue = (value == INFINITY ? "Infinity" : (value == -INFINITY ? "-Infinity" : "" + value));
        return printValue;
    }
}
