package q5;

//Q5b: Natural history. Susceptible -> Exposed -> Infectious -> Recovered 

import java.util.ArrayList;

public class Person {

 public String state; // S, E, I, R
 public int daysInfected; // days since infection
 public ArrayList<Integer> contacts; //
 

 public Person() {
     this.state = "S"; // start susceptible
     this.daysInfected = 0;
     this.contacts = new ArrayList<>();
 }
}