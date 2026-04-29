package q5;

// Q5a: Contact network
// For each person, sample a number of (unique) contacts and then 
// sample the contacts at random (without replacement) from among the other people on the island.

// Lecture 5, pg43

import java.util.*;

public class Network { 
	
    // ------------------
	// Construct network
    // ------------------
    public static ArrayList<Person> generateNetwork(int N, Random rand) {

        ArrayList<Person> population = new ArrayList<>(); // store all people

        // Create population
        for (int i = 0; i < N; i++) {
        	population.add(new Person()); // add a new person
        }

        // Assign contacts
        for (int i = 0; i < N; i++) {

            // Number of contacts ~ Poisson(λ = 5)
        	int n_contact = Math.min(samplePoisson(5, rand), N-1); // how many contacts this person has, cap at max possible contacts

            // HashSet ensures unique contacts (no duplicates)
            HashSet<Integer> contacts = new HashSet<>();

            // Keep sampling until the desired number of contacts is reached
            while (contacts.size() < n_contact) {
                int j = rand.nextInt(N); // randomly pick another person
                if (j != i) contacts.add(j); // no self-contact
            }

            // Add symmetric connections (undirected network)
            // Loop through all contacts (j) of person i
            for (int j : contacts) {
            	
            	// Avoid duplicate edges
            	if(!population.get(i).contacts.contains(j)) {
            	population.get(i).contacts.add(j); // person i interacts with person j
            	population.get(j).contacts.add(i); // person j ALSO interacts with person i
            	}
            }
        }

        return population; // return network
    }
    
    // ------------------
    // Poisson sampler (Lecture 5, pg43)
    // ------------------
    public static int samplePoisson(double lambda, Random rand) {
    	// Explanation:
    	// P(X=k) = (λ^k)*(e^-λ)/k!
    	// So
    	// P(X=0) = (λ^0)*(e^-λ)/0! = e^-λ 
    	// P(X=1) = (λ^1)*(e^-λ)/1! = (λ^1)*(e^-λ) = P(X=0) * λ = previous probability * (λ/1)
    	// P(X=2) = (λ^2)*(e^-λ)/2! = (λ^2)*(e^-λ)/2 = (λ^1)*(λ^1)*(e^-λ)*1/2 = P(X=1) * λ/2 = previous probability * (λ/2)
    	// Or: Next probs = Current probs * (λ/ next number)
    	

        double z = rand.nextDouble(); // Sample Z ~ U(0,1)

        double F = 0.0; // F(x) = 0
        int x = -1; // start at -1 so first step adds P(0) 

        double prob = Math.exp(-lambda); // P(0) = e^-λ -> current probs

        while (z > F) {

            F += prob; // add current probs

            x++; // move to next value

            // update probability: P(k) = P(k-1) * lambda / k
            prob *= lambda / (x + 1); // Next probs = Current probs * (λ/ next number)
        }

        return x; // return sampled value
    }
}

