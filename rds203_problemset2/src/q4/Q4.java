package q4;

// Lecture 5, pg48

import java.util.*;

public class Q4 {
	
	// ------------------
    // 1. Event class
	// ------------------
    static class Event implements Comparable<Event> { // compare events and sort by time
        double time; // event time
        int type; // 0 = arrival, 1 = discharge

        Event(double time, int type) {
            this.time = time; 
            this.type = type;
        }

        public int compareTo(Event other) {
            return Double.compare(this.time, other.time);
        }
    }
    
    // ------------------
    // 2. Exponential inverse CDF (Lecture 5, pg48)
    // ------------------
    public static double expSample(double rate, Random rand) {
    	
    	// X ~ Expo(1)
    	// F^-1(x) = -log(1-p) / lambda
        double p = rand.nextDouble();
        return -Math.log(1 - p) / rate;
    }
    
    // ------------------
    // 3. Main simulation 
    // ------------------
    public static void main(String[] args) {

        // Parameters
        double arrivalRate = 3.0; // Poisson rate (mean = 3 arrivals/day)
        double losRate = 1.0 / 6.0; // Exponential rate (mean = 6 days) (each patient stays for an average of 6 days)
        double T = 365.0; // Simulation time (1 year)

        int nSim = 1000; // Number of simulations

        Random rand = new Random(203);

        // Store max occupancy from each simulation
        int[] maxOccupancies = new int[nSim];

        // Run simulations
        for (int sim = 0; sim < nSim; sim++) {

            PriorityQueue<Event> eventList = new PriorityQueue<>(); // priority queue to manage events by time

            double t = 0.0;
            int currentPatients = 0;
            int maxPatients = 0;

            // Schedule first arrival
            double firstArrival = t + expSample(arrivalRate, rand); // expSample generates a random arrival time based on the exponential distribution
            eventList.add(new Event(firstArrival, 0));

            while (!eventList.isEmpty()) {

                Event e = eventList.poll(); // poll = remove and return the head of the queue (the earliest event)
                t = e.time;

                if (t > T) break; // stop if we exceed the simulation time

                if (e.type == 0) { // Arrival event
                	
                    currentPatients++;

                    if (currentPatients > maxPatients) {
                        maxPatients = currentPatients;
                    }

                    // Schedule next arrival
                    double nextArrival = t + expSample(arrivalRate, rand);
                    eventList.add(new Event(nextArrival, 0));

                    // Schedule discharge
                    double dischargeTime = t + expSample(losRate, rand);
                    eventList.add(new Event(dischargeTime, 1));

                } else {
                    // Discharge event
                    currentPatients--;
                }
            }

            maxOccupancies[sim] = maxPatients; // store the maximum occupancy for this simulation
        }

        // Calculate the 95th percentile of max occupancies
        Arrays.sort(maxOccupancies);

        int index95 = (int) (0.95 * nSim); 
        int bedsNeeded = maxOccupancies[index95];

        System.out.println("Number of beds needed: " + bedsNeeded);
    }

}




