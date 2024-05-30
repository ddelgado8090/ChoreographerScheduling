import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static int recursionCount;
    // Numerated availabilities
    public static List<ArrayList<Integer>> numConflicts = new ArrayList<>(1);

    public static Map<Choreographer, ArrayList<Integer>> timeDomains = new HashMap<Choreographer, ArrayList<Integer>>();
    public static Map<Choreographer, ArrayList<Choreographer>> neighbors = new HashMap<Choreographer, ArrayList<Choreographer>>();
    public static Map<Choreographer, Integer> finalVals = new HashMap<Choreographer, Integer>();

    public static ArrayList<Choreographer> listOfChoreographers = new ArrayList<Choreographer>();//list of choreographers
    public static Queue<Arc> globalQueue = new LinkedList<Arc>();
    public static int solvedCounter = 0;


    public static void main(String[] args) throws IOException {


        //Retrieve choreographer conflict csv
        List<List<String>> conflicts = new ArrayList<>();

        Scanner scan = new Scanner(System.in);

        System.out.println("Which file would you like to use?");

        String fileName = scan.nextLine();


        // Read csv and turn response into array
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                values = Arrays.stream(values).
                        map(String::trim).
                        filter(s -> !s.isEmpty()).
                        toArray(String[]::new);
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].replaceAll("^\"|\"$", "");
                }
                conflicts.add(Arrays.asList(values));
            }
        }


        // Removes questions
        conflicts.remove(0);

        // Adds empty array lists to numConflicts
        for (int i = 0; i < conflicts.size(); i++) {
            numConflicts.add(new ArrayList<>());
        }

        Random call = new Random();

        // Pre-process times as integers
        for (int i = 0; i < conflicts.size(); i++) {
            List<String> choreographerAvailabilities = conflicts.get(i);
            for (int j = 0; j < choreographerAvailabilities.size(); j++) {
                String availability = choreographerAvailabilities.get(j);
                switch (availability) {
                    case "Monday 7-8PM" -> {
                        numConflicts.get(i).add(1);
                    }
                    case "Monday 8-9PM" -> {
                        numConflicts.get(i).add(2);
                    }
                    case "Monday 9-10PM" -> {
                        numConflicts.get(i).add(3);
                    }
                    case "Tuesday 7-8PM" -> {
                        numConflicts.get(i).add(4);
                    }
                    case "Tuesday 8-9PM" -> {
                        numConflicts.get(i).add(5);
                    }
                    case "Tuesday 9-10PM" -> {
                        numConflicts.get(i).add(6);
                    }
                    case "Wednesday 7-8PM" -> {
                        numConflicts.get(i).add(7);
                    }
                    case "Wednesday 8-9PM" -> {
                        numConflicts.get(i).add(8);
                    }
                    case "Wednesday 9-10PM" -> {
                        numConflicts.get(i).add(9);
                    }
                    case "Thursday 7-8PM" -> {
                        numConflicts.get(i).add(10);
                    }
                    case "Thursday 8-9PM" -> {
                        numConflicts.get(i).add(11);
                    }
                    case "Thursday 9-10PM" -> {
                        numConflicts.get(i).add(12);
                    }
                    case "Friday 2-3PM" -> {
                        numConflicts.get(i).add(13);
                    }
                    case "Friday 3-4PM" -> {
                        numConflicts.get(i).add(14);
                    }
                    case "Friday 4-5PM" -> {
                        numConflicts.get(i).add(15);
                    }
                    case "Sunday 2-3PM" -> {
                        numConflicts.get(i).add(16);
                    }
                    case "Sunday 3-4PM" -> {
                        numConflicts.get(i).add(17);
                    }
                    case "Sunday 4-5PM" -> {
                        numConflicts.get(i).add(18);
                    }
                }
            }

            Collections.shuffle(numConflicts.get(i)); // Randomize the constraint times

            Choreographer newChoreographer = new Choreographer(-1, numConflicts.get(i), conflicts.get(i).get(conflicts.get(i).size() - 1));
            listOfChoreographers.add(newChoreographer);
        }

        // No need to ask the number of choreographers -- based off csv
        // Choose which algorithm to runs
        System.out.println("Which algorithm would you like? (1) AC-3 & Backtracking & Most Constrained Variable or (2) Greedy Algorithm");
        int algInput = scan.nextInt();

        if(algInput == 1){
            AC3Init();
            printPractice();
            System.out.println();
        } else if (algInput == 2) {
            greedyAlgorithm();
            printPractice();
            System.out.println();
        } else {
            System.out.println("Oops! That is not an option!");
        }
        scan.close();
    }

    private static void printPractice() {
        for (Choreographer choreographer : finalVals.keySet()) {
            String key = choreographer.getName();
            String value = finalVals.get(choreographer).toString();
            String value1 = "";
            if (value.equals("1")){
                value1 = "Mondays 7-8pm";
            } else if (value.equals("2")){
                value1 = "Mondays 8-9pm";
            } else if (value.equals("3")){
                value1 = "Mondays 9-10pm";
            } else if (value.equals("4")){
                value1 = "Tuesdays 7-8pm";
            } else if (value.equals("5")){
                value1 = "Tuesdays 8-9pm";
            } else if (value.equals("6")){
                value1 = "Tuesdays 9-10pm";
            } else if (value.equals("7")){
                value1 = "Wednesdays 7-8pm";
            } else if (value.equals("8")){
                value1 = "Wednesdays 8-9pm";
            } else if (value.equals("9")){
                value1 = "Wednesdays 9-10pm";
            } else if (value.equals("10")){
                value1 = "Thursdays 7-8pm";
            } else if (value.equals("11")){
                value1 = "Thursdays 8-9pm";
            } else if (value.equals("12")){
                value1 = "Thursdays 9-10pm";
            } else if (value.equals("13")){
                value1 = "Fridays 4-5pm";
            } else if (value.equals("14")){
                value1 = "Fridays 5-6pm";
            } else if (value.equals("15")){
                value1 = "Fridays 6-7pm";
            } else if (value.equals("16")){
                value1 = "Sundays 7-8pm";
            } else if (value.equals("17")){
                value1 = "Sundays 8-9pm";
            } else if (value.equals("18")){
                value1 = "Sundays 9-10pm";
            }
            System.out.println(key + ": " + value1);
        }
    }

    private static void choreographerMaker(){

        for (ArrayList<Integer> conflict: numConflicts) {
            String name = "";
            Choreographer newChoreographer = new Choreographer(-1, conflict, name);
            listOfChoreographers.add(newChoreographer);
        }
    }

    /*
     * This is the backtracking algorithm.
     */
    private static boolean backtrack(Choreographer person, Map<Choreographer, ArrayList<Integer>> Domains) {
        recursionCount++;

        if (person == null) {
            return true; // All choreographers have been assigned a valid time slot according to their availability
        }

        ArrayList<Integer> personDomains = Domains.get(person);

        for (int i = 0; i < personDomains.size(); i++) {
            int currentValue = finalVals.get(person);

            int assignedValue = personDomains.get(i);
            if (!isValueAssigned(assignedValue)) {
                finalVals.put(person, assignedValue);

                // Create a new domain for the choreographer without the assigned value
                Map<Choreographer, ArrayList<Integer>> domainsCopy = copy(Domains);
                ArrayList<Integer> updatedDomain = new ArrayList<>(domainsCopy.get(person));
                updatedDomain.remove(Integer.valueOf(assignedValue));
                domainsCopy.put(person, updatedDomain);

                if (AC3(domainsCopy) && backtrack(selectUnassignedChoreographer(domainsCopy), domainsCopy)) {
                    return true;
                } else {
                    finalVals.put(person, currentValue);
                }
            }
        }

        return false; // No valid assignment found for the current person
    }

    // Helper method to check if a value is already assigned to any choreographer
    private static boolean isValueAssigned(int value) {
        for (int assignedValue : finalVals.values()) {
            if (assignedValue == value) {
                return true;
            }
        }
        return false;
    }

    private static Choreographer selectUnassignedChoreographer(Map<Choreographer, ArrayList<Integer>> Domains) {
        Choreographer mostConstrainedChoreographer = null;
        int minDomainSize = Integer.MAX_VALUE;

        for (Choreographer choreographer : Domains.keySet()) {
            if (finalVals.get(choreographer) == -1) {
                ArrayList<Integer> domain = Domains.get(choreographer);
                int domainSize = domain.size();

                // Update mostConstrainedChoreographer if the current choreographer has a smaller domain
                if (domainSize < minDomainSize) {
                    mostConstrainedChoreographer = choreographer;
                    minDomainSize = domainSize;
                }
            }
        }

        return mostConstrainedChoreographer;
    }


    /*
     * This is the Revise() method. Takes in an Arc of choreographers and the domains of each choreographer.
     */
    private static boolean Revise(Arc currentArc, Map<Choreographer, ArrayList<Integer>> Domains) {

        boolean revised = false;
        Map<Choreographer, ArrayList<Integer>> copyTimeDomains = copy(Domains); //Deep copy of the timeDomains map
        ArrayList<Integer> xiHolder = new ArrayList<Integer>(copyTimeDomains.get(currentArc.Xi)); //Get the domains of xi and store it

        // Iterate through the domain of Xi
        for (int i = 0; i < xiHolder.size(); i++) {
            int valChecker = xiHolder.get(i);

            if(copyTimeDomains.get(currentArc.Xj).size() == 1) {
                if (valChecker == copyTimeDomains.get(currentArc.Xj).get(0)){
                    xiHolder.remove(Integer.valueOf(valChecker));
                    copyTimeDomains.put(currentArc.Xi, xiHolder);
                    revised = true;
                }
            }
        }
        return revised;
    }

    /*
     * Deep copy method for the map so that we don't affect the actual copy when editing.
     */
    public static Map<Choreographer, ArrayList<Integer>> copy(Map<Choreographer, ArrayList<Integer>> original){
        Map<Choreographer, ArrayList<Integer>> copy = new HashMap<Choreographer, ArrayList<Integer>>();

        for (Map.Entry<Choreographer, ArrayList<Integer>> entry : original.entrySet()){
            copy.put(entry.getKey(), new ArrayList<Integer>(entry.getValue()));
        }

        return copy;
    }


    /*
     * This method sets up the data structures and the initial global constraints
     * (by calling allDiff()) and makes the initial call to backtrack().
     */
    private static void AC3Init() {
        recursionCount = 0;

        //set up the domains and initial final values of all the choreographers in the list
        int i = 0;
        while(i < listOfChoreographers.size()){
            finalVals.put(listOfChoreographers.get(i), -1);
            timeDomains.put(listOfChoreographers.get(i), listOfChoreographers.get(i).getConstraintTimes());
            i++;
        }

        allDiff();
        //Initial call to backtrack() on first choreographer
        boolean success = backtrack(listOfChoreographers.get(0), timeDomains);
        //Prints evaluation of run
        Finished(success);

    }

    private static void allDiff() {
        for (int i = 0; i < listOfChoreographers.size(); i++) {
            Choreographer currentChoreographer = listOfChoreographers.get(i);
            ArrayList<Choreographer> tempArray = new ArrayList<Choreographer>();

            for (int j = 0; j < listOfChoreographers.size(); j++) {
                if (i != j) {
                    Choreographer otherChoreographer = listOfChoreographers.get(j);

                    // Check if the domains of the current choreographer and other choreographer have common elements
                    ArrayList<Integer> currentDomains = timeDomains.get(currentChoreographer);
                    ArrayList<Integer> otherDomains = timeDomains.get(otherChoreographer);

                    for (Integer timeSlot : currentDomains) {
                        if (otherDomains.contains(timeSlot)) {
                            tempArray.add(otherChoreographer);
                            break;
                        }
                    }
                }
            }

            neighbors.put(currentChoreographer, tempArray);
        }

        // Make an arc with each choreographer's neighbors and add to the globalQueue
        for (Choreographer choreographer : listOfChoreographers) {
            ArrayList<Choreographer> neighborsList = neighbors.get(choreographer);
            for (Choreographer neighbor : neighborsList) {
                Arc arc = new Arc(choreographer, neighbor);
                if (!globalQueue.contains(arc)) {
                    globalQueue.add(arc);
                }
            }
        }
    }


    /*
     * Arc Consistency-3 Algorithm
     */
    private static boolean AC3(Map<Choreographer, ArrayList<Integer>> Domains){

        Queue<Arc> copyGlobalQueue = new LinkedList<Arc>(globalQueue);

        while(!copyGlobalQueue.isEmpty()){
            Arc currentArc = copyGlobalQueue.remove(); //traverses through the global queue of choreographers
            if(Domains.get(currentArc.Xi).size() == 0){// If the domain of Xi in arc is 0, then must mean not satisfiable so return false
                return false;
            }
            if(Revise(currentArc, Domains)){// Revise so that the domain is updated for the choreographers according to the their neighbors
                ArrayList<Choreographer> xiNeighbor = neighbors.get(currentArc.Xi);//holds the neighbor of Xi
                for(int i = 0; i < xiNeighbor.size(); i++){
                    Arc newArc = new Arc(xiNeighbor.get(i), currentArc.Xi);
                    copyGlobalQueue.add(newArc);
                }
            }
        }
        return true;
    }

    private static void Finished(boolean success){

        if(success) {
            solvedCounter++;
            System.out.println("Solved in " + recursionCount + " recursive ops");

        } else {
            System.out.println("No valid assignment found");
        }
        recursionCount = 0;

    }


    /*
     * Arc Class: Creates an arc between choreographers who have the same time availabilities.
     */
    public static class Arc implements Comparable<Arc> {
        Choreographer Xi, Xj;

        public Arc(Choreographer choreo_i, Choreographer choreo_j) {
            if (choreo_i.equals(choreo_j)) {
                try {
                    throw new Exception(choreo_i + " is equal to " + choreo_j);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
            Xi = choreo_i;
            Xj = choreo_j;
        }

        @Override
        public int compareTo(Arc otherArc) {
            // Assuming Choreographer implements Comparable
            int compareXi = this.Xi.compareTo(otherArc.Xi);
            if (compareXi != 0) {
                return compareXi;
            }
            return this.Xj.compareTo(otherArc.Xj);
        }

        @Override
        public String toString() {
            return "(" + Xi + "," + Xj + ")";
        }
    }

    private static void greedyAlgorithm() {
        // Sort choreographers based on the number of constraints in ascending order
        Collections.sort(listOfChoreographers, (c1, c2) -> c1.getConstraintTimes().size() - c2.getConstraintTimes().size());

        // Assign time slots to choreographers one by one
        for (Choreographer choreographer : listOfChoreographers) {
            ArrayList<Integer> availableSlots = choreographer.getConstraintTimes();

            // Find the first available time slot
            int chosenSlot = -1;
            for (Integer slot : availableSlots) {
                if (!isSlotOccupied(slot)) {
                    chosenSlot = slot;
                    break;
                }
            }

            // Assign the chosen time slot
            if (chosenSlot != -1) {
                finalVals.put(choreographer, chosenSlot);
            } else {
                System.out.println("Error: Unable to find a valid time slot for choreographer " + choreographer);
            }
        }
    }

    private static boolean isSlotOccupied(int slot) {
        // Check if the time slot is already assigned to another choreographer
        for (Integer assignedSlot : finalVals.values()) {
            if (assignedSlot == slot) {
                return true;
            }
        }
        return false;
    }


}