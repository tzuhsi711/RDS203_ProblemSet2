package q9;

import java.util.*;

public class Simulation {

    public static int[] runSimulationWithP(double p, Random rand) {

        int N = 100;

        ArrayList<Person> people = Network.generateNetwork(N, rand);

        // Initialise index case
        people.get(0).state = "E";

        int totalInfected = 1;
        int days = 0;

        while (true) {

            HashSet<Integer> newExposed = new HashSet<>();

            // Transmission
            for (int i = 0; i < N; i++) {

                Person person = people.get(i);

                if (person.state.equals("I")) {

                    for (int j : person.contacts) {

                        Person contact = people.get(j);

                        if (contact.state.equals("S")) {

                            if (rand.nextDouble() < p) {
                                newExposed.add(j);
                            }
                        }
                    }
                }
            }

            // Apply infections
            for (int j : newExposed) {
                if (people.get(j).state.equals("S")) {
                    people.get(j).state = "E";
                    people.get(j).daysInfected = 0;
                    totalInfected++;
                }
            }

            // Natural history
            int active = 0;

            for (Person person : people) {

                if (person.state.equals("E") || person.state.equals("I")) {

                    active++;
                    person.daysInfected++;

                    if (person.state.equals("E") && person.daysInfected >= 2) {
                        person.state = "I";
                    }

                    if (person.daysInfected >= 7) {
                        person.state = "R";
                    }
                }
            }

            days++;

            if (active == 0) break;
        }

        return new int[]{totalInfected, days};
    }
}


