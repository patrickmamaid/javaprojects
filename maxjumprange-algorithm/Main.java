/*
 *  Main.java
 *  Assignment 2 (freddy needs to find a path to fiona using least # of hops)
 *      algorithms used: BFS  &  GREEDY algos
 *  Author:
 *      Patrick Mamaid,  Kevin Hsieh
 */


package frog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;



public class Main {

    ArrayList<ArrayList<String>> cases = new ArrayList<ArrayList<String>>();

    //this constructor is the entrypoint of the program
    //opens the file
    //and if it does open, runs the algorithm
    Main(String filename) throws FileNotFoundException, IOException {
        if (openfile(filename)) {
            initAlgorithm();
        }
    }

    //opens a file returns false and prints message if it cannot open
    boolean openfile(String filename) throws FileNotFoundException, IOException {
        ArrayList<String> lines = new ArrayList<String>();
        String line = "";
        File testFile = new File(filename);
        BufferedReader input = null;
        boolean caseOpen = false;
        boolean result = false;

        try {
            input = new BufferedReader(new FileReader(testFile));
            result = true;
        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("cannot open input file.. usage: java [program name] [filename.txt]");
            return false;
        }

        while ((line = input.readLine()) != null) {
            //adds everything into arraylist
            //lines.add(line);
            String[] getnumlines = line.split("\\s");
            if (getnumlines.length == 1 && !line.equals("")) {
                caseOpen = !caseOpen; //flips switch
                if (caseOpen == false) {
                    lines.add(line);
                    cases.add(lines);
                    lines = new ArrayList<String>();
                } else {
                    lines.add(line);
                }
            } else {
                if (!line.equals("")) {
                    lines.add(line);
                }
            }
        }
        //cannot catch end of file so i will add the line here
        cases.add(lines);

        return result;
    }

    //method returns x coordinate for stone at index stonenum in casenum
    int getx(int stoneNum, int caseNum) {
        //returns the x coord of a selected case and stone
        int x = 0;
        String temp = cases.get(caseNum).get(stoneNum + 1);
        String[] temp2 = temp.split("\\s");
        x = Integer.parseInt(temp2[0]);
        return x;
    }

    //method returns y coordinate for stone at index stonenum in casenum
    int gety(int stoneNum, int caseNum) {
        //returns the y coord of a selected case and stone
        int y = 0;
        String temp = cases.get(caseNum).get(stoneNum + 1);
        String[] temp2 = temp.split("\\s");
        y = Integer.parseInt(temp2[1]);
        return y;
    }

    //method returns number of stones for case
    int getnumstones(int caseNum) {

        int result = Integer.parseInt(cases.get(caseNum).get(0));
        // System.out.println("reporting # of stones: " + result);
        return result;
    }

    //method gets maximum distance for secified caseNum
    int getmaxDistance(int caseNum) {
        int result = Integer.parseInt(cases.get(caseNum).get(cases.get(caseNum).size() - 1));
        //System.out.println("reporting max distance #: "  + result);
        return result;
    }

    //returns number of cases
    int getnumcases() {
        int i = cases.size() - 1;
        //System.out.println("reporting # of cases: " + i);
        return i;
    }

