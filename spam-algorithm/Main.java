/*
 * Patrick Mamaid A00609663
 * Set :: 3F
 * Assignment 1
 * Main.java
 *
 * Created on Oct 18, 2007, 2:42:07 PM
 *
 */

package Spam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patrick
 */
public class Main {

    Main(String fileName) {
        run(fileName);
    }

    public void run(String filename) {
        ArrayList<String> lines = new ArrayList<String>();
        ArrayList<String> permutations = new ArrayList<String>();

        lines = readFileLineByLine(filename); //gets an arraylist of lines captured by
        //the readline function
//        for (int i = 0; i < lines.size(); i++) {
//            System.out.println(lines.get(i));
//        }
        //validate lines
        if (!lineValidation(lines)) {
            System.err.println("line validation failed");
            System.exit(-1);
        }

        //process all lines
        processAllLines(lines);
    }

    public void processAllLines(ArrayList<String> lines) {
        //ArrayList of liness
        ArrayList<String> combinations = new ArrayList<String>(); //will store the permutations
        int a = 0; //spam lines size
        int b = 0; //input line size
        int aCounter = 0; //spam lines traversal count
        int bCounter = 0; //input lines traversal count
        int abSwitcher = 1; //line check switch 1for a 2for b
        //line.get(i) is the current line i list it here because it is not obvious
        for (int i = 0; i < lines.size(); i++) {
            //traversing each line
            String[] temp = lines.get(i).split("\\s"); //take a line and tokenize it
            if (i == 0) {
                //start check if its first line
                //this is the first line
                a = Integer.parseInt(temp[0]); //sets max a
                b = Integer.parseInt(temp[1]); //sets max b
            } //end first line
            if (i > 0) {
                //the following next lines after 0
                switch (abSwitcher) {
                //switches checking mode from a and b lines
                    case 1:
                        //for a lines
                        if (aCounter < b) {
                            //we are checking a lines
                            String[] splitLine = lines.get(i).split("\\s");
                            ArrayList<String> atemp = new ArrayList<String>(); //this will go into permutation function
                            for (int ai = 0; ai < splitLine.length - 1; ai++) {
                                //adding strings to splitLine -1 because we dont include the number at the end
                                atemp.add(splitLine[ai]);
                            }
                            ArrayList<String> btemp = new ArrayList<String>(); //btemp contains permutations of atemp
                            btemp = permuteStringsFunction(atemp); //passes atemp to permutation function stores into btemp
                            combinations.add(splitLine[splitLine.length - 1]); //adds the weight # of the spam
                            for (int ai = 0; ai < btemp.size(); ai++) {
                                combinations.add(btemp.get(ai)); //retrives all combinations to btemp
                            }


                            aCounter++; //retrieve next line
                        } else {
                            abSwitcher = 2; //switch to b after we are done with spam lines
                        }
                        break;
                    case 2:
                        int count = 0; //# of times combination was found
                        int tally = 0; //
                        int multiplier = 0; //the weight of the combination
                        //for b lines
                        if (bCounter < b) {
                            //checking input lines
                            //we are checking a lines
                            String processSingleLine = lines.get(i); //put the current line in a variable to process
                            for (int j = 0; j < processSingleLine.length(); j++) {

                                if (processSingleLine.charAt(0) == ':') {
                                    //caught a line skip :
                                    break; //skipping bCounter++
                                } else {
                                    //process line
                                    multiplier = 0;
                                    count = 0;
                                    for (int cmb = 0; cmb < combinations.size(); cmb++) {
                                        //for # combinations
                                        //for each combinations check the line
                                        if (!hasInteger(combinations.get(cmb))) {
                                            processSingleLine = processSingleLine.toLowerCase();
                                            String noSpaces = removeLineSeparators(processSingleLine);
                                            int numtimes = bruteForceSearch(combinations.get(cmb), noSpaces);
                                            count += numtimes;
                                            tally += count * multiplier;
//                                            System.out.println(combinations.get(cmb)+":"+noSpaces+":"+count+":"+tally+":"+multiplier);
                                            multiplier = 0;
                                        } else {
//                                            tally up
                                            multiplier = Integer.parseInt(combinations.get(cmb));
//                                            System.out.println("multiplier: "+multiplier);
//                                            tally += count * multiplier;
//                                            // System.out.println(multiplier);
//                                            // System.out.println(count);
//                                            multiplier = 0;
//                                            count = 0;
                                        }
                                    }
                                }
                            }

                            if (processSingleLine.charAt(0) != ':') {
                                bCounter++; //gets next line
                                System.out.println("----------------------------------------------------------------------------------------------:" + processSingleLine + tally);
                                tally = 0;
                            }
                        } else {
                            //done with input lines
                            i = lines.size() + 1; //force end scanning
                        }
                        break;
                    default:
                        //should not get here
                        System.err.println("WARNING UNKNOWN ERROR!!!");
                        break;
                }
            }
        } //end line traversal
    }

