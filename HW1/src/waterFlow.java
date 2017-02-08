/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//COMMENT THE OUTPUT TO REDUCE RUN TIME
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.*;
/*
 * @author Snehal Adsule
 * USC 2080872073
 */

public class waterFlow {

    /**
     * @param args the command line arguments
     */
    public static int TextCasesCount = 1;
    String Method;
    String StartNodes[];
    String EndNodes[];
    String MiddleNodes[];
    int edgeCount;
    int noOfNodes;
    int intervalCounts;
    int waterFlowStartTime;
    int waterFlowEndTime;
    String EndNode;
    Graph G;
    Node n;
    Edge e;

    public static String outputResult = "";

    public class ClosedInterval {

        int startTime;
        int endTime;

        public ClosedInterval(int a, int b) {
            this.startTime = a;
            this.endTime = b;
        }
    }

    public class Node {

        String vName;
        int pathCost;

        Node(String vName, int cost) {
            this.vName = vName;
            this.pathCost = cost;
        }
    }

    public class Edge {

        Node u;
        Node v;
        int cost;
        int intervalCount;
        ClosedInterval cInterval[];

        Edge(String uName, String vName, int costN, int invlCount, ClosedInterval c[]) {
            this.u = new Node(uName, 0);
            this.v = new Node(vName, 0);
            this.cost = costN;
            this.intervalCount = invlCount;
            this.cInterval = c;

        }
    }

    public class Graph {

        List<Node> nodes;
        List<Edge> edges;

        Graph() {
            nodes = new ArrayList<>();
            edges = new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
        String inputFile = ".//src//inputGenerator.txt";
      
        boolean a = false;
        try {
            if (args.length > 0) {
                args[0] = "-i";

                inputFile = args[1];
            }
            System.out.println("path " + inputFile);
          
            //inputFile = args[1];
            a = readInputFile(inputFile);
        } catch (Exception e) {
            System.out.println("Error");
            if (!a) {
                readInputFile(inputFile);
            }
        }
    }

