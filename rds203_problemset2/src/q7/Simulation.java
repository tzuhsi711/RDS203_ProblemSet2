package q7;

import java.util.Random;

public class Simulation {
	
	// Q7d.
	// Indicators used for calibration
    public double[] prev = new double[6]; // disease prevalence
    public double[] mort = new double[6]; // mortality by age

    int[] ages = {20, 35, 45, 60, 75, 90};

    public void run(int nPeople, Parameters params, Random rand) {

        int[] sick = new int[6];
        int[] alive = new int[6];
        int[] deaths = new int[6];
        
        // Simulate a cohort
        // Estimate disease prevalence & 1-year mortality among sick at defined ages
        for (int i = 0; i < nPeople; i++) {

            Person p = new Person(); // create a new person
            
            // ----------------
            // ALIVE
            // ----------------
            // Build prevalnce = (# of sick) / (# of alive)
            while (!p.state.equals("Dead") && p.age <= 100) { // if the person isn't dead and is within 100 years

                // match age groups
                for (int k = 0; k < 6; k++) { 
                    if (p.age == ages[k]) { // check if current age matches target ages

                        alive[k]++; // count number of individuals alive at this age
                        
                        if (p.state.equals("Sick")) 
                        	sick[k]++; // count number of individuals who are sick at this age
                    }
                }

                // simulate one year 
                p.simulateYear(params, rand);
                
                // ----------------
                // DEAD
                // ----------------
                // Track dead among sick to identify deaths among prevalent cases
                if (p.state.equals("Dead") && p.prevState.equals("Sick")) { // if the person is previously sick and currently dead
                	
                    for (int k = 0; k < 6; k++) { 
                        if (p.age == ages[k]) // check if current age matches target ages
                        	deaths[k]++; // count deaths among sick individuals at this age
                    }
                }
            }
        }

        // Compute outputs
        for (int k = 0; k < 6; k++) {
        	
        	// Ensures denominator is not 0 
        	// condition ? value_if_true : value_if_false
        	// condition > 0
        	
        	// Disease prevalence (%) = sick / alive × 100
            prev[k] = alive[k] > 0 ? (double) sick[k] / alive[k] * 100 : 0;
            
            // Mortality prevalence (%) = deaths / sick × 100
            mort[k] = sick[k] > 0 ? (double) deaths[k] / sick[k] * 100 : 0;
        }
    }
}