    //http://www.concentric.net/~Ttwang/tech/stringscan.htm
    public int bruteForceSearch(String pattern, String source) {
        int result = 0;
        boolean match = false;
        for (int i = 0; i <= source.length() - pattern.length(); i++) {
            if (pattern.charAt(0) == source.charAt(i)) {
                for (int j = 0; j < pattern.length(); j++) {
                    if (pattern.charAt(j) != source.charAt(i + j)) {
                        match = false;
                        break;
                    } else {
                        match = true;
                    }
                }
                if (match == true) {
                    result++; //all matched
                    match = false;
                }
                match = false;
            }
        }
        return result;
    }

    public boolean hasInteger(String a) {
        boolean result = false;

        for (int i = 0; i < a.length(); i++) {
            int ascii = (int) a.charAt(i);
            if ((ascii >= 48) && (ascii <= 57)) {
                result = true;
                break;
            }
        }


        return result;
    }

    public String removeLineSeparators(String anyCaseString) {
        String lowerCaseString = anyCaseString.toLowerCase(); //converts to lowercase
        //remove separators: anything that isnt ASCII 33-42 & 97-122
        String stringWithoutSeparators = ""; //the string that contains no separators
        for (int j = 0; j < lowerCaseString.length(); j++) {
            int ascii = (int) lowerCaseString.charAt(j);
            if (((ascii >= 33) && (ascii <= 42)) || ((ascii >= 97) && (ascii <= 122))) {
                stringWithoutSeparators = stringWithoutSeparators.concat("" + lowerCaseString.charAt(j)); //append to temp variable
            }
        } //finished removing separators
        return stringWithoutSeparators;
    }

