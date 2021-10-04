package ir.maktab58.homework6.models;

import ir.maktab58.homework6.dataaccess.DriverDataBaseAccess;
import ir.maktab58.homework6.dataaccess.PassengerDataBaseAccess;

import java.util.ArrayList;
import java.util.Scanner;

public class OnlineTaxiSys implements OnlineTaxiInterface {
    Admin admin = new Admin();
    ArrayList<Driver> drivers = new ArrayList<>();
    ArrayList<Passenger> passengers = new ArrayList<>();
    PassengerDataBaseAccess passengerAccess = new PassengerDataBaseAccess();
    DriverDataBaseAccess driversAccess = new DriverDataBaseAccess();

    public void showAllDriversInformation() {
        System.out.println("Consider that only admin could see this list.");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your username.");
        String username = deleteLastSpaces(scanner.nextLine());
        System.out.println("Please enter your password.");
        String password = deleteLastSpaces(scanner.nextLine());
        boolean Allowed = admin.isUserAdmin(username, password);
        if (Allowed) {
            drivers = driversAccess.getAllDrivers();
            if (drivers.size() == 0)
                System.out.println("There is no driver to show you.");
            else {
                System.out.println("Drivers List: ");
                for (Driver driver : drivers) {
                    System.out.println(driver);
                }
            }
        } else {
            System.out.println("You're not allowed to see passengers Information.");
        }
    }

    public void showAllPassengersInformation() {
        System.out.println("Consider that only admin could see this list.");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your username.");
        String username = deleteLastSpaces(scanner.nextLine());
        System.out.println("Please enter your password.");
        String password = deleteLastSpaces(scanner.nextLine());
        boolean Allowed = admin.isUserAdmin(username, password);
        if (Allowed) {
            passengers = passengerAccess.getAllPassengers();
            if (passengers.size() == 0)
                System.out.println("There is no passenger to show you.");
            else {
                System.out.println("Passengers List: ");
                for (Passenger passenger : passengers) {
                    System.out.println(passenger);
                }
            }
        } else {
            System.out.println("You're not allowed to see passengers Information.");
        }
    }

    private String deleteLastSpaces(String inputLine) {
        if (inputLine.length() == 0) {
            System.out.println("Input buffer is empty.");
            return inputLine;
        }

        if (inputLine.charAt(inputLine.length() - 1) != ' ')
            return inputLine;

        if (inputLine.equals(" ")) {
            System.out.println("Input buffer is just a space char.");
            return inputLine;
        }

        inputLine = inputLine.substring(0, inputLine.length() - 2);
        return deleteLastSpaces(inputLine);
    }
}
