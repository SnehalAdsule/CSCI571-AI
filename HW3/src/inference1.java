/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 *
 * @author Snehal
 */
public class inference1 {

    /**
     * @param args the command line arguments
     */
    public static String IMPLIES = "=>";
    public static String AND = "^";
    public static String OR = "";
    public static String NOT = "~";

    public static int queryCount;
    public static int KBCount;
    public static int varCount = 1;
    public static ArrayList<Sentence> KBSentence = new ArrayList();
    public static ArrayList<Sentence> Query = new ArrayList();

    public static class Sentence {

        LinkedHashMap<Integer, List<Clause>> premise = new LinkedHashMap();
        LinkedHashMap<Integer, List<Clause>> conclusion = new LinkedHashMap();
        String text;

        Sentence(String txt) {
            this.text = txt;
        }
    }

    public static class Clause {

        String Predicate;
        ArrayList Variable = new ArrayList();
        //ArrayList Constant = new ArrayList();
        String text;

        Clause(String txt) {
            this.text = txt;
        }

        Clause(Clause c) {
            this.Predicate = c.Predicate;
            this.Variable = c.Variable;
            //this.Constant = c.Constant;
        }

        public String getString() {
            return this.Predicate + "(" + this.Variable + ")";
        }

    }

    public static class Operator {

        String text;

        Operator(String txt) {
            this.text = txt;
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
        readInputFile("C:\\Users\\Snehal\\Documents\\NetBeansProjects\\inference\\src\\input_1.txt");
        for (int i = 0; i < KBSentence.size(); i++) {
            KBSentence.set(i, parseSentence(KBSentence.get(i).text, i));
        }
        for (int i = 0; i < KBSentence.size(); i++) {
            for (Object obj : KBSentence.get(i).premise.entrySet()) {
                Map.Entry entry = (Map.Entry) obj;
                List<Clause> cList = (List<Clause>) entry.getValue();
                for (int j = 0; j < cList.size(); j++) {
                    print(i + ".Premise" + cList.get(j).Predicate + "::" + cList.get(j).Variable);
                }
            }

            for (Object obj : KBSentence.get(i).conclusion.entrySet()) {
                Map.Entry entry = (Map.Entry) obj;
                List<Clause> cList = (List<Clause>) entry.getValue();
                for (int j = 0; j < cList.size(); j++) {
                    print(i + ".Conclusion" + cList.get(j).Predicate + "::" + cList.get(j).Variable);
                }
print("");
            }

        }
    }

    public static Sentence parseSentence(String txt, int num) {
        char[] chr = txt.toCharArray();
        String variable[] = null;
        Sentence s = new Sentence(txt);
        int i = 0;
        boolean chkVar = false;
        boolean isVar = false;
        String var = "";
        int leftPar = 0, rightPar = 0;
        int startPred = 0;
        List<Clause> cList = new ArrayList<Clause>();
        Clause c = new Clause(txt);
        int implyIdx = txt.indexOf("=>");
        while (i < chr.length) {
            if (chr[i] == '~') {
                startPred = i;
            }
            if (chr[i] == '^') {
                startPred = i + 1;
                c = new Clause(txt);
            }
            if (chr[i] == '=' && chr[i + 1] == '>') {
                startPred = i + 2;
            }
            if (chr[i] == '(') {
                // print("predicate::" + txt.substring(startPred, i) + implyIdx);
                c.Predicate = txt.substring(startPred, i).trim();
                chkVar = true;
                leftPar = i;
                if (chr[i + 1] >= 65 && chr[i + 1] >= 90) {
                    isVar = true;
                }
            }
            if (chr[i] == ')') {
                startPred = i + 1;
                chkVar = false;
                rightPar = i;
                if (leftPar > 0 && rightPar > 0) {
                    String nTxt = txt.substring(leftPar + 1, rightPar);

                    variable = nTxt.split(",");
                    for (int k = 0; k < variable.length; k++) {
                        // print("variable::" + variable[k]);
                        c.Variable.add(variable[k].trim());
                    }
                    if (implyIdx < i || implyIdx == -1) {
                        cList = new ArrayList<Clause>();
                        cList.add(c);
                        s.conclusion.put(num, cList);
                    } else {
                        cList.add(c);
                        s.premise.put(num, cList);

                    }

                }
                c = new Clause(txt);
            }
            i++;
        }

        return s;
    }

    public static boolean readInputFile(String inputFile) {
        try {
            Path path = Paths.get(inputFile);
            String line = null;
            inference1 inf = new inference1();
            BufferedReader reader = Files.newBufferedReader(path);
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                if (lineCount == 1) {
                    queryCount = Integer.parseInt(line);
                } else if (lineCount <= queryCount + 1) {
                    Sentence s = new Sentence(line);
                    Query.add(s);
                } else {
                    if (lineCount == queryCount + 2) {
                        KBCount = Integer.parseInt(line);
                    } else {
                        Sentence s = new Sentence(line);
                        KBSentence.add(s);
                    }

                }
            }

        } catch (Exception e) {
            System.out.println("Error readInputFile");
            e.printStackTrace();
        }
        return false;
    }

    public static void print(String s) {
        System.out.println(s);
    }

}
