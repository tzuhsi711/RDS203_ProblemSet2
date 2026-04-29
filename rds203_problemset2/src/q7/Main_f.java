package q7;

//Calibrate microsimulation model from Problem 3 (w/o common random numbers)

import java.io.PrintWriter;
import java.util.Random;

public class Main_f {

    public static void main(String[] args) {

        int nPeople = 10000; // cohort size

        try {
        	// Output CSV file
            PrintWriter writer = new PrintWriter("data/Q7f.csv");

            // header
            writer.print("beta0,beta1,recovery,mortDx");

            for (int k = 0; k < 6; k++) writer.print(",prev_" + k); // prev_{age 0}
            for (int k = 0; k < 6; k++) writer.print(",mort_" + k); // mort_{age 0}
            writer.println();

            for (int i = 0; i < 10; i++) { // loop over 10 calibration runs

                Random rand = new Random(i);  // different seed per run
                
                // 1. Calibrate model
                Parameters best = Calibration.calibrate(nPeople, rand); // find the best parameters

                // 2. Run simulation with best parameters
                Simulation sim = new Simulation();
                sim.run(nPeople, best, rand);

                // 3. Write parameters
                writer.print(best.beta0_best + "," +
                             best.beta1_best + "," +
                             best.recovery_best + "," +
                             best.mortDx_best);

                // 4. Write predictions
                for (int k = 0; k < 6; k++) writer.print("," + sim.prev[k]); // prevalence
                for (int k = 0; k < 6; k++) writer.print("," + sim.mort[k]); // mortality

                writer.println();
            }

            writer.close();
            System.out.println("CSV file saved.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

