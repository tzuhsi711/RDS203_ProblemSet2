package q2;

import java.util.Random;  
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class Q2 {

    public static void main(String[] args) {

        // Parameters
        int nCases = 100; // Total number of observed cases
        int nSim = 100000; // Number of Monte Carlo simulations 

        double width = 8.0; // Width of city (East-West)
        double height = 4.0; // Height of city (North-South)

        double radius = 2.0; // Radius of circle around center 

        Random rand = new Random(203); // Random number generator

        int countExtreme = 0; // Simulations when result ≥ observed (50 cases)

        // Store simulation results (for CSV)
        int[] insideCounts = new int[nSim];

        // Monte Carlo Simulation 
        for (int sim = 0; sim < nSim; sim++) {  // Loop over number of simulations

            int insideCount = 0;  // # of cases fall within radius = 2.0

            for (int i = 0; i < nCases; i++) {  // Loop over all 100 cases
            	
            	// Randomly place a case uniformly anywhere in the city
            	// Check how far from the center
            	
                // Generate random x-coordinate uniformly in [-4, 4]
            	// -0.5 to transform [0, 1)  ->  [-0.5, 0.5)
                double x = (rand.nextDouble() - 0.5) * width;

                // Generate random y-coordinate uniformly in [-2, 2]
                double y = (rand.nextDouble() - 0.5) * height;

                // Compute distance from center (Euclidean distance)
                double distance = Math.sqrt(x * x + y * y);

                // Check if the point lies within the target radius
                if (distance <= radius) {
                    insideCount++; 
                }
            }

            // Save result for this simulation
            insideCounts[sim] = insideCount;

            // Check if simulated result is at least as extreme as observed (100*0.5=50cases)
            if (insideCount >= 50) {
                countExtreme++;  
            }
        }

        // Compute p-val
        double pValue = (double) countExtreme / nSim;  // Proportion of extreme simulations

        System.out.println("Estimated p-value: " + pValue);

        try {
            PrintWriter writer = new PrintWriter("data/Q2.csv");

            // Header
            writer.println("Simulation,InsideCount");

            for (int sim = 0; sim < nSim; sim++) {
                writer.println(sim + "," + insideCounts[sim]);
            }

            writer.println();
            writer.println("p-value," + pValue);

            writer.close();

            System.out.println("CSV file saved.");

        } catch (FileNotFoundException e) {
            System.out.println("Error writing CSV file.");
            e.printStackTrace();
        }
    }
}
