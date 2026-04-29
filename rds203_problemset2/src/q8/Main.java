package q8;

import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) {

        double[] X = {201,135,143,165,150,132,133,131,131,173,141,133,189,143};

        int burnIn = 1000;
        int nSamples = 10000;

        double proposalSD = Math.sqrt(0.2); // N(0, 0.2) 

        int nChains = 2;

        double[][] chains = new double[nChains][];
        double[] acceptanceRates = new double[nChains]; // store acceptance rates

        // Run chains
        for (int c = 0; c < nChains; c++) {
            double initTheta = 120 + c * 20; // different starting points

            MetropolisHastings.Result result =
                MetropolisHastings.runChain(X, burnIn, nSamples, proposalSD, initTheta);

            chains[c] = result.samples;
            acceptanceRates[c] = result.acceptanceRate;
        }
        
        try {
            PrintWriter writer = new PrintWriter("data/Q8.csv");

            writer.println("Iter,Chain1,Chain2");

            for (int i = 0; i < nSamples; i++) {
                writer.println(i + "," + chains[0][i] + "," + chains[1][i]);
            }

            writer.close();
            System.out.println("CSV file saved.");

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Print acceptance rates
        double sum = 0;
        		
        for (int c = 0; c < nChains; c++) {
        	// Compute acceptance rate by chain
        	System.out.printf("Chain %d acceptance rate: %.4f%n", c + 1, acceptanceRates[c]);
        	
        	// Compute average
        	sum += acceptanceRates[c];
        }
        
        double acceptanceRatesAvg = sum / nChains;
        System.out.printf("Average acceptance rate: %.4f%n", acceptanceRatesAvg);

        // Compute R-hat
        double rhat = GelmanRubin.computeRhat(chains);
        System.out.println("Gelman-Rubin R-hat: " + rhat);
    }
}


