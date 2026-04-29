package q3;

// Supplies all parameter values for the current simulation

// Lecture 5, pg81

import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;

public class Parameters2 {

	// ---------------
    // 0. Parameters
	// ---------------
    public double beta0;
    public double beta1;
    public double recovery;
    public double baseMort;

    // ---------------
    // 1. Load life table
    // ---------------
    private double[] lifeTable = new double[101];

    private void loadLifeTable(String filePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                int age = Integer.parseInt(parts[0]);
                double mx = Double.parseDouble(parts[1]); // mortality rate

                // Convert rate -> probability
                double prob = 1 - Math.exp(-mx);

                lifeTable[age] = prob;
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Parameters2() {
        loadLifeTable("data/us_lifetable_2022.csv");
    }

    // ---------------	
    // 2. Sample parameters from distributions
    // ---------------
    public void sample(Random rand) {

        // β0 ~ exp(N(-6.21, 0.5))
        // Draw Z ~ N(0,1), scale and shift to N(-6.21, 0.5), then exponentiate
        beta0 = Math.exp(rand.nextGaussian() * Math.sqrt(0.5) - 6.21); 
        
        // β1 ~ exp(N(-6.91, 0.3))
        beta1 = Math.exp(rand.nextGaussian() * Math.sqrt(0.3) - 6.91);

        // Recovery ~ Beta(30, 70)
        recovery = sampleBeta(30, 70, rand);

        // Disease mortality base ~ Beta(16, 64)
        baseMort = sampleBeta(16, 64, rand);
    }

    // Incidence: β0 + β1 * t
    public double getIncidence(int t) {
        return beta0 + beta1 * t;
    }

    // Background mortality from lifetable
    public double getBackgroundMortality(int age) {
        return lifeTable[age];
    }
    
    // ---------------
    // 3. Sample from Beta distribution using Gamma sampling
    // ---------------
    private double sampleBeta(double a, double b, Random rand) {

        // y1 ~ Gamma(a,1), y2 ~ Gamma(b,1)
        double y1 = sampleGamma(a, rand);
        double y2 = sampleGamma(b, rand);

        // Normalize -> proportion
        return y1 / (y1 + y2);
    }

    // ---------------
    // 4. Sample from Gamma distribution using sum of exponentials (shape, scale=1)
    // ---------------
    private double sampleGamma(double shape, Random rand) {
        double sum = 0;
        for (int i = 0; i < (int) shape; i++) {
            sum += -Math.log(rand.nextDouble());
        }
        return sum;
    }
}



