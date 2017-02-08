/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 * @author Snehal
 */
public class inference {

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
    public ArrayList<KBNode> KBSentence = new ArrayList();
    public ArrayList<KBNode> Query = new ArrayList();
    public List<List<FOLPredicate>> tempKB2 = new ArrayList();
    FOLUnifier uni = new FOLUnifier();

    public static class KBNode {

        List< List<FOLPredicate>> premise = new ArrayList();
        List<FOLPredicate> conclusion = new ArrayList();
        String text;

        KBNode(String txt) {
            this.text = txt;
        }

        KBNode(List<List<FOLPredicate>> premise, List<FOLPredicate> conclusion) {
            this.premise = premise;
            this.conclusion = conclusion;
            this.text = null;
        }

        public void getString() {
            print(this.premise.toString() + "--" + this.conclusion.toString());
        }

        public String getPremiseList() {
            String list = "";
            for (int i = 0; i < this.premise.size(); i++) {
                list = list + this.premise.get(i).toString();
            }
            return list;
        }

        public String getConclusionList() {
            return conclusion.toString();
        }

        public String toString() {
            return getPremiseList() + getConclusionList();
        }
    }

    public FOLUnifier getFOLUnifier() {
        return uni;
    }

    public static void main(String[] args) {

        String inputFile = "C:\\Users\\Snehal\\Documents\\NetBeansProjects\\inference\\src\\input.txt";
        boolean a = false;

        try {
            if (args.length > 0) {
                args[0] = "-i";
                inputFile = args[1];
            }
            // TODO code application logic here           
            inference FOL = readInputFile(inputFile);
            FOLUnifier u = FOL.getFOLUnifier();
            // intialize KB
            for (int i = 0; i < FOL.KBSentence.size(); i++) {
                KBNode kb = FOL.parseSentence(FOL.KBSentence.get(i).text, i);
                FOL.KBSentence.set(i, kb);
                FOL.KBSentence.get(i).getString();
            }

            List<String> result = new ArrayList();
            // initialize queries
            for (int j = 0; j < FOL.Query.size(); j++) {
                FOL.tempKB2 = new ArrayList();
                print("==========================================");
                KBNode kb = FOL.parseSentence(FOL.Query.get(j).text, j);
                FOL.Query.set(j, kb);
                FOL.Query.get(j).getString();
                try{
                List<Map<FOLVariable, FOLTerm>> match = FOL.FOL_BC_ASK(FOL, u, j);
                print("Temp KB" + FOL.tempKB2.toString());
                if (match != null) {
                    result.add("TRUE\n");
                    System.out.println("TRUE" + FOL.tempKB2.toString() + match.toString());
                } else {
                    result.add("FALSE\n");
                    System.out.println("False " + FOL.tempKB2.toString());
                }
                }catch(Exception e){
                result.add("FALSE\n");
                    System.out.println("FALSE" + FOL.tempKB2.toString());
                }

            }
            // System.out.print(inputFile.substring(inputFile.lastIndexOf("\\")));
            writeTextFile("output.txt", result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean[] testCase(String File) {
        inference FOL = readInputFile(File);
        FOLUnifier u = FOL.getFOLUnifier();
        // intialize KB
        for (int i = 0; i < FOL.KBSentence.size(); i++) {
            KBNode kb = FOL.parseSentence(FOL.KBSentence.get(i).text, i);
            FOL.KBSentence.set(i, kb);
            FOL.KBSentence.get(i).getString();
        }

        boolean result[] = new boolean[FOL.Query.size()];
        // initialize queries
        for (int j = 0; j < FOL.Query.size(); j++) {
            FOL.tempKB2 = new ArrayList();
            print("==========================================");
            KBNode kb = FOL.parseSentence(FOL.Query.get(j).text, j);
            FOL.Query.set(j, kb);
            FOL.Query.get(j).getString();
            List<Map<FOLVariable, FOLTerm>> match = FOL.FOL_BC_ASK(FOL, u, j);
            print("Temp KB" + FOL.tempKB2.toString());
            if (match != null) {
                result[j] = (true);
                System.out.println("TRUE" + FOL.tempKB2.toString() + match.toString());
            } else {
                result[j] = (false);
                System.out.println("False " + FOL.tempKB2.toString());
            }

        }

        return result;

    }

    public List<Map<FOLVariable, FOLTerm>> FOL_BC_ASK(inference FOL, FOLUnifier unifier, int QueryNo) {
        Map<FOLVariable, FOLTerm> m = new LinkedHashMap();
        KBNode kb = FOL.Query.get(QueryNo);
        List<Map<FOLVariable, FOLTerm>> listTheta = null;
        listTheta = FOL_BC_OR(FOL, unifier, kb, m,tempKB2);
        return listTheta;
    }

    public List<Map<FOLVariable, FOLTerm>> FOL_BC_OR(inference FOL, FOLUnifier unifier, KBNode kb, Map<FOLVariable, FOLTerm> theta,List<List<FOLPredicate>> tempKB1) {
        List<Map<FOLVariable, FOLTerm>> listTheta = null;
        Map<FOLVariable, FOLTerm> listMatch = null;
        // Fetch multiple rules
        print("====\n====\nFOL_OR (" + kb.getPremiseList() + "=>" + kb.getConclusionList() + ")\ttheta" + theta);
        boolean checkLoop = false;
        boolean checkFact = false;
        List<List<FOLPredicate>> tempKB=new ArrayList();
        tempKB.addAll(tempKB1);
        
             
        if (!tempKB.contains(kb.conclusion)) {
            print("TempKB" + tempKB.toString());
            //if (!checkFact)
            {
                tempKB.add(kb.conclusion);
            }
        } else {
            print("Fail" + kb.conclusion + theta + "\t" + tempKB);
            listTheta = null;
            checkLoop = true;
        }

        for (int i = 0; i < FOL.KBSentence.size(); i++) {
            
            Map<FOLVariable, FOLTerm> m = new LinkedHashMap();
            //m.putAll(theta);
            //print("checked " + FOL.KBSentence.get(i).premise.toString() + "=>" + FOL.KBSentence.get(i).conclusion.get(0).toString());
            m = unifier.unify((FOLNode) FOL.KBSentence.get(i).conclusion.get(0), (FOLNode) kb.conclusion.get(0), m);
            if (m != null) {
                if (listTheta == null) {
                }
                List<Map<FOLVariable, FOLTerm>> tempTheta = null;
                listMatch =  m;

                print("\nUnified with " + FOL.KBSentence.get(i).premise.toString() + "=> " + FOL.KBSentence.get(i).conclusion.toString() + "for" + kb.conclusion.toString());
                print("m" + m.toString());
                if (!checkLoop) {
                    tempTheta = FOL_BC_AND(FOL, unifier, FOL.KBSentence.get(i), m,tempKB);
                }
                //print("m" + m.toString()+tempTheta);
                if (tempTheta != null) {
                    if (listTheta == null) {
                        listTheta = new ArrayList();
                    }
                   
                    for (int k = 0; k < tempTheta.size(); k++) {
                        for (FOLVariable var : theta.keySet()) {
                            if (tempTheta.get(k).containsKey(var)) {
                                FOLTerm t1 = theta.get(var);
                                if (t1 instanceof FOLVariable) {
                                    Map<FOLVariable, FOLTerm> m1 = new LinkedHashMap();
                                    m1.put((FOLVariable) t1, tempTheta.get(k).get(var));
                                    print("Found" + t1 + tempTheta.get(k).get(var) + tempTheta.get(k));
                                    tempTheta.add(m1);
                                }
                            }
                        }
                        listMatch.putAll(tempTheta.get(k));
                       // listTheta.addAll(tempTheta);
                        //if(!listTheta.contains(listMatch))
                        listTheta.add(listMatch);
                    }
                     print("tempTheta" + tempTheta.toString()+"\n"+listTheta);
                    
                } else {
                    // listTheta=null;
                }
              

            }

        }

        print("==== End FOL_OR " + listTheta + "for\t (" + kb.getPremiseList() + "=>" + kb.getConclusionList());
        return listTheta;
    }

    public List<Map<FOLVariable, FOLTerm>> FOL_BC_AND(inference FOL, FOLUnifier unifier, KBNode kb, Map<FOLVariable, FOLTerm> theta,List<List<FOLPredicate>> tempKB1) {
        Map<FOLVariable, FOLTerm> match = null;
        List<Map<FOLVariable, FOLTerm>> listMatch = null;
        List<Map<FOLVariable, FOLTerm>> nlistMatch = null;
           List<List<FOLPredicate>> tempKB=new ArrayList();
        tempKB.addAll(tempKB1);
        if (theta == null) {
            return null;
        } else if (kb.premise.isEmpty()) {
            listMatch = new ArrayList();
            listMatch.add(theta);
            return listMatch;
        } else {
            print("===========\nFOL_AND" + kb.premise.toString() + "=>" + kb.conclusion.toString() + "\ttheta" + theta);
            List< List<FOLPredicate>> rest = new ArrayList();
            if (kb.premise.size() >= 2) {
                rest = new ArrayList<>(kb.premise.subList(1, kb.premise.size()));
            }
            List<FOLPredicate> first = new ArrayList<>(kb.premise.get(0));
            first = substr(first, theta);

            for (int i = 0; i < rest.size() && !rest.isEmpty(); i++) {
                List<FOLPredicate> newList = substr(rest.get(i), theta);
                rest.set(i, newList);
                print(kb.premise.get(i + 1) + rest.get(i).toString() + i + "\t Substr list" + newList);
            }
            print("first" + first.toString() + "Rest" + rest.toString() + "\ttheta" + theta);
            List< List<FOLPredicate>> bogusPremise = new ArrayList();

            KBNode firstKB = new KBNode(bogusPremise, first);
            if (first != null && !first.isEmpty()) {
                listMatch = FOL_BC_OR(FOL, unifier, firstKB, theta,tempKB);

                if (listMatch == null) {
                    print("====End FOL_AND " + listMatch + "for\t" + kb.getPremiseList());
                    return null;
                } else {             //if(!rest.isEmpty())
                    {
                        for (Map<FOLVariable, FOLTerm> listMatch1 : listMatch) {
                            print("list" + listMatch + "rest" + rest.toString() + "first" + first.toString());
                            print("temp theta'" + listMatch1.toString() + " AND-OR theta " + listMatch.toString());
                            print("\t AND Check here " + rest.toString() + "--" + first);
                            List<FOLPredicate> dummy = new ArrayList();
                            KBNode tempKB3 = new KBNode(rest, dummy);
                            List<Map<FOLVariable, FOLTerm>> listMatch2 = FOL_BC_AND(FOL, unifier, tempKB3, listMatch1,tempKB);
                            if (listMatch2 != null) {
                                print("theta''" + listMatch2.toString());
                                if (nlistMatch == null) {
                                    nlistMatch = new ArrayList();
                                }

                                nlistMatch.add(listMatch1);
                                
                                for (int k = 0; k < listMatch2.size(); k++) {
                                    for (FOLVariable var : theta.keySet()) {
                                        if (listMatch2.get(k).containsKey(var)) {
                                            FOLTerm t1 = theta.get(var);
                                            if (t1 instanceof FOLVariable) {
                                                Map<FOLVariable, FOLTerm> m1 = new LinkedHashMap();
                                                m1.put((FOLVariable) t1, listMatch2.get(k).get(var));
                                                print("Found" + t1 + listMatch2.get(k).get(var));
                                                listMatch2.add(m1);
                                            }
                                        }
                                    }
                                    if(!nlistMatch.contains(listMatch2.get(k)))
                                    nlistMatch.add(listMatch2.get(k));
                                    //    nlistMatch.add(nlistMatch2);
                                }
                            } else {     // nlistMatch = null;
                            }

                        }

                    }
                }

            } else {

                nlistMatch = new ArrayList();
                nlistMatch.add(theta);
                return nlistMatch;
            }

        }

        print("====End FOL_AND " + nlistMatch + "for\t" + kb.getPremiseList());
        return nlistMatch;
    }

    public List<FOLPredicate> substr(List<FOLPredicate> clause, Map<FOLVariable, FOLTerm> theta) {
        List<FOLPredicate> nConclude = new ArrayList();
        for (int i = 0; i < clause.size(); i++) {
            nConclude.add(clause.get(i));
        }

        if (!theta.isEmpty()) {
            for (Entry e : theta.entrySet()) {
                String variable = e.getKey().toString();
                String constant = e.getValue().toString();
                print("Clause " + clause + "theta" + theta);

                // for each predicate
                Iterator aa = nConclude.iterator();
                int idx = 0;

                for (; aa.hasNext(); idx++) {
                    FOLPredicate itPred = (FOLPredicate) aa.next();
                    String predicate = itPred.getFOLPredicateName();
                    //print(clause.size() + "old" + itPred);
                    FOLPredicate p = null;
                    // for each term
                    List<FOLTerm> terms = itPred.getFOLTerms();
                    FOLConstant c = null;
                    List<FOLTerm> nterms = new ArrayList();
                    // print("FOLTerms"+terms.size());
                    for (int k = 0; k < terms.size(); k++) {
                        FOLTerm t1 = terms.get(k);
                        char chkConst = t1.toString().charAt(0);
                        boolean varChk = (chkConst >= 65 && chkConst <= 90);

                        if (t1 instanceof FOLVariable || !varChk) {
                            FOLVariable v = new FOLVariable(t1.toString());
                            if (v.getValue().equals(variable)) {
                                boolean varConst = (constant.charAt(0) >= 65 && constant.charAt(0) <= 90);
                                if (varConst) {
                                    c = new FOLConstant(constant);
                                    nterms.add(k, c);
                                } else {
                                    print(constant + "is const" + varConst);
                                    nterms.add(k, v);
                                }
                            } else {
                                print(t1.toString() + "is var" + (int) chkConst + !varChk);
                                nterms.add(k, v);
                            }
                        } else {
                            FOLConstant v = new FOLConstant(t1.toString());
                            nterms.add(k, v);
                        }

                    }
                    p = new FOLPredicate(predicate, nterms);
                    print(" predicate " + variable + constant + nterms + p);
                    nConclude.set(idx, p);
                    print(nConclude.toString());
                }

            }
        }

        return nConclude;
    }

    public Map<FOLVariable, FOLTerm> testUnification(inference FOL, FOLUnifier unifier, int QueryNo) {
        KBNode kb = FOL.Query.get(QueryNo);
        Map<FOLVariable, FOLTerm> listMatch = new LinkedHashMap();
        for (int i = 0; i < FOL.KBSentence.size(); i++) {
            KBNode kb1 = FOL.KBSentence.get(i);
            Map<FOLVariable, FOLTerm> m = new LinkedHashMap();
            m = unifier.unify((FOLNode) kb.conclusion.get(0), (FOLNode) kb1.conclusion.get(i), m);
            if (m != null) {
                if (m.isEmpty()) {
                    print("Empty List Success");
                }
                for (Entry e : m.entrySet()) {
                    listMatch.put((FOLVariable) e.getKey(), (FOLTerm) e.getValue());
                    print(e.getKey().toString() + "=" + e.getValue().toString());
                }
            }
        }
        return listMatch;
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

    public static inference readInputFile(String inputFile) {
        inference inf = new inference();
        try {

            Path path = Paths.get(inputFile);
            String line = null;

            BufferedReader reader = Files.newBufferedReader(path);
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                if (lineCount == 1) {
                    queryCount = Integer.parseInt(line);
                } else if (lineCount <= queryCount + 1) {
                    KBNode s = new KBNode(line);
                    inf.Query.add(s);
                } else {
                    if (lineCount == queryCount + 2) {
                        KBCount = Integer.parseInt(line);
                    } else {
                        KBNode s = new KBNode(line);
                        inf.KBSentence.add(s);
                    }

                }
            }

        } catch (Exception e) {
            System.out.println("Error readInputFile");
            e.printStackTrace();
        }
        return inf;
    }

    public KBNode parseSentence(String txt, int num) {
        char[] chr = txt.toCharArray();
        String variable[] = null;
        KBNode s = new KBNode(txt);
        int i = 0;
        boolean chkVar = false;
        boolean isVar = false;
        String var = "";
        int leftPar = 0, rightPar = 0;
        int startPred = 0;
        List<FOLPredicate> cList = new ArrayList<>();
        FOLPredicate c;
        String predicate = null;
        List<FOLTerm> terms = new ArrayList<>();
        int implyIdx = txt.indexOf("=>");
        while (i < chr.length) {
            if (chr[i] == '~') {
                startPred = i;
            }
            if (chr[i] == '^') {
                startPred = i + 1;
            }
            if (chr[i] == '=' && chr[i + 1] == '>') {
                startPred = i + 2;
                // cList = new ArrayList<>();
            }
            if (chr[i] == '(') {
                predicate = txt.substring(startPred, i).trim();
                chkVar = true;
                leftPar = i;
            }

            if (chr[i] == ')') {
                startPred = i + 1;
                rightPar = i;
                if (leftPar > 0 && rightPar > 0) {
                    String nTxt = txt.substring(leftPar + 1, rightPar);
                    variable = nTxt.split(",");
                    for (int k = 0; k < variable.length; k++) {
                        char a = variable[k].trim().charAt(0);
                        if ((int) a > 90 || (int) a < 65) {
                            isVar = true;
                        } else {
                            isVar = false;
                        }
                        FOLTerm v;

                        if (isVar) {
                            v = new FOLVariable(variable[k] + num);
                        } else {
                            v = new FOLConstant(variable[k]);
                        }
                        terms.add(v);
                        //  print(isVar+variable[k]+(int)a+v.getClass()+terms);
                    }

                    c = new FOLPredicate(predicate, terms);
                    if (implyIdx < i || implyIdx == -1) {

                        s.conclusion.add(c);
                    } else {
                        cList = new ArrayList();
                        cList.add(c);
                        s.premise.add(cList);
                    }

                }
                terms = new ArrayList<>();
            }
            i++;
        }

        return s;
    }

    public static void print(String s) {
      System.out.println(s);
    }

    public interface FOLNode {

        String getNameValue();

        boolean isCmpnd();

        List<? extends FOLNode> getParameters();

        Object accept(FOLVisitor v, Object arg);

        FOLNode duplicate();
    }

    public interface FOLAtomicSentence extends FOLSentence {

        List<FOLTerm> getParameters();

        FOLAtomicSentence duplicate();
    }

    public class FOLConstant implements FOLTerm {

        private String value;
        private int hashCode = 0;

        public FOLConstant(String s) {
            value = s;
        }

        public String getValue() {
            return value;
        }

        //
        // START-FOLTerm
        public String getNameValue() {
            return getValue();
        }

        public boolean isCmpnd() {
            return false;
        }

        public List<FOLTerm> getParameters() {
            // Is not Compound, therefore should
            // return null for its arguments
            return null;
        }

        public Object accept(FOLVisitor v, Object arg) {
            return v.visitFOLConstant(this, arg);
        }

        public FOLConstant duplicate() {
            return new FOLConstant(value);
        }

        // END-FOLTerm
        //
        @Override
        public boolean equals(Object o) {

            if (this == o) {
                return true;
            }
            if (!(o instanceof FOLConstant)) {
                return false;
            }
            FOLConstant c = (FOLConstant) o;
            return c.getValue().equals(getValue());

        }

        @Override
        public int hashCode() {
            if (0 == hashCode) {
                hashCode = 17;
                hashCode = 37 * hashCode + value.hashCode();
            }
            return hashCode;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public class FOLFunction implements FOLTerm {

        private String functionName;
        private List<FOLTerm> terms = new ArrayList<FOLTerm>();
        private String stringRep = null;
        private int hashCode = 0;

        public FOLFunction(String functionName, List<FOLTerm> terms) {
            this.functionName = functionName;
            this.terms.addAll(terms);
        }

        public String getFOLFunctionName() {
            return functionName;
        }

        public List<FOLTerm> getFOLTerms() {
            return Collections.unmodifiableList(terms);
        }

        //
        // START-FOLTerm
        public String getNameValue() {
            return getFOLFunctionName();
        }

        public boolean isCmpnd() {
            return true;
        }

        public List<FOLTerm> getParameters() {
            return getFOLTerms();
        }

        public Object accept(FOLVisitor v, Object arg) {
            return v.visitFOLFunction(this, arg);
        }

        public FOLFunction duplicate() {
            List<FOLTerm> duplicateFOLTerms = new ArrayList<FOLTerm>();
            for (FOLTerm t : terms) {
                duplicateFOLTerms.add(t.duplicate());
            }
            return new FOLFunction(functionName, duplicateFOLTerms);
        }

        // END-FOLTerm
        //
        @Override
        public boolean equals(Object o) {

            if (this == o) {
                return true;
            }
            if (!(o instanceof FOLFunction)) {
                return false;
            }

            FOLFunction f = (FOLFunction) o;

            return f.getFOLFunctionName().equals(getFOLFunctionName())
                    && f.getFOLTerms().equals(getFOLTerms());
        }

        @Override
        public int hashCode() {
            if (0 == hashCode) {
                hashCode = 17;
                hashCode = 37 * hashCode + functionName.hashCode();
                for (FOLTerm t : terms) {
                    hashCode = 37 * hashCode + t.hashCode();
                }
            }
            return hashCode;
        }

        @Override
        public String toString() {
            if (null == stringRep) {
                StringBuilder sb = new StringBuilder();
                sb.append(functionName);
                sb.append("(");

                boolean first = true;
                for (FOLTerm t : terms) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(",");
                    }
                    sb.append(t.toString());
                }

                sb.append(")");

                stringRep = sb.toString();
            }
            return stringRep;
        }
    }

    public class FOLPredicate implements FOLAtomicSentence {

        public String predicate;
        public List<FOLTerm> terms = new ArrayList<FOLTerm>();
        private String stringRep = null;
        private int hashCode = 0;

        public FOLPredicate(String predicate, List<FOLTerm> terms) {
            this.predicate = predicate;
            this.terms.addAll(terms);
        }

        public FOLPredicate() {
        }

        public String getFOLPredicateName() {
            return predicate;
        }

        public List<FOLTerm> getFOLTerms() {
            return Collections.unmodifiableList(terms);
        }

        //
        // START-FOLAtomicSentence
        public String getNameValue() {
            return getFOLPredicateName();
        }

        public boolean isCmpnd() {
            return true;
        }

        public List<FOLTerm> getParameters() {
            return getFOLTerms();
        }

        public Object accept(FOLVisitor v, Object arg) {
            return v.visitFOLPredicate(this, arg);
        }

        public FOLPredicate duplicate() {
            List<FOLTerm> duplicateFOLTerms = new ArrayList<FOLTerm>();
            for (FOLTerm t : terms) {
                duplicateFOLTerms.add(t.duplicate());
            }
            return new FOLPredicate(predicate, duplicateFOLTerms);
        }

        // END-FOLAtomicSentence
        //
        @Override
        public boolean equals(Object o) {

            if (this == o) {
                return true;
            }
            if (!(o instanceof FOLPredicate)) {
                return false;
            }
            FOLPredicate p = (FOLPredicate) o;
            return p.getFOLPredicateName().equals(getFOLPredicateName())
                    && p.getFOLTerms().equals(getFOLTerms());
        }

        @Override
        public int hashCode() {
            if (0 == hashCode) {
                hashCode = 17;
                hashCode = 37 * hashCode + predicate.hashCode();
                for (FOLTerm t : terms) {
                    hashCode = 37 * hashCode + t.hashCode();
                }
            }
            return hashCode;
        }

        @Override
        public String toString() {
            if (null == stringRep) {
                StringBuilder sb = new StringBuilder();
                sb.append(predicate);
                sb.append("(");

                boolean first = true;
                for (FOLTerm t : terms) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(",");
                    }
                    sb.append(t.toString());
                }

                sb.append(")");
                stringRep = sb.toString();
            }

            return stringRep;
        }
    }

    public interface FOLSentence extends FOLNode {

        FOLSentence duplicate();

    }

    public interface FOLTerm extends FOLNode {

        List<FOLTerm> getParameters();

        FOLTerm duplicate();
    }

    public class FOLVariable implements FOLTerm {

        private String value;
        private int hashCode = 0;
        private int indexical = -1;

        public FOLVariable(String s) {
            value = s.trim();
        }

        public FOLVariable(String s, int idx) {
            value = s.trim();
            indexical = idx;
        }

        public String getValue() {
            return value;
        }

        //
        // START-FOLTerm
        public String getNameValue() {
            return getValue();
        }

        public boolean isCmpnd() {
            return false;
        }

        public List<FOLTerm> getParameters() {
            // Is not Compound, therefore should
            // return null for its arguments
            return null;
        }

        public Object accept(FOLVisitor v, Object arg) {
            return v.visitFOLVariable(this, arg);
        }

        public FOLVariable duplicate() {
            return new FOLVariable(value, indexical);
        }

        // END-FOLTerm
        //
        public int getIndexical() {
            return indexical;
        }

        public void setIndexical(int idx) {
            indexical = idx;
            hashCode = 0;
        }

        public String getIndexedValue() {
            return value + indexical;
        }

        @Override
        public boolean equals(Object o) {

            if (this == o) {
                return true;
            }
            if (!(o instanceof FOLVariable)) {
                return false;
            }

            FOLVariable v = (FOLVariable) o;
            return v.getValue().equals(getValue())
                    && v.getIndexical() == getIndexical();
        }

        @Override
        public int hashCode() {
            if (0 == hashCode) {
                hashCode = 17;
                hashCode += indexical;
                hashCode = 37 * hashCode + value.hashCode();
            }

            return hashCode;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public interface FOLVisitor {

        public Object visitFOLPredicate(FOLPredicate p, Object arg);

//	public Object visitFOLTermEquality(FOLTermEquality equality, Object arg);
        public Object visitFOLVariable(FOLVariable variable, Object arg);

        public Object visitFOLConstant(FOLConstant constant, Object arg);

        public Object visitFOLFunction(FOLFunction function, Object arg);

//	public Object visitNotSentence(NotSentence sentence, Object arg);
//	public Object visitConnectedSentence(ConnectedSentence sentence, Object arg);
//	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
//			Object arg);
    }

    public class AbstractFOLVisitor implements FOLVisitor {

        public AbstractFOLVisitor() {
        }

        protected FOLSentence recreate(Object ast) {
            return ((FOLSentence) ast).duplicate();
        }

        public Object visitFOLVariable(FOLVariable variable, Object arg) {
            return variable.duplicate();
        }

//	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
//			Object arg) {
//		List<FOLVariable> variables = new ArrayList<FOLVariable>();
//		for (FOLVariable var : sentence.getFOLVariables()) {
//			variables.add((FOLVariable) var.accept(this, arg));
//		}
//
//		return new QuantifiedSentence(sentence.getQuantifier(), variables,
//				(Sentence) sentence.getQuantified().accept(this, arg));
//	}
        public Object visitFOLPredicate(FOLPredicate predicate, Object arg) {
            List<FOLTerm> terms = predicate.getFOLTerms();
            List<FOLTerm> newFOLTerms = new ArrayList<FOLTerm>();
            for (int i = 0; i < terms.size(); i++) {
                FOLTerm t = terms.get(i);
                FOLTerm subsFOLTerm = (FOLTerm) t.accept(this, arg);
                newFOLTerms.add(subsFOLTerm);
            }
            return new FOLPredicate(predicate.getFOLPredicateName(), newFOLTerms);

        }

//	public Object visitFOLTermEquality(FOLTermEquality equality, Object arg) {
//		FOLTerm newFOLTerm1 = (FOLTerm) equality.getFOLTerm1().accept(this, arg);
//		FOLTerm newFOLTerm2 = (FOLTerm) equality.getFOLTerm2().accept(this, arg);
//		return new FOLTermEquality(newFOLTerm1, newFOLTerm2);
//	}
        public Object visitFOLConstant(FOLConstant constant, Object arg) {
            return constant;
        }

        public Object visitFOLFunction(FOLFunction function, Object arg) {
            List<FOLTerm> terms = function.getFOLTerms();
            List<FOLTerm> newFOLTerms = new ArrayList<FOLTerm>();
            for (int i = 0; i < terms.size(); i++) {
                FOLTerm t = terms.get(i);
                FOLTerm subsFOLTerm = (FOLTerm) t.accept(this, arg);
                newFOLTerms.add(subsFOLTerm);
            }
            return new FOLFunction(function.getFOLFunctionName(), newFOLTerms);
        }

//	public Object visitNotSentence(NotSentence sentence, Object arg) {
//		return new NotSentence((Sentence) sentence.getNegated().accept(this,
//				arg));
//	}
//	public Object visitConnectedSentence(ConnectedSentence sentence, Object arg) {
//		Sentence substFirst = (Sentence) sentence.getFirst().accept(this, arg);
//		Sentence substSecond = (Sentence) sentence.getSecond()
//				.accept(this, arg);
//		return new ConnectedSentence(sentence.getConnector(), substFirst,
//				substSecond);
//	}
    }

    public class FOLSubstrVisitor extends AbstractFOLVisitor {

        public FOLSubstrVisitor() {
        }

        /**
         * Note: Refer to Artificial Intelligence A Modern Approach (3rd
         * Edition): page 323.
         *
         * @param theta a substitution.
         * @param sentence the substitution has been applied to.
         * @return a new FOLSentence representing the result of applying the
         * substitution theta to aSentence.
         *
         */
        public FOLSentence subst(Map<FOLVariable, FOLTerm> theta, FOLSentence sentence) {
            return (FOLSentence) sentence.accept(this, theta);
        }

        public FOLTerm subst(Map<FOLVariable, FOLTerm> theta, FOLTerm aFOLTerm) {
            return (FOLTerm) aFOLTerm.accept(this, theta);
        }

        public FOLFunction subst(Map<FOLVariable, FOLTerm> theta, FOLFunction function) {
            return (FOLFunction) function.accept(this, theta);
        }

//	public Literal subst(Map<FOLVariable, FOLTerm> theta, Literal literal) {
//		return literal.newInstance((FOLAtomicSentence) literal
//				.getFOLAtomicSentence().accept(this, theta));
//	}
        @SuppressWarnings("unchecked")
        @Override
        public Object visitFOLVariable(FOLVariable variable, Object arg) {
            Map<FOLVariable, FOLTerm> substitution = (Map<FOLVariable, FOLTerm>) arg;
            if (substitution.containsKey(variable)) {
                return substitution.get(variable).duplicate();
            }
            return variable.duplicate();
        }

//	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
//			Object arg) {
//
//		Map<FOLVariable, FOLTerm> substitution = (Map<FOLVariable, FOLTerm>) arg;
//
//		Sentence quantified = sentence.getQuantified();
//		Sentence quantifiedAfterSubs = (Sentence) quantified.accept(this, arg);
//
//		List<FOLVariable> variables = new ArrayList<FOLVariable>();
//		for (FOLVariable v : sentence.getFOLVariables()) {
//			FOLTerm st = substitution.get(v);
//			if (null != st) {
//				if (st instanceof FOLVariable) {
//					// Only if it is a variable to I replace it, otherwise
//					// I drop it.
//					variables.add((FOLVariable) st.duplicate());
//				}
//			} else {
//				// No substitution for the quantified variable, so
//				// keep it.
//				variables.add(v.duplicate());
//			}
//		}
//
//		// If not variables remaining on the quantifier, then drop it
//		if (variables.size() == 0) {
//			return quantifiedAfterSubs;
//		}
//
//		return new QuantifiedSentence(sentence.getQuantifier(), variables,
//				quantifiedAfterSubs);
//	}
    }

    public class FOLUnifier {

        //
        private FOLSubstrVisitor _substVisitor = new FOLSubstrVisitor();

        public FOLUnifier() {

        }

        /**
         * Returns a Map<FOLVariable, FOLTerm> representing the substitution (j.e. a
         * set of variable/term pairs) or null which is used to indicate a
         * failure to unify.
         *
         * @param x a variable, constant, list, or compound
         * @param y a variable, constant, list, or compound
         *
         * @return a Map<FOLVariable, FOLTerm> representing the substitution (j.e. a
         * set of variable/term pairs) or null which is used to indicate a
         * failure to unify.
         */
        public Map<FOLVariable, FOLTerm> unify(FOLNode x, FOLNode y) {
            return unify(x, y, new LinkedHashMap<FOLVariable, FOLTerm>());
        }

        /**
         * Returns a Map<FOLVariable, FOLTerm> representing the substitution (j.e. a
         * set of variable/term pairs) or null which is used to indicate a
         * failure to unify.
         *
         * @param x a variable, constant, list, or compound
         * @param y a variable, constant, list, or compound
         * @param theta the substitution built up so far
         *
         * @return a Map<FOLVariable, FOLTerm> representing the substitution (j.e. a
         * set of variable/term pairs) or null which is used to indicate a
         * failure to unify.
         */
        public Map<FOLVariable, FOLTerm> unify(FOLNode x, FOLNode y,
                Map<FOLVariable, FOLTerm> theta) {
            // if theta = failure then return failure
            if (theta == null) {
                return null;
            } else if (x.equals(y)) {
                // else if x = y then return theta
                return theta;
            } else if (x instanceof FOLVariable) {
                // else if VARIABLE?(x) then return UNIVY-VAR(x, y, theta)
                return unifyVar((FOLVariable) x, y, theta);
            } else if (y instanceof FOLVariable) {
                // else if VARIABLE?(y) then return UNIFY-VAR(y, x, theta)
                return unifyVar((FOLVariable) y, x, theta);
            } else if (isCmpnd(x) && isCmpnd(y)) {
                // else if COMPOUND?(x) and COMPOUND?(y) then
                // return UNIFY(x.ARGS, y.ARGS, UNIFY(x.OP, y.OP, theta))
                return unify(args(x), args(y), uSubOps(op(x), op(y), theta));
            } else {
                // else return failure
                return null;
            }
        }

        /**
         * Returns a Map<FOLVariable, FOLTerm> representing the substitution (j.e. a
         * set of variable/term pairs) or null which is used to indicate a
         * failure to unify.
         *
         * @param x a variable, constant, list, or compound
         * @param y a variable, constant, list, or compound
         * @param theta the substitution built up so far
         *
         * @return a Map<FOLVariable, FOLTerm> representing the substitution (j.e. a
         * set of variable/term pairs) or null which is used to indicate a
         * failure to unify.
         */
        // else if LIST?(x) and LIST?(y) then
        // return UNIFY(x.REST, y.REST, UNIFY(x.FIRST, y.FIRST, theta))
        public Map<FOLVariable, FOLTerm> unify(List<? extends FOLNode> x,
                List<? extends FOLNode> y, Map<FOLVariable, FOLTerm> theta) {
            if (theta == null) {
                return null;
            } else if (x.size() != y.size()) {
                return null;
            } else if (x.size() == 0 && y.size() == 0) {
                return theta;
            } else if (x.size() == 1 && y.size() == 1) {
                return unify(x.get(0), y.get(0), theta);
            } else {
                return unify(x.subList(1, x.size()), y.subList(1, y.size()),
                        unify(x.get(0), y.get(0), theta));
            }
        }

        //
        // PROTECTED METHODS
        //
        // Note: You can subclass and override this method in order
        // to re-implement the OCCUR-CHECK?() to always
        // return false if you want that to be the default
        // behavior, as is the case with Prolog.
        // Note: Implementation is based on unify-bug.pdf document by Peter Norvig:
        // http://norvig.com/unify-bug.pdf
        protected boolean occurCheck(Map<FOLVariable, FOLTerm> theta, FOLVariable var,
                FOLNode x) {
            // ((equal var x) t)
            if (var.equals(x)) {
                return true;
                // ((bound? x subst)
            } else if (theta.containsKey(x)) {
                // (occurs-in? var (lookup x subst) subst))
                return occurCheck(theta, var, theta.get(x));
                // ((consp x) (or (occurs-in? var (first x) subst) (occurs-in? var
                // (rest x) subst)))
            } else if (x instanceof FOLFunction) {
                // (or (occurs-in? var (first x) subst) (occurs-in? var (rest x)
                // subst)))
                FOLFunction fx = (FOLFunction) x;
                for (FOLTerm fxt : fx.getParameters()) {
                    if (occurCheck(theta, var, fxt)) {
                        return true;
                    }
                }
            }
            return false;
        }

        //
        // PRIVATE METHODS
        //
        /**
         * <code>
         * function UNIFY-VAR(var, x, theta) returns a substitution
         *   inputs: var, a variable
         *       x, any expression
         *       theta, the substitution built up so far
         * </code>
         */
        private Map<FOLVariable, FOLTerm> unifyVar(FOLVariable var, FOLNode x,
                Map<FOLVariable, FOLTerm> theta) {

            if (!FOLTerm.class.isInstance(x)) {
                return null;
            } else if (theta.keySet().contains(var)) {
                // if {var/val} E theta then return UNIFY(val, x, theta)
                return unify(theta.get(var), x, theta);
            } else if (theta.keySet().contains(x)) {
                // else if {x/val} E theta then return UNIFY(var, val, theta)
                return unify(var, theta.get(x), theta);
            } else if (occurCheck(theta, var, x)) {
                // else if OCCUR-CHECK?(var, x) then return failure
                return null;
            } else {
                // else return add {var/x} to theta
                cascadeSubstitution(theta, var, (FOLTerm) x);
                return theta;
            }
        }

        private Map<FOLVariable, FOLTerm> uSubOps(String x, String y,
                Map<FOLVariable, FOLTerm> theta) {
            if (theta == null) {
                return null;
            } else if (x.equals(y)) {
                return theta;
            } else {
                return null;
            }
        }

        private List<? extends FOLNode> args(FOLNode x) {
            return x.getParameters();
        }

        private String op(FOLNode x) {
            return x.getNameValue();
        }

        private boolean isCmpnd(FOLNode x) {
            return x.isCmpnd();
        }

        // See:
        // http://logic.stanford.edu/classes/cs157/2008/miscellaneous/faq.html#jump165
        // for need for this.
        private Map<FOLVariable, FOLTerm> cascadeSubstitution(Map<FOLVariable, FOLTerm> theta,
                FOLVariable var, FOLTerm x) {
            theta.put(var, x);
            for (FOLVariable v : theta.keySet()) {
                //theta.put(v, _substVisitor.subst(theta, theta.get(v)));
            }
            // Ensure FOLFunction FOLTerms are correctly updates by passing over them
            // again. Fix for testBadCascadeSubstitution_LCL418_1()
            for (FOLVariable v : theta.keySet()) {
                FOLTerm t = theta.get(v);
                if (t instanceof FOLFunction) {
                    //	theta.put(v, _substVisitor.subst(theta, t));
                }
            }
            return theta;
        }
    }

}
