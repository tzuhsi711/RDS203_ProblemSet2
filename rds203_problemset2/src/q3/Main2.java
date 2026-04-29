package q3;

//Q3e

import java.io.PrintWriter;
import java.util.Random;
import java.util.Arrays;

public class Main2 {

  public static void main(String[] args) {

      int nSim = 100; // PSA iterations
      int nPeople = 100000; // cohort size

      double[] results = new double[nSim];

      Random rand = new Random(203);
      
   // ---------------
      // Run simulations
   // ---------------
      for (int i = 0; i < nSim; i++) {

          Parameters2 params = new Parameters2(); // Create new parameters object for each iteration
          params.sample(rand);

          double le = Simulations2.runSimulation(nPeople, params, rand);

          results[i] = le;

          System.out.println("Iteration " + i + ": LE = " + le); 
      }

      try {
          PrintWriter writer = new PrintWriter("data/Q3e.csv");
          writer.println("LifeExpectancy");

          for (double r : results) {
              writer.println(r);
          }

          writer.close();
          System.out.println("CSV file saved.");
      } catch (Exception e) {
          e.printStackTrace();
      }
      
      // ---------------
      // Summary
   // ---------------
      Arrays.sort(results);

      double mean = Arrays.stream(results).average().getAsDouble();
      double lower = results[(int)(0.025 * nSim)];
      double upper = results[(int)(0.975 * nSim)];

      System.out.printf("Mean LE: %.2f\n", mean);
      System.out.printf("95%% UI: [%.2f, %.2f]", lower, upper);
  }
}


