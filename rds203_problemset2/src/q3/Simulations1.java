package q3;

import java.util.Random;

public class Simulations1 {

    public static double runSimulation(int nPeople) {

        Parameters1 params = new Parameters1();
        Random rand = new Random(203);

        double totalLifeYears = 0;

        for (int i = 0; i < nPeople; i++) {

            Person1 p = new Person1();

            while (!p.state.equals("Dead") && p.age <= 100) { // Q3c: run the model from birth to age 100
                p.simulateYear(params, rand); // Simulate one year of life, updating state and age
            }

            totalLifeYears += p.timeAlive; // Accumulate total life years across all people
        }

        return totalLifeYears / nPeople; // Average life expectancy across the population
    }
}




