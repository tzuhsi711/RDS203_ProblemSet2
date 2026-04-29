package q6;

// Supplies all parameter values for the current simulation

import java.io.BufferedReader;
import java.io.FileReader;

public class Parameters {
	
	// 0. Fixed parameters 
    public double recovery = 0.30;
    public double baseMort = 0.20;
    
    // 1. Load life table
    private double[] lifeTable = new double[101];
    // Function to read lifetable 
    private void loadLifeTable(String filePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                int age = Integer.parseInt(parts[0]);
                double mx = Double.parseDouble(parts[1]);

                double prob = 1 - Math.exp(-mx);

                lifeTable[age] = prob;
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Constructor loads the life table when a Parameters object is created
    public Parameters() {
        loadLifeTable("data/us_lifetable_2022.csv");
    }

    // 2. Incidence
    public double getIncidence(int t) {
        return 0.002 + 0.001 * t; // Q1
    }

    // 3. Background mortality 
    public double getBackgroundMortality(int age) {
        return lifeTable[age];
    }
}


