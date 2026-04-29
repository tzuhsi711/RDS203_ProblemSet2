package q1;

import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class Q1a {
	public static void main(String[] args) {
		
		// ---------------
		// 0. Variables
		// ---------------
		int cycles = 100;
		
		double[] well = new double[cycles + 1];
		double[] sick = new double[cycles + 1];
		double[] dead = new double[cycles + 1];
		
		// ---------------
		// 1. Initial states
		// ---------------
		well[0] = 1.0;
		sick[0] = 0.0;
		dead[0] = 0.0;
		
		// ---------------
		// 2. Markov model (state transitions)
		// ---------------
		for(int t = 0; t < cycles; t++) {
			
			double p_ws = 0.002 + 0.001 * t; // incidence
			double p_wd = 0.03; // background mortality
			
			double p_sw = 0.30; // recovery
			
			double p_s_background = 0.03; // background mortality
			double p_s_disease = 0.10; // disease-specific mortality
			
			// combined mortality (independent risks)
			double p_sd = 1 - (1 - p_s_background) * (1 - p_s_disease);
			
			double p_ww = 1 - p_ws - p_wd;
			double p_ss = 1 - p_sw - p_sd;
			
			// Transitions
			well[t + 1] = well[t] * p_ww + sick[t] * p_sw;
			sick[t + 1] = well[t] * p_ws + sick[t] * p_ss;
			dead[t + 1] = dead[t] + well[t] * p_wd + sick[t] * p_sd;
		}
		
		// ---------------
		// 3. Print trace
		// ---------------
		for(int t = 0; t <= cycles; t++) {
			System.out.printf("Year %d: Well=%.4f Sick=%.4f Dead=%.4f%n",
					t, well[t], sick[t], dead[t]);
		}
		
		// ---------------
		// 4. Median survival 
		// ---------------
		for (int t = 0; t <= cycles; t++) {
			if (dead[t] >= 0.5) { // 50% dead
				System.out.println("Median survival: Year " + t);
				break;
			}
		}
		
		// ---------------
		// 5. Save as CSV
		// ---------------
		try {
			PrintWriter writer = new PrintWriter("data/Q1a.csv");
			
			writer.println("Year,Well,Sick,Dead");
			
			for (int t = 0; t <= cycles; t++) {
				writer.println(t + "," + well[t] + "," + sick[t] + "," + dead[t]);
			}
			
			writer.close();
			System.out.println("CSV file saved.");
			
		} catch (FileNotFoundException e) {
			System.out.println("Error writing file.");
			e.printStackTrace();
		}
	}
}



