package q7;

// Q7e. Calibrate the model using greedy simulated annealing

import java.util.Random;

public class Calibration {

    public static Parameters calibrate(int nPeople, Random rand) {
    	
    	// Initialise objects
        Parameters params = new Parameters(rand); // current and best parameters
        Targets targets = new Targets(); // observed data and loss function
        
        // Optimisation variables
        double bestScore = Double.MAX_VALUE; // best loss
        double temp = 1.0;
        
        
        // Annealing loop to generate new candidate parameters
        // 500 iterations
        for (int t = 0; t < 500; t++) {

            params.sample(temp, rand); // sample new parameters

            Simulation sim = new Simulation(); // run simulation with current parameters
            sim.run(nPeople, params, rand);

            double score = targets.score(sim); // compare simulation vs observed data, compute loss

            boolean accept;

            if (score < bestScore) { // if new parameters improve fit -> accept
                accept = true;
            } else {
            	// otherwise use acceptance probability = exp(-(new - best) / temp)
                double prob = Math.exp(-(score - bestScore) / temp);
                accept = rand.nextDouble() < prob; // accept if greater than random number
            }
            
            // accept best parameters
            if (accept) {
                params.updateBest();
                bestScore = score;
            }

            temp *= 0.992; // cooling
        }

        return params;
    }
}
