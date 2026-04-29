package q7;

// Keep track of the current and the best sets of parameters found during calibration

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

public class Parameters {
	
	// Q5a.
	// -------------------
	// 0. Parameters
	// -------------------

    // a. Current parameters
    public double beta0_curr;
    public double beta1_curr;
    public double recovery_curr;
    public double mortDx_curr;

    // b. Best parameters
    public double beta0_best;
    public double beta1_best;
    public double recovery_best;
    public double mortDx_best;

    // -------------------
    // 1. Load life table
    // -------------------
    private double[] lifeTable = new double[101];

    public Parameters(Random rand) {

        loadLifeTable("data/us_lifetable_2022.csv");

        // Incidence
        // nextGaussian() returns random number from normal distribution
        beta0_best = Math.exp(-6.21 + rand.nextGaussian() * Math.sqrt(0.5)); // 𝛽0 ~exp(𝑁(−6.21, 0.5))
        beta1_best = Math.exp(-6.91 + rand.nextGaussian() * Math.sqrt(0.3)); // 𝛽1 ~exp(𝑁(−6.91, 0.3))
        
        // Recovery
        recovery_best = 0.3; // 𝑁(0.3, 0.01)
        
        // Disease-specific mortality
        mortDx_best = 0.2; // 𝑚𝑜𝑟𝑡𝐷𝑥 ~ 𝑁(0.2, 0.01)
        
        // Initialises current = best
        copyBestToCurrent();
    }

    // Q5b.
    // ----------------------
    // 2. Sampling
    // ----------------------
    public void sample(double temp, Random rand) {
    	
    	// log-space transformation
        double log_b0 = Math.log(beta0_best);
        double log_b1 = Math.log(beta1_best);
        
        // Gaussian sampling: N(mean, variance), scale variances proportionally to the temperature
        // mean + rand.nextGaussian() * sqrt(variance)
        // output: random number that follows N(mean, variance)
        beta0_curr = Math.exp(log_b0 + rand.nextGaussian() * Math.sqrt(0.5 * temp));
        beta1_curr = Math.exp(log_b1 + rand.nextGaussian() * Math.sqrt(0.3 * temp));

        recovery_curr = clamp(recovery_best + rand.nextGaussian() * Math.sqrt(0.01 * temp));
        mortDx_curr = clamp(mortDx_best + rand.nextGaussian() * Math.sqrt(0.01 * temp));
    }
    
    
    // Helper functions:
    // -------------------
    // Incidence
    // -------------------
    public double getIncidence(int age) {
        return beta0_curr + beta1_curr * age;
    }

    // -------------------
    // Background mortality
    // -------------------
    // Q5c. 
    // Updates the best values of all parameters with the current values
    public void copyBestToCurrent() {
        beta0_curr = beta0_best;
        beta1_curr = beta1_best;
        recovery_curr = recovery_best;
        mortDx_curr = mortDx_best;
    }
    
    // Update best
    public void updateBest() {
        beta0_best = beta0_curr;
        beta1_best = beta1_curr;
        recovery_best = recovery_curr;
        mortDx_best = mortDx_curr;
    }
    
    // Ensures 0 ≤ probability ≤ 1
    private double clamp(double x) {
        return Math.max(0, Math.min(1, x));
    }
    
    // Pulls mortality from life table, caps at age 100
    public double getBackgroundMortality(int age) {
        if (age >= 100) age = 100;
        return lifeTable[age];
    }

    // Load life table
    private void loadLifeTable(String filePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            String line;
            br.readLine(); 

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                int age = Integer.parseInt(parts[0]);
                double mx = Double.parseDouble(parts[1]);
                
                // Converts hazard rate (mx) to probability
                lifeTable[age] = 1 - Math.exp(-mx);
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



