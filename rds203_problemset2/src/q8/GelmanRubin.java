package q8;

// Lecture 9, pg112

public class GelmanRubin {
	
    // Compute the mean of an array
    private static double mean(double[] arr) {
        double sum = 0;
        for (double v : arr) sum += v; 
        return sum / arr.length;
    }
    
    // Compute the sample variance of an array given its mean
    private static double variance(double[] arr, double mean) {
        double sum = 0;
        for (double v : arr) sum += Math.pow(v - mean, 2); // sum((x_i - mean)^2)
        return sum / (arr.length - 1);
    }
    
	// Computes the potential scale reduction factor (R-hat) for MCMC convergence diagnostics
    public static double computeRhat(double[][] chains) {

        int j = chains.length; // number of chains
        int l = chains[0].length; // number of iterations per sample

        double[] means = new double[j]; // mean of each chain
        double[] vars = new double[j]; // variance of each chain
        
        // 1. Compute means and variances for each chain
        for (int i = 0; i < j; i++) {
            means[i] = mean(chains[i]); 
            vars[i] = variance(chains[i], means[i]);
        }
        
        // 2. Compute overall (global) mean across all chains
        double meanAll = mean(means); 
        
        // 3. Compute between-chain variance of the means
        double B = 0;
        for (int i = 0; i < j; i++) { 
            B += Math.pow(means[i] - meanAll, 2); // (m_i - m)^2
        }
        B *= l / (double)(j - 1); // B = (l / (j - 1)) * sum((m_i - m)^2)
        
        
        // 4. Compute within-chain variance (average of variances)
        double W = 0;
        for (int i = 0; i < j; i++) {
            W += vars[i];  
        }
        W /= j; // W = (1/j) * sum(s_i^2)
        
        // 5. Compute Gelman-Rubin potential scale reduction factor (R-hat)
        double varHat = ((l - 1.0) / l) * W + (1.0 / l) * B; // varHat = ((l - 1) / l) * W + (1 / l) * B

        return varHat / W; // R-hat = varHat / W 
    } 
    
}
