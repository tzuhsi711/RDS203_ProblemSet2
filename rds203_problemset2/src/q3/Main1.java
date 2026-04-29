package q3;

// Q3c

public class Main1 {

    public static void main(String[] args) {

        int nPeople = 100000;

        double lifeExpectancy = Simulations1.runSimulation(nPeople);

        System.out.printf("Life Expectancy: %.2f", lifeExpectancy);
    }
}

