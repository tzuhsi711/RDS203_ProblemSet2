package q3;

//// Q3d
//// each simulation uses a different set of parameters sampled from distributions
//// (Q3e: Run 100 iterations of the model sampling each parameter independently)


import java.util.Random;

public class Simulations2 {

    public static double runSimulation(int nPeople, Parameters2 params, Random rand) {

        double totalLifeYears = 0;

        for (int i = 0; i < nPeople; i++) {

            Person2 p = new Person2();

            while (!p.state.equals("Dead") && p.age <= 100) { // not dead, age less than or equal to 100
                p.simulateYear(params, rand);
            }

            totalLifeYears += p.timeAlive; // count as Alive
        }

        return totalLifeYears / nPeople; // average life expectancy
    }
}


