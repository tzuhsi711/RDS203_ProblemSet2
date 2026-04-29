package q8;

// Lecture 9, pg85, pg99

import java.util.Random;

public class MetropolisHastings {

    // Simple container to return samples and acceptance rate
    public static class Result {
        public double[] samples;
        public double acceptanceRate; 

        public Result(double[] samples, double acceptanceRate) {
            this.samples = samples;
            this.acceptanceRate = acceptanceRate;
        }
    }

    public static Result runChain(double[] X, int burnIn, int nSamples, double proposalSD, double initTheta) {

        Random rand = new Random(203);

        double[] samples = new double[nSamples]; // to store the samples after burn-in

        // 1. Initialise theta (mean) 
        double theta = initTheta; 
        int sampleIndex = 0;

        int acceptCount = 0; // track number of accepted proposals
        
        int totalIter = burnIn + nSamples;
        
        // 2. Run the MCMC chain for burn-in + nSamples iterations
        for (int iter = 0; iter < totalIter; iter++) {
        	
        	// 3. Propose a new theta from the proposal distribution (Gaussian random walk)
            double thetaProp = theta + rand.nextGaussian() * proposalSD; 

            // 4. Compute log posterior for current and proposed theta
            double logCurrent = Posterior.logPosterior(theta, X); 
            
            // 5. Compute log posterior for the proposed theta
            double logProp = Posterior.logPosterior(thetaProp, X); 

            // 6. Compute log acceptance ratio (logAlpha)
            double logAlpha = logProp - logCurrent; 
            
            // 7. Accept or reject the proposal based on logAlpha
            if (Math.log(rand.nextDouble()) < logAlpha) {  // slide: u < alpha, where u ~ Uniform(0,1)
                theta = thetaProp; // accept the proposal with probability min(1, exp(logAlpha))
                acceptCount++; // count accepted proposals
            }
            
            // 8. After burn-in, store the sample
            if (iter >= burnIn) { 
                samples[sampleIndex++] = theta;
            }
        }

        // Compute acceptance rate
        double acceptanceRate = acceptCount / (double) totalIter;

        return new Result(samples, acceptanceRate);
    }
}

