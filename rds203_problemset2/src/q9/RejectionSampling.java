package q9;

// Exact Bayesian Computation rejection sampling approach
// estimate the posterior for this parameter given a prior of Beta(20, 80)

import java.util.*;

public class RejectionSampling {
	
	// Run Approximate Bayesian Computation (ABC) until we get nAccept accepted samples
	// Lecture 9, pg67
    public static class Result {
        public double[] posterior;
        public double acceptanceRate;

        public Result(double[] posterior, double acceptanceRate) {
            this.posterior = posterior;
            this.acceptanceRate = acceptanceRate;
        }
    }

    public static Result runRejectionSampling(int nAccept, Random rand) {
    	
    	// accepted samples approximate draws from posterior f(x)
        ArrayList<Double> accepted = new ArrayList<>(); // p = the probability of infection per contact (posterior samples)
        
        int maxAttempts = 1000000;
        int attempts = 0;

        while (accepted.size() < nAccept && attempts < maxAttempts) {
        	
        	attempts++;

            // 1. Sample from proposal function q(x) = prior Beta(20,80)
            double p = sampleBeta(20, 80, rand);

            // 2. Simulate epidemic at p
            int[] result = Simulation.runSimulationWithP(p, rand);
            int totalInf = result[0];

            // 3. Accept if matches observed data (42 infections)
            if (totalInf == 42) { // exact ABC (integer match)
                accepted.add(p);
                System.out.println("Accepted: " + accepted.size());
            }

            // Print progress 
            if (attempts % 10000 == 0) {
                System.out.println("Attempts: " + attempts + ", Accepted: " + accepted.size());
            }
        }

        // Warning if we did not reach nAccept samples
        if (accepted.size() < nAccept) {
            System.out.println("Warning: only got " + accepted.size() + " samples (maxAttempts reached)");
        }

        // Convert to array 
        double[] posterior = new double[accepted.size()];
        for (int i = 0; i < accepted.size(); i++) {
            posterior[i] = accepted.get(i);
        }

        // Compute acceptance rate
        double acceptanceRate = accepted.size() / (double) attempts;

        return new Result(posterior, acceptanceRate);
    }
    
    // Proposal distribution: Beta sampling (Gamma method; Lecture 5, pg81)
    private static double sampleBeta(double a, double b, Random rand) {
    	
    	// draw two independent Gamma random variables
        double y1 = sampleGamma(a, rand); // Gamma(a,1)
        double y2 = sampleGamma(b, rand); // Gamma(b,1)
        
        // normalise to get proportion -> Beta(a,b)
        return y1 / (y1 + y2);
    }
    
    // Gamma sampling (Lecture 5, pg48)
    private static double sampleGamma(double shape, Random rand) {
        double sum = 0;
        for (int i = 0; i < (int) shape; i++) {
        	
        	// F^-1(u) = -log(1-u)/lambda
        	
        	// Sampling from Exponential(1) -> lambda = 1
        	// F^-1(u) = -log(1-u)
        	
        	// Since U ~ Uniform(0,1), (1 - U) is also Uniform(0,1)
        	// Because subtracting a uniform random variable from 1 simply mirrors it across the interval
        	// preserving the uniform distribution
        	// -log(U) instead of -log(1 - U)
        	
            sum += -Math.log(rand.nextDouble()); // X ~ Exponential(1)
        }
        return sum;
    }
}






