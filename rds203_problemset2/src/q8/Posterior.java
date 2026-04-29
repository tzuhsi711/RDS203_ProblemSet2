package q8;

public class Posterior {

    public static double logPosterior(double theta, double[] X) {

        // 1 Prior: N(120, variance=2)
        double logPrior = -0.5 * Math.pow(theta - 120, 2) / 2;

        // 2. Likelihood: N(theta, variance=5)
        double logLik = 0;
        for (double x : X) {
            logLik += -0.5 * Math.pow(x - theta, 2) / 5; // log likelihood for each data point, sum over all data points
        }

        return logPrior + logLik; // Return the log posterior 
    }
    
}
