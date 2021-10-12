package ir.maktab58.homework6;

import ir.maktab58.homework6.models.OnlineTaxiSys;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        OnlineTaxiSys onlineTaxiSys = new OnlineTaxiSys();
        Scanner inputLine = new Scanner(System.in);
        while (true){
            System.out.println("**********Welcome**********");
            System.out.println("1) Add a group of drivers");
            System.out.println("2) Add a group of passengers");
            System.out.println("3) Driver signup or login");
            System.out.println("4) Passenger signup or login");
            System.out.println("5) Show Ongoing Travels");
            System.out.println("6) show a list of drivers");
            System.out.println("7) show a list of passengers");
            System.out.println("8) exit");
            String choice = inputLine.nextLine();
            if (choice.equals("1")) {
                onlineTaxiSys.addAGroupOfDrivers();
            } else if (choice.equals("2")) {
                onlineTaxiSys.addAGroupOfPassengers();
            } else if (choice.equals("3")) {
                onlineTaxiSys.driverSignupOrLogin();
            } else if (choice.equals("4")) {
                onlineTaxiSys.passengerSignupOrLogin();
            } else if (choice.equals("5")){
                onlineTaxiSys.showOngoingTravels();
            } else if (choice.equals("6")) {
                onlineTaxiSys.showAllDriversInformation();
            } else if (choice.equals("7")) {
                onlineTaxiSys.showAllPassengersInformation();
            } else if (choice.equals("8")) {
                break;
            } else {
                System.out.println("Invalid input command. Your choice must be an integer between 1 to 7.");
            }
        }
    }
}
