package q7;

// Q7d. Stores calibration target values (means and 95% CIs)
// Compares the simulated estimates from the model to the observed data
// Calculates an appropriate loss score

public class Targets {
	// ----------------
	// 1. Target means
	// ----------------

	// Disease prevalence (%)
	public double[] prev_mean = {4.21, 7.19, 9.41, 12.03, 14.82, 18.59};

	// 1-year risk of mortality (%)
	public double[] mort_mean = {14.03, 13.26, 13.21, 13.68, 15.09, 23.87};

	// ----------------
	// 2. 95% CI 
	// ----------------
	
	// Disease prevalence (%)
	public double[] prev_low = {3.88, 6.70, 8.85, 11.29, 13.64, 16.15};
	public double[] prev_high = {4.61, 7.68, 10.07, 12.96, 15.94, 21.43};

	// 1-year risk of mortality (%)
	public double[] mort_low = {10.80, 10.97, 10.58, 11.22, 11.80, 16.07};
	public double[] mort_high = {17.32, 15.64, 16.00, 16.17, 18.44, 32.96};

	// ----------------
	// 3. Score function: Compares simulated outputs to observed targets
	// ----------------
	
	// Explanation: Sum of Squared Errors (SSE)
	// sum(y_simularted - y_observed)^2
	
	public double score(Simulation sim) {

		double loss = 0;

		for (int i = 0; i < 6; i++) {

			// Difference between simulated and observed values
			double d_prev = sim.prev[i] - prev_mean[i];
			double d_mort = sim.mort[i] - mort_mean[i];

			// Sum of squared differences
			loss += d_prev * d_prev;
			loss += d_mort * d_mort;
		}
		
		// Total loss prevalence error + mortality error
		return loss;
	}
}




