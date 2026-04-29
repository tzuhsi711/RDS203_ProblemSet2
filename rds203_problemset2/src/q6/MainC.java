package q6;

import java.util.*;
import java.io.PrintWriter;

public class MainC {

    public static void main(String[] args) {

        int nSim = 1000;
        int nPeople = 5000;

        double[] baseline = new double[nSim];
        double[] intervention = new double[nSim];
        double[] gain = new double[nSim];

        long startTime = System.currentTimeMillis(); // Start time

        // Simulation using the same random number
        for (int i = 0; i < nSim; i++) {

            // Same seed for baseline and intervention cases
            Random rand = new Random(i);

            // ----------------------
            // 1. Baseline parameters
            // ----------------------
            Parameters paramsBase = new Parameters();
            paramsBase.baseMort = 0.20; // baseline: 0.20 * 0.75^x

            // ----------------------
            // 2. Intervention parameters
            // ----------------------
            Parameters paramsInt = new Parameters();
            paramsInt.baseMort = 0.13; // intervention: 0.13 * 0.75^x

            // ----------------------
            // 3. CRN simulation (shared random numbers internally)
            // ----------------------
            double[] result = SimulationCRN.runSimulationCRN(nPeople, paramsBase, paramsInt, rand);

            double le_base = result[0];
            double le_int = result[1];

            baseline[i] = le_base;
            intervention[i] = le_int;
            
            // ----------------------
            // 4. Compute gain
            // ----------------------
            gain[i] = le_int - le_base;
        }

        long endTime = System.currentTimeMillis(); // End time

        double avgTime = (endTime - startTime) / (double) nSim;

        System.out.printf("Average time per iteration (CRN): %.2f ms%n", avgTime);

        // ----------------------
        // 5. Compute mean gain
        // ----------------------
        double sum = 0;
        for (double g : gain) {
            sum += g;
        }
        double mean = sum / nSim;

        // ----------------------
        // 6. Compute 95% UI
        // ----------------------
        double[] gainCopy = gain.clone(); 
        Arrays.sort(gainCopy);

        int lowerIdx = (int)Math.floor(0.025 * nSim);
        int upperIdx = (int)Math.floor(0.975 * nSim);

        double lower = gainCopy[lowerIdx];
        double upper = gainCopy[upperIdx];

        // ----------------------
        // Results
        // ----------------------
        System.out.printf("Mean gain: %.2f%n", mean);
        System.out.printf("95%% UI: [%.2f, %.2f]%n", lower, upper);
        
        // ----------------------
        // CSV file
        // ----------------------
        try{
            PrintWriter writer = new PrintWriter("data/Q6c.csv");

            writer.println("Baseline,Intervention,Gain");

            for(int i = 0; i < nSim; i++) {
                writer.println(baseline[i] + "," + intervention[i] + "," + gain[i]);
            }

            writer.close();
            System.out.println("CSV file saved.");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}



