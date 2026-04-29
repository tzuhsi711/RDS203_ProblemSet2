package q1;

import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;

public class Q1c {
	public static void main(String[] args) {
		
		// ------------------
		// 0. Variables
		// ------------------
		int cycles = 100;
		
		double[] well = new double[cycles + 1];
		double[] sick = new double[cycles + 1];
		double[] dead = new double[cycles + 1];
		
		// Cause-specific death
		double[] dead_bg = new double[cycles + 1]; // other causes
		double[] dead_disease = new double[cycles + 1]; // disease
		
		double[] backgroundMortality = new double[cycles + 1];
		
		// ------------------
		// 1. Load US lifetable
		// ------------------
		try {
			Scanner scanner = new Scanner(new File("data/us_lifetable_2022.csv"));
			
			scanner.nextLine(); // skip header
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] parts = line.split(",");
				
				int age = Integer.parseInt(parts[0]);
				double mx = Double.parseDouble(parts[1]); // mortality rate
				
				double prob = 1 - Math.exp(-mx); // convert to probability
				
				backgroundMortality[age] = prob;
			}
			
			scanner.close();
			
		} catch (Exception e) {
			System.out.println("Error reading input file.");
			e.printStackTrace();
		}
		
		// ------------------
		// 2. Initial states
		// ------------------
		well[0] = 1.0; // all Well
		sick[0] = 0.0;
		dead[0] = 0.0;
		dead_bg[0] = 0.0;
		dead_disease[0] = 0.0;
		
		// ------------------
		// 3. Markov model
		// ------------------
		for(int t = 0; t < cycles; t++) {
			
			double p_ws = 0.002 + 0.001 * t; // incidence
			
			double p_wd = backgroundMortality[t]; // Well to Dead
			
			double p_sw = 0.30; // recovery (Sick to Well)
			
			double p_s_background = backgroundMortality[t];
			double p_s_disease = 0.10;
			
			// combined mortality 
			double p_sd = 1 - (1 - p_s_background) * (1 - p_s_disease);
			
			double p_ww = 1 - p_ws - p_wd;
			double p_ss = 1 - p_sw - p_sd;
			
			
			// State transitions 
			double next_well = well[t] * p_ww + sick[t] * p_sw;
			double next_sick = well[t] * p_ws + sick[t] * p_ss;
			double next_dead = dead[t] + well[t] * p_wd + sick[t] * p_sd;
			
			
			// Cause-specific deaths 
			double deaths_from_well = well[t] * p_wd; // background deaths
			double deaths_from_sick = sick[t] * p_sd; // mixed deaths
			
			double total = p_s_background + p_s_disease;
			double frac_bg = p_s_background / total;
			double frac_dis = p_s_disease / total;
			
			double next_dead_bg = dead_bg[t] + deaths_from_well + deaths_from_sick * frac_bg;
			double next_dead_disease = dead_disease[t] + deaths_from_sick * frac_dis;
			
			
			well[t + 1] = next_well;
			sick[t + 1] = next_sick;
			dead[t + 1] = next_dead;
			
			dead_bg[t + 1] = next_dead_bg;
			dead_disease[t + 1] = next_dead_disease;
		}
		
		// ------------------
		// 4. Print trace
		// ------------------
		for(int t = 0; t <= cycles; t++) {
			System.out.printf("Year %d: Well=%.4f Sick=%.4f Dead=%.4f%n",
					t, well[t], sick[t], dead[t]);
		}
		
		// ------------------
		// 5. Median survival
		// ------------------
		for (int t = 0; t <= cycles; t++) {
			if (dead[t] >= 0.5) {
				System.out.println("Median survival: Year " + t);
				break;
			}
		}
		
		// ------------------
		// 6. Lifetime risks
		// ------------------
		System.out.printf("Lifetime risk of death (background): %.4f\n", dead_bg[cycles]);
		System.out.printf("Lifetime risk of death (disease): %.4f\n", dead_disease[cycles]);
		
		// ------------------
		// 7. Save to CSV
		// ------------------
		try {
			PrintWriter writer = new PrintWriter("data/Q1c.csv");
			
			writer.println("Year,Well,Sick,Dead,Dead_BG,Dead_Disease");
			
			for (int t = 0; t <= cycles; t++) {
				writer.println(t + "," + well[t] + "," + sick[t] + "," + dead[t]
						+ "," + dead_bg[t] + "," + dead_disease[t]);
			}
			
			writer.close();
			System.out.println("CSV file saved.");
			
		} catch (FileNotFoundException e) {
			System.out.println("Error writing file.");
			e.printStackTrace();
		}
	}
}