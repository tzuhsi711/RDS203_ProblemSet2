package q7;

// Individual characteristics

import java.util.Random;

public class Person {

    public int age;
    public String state; // Well, Sick, Dead
    public int sickYears; // number of consecutive years sick
    public double timeAlive; // total years alive
    public String prevState; // previous state 

    public Person() {
        this.age = 0;
        this.state = "Well";
        this.prevState = "Well";
        this.sickYears = 0;
        this.timeAlive = 0;
    }

    // Simulate one year of life
    public void simulateYear(Parameters params, Random rand) {

        if (state.equals("Dead")) return; // No changes if already dead

        prevState = state; // store previous state 

        double p_bg = params.getBackgroundMortality(age);

        // --------------
        // Well
        // --------------
        if (state.equals("Well")) {

            double p_ws = params.getIncidence(age); // incidence from calibrated params
            double p_wd = p_bg; // background mortality

            // Normalise if needed
            double total = p_ws + p_wd;
            if (total > 1.0) {
                p_ws /= total;
                p_wd /= total;
            }

            double u = rand.nextDouble();

            if (u < p_wd) {
                state = "Dead";
            } else if (u < p_wd + p_ws) {
                state = "Sick";
                sickYears = 0;
            }

        }

        // --------------
        // Sick
        // --------------
        else if (state.equals("Sick")) {

            double p_sw = params.recovery_curr; // calibrated recovery

            // calibrated disease mortality
            double p_disease = params.mortDx_curr * Math.pow(0.75, sickYears);

            // combine risks
            double p_sd = 1 - (1 - p_bg) * (1 - p_disease);

            // Normalise if needed
            double total = p_sd + p_sw;
            if (total > 1.0) {
                p_sd /= total;
                p_sw /= total;
            }

            double u = rand.nextDouble();

            if (u < p_sd) {
                state = "Dead";
            } else if (u < p_sd + p_sw) {
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