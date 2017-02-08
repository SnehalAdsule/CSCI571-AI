/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import java.util.*;

/**
 *
 * @author Sneh
 */
public class Test {

    public class Node {

        String name;
        int cost;

        public Node(String name, int cost) {
            this.name = name;
            this.cost = cost;
        }
    }

    public static void main(String[] args) {
        Test t = new Test();
        List<Node> list;

        list = new ArrayList();

        list.add(t.new Node("CZ", 1));
        list.add(t.new Node("A1", 2));
        list.add(t.new Node("C2", 3));
        list.add(t.new Node("EB", 4));
        list.add(t.new Node("CB", 1));
        list.add(t.new Node("DB", 2));

        System.out.println("\n");
        Collections.sort(list, new Comparator<Node>() {
            public int compare(Node a1, Node a2) {
                return a1.cost < a2.cost ? -1 : (a1.cost == a2.cost ? (a1.name.compareToIgnoreCase(a2.name)) : 1);
            }
        });
        Collections.reverse(list);
        for (Node a : list) {
            System.out.print(a.name + ":" + a.cost + "\t");

        }
         /* inputFile = "./";
            File dir = new File(inputFile);
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    System.out.println("directory:" + file.getCanonicalPath());

                } else {
                    System.out.println("     file:" + file.getCanonicalPath());
                }
            }
            */
    }
}
