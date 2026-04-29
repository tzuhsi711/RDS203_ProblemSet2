package q6;

import java.util.*;
import java.io.PrintWriter;

public class MainA {

    public static void main(String[] args) {

        int nSim = 1000;
        int nPeople = 5000;

        double[] results = new double[nSim];

        long startTime = System.currentTimeMillis(); // Start time
        
        // Baseline simulation
        for (int i = 0; i < nSim; i++) {

            Random rand = new Random(i); // seed = iteration number

            Parameters params = new Parameters();
            params.baseMort = 0.20; // baseline: 0.20 ∗ 0.75^x
            
            // Microsimulation
            double le = Simulations.runSimulation(nPeople, params, rand);
            results[i] = le;
        }

        long endTime = System.currentTimeMillis(); // End time

        double avgTime = (endTime - startTime) / (double) nSim;
        System.out.printf("Average time per iteration: %.2f ms%n", avgTime);

        // Save CSV
        try {
            PrintWriter writer = new PrintWriter("data/Q6a.csv");
            writer.println("LifeExpectancy");

            for (double r : results) {
                writer.println(r);
            }

            writer.close();
            System.out.println("CSV file saved.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}