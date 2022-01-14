/*
 * SamsList.java
 *
 * By   Patrick Mamaid  A00609663
 *      Kevin Hsieh     A00527530
 *   Date :  12/6/2007
 *
 * this is the main section of the samslist algorithm
 * this is were we handle & process input & retrieve queries and
 * inputs to our datastructure.
 *
 */

package travel;

import java.util.ArrayList;

public class SamsList {

    private ArrayList<ArrayList<Integer>> database = null;

    SamsList() {
        database = new ArrayList<ArrayList<Integer>>();
    }

    public boolean addToDatabase(String inputLine) {
        boolean result = false;
        String[] tempLine = inputLine.split("\\s");
        ArrayList<Integer> capturedInts = new ArrayList<Integer>();

        //captures ints from inputline to cacpturedints
        for (int i = 0; i < tempLine.length; i++) {
            if (!tempLine[i].equals("*") && !tempLine[i].equals("!") && !tempLine[i].equals("?") && !tempLine[i].equals("{") && !tempLine[i].equals("}")) {
                capturedInts.add(Integer.parseInt(tempLine[i]));
            }
        }

        //check if it is in database first
        //if it is pull it out
        //loop captured ints
        int x = -1;
        int y = -1;
        boolean foundInDatabase = false;
        for (int i = 0; i < capturedInts.size(); i++) {

            x = findDatabaseIndex(capturedInts.get(i));
            y = findIntegerListIndex(capturedInts.get(i));
            if (x != -1 && y != -1) {
                foundInDatabase = true;
                break;
            }
        }
        //found it so pull it out
        ArrayList<Integer> tempIntegers;
        if (foundInDatabase) {
            tempIntegers = database.get(x);
            database.remove(x);
            //loop captured ints
            for (int i = 0; i < capturedInts.size(); i++) {
                int capTemp = capturedInts.get(i);
                //check if num already in this list
                if (!checkIfInArrayList(tempIntegers, capTemp)) {
                    tempIntegers.add(capTemp); //captemp
                    result = true;
                }
            }
            //sort it first
            tempIntegers = sortArrayList(tempIntegers);
            //read to database
            database.add(tempIntegers);
        } else {
            tempIntegers = new ArrayList<Integer>();
            for (int i = 0; i < capturedInts.size(); i++) {
                tempIntegers.add(capturedInts.get(i));
            }
            database.add(tempIntegers);
            result = true;
        }



        return result;
    }

    private boolean checkIfInArrayList(ArrayList<Integer> a, int comp) {

        for (int i = 0; i < a.size(); i++) {
            if (a.get(i) == comp) {
                return true;
            }
        }

        return false;
    }

    private ArrayList<Integer> sortArrayList(ArrayList<Integer> a) {
        int[] b = new int[a.size()];
        for (int i = 0; i < a.size(); i++) {
            b[i] = a.get(i);
        }

        //perform insertiionsort
        for (int i = 0; i < b.length; i++) {
            int value = b[i];
            int j = i - 1;
            while (j >= 0 && b[j] > value) {
                b[j + 1] = b[j];
                j = j - 1;
            }
            b[j + 1] = value;
        }
        //now it is sorted
        a = new ArrayList<Integer>();

        for (int i = 0; i < b.length; i++) {
            a.add(b[i]);
        }


        return a;
    }

    private int findDatabaseIndex(int findthis) {
        //find in database & integersset of databaes
        for (int i = 0; i < database.size(); i++) {
            for (int j = 0; j < database.get(i).size(); j++) {
                if (findthis == database.get(i).get(j)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int findIntegerListIndex(int findthis) {
        //find in database & integersset of databaes
        for (int i = 0; i < database.size(); i++) {
            for (int j = 0; j < database.get(i).size(); j++) {
                if (findthis == database.get(i).get(j)) {
                    return j;
                }
            }
        }
        return -1;
    }

    public String findIntegerSetGetString(int findThisInt) {
        String returnval = "cannot find";
        int dblocation = findDatabaseIndex(findThisInt);


        if (dblocation != -1) {
            //can find it
            //process it now
            returnval = findThisInt + ": ";
            ArrayList<Integer> thingsFound = new ArrayList<Integer>();

            for (int i = 0; i < database.get(dblocation).size(); i++) {
                if (database.get(dblocation).get(i) != findThisInt) {
                    //5: 10 11 13 14 18
                    thingsFound.add(database.get(dblocation).get(i));
                }
            }

            thingsFound = sortArrayList(thingsFound);
            //sort arraylist at the end before processing it

            returnval = (thingsFound.size() + 1) + ":";

            for (int i = 0; i < thingsFound.size(); i++) {
                if (!thingsFound.get(i).equals(findThisInt)) {
                    //append to string
                    returnval = returnval.concat(" " + thingsFound.get(i));
                }
            }
        }
        return returnval;
    }
}