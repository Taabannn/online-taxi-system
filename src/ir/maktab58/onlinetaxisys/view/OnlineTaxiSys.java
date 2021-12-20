package ir.maktab58.onlinetaxisys.view;

import ir.maktab58.onlinetaxisys.enumeration.PaymentMode;
import ir.maktab58.onlinetaxisys.exceptions.OnlineTaxiSysEx;
import ir.maktab58.onlinetaxisys.models.Admin;
import ir.maktab58.onlinetaxisys.models.Driver;
import ir.maktab58.onlinetaxisys.models.Passenger;
import ir.maktab58.onlinetaxisys.models.places.Coordinates;
import ir.maktab58.onlinetaxisys.service.OnlineTaxiService;

import java.util.List;
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
            System.out.println("""
                    **********Welcome**********
                    1) Add a group of drivers
                    2) Add a group of passengers
                    3) Driver signup or login
                    4) Passenger signup or login
                    5) Show Ongoing Travels
                    6) Show a list of drivers
                    7) Show a list of passengers
                    8) Exit""");
            String choice = scanner.nextLine().trim();
            try {
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
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void showAllPassengersInformation() {
        System.out.println("Only admin can see passengersInfo list");
        isUserAllowed();
        List<Passenger> passengerList = onlineTaxiService.getAllPassengers();
        if (passengerList.size() == 0) {
            System.out.println("There is no passenger to show...");
            return;
        }
        passengerList.forEach(System.out::println);
    }

    private void showAllDriversInformation() {
        System.out.println("Only admin can see driversInfo list");
        isUserAllowed();
        List<Driver> driverList = onlineTaxiService.getAllDrivers();
        if (driverList.size() == 0) {
            System.out.println("There is no driver to show...");
            return;
        }
        driverList.forEach(System.out::println);
    }

    private void showOngoingTravels() {

    }

    private void passengerSignupOrLogin() {
        System.out.println("Would you like to signup or login?");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().trim();
        try {
            if (choice.equalsIgnoreCase("signup")) {
                passengerSignup();
            } else if (choice.equalsIgnoreCase("login")) {
                passengerLogin();
            } else {
                throw OnlineTaxiSysEx.builder()
                        .message("Your choice must login or signup")
                        .errorCode(400).build();
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void passengerLogin() {
        System.out.println("Please enter your username: ");
        String username = scanner.nextLine().trim();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine().trim();
        int passengerId = onlineTaxiService.getPassengerId(username, password);
        if (passengerId != 0)
            showPassengerMenu(passengerId);
        else
            throw OnlineTaxiSysEx.builder()
                    .message("You might have entered wrong username or password")
                    .errorCode(400).build();
    }

    private void passengerSignup() {
        int passengerId = addANewPassenger();
        showPassengerMenu(passengerId);
    }

    private void showPassengerMenu(int passengerId) {
        boolean stateOfAttendance = onlineTaxiService.getPassengerStateOfAttendance(passengerId);
        if (!stateOfAttendance)
            System.out.println("Sorry you're still in travel.");
        else {
            boolean exit = false;
            while (stateOfAttendance && !exit) {
                stateOfAttendance = onlineTaxiService.getPassengerStateOfAttendance(passengerId);
                System.out.println("""
                        **********Passenger Menu**********
                        1) Apply for a new travel (pay cash)
                        2) Apply for a new travel (pay from your balance)3) Deposit your balance
                        4) Back to main menu""");
                String choice = scanner.nextLine().trim();
                try {
                    switch (choice) {
                        case "1" -> applyForTravelPayCash(passengerId);
                        case "2" -> applyForTravelPayFromYourBalance(passengerId);
                        case "3" -> depositPassengerWallet(passengerId);
                        case "4" -> exit = true;
                        default -> throw OnlineTaxiSysEx.builder()
                                .message("Invalid input command. Your choice must be an integer between 1 to 4.")
                                .errorCode(400).build();
                    }
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void depositPassengerWallet(int passengerId) {
        System.out.println("How much do you want deposit your balance?");
        String chargeStr = scanner.nextLine().trim();
        long charge = Long.parseLong(chargeStr);
        onlineTaxiService.depositPassengerWallet(passengerId, charge);
        System.out.println("Your balance has updated successfully.");
    }

    private void applyForTravelPayFromYourBalance(int passengerId) {
        getSourceAndDestination(passengerId, PaymentMode.ONLINE);
    }

    private void applyForTravelPayCash(int passengerId) {
        getSourceAndDestination(passengerId, PaymentMode.CASH);
    }

    private void getSourceAndDestination(int passengerId, PaymentMode paymentMode){
        System.out.println("Please enter source: ");
        String source = scanner.nextLine().trim();
        System.out.println("Please enter destination: ");
        String destination = scanner.nextLine().trim();
        makeATrip(passengerId, paymentMode, source, destination);
    }

    private void makeATrip(int passengerId, PaymentMode paymentMode, String source, String destination) {
        String[] sourceXY = source.split(" ");
        Coordinates sourceCoordinate = Coordinates.builder()
                .withX(Integer.parseInt(sourceXY[0]))
                .withY(Integer.parseInt(sourceXY[1])).build();
        String[] destXY = destination.split(" ");
        Coordinates desCoordinate = Coordinates.builder()
                .withX(Integer.parseInt(destXY[0]))
                .withY(Integer.parseInt(destXY[1])).build();
        long cost = onlineTaxiService.calculateCost(sourceCoordinate, desCoordinate);
        System.out.println("The cost is: " + cost + ". Do you want to continue?");
        String choice = scanner.nextLine().trim();
        if (choice.equalsIgnoreCase("yes")) {
            if (paymentMode.equals(PaymentMode.CASH)) {
                onlineTaxiService.assignDriverAndSave(passengerId, paymentMode, sourceCoordinate, desCoordinate);
            }
        }


    }

    private void driverSignupOrLogin() {
        System.out.println("Would you like to signup or login?");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().trim();
        try {
            if (choice.equalsIgnoreCase("signup")) {
                driverSignup();
            } else if (choice.equalsIgnoreCase("login")) {
                driverLogin();
            } else {
                throw OnlineTaxiSysEx.builder()
                        .message("Your choice must login or signup")
                        .errorCode(400).build();
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void driverLogin() {
        System.out.println("Please enter your username: ");
        String username = scanner.nextLine().trim();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine().trim();
        int driverId = onlineTaxiService.getDriverId(username, password);
        if (driverId != 0)
            showDriverMenu(driverId);
        else
            throw OnlineTaxiSysEx.builder()
                    .message("You might have entered wrong username or password")
                    .errorCode(400).build();
    }

    private void driverSignup() {
        int driverId = addANewDriver();
        showDriverMenu(driverId);
    }

    private void showDriverMenu(int driverId) {

    }

    private void addAGroupOfPassengers() {
        isUserAllowed();
        System.out.println("How many Passengers would you like to add?");
        int numOfPassengers = Integer.parseInt(scanner.nextLine().trim());
        for (int i = 0; i < numOfPassengers; i++) {
            addANewPassenger();
        }
    }

    private void addAGroupOfDrivers() {
        isUserAllowed();
        System.out.println("How many Drivers would you like to add?");
        int numOfDivers = Integer.parseInt(scanner.nextLine().trim());
        for (int i = 0; i < numOfDivers; i++) {
            addANewDriver();
        }
    }

    private int addANewPassenger() {
        System.out.println("Please enter name, family, username, password, national-code, initial balance.");
        String inputLine = scanner.nextLine().trim();
        return onlineTaxiService.addNewPassenger(inputLine);
    }

    private int addANewDriver() {
        System.out.println("Please enter name, family, username, password, national-code, vehicle-type, plate-number, model, color and current location.");
        String inputLine = scanner.nextLine().trim();
        return onlineTaxiService.addNewDriver(inputLine);
    }

    private void isUserAllowed() {
        System.out.println("Please enter your username and password.");
        String[] tokens = scanner.nextLine().trim().split(" ");
        String username = tokens[0];
        String password = tokens[1];
        boolean isAllowed = Admin.getInstance().isUserAdmin(username, password);
        if (!isAllowed)
            throw OnlineTaxiSysEx.builder()
                    .message("Wrong user or pass for admin\n" +
                            "You're not allowed to add passengers or drivers.")
                            .errorCode(400).build();
    }
}