    //whenever greedy algorithm fails we will run bfs to attempt to reach fiona
    void attemptBfs(int caseNum) {

        System.out.print("case " + (caseNum + 1) + " :");

        //initialize all stones for BFS
        Stone[] stones = new Stone[getnumstones(caseNum)];
        for (int i = 0; i < getnumstones(caseNum); i++) {
            stones[i] = new Stone(getx(i, caseNum), gety(i, caseNum), i + 1);
            stones[i].visited = false;
        }
        //at this point stones have been registered properly
        //begin algorithm:
        ArrayList<Stone> stoneQueue = new ArrayList<Stone>();
        stoneQueue.add(stones[0]);
        Stone fionaStone = stones[1];
        boolean done = false;
        Stone currentStone = stones[0];
        currentStone.visited = true;
        
        //initiate loop
        //check if surrounding stones are reachable
        while (!done) {
            boolean foundNeighbour = false;
            for (int i = 0; i < getnumstones(caseNum); i++) {
                //compares current stone to next i stone to see if it will reach it
                //finds neighbours
                if (!currentStone.equals(stones[i]) && !stones[i].visited && isStoneObjectIsReachable(currentStone, stones[i], getmaxDistance(caseNum))) {
                    //add to queue and break
                    stones[i].visited = true; //sets current stone as visited
                    stoneQueue.add(stones[i]); //adds to the queue
                    foundNeighbour = true;
                    break;
                }
            }
            //no neighbour found
            if (!foundNeighbour) {
                //jump back
                //pop off a stone from the queue
                stoneQueue.remove(stoneQueue.size() - 1); //pops a stone from the queue
                if (stoneQueue.size() != 0) {
                    currentStone = stoneQueue.get(stoneQueue.size() - 1);
                } else {
                    //no more stones end of bfs
                    System.out.print(" Freddy cannot reach Fiona");
                    break;
                }
            } else {
                currentStone = stoneQueue.get(stoneQueue.size() - 1);
                if (currentStone.equals(fionaStone)) {
                    //found link to fiona
                    //send out link to the method to figure out minimum distance
                    minimizeDistanceAndPrint(stoneQueue, getmaxDistance(caseNum));
                    break;
                }
            }
        }
        //end loop
        //end of case
        System.out.println();
    }

    //this method works in conjunction with our BFS algorithm
    //when the BFS finds a path to fiona 
    //the queue will be passed here, attepmts to see if it can minimize
    //# of hops to fiona with the current queue
    void minimizeDistanceAndPrint(ArrayList<Stone> stones, double maxDistance) {
        ArrayList<Stone> minDistance = new ArrayList<Stone>();


        Stone currentStone = stones.get(0);
        //minDistance.add(currentStone);
        int currentStoneIndex = 0;


        while (true) {
            //if the current stone is the end stone print the mindistance list
            if (currentStone.equals(stones.get(stones.size() - 1))) {
                minDistance.add(currentStone);
                for (int i = 0; i < minDistance.size(); i++) {
                    System.out.print(" " + minDistance.get(i).getStoneName());
                }
                break;
            }
            for (int i = stones.size() - 1; i > currentStoneIndex; i--) {
                //iterating through stones backwards
                //this stone at i can reach current stone
                if (isStoneObjectIsReachable(currentStone, stones.get(i), maxDistance)) {
                    minDistance.add(currentStone);
                    currentStone = stones.get(i);
                    break;
                }
            }
        }
    }

