package ir.maktab58.homework6.view;

import ir.maktab58.homework6.exceptions.OnlineTaxiSysEx;
import ir.maktab58.homework6.service.OnlineTaxiService;

import java.util.Scanner;

/**
 * @author Taban Soleymani
 */
public class OnlineTaxiSys {
    private final Scanner scanner = new Scanner(System.in);
    private final OnlineTaxiService onlineTaxiService = new OnlineTaxiService();

    public void showMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("**********Welcome**********\n" +
                    "1) Add a group of drivers\n" +
                    "2) Add a group of passengers\n +" +
                    "3) Driver signup or login\n" +
                    "4) Passenger signup or login\n" +
                    "5) Show Ongoing Travels\n" +
                    "6) Show a list of drivers\n" +
                    "7) Show a list of passengers\n" +
                    "8) Exit");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addAGroupOfDrivers();
                case "2" -> addAGroupOfPassengers();
                case "3" -> driverSignupOrLogin();
                case "4" -> passengerSignupOrLogin();
                case "5" -> showOngoingTravels();
                case "6" -> showAllDriversInformation();
                case "7" -> showAllPassengersInformation();
                case "8" -> exit = true;
                default -> throw OnlineTaxiSysEx.builder()
                        .message("Your choice must be between 1 to 8.")
                        .errorCode(400).build();
            }
        }
    }

    private void showAllPassengersInformation() {

    }

    private void showAllDriversInformation() {
    }

    private void showOngoingTravels() {

    }

    private void passengerSignupOrLogin() {

    }

    private void driverSignupOrLogin() {

    }

    private void addAGroupOfPassengers() {

    }

    private void addAGroupOfDrivers() {

    }
}
