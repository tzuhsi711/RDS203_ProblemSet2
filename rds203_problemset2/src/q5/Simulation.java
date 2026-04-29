package q5;

// Agent-based model (Lecture 8, pg37, 47, 48)
//Q5b: Update history of disease
//Q5c: Simulate interactions
//Q5d: Simulate the course of epidemic (daily cycle length) while the number of current infections (exposed or infectious) is >0.

import java.util.*;

public class Simulation {

 public static int[] runSimulation(Random rand) {

     int N = 100; // 100 people on the island
     
     // ---------------------
     // Q5a: Contact network
     // ---------------------
     ArrayList<Person> people = Network.generateNetwork(N, rand);

     // Initialise index case
     people.get(0).state = "E"; // Exposed: infected but not yet infectious

     int totalInfected = 1; // number of current infections (exposed or infectious) is >0
     int days = 0; // length of epidemic ((# of cycles before current infections goes to 0)
     
     // ---------------------
     // Q5d: Simulate course of epidemic, run until no active infections
     // ---------------------
     while (true) {

    	 HashSet<Integer> newExposed = new HashSet<>();

         // ---------------------
         // Q5c: Simulate interaction
         // ---------------------
         for (int i = 0; i < N; i++) { // loop through each person in the population
        	 
             Person p = people.get(i); // p = current person
             
             // 1. For each infectious person
             if (p.state.equals("I")) {
            	 
            	 // 2. For each of their contacts
                 for (int j : p.contacts) { // j = each person p interacts with

                     Person q = people.get(j); // q = contact person
                     
                     // 3. If contact is susceptible
                     if (q.state.equals("S")) {
                    	 
                    	 // 4. With 20% probability
                         if (rand.nextDouble() < 0.2) { // 20% chance of contracting the disease per contact with a Sick person
                        	 
                        	 // 5. Mark as newly infected
                             newExposed.add(j); // add to new infections
                         }
                     }
                 }
             }
         }

         // Apply infections, avoid double counting
         for (int j : newExposed) {
             if (people.get(j).state.equals("S")) {
                 people.get(j).state = "E";
                 people.get(j).daysInfected = 0;
                 totalInfected++;
             }
         }

         // ---------------------
         // Q5b: Update history of disease
         // ---------------------
         int active = 0;

         for (Person p : people) {

             if (p.state.equals("E") || p.state.equals("I")) {

                 active++;
                 p.daysInfected++;

                 // Incubation: 2 days -> "I"
                 if (p.state.equals("E") && p.daysInfected >= 2) {
                     p.state = "I";
                 }

                 // Recovery at day 7
                 if (p.daysInfected >= 7) {
                     p.state = "R";
                 }
             }
         }

         days++;

         // Stop if no more infections
         if (active == 0) break;
     }

     return new int[]{totalInfected, days};
 }
}



