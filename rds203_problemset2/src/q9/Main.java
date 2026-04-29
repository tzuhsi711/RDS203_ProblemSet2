package q9;

import java.util.*;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) {

        Random rand = new Random(203);

        // Run ABC
        RejectionSampling.Result result = RejectionSampling.runRejectionSampling(100, rand);
        double[] posterior = result.posterior;

        // Save to CSV
        try {
            PrintWriter writer = new PrintWriter("data/Q9.csv");

            writer.println("TransmissionProbability");

            for (double p : posterior) {
                writer.println(p);
            }

            writer.close();
            System.out.println("CSV file saved.");

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Print acceptance rate
        System.out.printf("Acceptance rate: %.4f%%%n", result.acceptanceRate * 100);
    }
}