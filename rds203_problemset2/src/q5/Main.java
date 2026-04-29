package q5;

//Q5e: Run 10,000 simulations and summarise outputs

import java.util.*;
import java.io.PrintWriter;

public class Main {

 public static void main(String[] args) {

     int nSim = 10000;
     Random rand = new Random(203); 

     int[] totalInf = new int[nSim]; // total infections in simulation i
     int[] duration = new int[nSim]; // number of days epidemic lasted

     int noSpread = 0; // count number of simulations

     // ---------------------------
     // Q5e: run simulations
     // ---------------------------
     for (int i = 0; i < nSim; i++) {

         int[] result = Simulation.runSimulation(rand);

         totalInf[i] = result[0];
         duration[i] = result[1];

         // If only 1 infected -> Epidemic didn't spread
         if (result[0] == 1) noSpread++;
     }

     // Probability no epidemic
     double probNoEpi = (double) noSpread / nSim;

     System.out.printf("Probability no epidemic: %.4f%n", probNoEpi);
     
     // Save CSV file
     try {
    	 PrintWriter writer = new PrintWriter("data/Q5e.csv");
    	 
    	 writer.println("TotalInfection,Duration"); // header
    	 
    	 for(int i = 0; i < nSim; i++) {
    		 writer.println(totalInf[i] + "," + duration[i]);
    	 }
    	 
    	 writer.close();
    	 System.out.println("CSV file saved.");
    	 
     }catch (Exception e) {
    	 e.printStackTrace();
     }
     
 }
}












