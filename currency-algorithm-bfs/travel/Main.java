/*
 * Main.java
 *
 * By   Patrick Mamaid  A00609663
 *      Kevin Hsieh     A00527530
 *   Date :  12/6/2007
 *
 * This is the main class the "engine" of our solution
 * sams and bllys algorithms are initiated and worked with here
 * but mainly billys algorithm is put here in a method
 *
 *
 */

package travel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {

    ArrayList<String> lines; //contains all the lines from input

    Main(String filename) {

        //read this file now
        lines = readFilesToArrayList(filename);
        ArrayList<String> tosam = readFilesToArrayList(filename);
        checkBillyInputForBilly();
        runSamsProgram(tosam);
    }

    void checkBillyInputForBilly() {
        ArrayList<String> queries = new ArrayList<String>();
        ArrayList<String> input = new ArrayList<String>();
        ArrayList<Node> nodes = new ArrayList<Node>();

        //! 6 cdn = 15 franc
        for (int i = 0; i < lines.size(); i++) {
            String temp = lines.get(i); //pull a line from input file
            String[] temptok = temp.split("\\s"); //split the string by space
            if (temptok[0].equals("!") && !temptok[1].equals("*") && !temptok[1].equals("{")) {
                //input
                //this if statement creates the links
                // temptok[]
                //0  1  2    3   4  5
                //! 47 euro  =   9 lira
                // ---9-->
                // <--47--
                Node a = null;
                Node b = null;
                // 47    9
                int[] lcm = lowestCommonMultiple(Integer.parseInt(temptok[1]), Integer.parseInt(temptok[4]));


                for (int x = 0; x < nodes.size(); x++) {
                    //see if the node to add already exsists
                    if (nodes.get(x).getName().equals(temptok[2])) {
                        a = nodes.get(x); //if it does grab it!!
                        break;
                    }
                }

                for (int x = 0; x < nodes.size(); x++) {
                    if (nodes.get(x).getName().equals(temptok[5])) {
                        b = nodes.get(x);
                        break;
                    }
                }

                if (a == null) {
                    a = new Node(temptok[2]); //euro
                }
                if (b == null) {
                    b = new Node(temptok[5]); //libra
                }


                Edge edgeba = new Edge(lcm[0]); //47
                Edge edgeab = new Edge(lcm[1]); //9
                edgeba.setNode(a); //points to : euro
                edgeab.setNode(b); //points to : libra
                a.setAddEdge(edgeab); //euro ---9--> libra
                b.setAddEdge(edgeba); //libra <--47-- euro
                boolean found = false; //sees if it actually found it
                //add to the list of nodes
                //first check if node exsists:
                for (int x = 0; x < nodes.size(); x++) {
                    //now add or replace that node
                    if (a.getName().equals(nodes.get(x).getName())) {
                        nodes.remove(x); //already a node a so take it out
                        nodes.add(a); //re add it to the list of nodes
                        found = true;
                        break; //get out of loop
                    }
                }
                if (!found) {
                    nodes.add(a);
                }

                found = false;
                //now check if b already exsists in the list of nodes
                for (int x = 0; x < nodes.size(); x++) {
                    //now add or replace that node
                    if (b.getName().equals(nodes.get(x).getName())) {
                        nodes.remove(x); //already a node so take it out
                        nodes.add(b); //re add it to the list of nodes
                        found = true;
                        break; //get out of the loop
                    }
                }
                if (!found) {
                    nodes.add(b);
                }


                //prints edges correctly proceed to next implementation
//                System.out.print(a.getName() + "  ");
//                System.out.print("----" + a.getEdge(0).getWeight() + "--->");
//                System.out.print("  " + b.getName());
//                System.out.println();
            } else if (temptok[0].equals("?") && !temptok[1].equals("*") && !temptok[1].equals("{")) {
                //query
                //? franc = cdn
                queries.add(temp);
                processQueries(nodes, queries);
                queries = new ArrayList<String>();
            }
        }
    }

    void runSamsProgram(ArrayList<String> tinput) {
        SamsList samslist = new SamsList();

//         SAMPLE INPUT
//        ! 6 cdn = 15 franc
//        ! { 0 1 }
//        ! 47 euro = 9 lira
//        ! { 10 13 14 11 18 }
//        ? franc = cdn
//        ? * 11
//        ? cdn = lira
//        ! 2 franc = 1 euro
//        ! { 42 0 }
//        ? lira = cdn
//        ! { 99 1 }
//        ? * 99
//        ? * 9999
        for (int i = 0; i < tinput.size(); i++) {
            String[] temp = tinput.get(i).split("\\s");
            if (temp[0].equals("!") && temp[1].equals("{")) {
                //add to database
                samslist.addToDatabase(tinput.get(i));
            }
            if (temp[0].equals("?") && temp[1].equals("*")) {
                //query
                String temps = samslist.findIntegerSetGetString(Integer.parseInt(temp[2]));
                if (!temps.equals("cannot find")) {
                    System.out.println(temps);
                } else {
                    System.out.println(1 + ": " + temp[2]);
                }
            }
        }
    }

    void processQueries(ArrayList<Node> nodes, ArrayList<String> queries) {
//        System.out.println(nodes.size());
//        for(int i = 0; i < nodes.size(); i ++){
//            System.out.println(nodes.get(i).getName());
//        }
        //lets iterate through our queries
        for (int i = 0; i < queries.size(); i++) {
            //0  1    2  3
            //? franc = cdn
            //start at franc and find cdn
            //result should be
            //5 franc = 2 cdn
            String[] strtok = queries.get(i).split("\\s");

            //find the path now using bfs
            Node start = null;


            boolean found = false;
            for (int x = 0; x < nodes.size(); x++) {
                if (nodes.get(x).getName().equals(strtok[1])) {
                    start = nodes.get(x); //got the franc node
                    found = true;
                    break;
                }
            }
            //process if it is not found
            if (!found) {
                System.out.println("? " + strtok[1] + " = " + strtok[3]);
            }


            //set all nodes to NOT visited
            for (int x = 0; x < nodes.size(); x++) {
                nodes.get(x).visited = false;
            }


            //run the algorithm to find if there is a path
            Node currentPos = start;
            ArrayList<Node> nodeQueue = new ArrayList<Node>();

            nodeQueue.add(start);
            currentPos.visited = true;


            //BFS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //i like bfs.. +9 marks? hehehhehe
            boolean foundPath = false;
            while (nodeQueue.size() > 0) {

                if (currentPos.getName().equals(strtok[3])) {

                    ArrayList<Node> forwardTemp = new ArrayList<Node>();
                    ArrayList<Node> backwardTemp = new ArrayList<Node>();

                    for (int j = 0; j < nodeQueue.size(); j++) {
                        //System.out.print(nodeQueue.get(j).getName());
                        forwardTemp.add(nodeQueue.get(j));
                        backwardTemp.add(nodeQueue.get(nodeQueue.size() - j - 1));
                    }
                    //System.out.println();
                    int forwardCount = 0;
                    for (int j = 0; j < forwardTemp.size() - 1; j++) {
                        for (int k = 0; k < forwardTemp.size(); k++) {
                            if (forwardTemp.get(j).getName().equals(forwardTemp.get(j + 1).getEdge(k).getNode().getName())) {
                                if (forwardCount == 0) {
                                    forwardCount = forwardTemp.get(j).getEdge(k).getWeight();
                                } else {
                                    forwardCount *= forwardTemp.get(j).getEdge(k).getWeight();
                                }
                                break;
                            }
                        }
                    }

                    int backwardCount = 0;
                    for (int j = 0; j < backwardTemp.size() - 1; j++) {
                        for (int k = 0; k < backwardTemp.size(); k++) {
                            if (backwardTemp.get(j).getName().equals(backwardTemp.get(j + 1).getEdge(k).getNode().getName())) {
                                if (backwardCount == 0) {
                                    backwardCount = backwardTemp.get(j).getEdge(k).getWeight();
                                } else {
                                    backwardCount *= backwardTemp.get(j).getEdge(k).getWeight();
                                }
                                break;
                            }
                        }
                    }

                    System.out.println(backwardCount + " " + strtok[1] + " = " + forwardCount + " " + strtok[3]);

                    foundPath = true;
                    break;
                }

                boolean hasNeighbour = false;
                for (int x = 0; x < currentPos.getEdges().size(); x++) {
                    if (currentPos.getEdge(x).getNode().visited != true) {
                        currentPos = currentPos.getEdge(x).getNode();
                        currentPos.visited = true;
                        nodeQueue.add(currentPos);
                        hasNeighbour = true;
                        break;
                    }
                }

                if (!hasNeighbour) {
                    //jump back if no neighbours found
                    nodeQueue.remove(nodeQueue.size() - 1);
                    if (nodeQueue.size() > 0) {
                        currentPos = nodeQueue.get(nodeQueue.size() - 1);
                    }
                }
            }

            if (!foundPath) {
                System.out.println("? " + strtok[1] + " ? " + strtok[3]);
            }
        }
    }

    ArrayList<String> readFilesToArrayList(String filename) {

        ArrayList<String> temp = new ArrayList<String>();
        try {
            FileReader input = new FileReader(filename);
            BufferedReader bufRead = new BufferedReader(input);
            String line; // String that holds current file line
            line = bufRead.readLine();
            while (line != null) {
                temp.add(line);
                line = bufRead.readLine();
            }
            bufRead.close();
        } catch (Exception e) {
            System.out.println("uh oh something is wrong.. whats going on?");
        }

        return temp;
    }

    int[] lowestCommonMultiple(int a, int b) {
        int[] result = new int[2];

        result[0] = a;
        result[1] = b;

        for (int i = 2; i < a * b; i++) {
            //a*b is upper bound
            if ((a % i == 0) && (b % i == 0)) {
                //System.out.println(a/i+"   "+b/i);//to test output of lcm
                result[0] = a / i;
                result[1] = b / i;
                break;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        new Main(args[0]);
    }
}