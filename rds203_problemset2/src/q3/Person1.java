package q3;

// Individual characteristics

import java.util.Random; 

public class Person1 {

    public int age;
    public String state; // well, sick , dead
    public int sickYears; // number of consecutive years sick
    public double timeAlive; // total years alive

    public Person1() {
        this.age = 0;
        this.state = "Well";
        this.sickYears = 0;
        this.timeAlive = 0;
    }
    
    // Simulate one year of life, updating state and age
    public void simulateYear(Parameters1 params, Random rand) {
    	

        if (state.equals("Dead")) return; // No changes if already dead

        double p_bg = params.getBackgroundMortality(age);

        if (state.equals("Well")) {

            double p_ws = params.getIncidence(age); // Probability of getting sick
            double p_wd = p_bg; // Background mortality applies to well people

            double u = rand.nextDouble();

            if (u < p_wd) {
                state = "Dead";
            } else if (u < p_wd + p_ws) { // Gets sick
                state = "Sick";
                sickYears = 0;
            }

        } else if (state.equals("Sick")) {

            double p_sw = params.recovery;
            
            // Disease-specific mortality declines with duration
            double p_disease = params.baseMort * Math.pow(0.75, sickYears); // mortDx * 0.75^x (x = years sick)

            // Combine independent risks
            double p_sd = 1 - (1 - p_bg) * (1 - p_disease);

            double u = rand.nextDouble();

            if (u < p_sd) { // Dies from either background or disease
                state = "Dead";
            } else if (u < p_sd + p_sw) { // Recovers
                state = "Well";
                sickYears = 0;
            } else {
                sickYears++;
            }
        }

        age++;
        timeAlive++;
    }
}