    public static void printGraph(List<Node> list) {
        System.out.print("print Graph\t");
        List<String> listStr = new ArrayList<>();
        try {

            for (int i = 0; i < list.size(); i++) {
                String temp = list.get(i).vName;
                int tempCost = list.get(i).pathCost;
                System.out.print(i + ":: (" + temp + "," + tempCost + ")\t");
                listStr.add(temp);
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("printGraph Error::" + e.toString());
        }

    }

    public static void printEdge(List<Edge> list) {
        System.out.print("print Edge\t");
        List<String> listStr = new ArrayList<>();
        try {

            for (int i = 0; i < list.size(); i++) {
                String temp = list.get(i).u.vName + "=>" + list.get(i).v.vName + " cost :" + list.get(i).cost;
                System.out.print(i + ":: (" + temp + ")\t");
                listStr.add(temp);
            }

        } catch (Exception e) {
            System.out.println("printEdge Error::" + e.toString());
        }

    }

    public static boolean readInputFile(String inputFile) {
        try {
            Path path = Paths.get(inputFile);
            BufferedReader reader = Files.newBufferedReader(path);
            String line = null;
            waterFlow w[] = null;
            //System.out.print("Reading " + inputFile);
            if ((line = reader.readLine()) != null) {
                TextCasesCount = Integer.parseInt(line.trim());
            }
            if (TextCasesCount > 0) {
                w = new waterFlow[TextCasesCount];
            }
            int lineCount = 0;
            int NoOfCases = 0;
            w[NoOfCases] = new waterFlow();
            w[NoOfCases].G = w[NoOfCases].new Graph();
            System.out.println("No of Cases " + TextCasesCount);
            //PrintStream ps=new PrintStream(new BufferedOutputStream(new FileOutputStream("verbose.txt")),true);
            //System.setOut(ps);
            while ((((line = reader.readLine()) != null)
                    && TextCasesCount > NoOfCases) || (((line = reader.readLine()) == null) && 
                    TextCasesCount > NoOfCases)) {
                try {
                    lineCount++;
                    if (line == null) {
                        line = ""; // to take care of last line of file being null or \n
                    }
                     if (line.trim().equals("") && lineCount>4) {
                        lineCount = 0;
                        String output = "";
                        System.out.println("Case " + NoOfCases + " Run method :: " + w[NoOfCases].Method);
                        if (w[NoOfCases].Method.equals("BFS")) {
                            output = BFS(w[NoOfCases]);

                            System.out.println("\nOutput:" + output);
                        } else if (w[NoOfCases].Method.equals("UCS")) {
                            output = UCS(w[NoOfCases]);
                            System.out.println("\nOutput:" + output);
                        } else if (w[NoOfCases].Method.equals("DFS")) {
                            output = DFS(w[NoOfCases]);
                            System.out.println("\nOutput:" + output);
                        } else {
                            System.out.println("Method not known " + w[NoOfCases].Method);
                            continue;
                        }
                        outputResult = outputResult + output + "\n";
                   // printGraph(w[NoOfCases].G.nodes);
                        //printEdge(w[NoOfCases].G.edges);
                        createOutputFile(w[NoOfCases].G.nodes);
                        NoOfCases++;
                        if (TextCasesCount > NoOfCases) {
                            w[NoOfCases] = new waterFlow();
                            w[NoOfCases].G = w[NoOfCases].new Graph();
                        }
                    } else {
                        if (lineCount == 1) {
                            // w[NoOfCases] = new waterFlow();
                            System.out.println("============================================================");
                            w[NoOfCases].Method = line;
                        } else if (lineCount == 2) {
                            w[NoOfCases].StartNodes = line.split(" ");
                            // add source nodes
                            for (String StartNode : w[NoOfCases].StartNodes) {
                                w[NoOfCases].n = w[NoOfCases].new Node(StartNode, 0);
                                w[NoOfCases].G.nodes.add(w[NoOfCases].n);
                              //  System.out.print("\tAdd start node for w." + NoOfCases + "->" + w[NoOfCases].G.nodes.size());
                            }
                        } else if (lineCount == 3) {
                            w[NoOfCases].EndNodes = line.split(" ");
                            for (String EndNodes : w[NoOfCases].EndNodes) {
                                w[NoOfCases].n = w[NoOfCases].new Node(EndNodes, 0);
                                w[NoOfCases].G.nodes.add(w[NoOfCases].n);
                                //System.out.println("Add end node for w." + NoOfCases + "->" + w[NoOfCases].G.nodes.size());
                            }

                        } else if (lineCount == 4) {
                            
                            try{
                            w[NoOfCases].MiddleNodes = line.split(" ");
                            for (String MiddleNode : w[NoOfCases].MiddleNodes) {
                                w[NoOfCases].n = w[NoOfCases].new Node(MiddleNode, 0);
                                w[NoOfCases].G.nodes.add(w[NoOfCases].n);
                                //System.out.println("Add middle node for w." + NoOfCases + "->" + w[NoOfCases].G.nodes.size());
                                }
                            }catch(Exception e){
                             System.out.print("Missing middle nodes");
                            }
                            
                        } else if (lineCount == 5) {
                            try {
                                w[NoOfCases].edgeCount = Integer.parseInt(line.trim());
                            } catch (NumberFormatException ne) {
                                w[NoOfCases].edgeCount = 1;
                            }
                            // System.out.print("No of edges w." + NoOfCases + "->" + w[NoOfCases].edgeCount);
                        } else {
                            try {
                                if (lineCount <= 5 + w[NoOfCases].edgeCount) {

                                    String parseEdge[] = line.split(" ");
                                    ClosedInterval c[] = null;
                                    // System.out.print("\t edge " + parseEdge[0] + parseEdge[1] + Integer.parseInt(parseEdge[2]) + Integer.parseInt(parseEdge[3]) + " ");
                                    int noOfInterval = Integer.parseInt(parseEdge[3].trim())+0;
                                    c = new ClosedInterval[noOfInterval];
                                    for (int i = 0; i < noOfInterval && noOfInterval > 0; i++) {
                                        String interval[] = parseEdge[i + 4].split("-");
                                        // System.out.print(interval[0] + "-" + interval[1] + " ");
                                        c[i] = w[NoOfCases].new ClosedInterval(Integer.parseInt(interval[0].trim()),
                                                Integer.parseInt(interval[1].trim()));
                                    }
                                    w[NoOfCases].e = w[NoOfCases].new Edge(parseEdge[0], parseEdge[1],
                                            Integer.parseInt(parseEdge[2].trim()), Integer.parseInt(parseEdge[3].trim()), c);
                                    w[NoOfCases].G.edges.add(w[NoOfCases].e);
                                } else {
                                    w[NoOfCases].waterFlowStartTime = Integer.parseInt(line.trim());
                                    System.out.print("waterFlowStartTime->" + w[NoOfCases].waterFlowStartTime);
                                }
                            } catch (Exception e) {
                                System.out.println(e.toString());
                                e.printStackTrace();
                            }
                        }

                        //System.out.println(line);
                    }
                } catch (Exception e) {
                    System.out.println("Error while loop in File :: " + e.toString());
                }
            }
        } catch (Exception e) {
            System.out.println("Error Reading File :: " + e.toString());
        }

        return false;
    }

    public static boolean createOutputFile(List<Node> list) {
        List<String> listStr = new ArrayList<>();
        try {
            listStr.add(outputResult);
            writeTextFile("output.txt", listStr);

        } catch (Exception e) {
            System.out.println("Error Creating File");
        }
        return false;
    }

    public static void writeTextFile(String aFileName, List<String> aLines) throws IOException {
        Path path = Paths.get(aFileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (String line : aLines) {
                writer.append(line);

            }
            //writer.newLine();  
        }
    }

    public static String UCS(waterFlow w1) {
        Node GoalNode = w1.new Node("None", -1);
        try {
            List<Node> frontier = new ArrayList<>();
            List<Node> explored = new ArrayList<>();
            int pathCost = w1.waterFlowStartTime;
            boolean isGoal = false;
            System.out.print("Initial PathCost " + pathCost);
            Node nTemp = w1.new Node(w1.StartNodes[0], pathCost);
            frontier.add(nTemp);
            while (!frontier.isEmpty() && !isGoal) {
                Node nFrontNode = frontier.remove(0);
                // search if node already explored
                explored.add(nFrontNode);
                // reorder queue as per weight 
                pathCost = nFrontNode.pathCost;
               // System.out.print("\nPopped Node " + nFrontNode.vName + " pathCost " + nFrontNode.pathCost);
                // check goal test
                for (String tempGoal : w1.EndNodes) {
                    if (tempGoal.equals(nFrontNode.vName)) {
                        isGoal = true;
                        GoalNode = nFrontNode;
                    }
                }
                // enqueue children
                for (Edge e : w1.G.edges) {
                    if (e.u.vName.equals(nFrontNode.vName)) {
                        boolean duplicate = false;
                        boolean offTime = false;
                        // check for duplicate children
                        Node tempNode = e.v;
                        if (e.u.vName.equals(nFrontNode.vName)) {
                            tempNode = e.v;
                        }
                        tempNode.pathCost = ((e.cost + pathCost));// removed 24 hours cycle
                        //int pathCost_24 = (e.cost + pathCost) % 24;
                        int pathCost_24 = (pathCost) % 24;
                        ClosedInterval intervals[] = e.cInterval;
                        if (intervals.length > 0) {
                            for (ClosedInterval c : intervals) {
                                offTime = offTime || c.startTime <= pathCost_24 && c.endTime >= pathCost_24;
                               // System.out.println("\t(" + tempNode.vName + "," + tempNode.pathCost + ")" + pathCost_24 + " offTime (" + c.startTime + "-" + c.endTime + ")" + offTime);
                            }
                        }
                      //  System.out.println("\tChecking Node " + tempNode.vName + tempNode.pathCost);
                        for (Node explored1 : explored) {
                            duplicate = duplicate || explored1.vName.equals(tempNode.vName);
                        }
                     //   System.out.println("\t(" + tempNode.vName + "," + tempNode.pathCost + ") Duplicate in ex " + duplicate);
                        boolean duplicate1 = false;
                        for (Node frontier1 : frontier) {
                            duplicate1 = frontier1.vName.equals(tempNode.vName);
                            duplicate = duplicate || frontier1.vName.equals(tempNode.vName);
                            if (duplicate1 && !offTime) {
                                if (frontier1.pathCost > tempNode.pathCost) {
                                 //   System.out.println(tempNode.vName + " -> " + frontier1.pathCost + "-" + tempNode.pathCost);
                                    frontier1.pathCost = tempNode.pathCost;
                                    break;
                                }
                            }
                        }
                     //   System.out.println("\t" + tempNode.vName + " Duplicate in fr " + duplicate);
                        if (!duplicate && !offTime) {
                            // check for the pipe closed intervals and modulo 24 :(
                            frontier.add(tempNode);
                     //       System.out.println("\t" + e.u.vName + " hasChild " + e.v.vName);
                     //       System.out.println("\tAdded Node " + tempNode.vName + duplicate);
                        }
                    }
                }
                // sort a temparory or frontier ???
                Collections.sort(frontier, new Comparator<Node>() {
                    @Override
                    public int compare(Node a1, Node a2) {
                        return a1.pathCost < a2.pathCost ? -1 : (a1.pathCost == a2.pathCost ? (a1.vName.compareToIgnoreCase(a2.vName)) : 1);
                    }
                });
           //     System.out.println("Frontier ::");
           //    printGraph(frontier);
            }
        } catch (Exception e) {
         //   System.out.println("Error:: UCS" + e.toString());
            e.printStackTrace();
        }
        return GoalNode.vName + " " + (GoalNode.pathCost < 0 ? "" : GoalNode.pathCost % 24);
    }

    public static String BFS(waterFlow w1) {
        Node GoalNode = w1.new Node("None", -1);
        try {
            List<Node> frontier = new ArrayList<>();
            List<Node> explored = new ArrayList<>();
            int pathCost = w1.waterFlowStartTime;
            boolean isGoal = false;

         //   System.out.println("Initial BFS PathCost " + pathCost);
            Node nTemp = w1.new Node(w1.StartNodes[0], pathCost);
            frontier.add(nTemp);
            while (!frontier.isEmpty() && !isGoal) {
                Node nFrontNode = frontier.remove(0);
                // search if node already explored
                // should be by design 
                explored.add(nFrontNode);
                pathCost = nFrontNode.pathCost + 1;
             //   System.out.println("\tPopped Node " + nFrontNode.vName + " pathCost " + nFrontNode.pathCost);

                // check goal test
                for (int i = 0; i < w1.EndNodes.length; i++) {
                    String tempGoal = w1.EndNodes[i];
                    if (tempGoal.equals(nFrontNode.vName)) {
                        isGoal = true;
                        GoalNode = nFrontNode;
                        break;
                    }
                }
                // enqueue children            
                List<Node> tempFrontier = new ArrayList<>();
                for (int i = 0; i < w1.G.edges.size(); i++) {
                    Edge e = w1.G.edges.get(i);
                    if (e.u.vName.equals(nFrontNode.vName)) {
                        boolean duplicate = false;
                        // check for duplicate children
                        Node tempNode = e.v;
                        // bi-directed graph notion u-v same as v-u
                        if (e.u.vName.equals(nFrontNode.vName)) {
                            tempNode = e.v;
                        }
                        tempNode.pathCost = (pathCost % 24);
                        //System.out.println("Checking Node " + tempNode.vName + tempNode.pathCost);
                        for (int j = 0; j < explored.size(); j++) {
                            duplicate = duplicate || explored.get(j).vName.equals(tempNode.vName);
                        }
                      //  System.out.println("\t" + tempNode.vName + " Duplicate in ex " + duplicate);
                        for (int j = 0; j < frontier.size(); j++) {
                            duplicate = duplicate || frontier.get(j).vName.equals(tempNode.vName);
                        }
                      //  System.out.println("\t" + tempNode.vName + "\tDuplicate in fr " + duplicate);
                        if (!duplicate) {
                            tempFrontier.add(tempNode);
                      //      System.out.println("\t" + e.u.vName + " hasChild " + e.v.vName);
                       //     System.out.println("\tAdded Node " + tempNode.vName + duplicate);
                        }
                    }
                    // sort a temparory and add to frontier                 
                    Collections.sort(tempFrontier, new Comparator<Node>() {
                        public int compare(Node a1, Node a2) {
                            return a1.vName.compareToIgnoreCase(a2.vName);
                        }
                    });

                }
                if (tempFrontier.size() > 0) {
                    for (Node tempNode : tempFrontier) {
                        frontier.add(tempNode);
                    }

                }
              //  System.out.println("Frontier ::");
              //  printGraph(frontier);

            }
        } catch (Exception e) {
            System.out.println("Error:: UCS" + e.toString());
            e.printStackTrace();
        }
        return GoalNode.vName + " " + (GoalNode.pathCost < 0 ? "" : GoalNode.pathCost);
    }

    public static String DFS(waterFlow w1) {
        Node GoalNode = w1.new Node("None", -1);
        try {
            List<Node> frontier = new ArrayList<>();
            List<Node> explored = new ArrayList<>();
            int pathCost = w1.waterFlowStartTime;
            boolean isGoal = false;

          //  System.out.println("Initial DFS PathCost " + pathCost);
            Node nTemp = w1.new Node(w1.StartNodes[0], pathCost);
            frontier.add(nTemp);
            while (!frontier.isEmpty() && !isGoal) {
                int k = frontier.size();
                Node nFrontNode = frontier.remove(k - 1);
                // search if node already explored
                // should be by design 
                explored.add(nFrontNode);
                pathCost = nFrontNode.pathCost + 1;
              //  System.out.println("\tPopped Node " + nFrontNode.vName + " pathCost " + nFrontNode.pathCost);
                // check goal test
                for (int i = 0; i < w1.EndNodes.length; i++) {
                    String tempGoal = w1.EndNodes[i];
                    if (tempGoal.equals(nFrontNode.vName)) {
                        isGoal = true;
                        GoalNode = nFrontNode;
                        break;
                    }
                }
                // push children            
                List<Node> tempFrontier = new ArrayList<>();
                for (int i = 0; i < w1.G.edges.size(); i++) {
                    Edge e = w1.G.edges.get(i);
                    if (e.u.vName.equals(nFrontNode.vName)) {
                        boolean duplicate = false;
                        // check for duplicate children
                        Node tempNode = e.v;

                        tempNode.pathCost = (pathCost % 24);
                        //System.out.println("Checking Node " + tempNode.vName + tempNode.pathCost);
                        for (int j = 0; j < explored.size(); j++) {
                            duplicate = duplicate || explored.get(j).vName.equals(tempNode.vName);
                        }
                       // System.out.println("\t" + tempNode.vName + " Duplicate in ex " + duplicate);
                        /*for (int j = 0; j < frontier.size(); j++) {
                         duplicate = duplicate || frontier.get(j).vName.equals(tempNode.vName);
                         }
                      //   System.out.println("\t" + tempNode.vName + "\tDuplicate in fr " + duplicate);
                         */
                        if (!duplicate) {
                            tempFrontier.add(tempFrontier.size(), tempNode);
                       //     System.out.println("\t" + e.u.vName + " hasChild " + e.v.vName);
                       //     System.out.println("\tAdded Node " + tempNode.vName + duplicate);
                        }
                    }

                }
                // add temp children to stack
                // sort a temparory and add to frontier                 
                Collections.sort(tempFrontier, new Comparator<Node>() {
                    public int compare(Node a1, Node a2) {
                        return a1.vName.compareToIgnoreCase(a2.vName);
                    }

                });
                Collections.reverse(tempFrontier);
                for (Node temp : tempFrontier) {
                    frontier.add(frontier.size(), temp);
                }
              //  System.out.println("Frontier LIFO::");
              //  printGraph(frontier);
            }
        } catch (Exception e) {
           // System.out.println("Error:: DFS" + e.toString());
            e.printStackTrace();
        }
        return GoalNode.vName + " " + (GoalNode.pathCost < 0 ? "" : GoalNode.pathCost);

    }

}
// CHECK UCS MOD TO BE TAKEN IN THE END OR MIDDLE ALSO
