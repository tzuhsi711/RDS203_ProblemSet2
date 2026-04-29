package q6;

import java.util.Random;

public class Simulations {

    public static double runSimulation(int nPeople, Parameters params, Random rand) {

        double totalLifeYears = 0;

        for (int i = 0; i < nPeople; i++) {

            Person p = new Person();

            while (!p.state.equals("Dead") && p.age <= 100) {
                p.simulateYear(params, rand);
            }

            totalLifeYears += p.timeAlive;
        }

        return totalLifeYears / nPeople;
    }
}