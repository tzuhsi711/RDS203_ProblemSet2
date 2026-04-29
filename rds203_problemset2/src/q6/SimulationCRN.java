package q6;

import java.util.Random;

public class SimulationCRN {

    public static double[] runSimulationCRN(int nPeople, Parameters paramsBase, Parameters paramsInt, Random rand) {

        double totalBase = 0;
        double totalInt = 0;

        for (int i = 0; i < nPeople; i++) {

            Person pBase = new Person(); // baseline
            Person pInt = new Person(); // intervention

            while ((!pBase.state.equals("Dead") || !pInt.state.equals("Dead"))
                    && (pBase.age <= 100 || pInt.age <= 100)) {

                // Shared random number
                double u = rand.nextDouble();

                // Simulate both with the shared random number
                simulateYearCRN(pBase, paramsBase, u);
                simulateYearCRN(pInt, paramsInt, u);
            }

            totalBase += pBase.timeAlive;
            totalInt += pInt.timeAlive;
        }

        return new double[]{
            totalBase / nPeople,
            totalInt / nPeople
        };
    }

    // Refer to Simulations
    private static void simulateYearCRN(Person p, Parameters params, double u) {

        if (p.state.equals("Dead")) return;
        
        if (p.age >= 100) {
        	p.state = "Dead";
        	return;
        }

        double p_bg = params.getBackgroundMortality(p.age);

        if (p.state.equals("Well")) {

            double p_ws = params.getIncidence(p.age);
            double p_wd = p_bg;

            if (u < p_wd) {
                p.state = "Dead";
            } else if (u < p_wd + p_ws) {
                p.state = "Sick";
                p.sickYears = 0;
            }

        } else if (p.state.equals("Sick")) {

            double p_sw = params.recovery;
            double p_disease = params.baseMort * Math.pow(0.75, p.sickYears);
            double p_sd = 1 - (1 - p_bg) * (1 - p_disease);

            if (u < p_sd) {
                p.state = "Dead";
            } else if (u < p_sd + p_sw) {
                p.state = "Well";
                p.sickYears = 0;
            } else {
                p.sickYears++;
            }
        }

        p.age++;
        p.timeAlive++;
    }
}