    //this is method 1 of the algorithm engine.
    //our idea is to use the greedy method algorithm first
    //it would jump to a stone which is as close to fiona as possible
    //if it gets stuck it will use BFS
    void initAlgorithm() {

        int numberOfCases = getnumcases();


        for (int currentCase = 0; currentCase < numberOfCases; currentCase++) {
            int n = getnumstones(currentCase);

            if (n > 0 && n < 1000) {
                //n is within range allow program to run current case
            } else {
                //n is not within range. skip this case
                continue;
            }


            Stone[] stone = new Stone[n];
            for (int i = 0; i < n; i++) {
                stone[i] = new Stone(getx(i, currentCase), gety(i, currentCase), i + 1);
            }

            //stone 0 is freddy
            //stone 1 is fiona
            //make sure all the other stones have the distance to fiona
            //make sure fred checks if stone is reachable
            for (int i = 0; i < n; i++) {
                //sets the distance to fiona
                stone[i].setDistanceToFiona(calculateDistance(stone[i], stone[1]));
            }


            Stone currentStone = stone[0];
            Stone tempStone = null;
            double maxdistance = getmaxDistance(currentCase);
            ArrayList<String> stoneVisited = new ArrayList<String>();
            ArrayList<Stone> stoneStack = new ArrayList<Stone>();
            boolean done = false;
            //third  get to a stone that is close to fiona and is reachable
            stoneVisited.add("" + 1); //adds freds stone to stones visited
            // Stone previousStone = stone[0];
            while (done == false) {

                int counter = 0;
                if (isStoneObjectIsReachable(currentStone, stone[1], maxdistance)) {
                    //current stone can reach fiona
                    //stoneVisited.add("" + currentStone.getStoneName());//adds the current stone
                    stoneVisited.add("" + 2); // this is fionas stone
                    System.out.print("case " + (currentCase + 1) + " :");

                    for (int i = 0; i < stoneVisited.size(); i++) {
                        //stones visited includes current stone
                        //excludes fionas stone and freds stone
                        //print out all visited stones
                        System.out.print(" " + stoneVisited.get(i));
                    }
                    System.out.println();

                    done = true;
                    break;
                }

                //otherwise iterate though the stones
                //flush stonestack
                stoneStack = new ArrayList<Stone>();
                for (int i = 0; i < n; i++) {
                    //looks around for a stone to jump to
                    //iterates through 3 stones
                    //fred cannot go back to beggining stone
                    if ((!currentStone.equals(stone[i])) && isStoneObjectIsReachable(currentStone, stone[i], maxdistance)) {
                        //surrounding stone is reachable
                        stoneStack.add(stone[i]);
                        counter++;
                    }
                }
                if (stoneStack.size() == 0 || counter == 0) {
                    System.out.println("case " + currentCase + " : cannot reach fiona :(");
                    done = true;
                    break;
                }
                //flush mindistance stone
                Stone minDistanceStone = stoneStack.get(0);
                for (int i = 0; i < stoneStack.size(); i++) {
                    if (stoneStack.get(i).getDistanceToFiona() < minDistanceStone.getDistanceToFiona()) {
                        //stone is smaller than min distance stone
                        //min distance stone is the stone closest to fiona
                        //set this to the min stone
                        minDistanceStone = stoneStack.get(i);
                    }
                }
                if (minDistanceStone.getDistanceToFiona() > currentStone.getDistanceToFiona()) {
                    //System.out.println("case " + currentCase + " : cannot reach fiona :(");
                    attemptBfs(currentCase);
                    done = true;
                    break;
                }
                //previousStone = currentStone;
                currentStone = minDistanceStone;
                stoneVisited.add("" + currentStone.getStoneName());
            }
        }
    }

    //calculates distance using src xy and dest xy
    double calculateDistance(int x1, int y1, int x2, int y2) {
        double result = 0;
        int tx = Math.abs(x1 - x2);
        int ty = Math.abs(y1 - y2);

        //sqare root of( side1^2 +  side2^2)
        //equals triangular distance.
        tx *= tx;
        ty *= ty;

        result = Math.sqrt(tx + ty);

        return result;
    }

    //calculates distance using src STONE and dest STONE
    double calculateDistance(Stone a, Stone b) {
        double result = calculateDistance(a.getX(), a.getY(), b.getX(), b.getY());
        // System.out.println("reporting distance calculation of: "+ a.getStoneName()+" and "+b.getStoneName()+" "+ + result);
        return result;
    }

    //checks if stone is reachable usng src xy and dest xy
    boolean isStoneReachable(int x1, int y1, int x2, int y2, double MAXDISTANCE) {
        boolean result = false;

        double curdistance = calculateDistance(x1, y1, x2, y2);
        if (MAXDISTANCE >= curdistance) {
            result = true;
        }
        return result;
    }

    //checks if stone is reachable using src stone and dest stone
    boolean isStoneObjectIsReachable(Stone a, Stone b, double MAXDISTANCE) {
        boolean result = isStoneReachable(a.getX(), a.getY(), b.getX(), b.getY(), MAXDISTANCE);
        return result;
    }

    //main function will be used to pass the filename
    public static void main(String[] args) throws FileNotFoundException, IOException {
        if(args.length > 0){
            new Main(args[0]);
        }else{
            System.err.println("no input file specified.. usage: java [program name] [filename.txt]");
        }
    }
}