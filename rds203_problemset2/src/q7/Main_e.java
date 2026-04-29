package q7;

// Q7e. Calibrate the model using greedy simulated annealing

import java.io.PrintWriter;
import java.util.Random;

public class Main_e {

    public static void main(String[] args) {

        int nPeople = 10000; // cohort size

        Random rand = new Random(203);

        try {
            // Output file
            PrintWriter writer = new PrintWriter("data/Q7e.csv");

            // 1. Calibrate model
            Parameters best = Calibration.calibrate(nPeople, rand);

            // 2. Run simulation with best parameters
            Simulation sim = new Simulation();
            sim.run(nPeople, best, rand);

            // 3. Write header
            writer.print("beta0,beta1,recovery,mortDx");

            for (int k = 0; k < 6; k++) writer.print(",prev_" + k);
            for (int k = 0; k < 6; k++) writer.print(",mort_" + k);

            writer.println();

            // 4. Write results
            writer.print(best.beta0_best + "," +
                         best.beta1_best + "," +
                         best.recovery_best + "," +
                         best.mortDx_best);

            // prevalence
            for (int k = 0; k < 6; k++) {
                writer.print("," + sim.prev[k]);
            }

            // mortality
            for (int k = 0; k < 6; k++) {
                writer.print("," + sim.mort[k]);
            }

            writer.println();

            writer.close();
            System.out.println("CSV file saved.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