    public boolean lineValidation(ArrayList<String> line) {
        boolean result = true;
        int a = 0; //spam lines size
        int b = 0; //input line size
        int aCounter = 0; //spam lines traversal count
        int bCounter = 0; //input lines traversal count
        int abSwitcher = 1; //line check switch 1for a 2for b
        //line.get(i) is the current line i list it here because it is not obvious
        for (int i = 0; i < line.size(); i++) {
            //traversing each line
            String[] temp = line.get(i).split("\\s"); //take a line and tokenize it
            if (i == 0) {
                //start check if its first line
                //this is the first line
                //process ints a and b and check if they positive ints n < 1000
                if (temp.length != 2) {
                    System.err.println("did not find a & b on the first line or # spam lines/ lines are not within range");
                    System.err.println("make sure the first line of input file is 2 integers  0 < n < 1000");
                    result = false; // could not find a and b on line 0
                    break; //stop method and return false
                }
                a = Integer.parseInt(temp[0]); //sets max a
                b = Integer.parseInt(temp[1]); //sets max b
                if (((a > 1000) && (a <= 0)) && ((b > 1000) && (b <= 0))) {
                    System.err.println("did not pass line validation");
                    result = false; //a & b did not pass range test
                    break; //stop method and return false
                }
            } //end check first line
            if (i > 0) {
                //the following next lines after 0
                switch (abSwitcher) {
                //switches checking mode from a and b lines
                    case 1:
                        //for a lines
                        if (aCounter < b) {
                            //we are checking a lines
                            String processSingleLine = line.get(i); //put the current line in a variable to process
                            for (int j = 0; j < processSingleLine.length(); j++) {
                                //scan for each character
                                //if statement checks if:
                                //the current characters in the line are within
                                //ASCII 33-42  ASCII 97-122 ASCII 48-57 ASCII 32
                                int curAscii = (int) processSingleLine.charAt(j); // converts to int
                                if (((curAscii >= 48) && (curAscii <= 57)) || ((curAscii >= 33) && (curAscii <= 42)) || ((curAscii >= 97) && (curAscii <= 122)) || (curAscii == 32)) {
                                    //everything is fine
                                } else {
                                    //it is not within range
                                    System.err.println("ERROR Spam lines are not within range: " + curAscii + "\tchar: " + processSingleLine.charAt(j));
                                    result = false;
                                }
                            }
                            aCounter++; //retrieve next line
                        } else {
                            abSwitcher = 2; //switch to b after we are done with spam lines
                        }
                        break;
                    case 2:
                        //for b lines
                        if (bCounter < b) {
                            //checking input lines
                            //we are checking a lines
                            String processSingleLine = line.get(i); //put the current line in a variable to process
                            for (int j = 0; j < processSingleLine.length(); j++) {
                                //scan for each character
                                if (processSingleLine.charAt(0) == ':') {
                                    //caught a line skip :
                                    break; //skipping bCounter++
                                }
                                int curAscii = (int) processSingleLine.charAt(j); //convert to ascii number
                                //if statement checks if chars are within ASCII 33-127 & ASCII 32-whitespace
                                if ((curAscii >= 33) && (curAscii <= 127) || (curAscii == 32)) {
                                    //everything is fine
                                } else {
                                    //it is not within range
                                    System.err.println("ERROR Input lines are not within range: " + curAscii + "\tchar: " + processSingleLine.charAt(j));
                                    result = false;
                                }
                            }
                            bCounter++;
                        } else {
                            //done with input lines
                            i = line.size() + 1; //force end scanning
                        }
                        break;
                    default:
                        //should not get here
                        System.err.println("WARNING NOT SCANNING INPUT AND OR SPAM LINES");
                        result = false;
                        break;
                }
            }
        } //end line traversal
        return result;
    }

    public ArrayList<String> readFileLineByLine(String filename) {

        BufferedReader input = null;
        ArrayList<String> list = new ArrayList<String>();
        String line;
        StringBuffer contents = new StringBuffer();
        try {
            input = new BufferedReader(new FileReader(filename));
            while ((line = input.readLine()) != null) {
                list.add(line);
            }
            input.close();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("ERROR CANT OPEN FILE: file could be in use. ");
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("ERROR CANNOT CLOSE FILE");
            }
        }
        return list;
    }

    //permutation function
    //thanks to http://www.geocities.com/permute_it/
    public ArrayList<String> permuteStringsFunction(ArrayList<String> a) {
        ArrayList<String> result = new ArrayList<String>();

        String atemp = ""; //a will be inserted into result we do this otherwise the combo a wont be listed
        for (int i = 0; i < a.size(); i++) {
            atemp = atemp.concat(a.get(i));
        }
        result.add(atemp); //adds to result
        int N = a.size();
        int[] p = new int[N];
        for (int i = 0; i < N; i++) {
            //generates all p[i] = 0
            p[i] = 0; // i is the length of a
        }
        int i = 0;
        int j = 0;
        while (i < N) {
            if (p[i] < i) {
                if ((i % 2) != 0) {
                    //i = odd
                    j = p[i];
                } else {
                    j = 0;
                }
                String temp = a.get(j); //swap a[i] a[j]
                a.set(j, a.get(i));
                a.set(i, temp); //end swap a[i] a[j]
                p[i] = p[i] + 1;
                i = 1;
                //permutations successful
                String btemp = ""; //reset temp variable for use
                for (int x = 0; x < N; x++) {
                    btemp = btemp.concat(a.get(x));
                }
                result.add(btemp);
                //permutations saved
            } else {
                p[i] = 0;
                ++i;
            }
        }
        return result;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 0) {
            String fname = args[0];
            System.out.println(fname);
            new Main(fname);
        } else {
            System.out.println("please enter the filename as an argument.");
        }
    }
}