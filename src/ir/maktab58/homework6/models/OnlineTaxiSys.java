package ir.maktab58.homework6.models;

import ir.maktab58.homework6.dataaccess.DriverDataBaseAccess;
import ir.maktab58.homework6.dataaccess.PassengerDataBaseAccess;
import ir.maktab58.homework6.exceptions.EmptyBufferException;
import ir.maktab58.homework6.exceptions.carexceptions.InvalidTypeOfVehicle;
import ir.maktab58.homework6.models.vehicles.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class OnlineTaxiSys implements OnlineTaxiInterface {
    Admin admin = new Admin();
    ArrayList<Driver> drivers = new ArrayList<>();
    ArrayList<Passenger> passengers = new ArrayList<>();
    PassengerDataBaseAccess passengerAccess = new PassengerDataBaseAccess();
    DriverDataBaseAccess driversAccess = new DriverDataBaseAccess();

    public void addAGroupOfDrivers() {
        System.out.println("Consider that only admin could add group of drivers to drivers list.");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your username.");
        String username = deleteLastSpaces(scanner.nextLine());
        System.out.println("Please enter your password.");
        String password = deleteLastSpaces(scanner.nextLine());
        drivers = driversAccess.getAllDrivers();
        boolean allowed = admin.isUserAdmin(username, password);
        if (allowed) {
            System.out.println("How many drivers would you like to add?");
            try {
                int numOfDrivers = Integer.parseInt(deleteLastSpaces(scanner.nextLine()));
                addEachDriver(numOfDrivers);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("You're not allowed to add drivers.");
        }
    }

    private void addEachDriver(int numOfDrivers){
        for (int i = 0; i < numOfDrivers; i++){
            Driver newDriver = getNewDriver();
            boolean existed = isDriverExisted(newDriver);
            boolean registeredCar = isVehicleRegistered(newDriver);
            boolean userNameTaken = isUserNameTaken(newDriver);
            if (existed){
                System.out.println("Sorry! This driver is already existed.");
            } else if (registeredCar) {
                System.out.println("Sorry! This car is already registered by another driver.");
            } else if (userNameTaken) {
                System.out.println("Sorry! username that you've entered is already taken. Please try again.");
            } else {
                boolean added = driversAccess.saveDriver(drivers, newDriver);
                if (added) {
                    drivers = driversAccess.getAllDrivers();
                    System.out.println("This driver " + newDriver + " is added successfully!");
                }
            }
        }
    }

    private boolean isUserNameTaken(Driver driver){
        for (Driver driver1 : drivers)
            if (driver1.getUsername().equals(driver.getUsername()))
                return true;

        return false;
    }

    private boolean isVehicleRegistered(Driver newDriver){
        for (Driver driver : drivers){
            if (driver.getVehicle().equals(newDriver.getVehicle())){
                return true;
            }
        }
        return false;
    }

    private boolean isDriverExisted(Driver driver){
        for (Driver driver1 : drivers){
            if (driver1.equals(driver))
                return true;
        }
        return false;
    }

    private Driver getNewDriver(){
        System.out.println("Please enter your username, password, firstName, lastName, birthDate(year, month, day)," +
                            "phoneNumber, nationalCode, vehicle(type, model, color, plateNumber).");
        Scanner scanner = new Scanner(System.in);
        String inputLine = deleteLastSpaces(scanner.nextLine());
        String[] tokens = inputLine.split(" ");
        String username = tokens[0]; String password = tokens[1];
        String firstName = tokens[2]; String lastName = tokens[3];
        checkEmptyBuffer(username, password, firstName, lastName);
        int year = Integer.parseInt(tokens[4]);
        int month = Integer.parseInt(tokens[5]);
        int day = Integer.parseInt(tokens[6]);
        Date birthDate = new Date(year - 1900, month - 1, day);
        long phoneNumber = Long.parseLong(tokens[7]);
        long nationalCode = Long.parseLong(tokens[8]);
        Vehicle vehicle = getVehicle(tokens[9], tokens[10], tokens[11], tokens[12]);
        return new Driver(drivers.size() + 1, username, password, firstName, lastName,
                birthDate, phoneNumber, nationalCode, vehicle);
    }

    private void checkEmptyBuffer(String username, String password, String firstName, String lastName){
        if (username.length() == 0)
            throw new EmptyBufferException("username can't be a zero-length string", 400);

        if (password.length() == 0)
            throw new EmptyBufferException("password can't be a zero-length string", 400);

        if (firstName.length() == 0)
            throw new EmptyBufferException("firstname can't be a zero-length string", 400);

        if (lastName.length() == 0)
            throw new EmptyBufferException("lastname can't be a zero-length string", 400);
    }

    private Vehicle getVehicle(String vehicleType, String model, String color, String plateNumber){
        if (vehicleType.equalsIgnoreCase(VehicleType.CAR.getVehicleType())) {
            VehicleInterface.validatePlateNumber(plateNumber, 400);
            VehicleInterface.checkInputBuffer(model, 400);
            VehicleInterface.checkInputBuffer(color, 400);
            return new Car(model, color, plateNumber);
        }
        //TODO for other types Of vehicle
        throw new InvalidTypeOfVehicle("Type of vehicle that you've entered is not acceptable.", 400);
    }

    public void driverSignupOrLogin(){
        drivers = driversAccess.getAllDrivers();
        System.out.println("Would you like to signup or login?");
        Scanner scanner = new Scanner(System.in);
        String choice = deleteLastSpaces(scanner.nextLine());
        if (choice.equalsIgnoreCase("signup")){
            try {
                int numOfDrivers = 1;
                addEachDriver(numOfDrivers);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        } else if (choice.equalsIgnoreCase("login")){
            driverLogin();
        } else {
            System.out.println("Invalid choice! \nEnter signup if you want to create account.");
            System.out.println("Enter login, if you've already had an account. \nPlease try again.");
        }
    }

    private void driverLogin(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your username.");
        String username = deleteLastSpaces(scanner.nextLine());
        System.out.println("Please enter your password.");
        String password = deleteLastSpaces(scanner.nextLine());
        for (Driver driver : drivers){
            if (driver.getUsername().equals(username) && driver.getPassword().equals(password)){
                System.out.println("Welcome back " + driver.getFirstName() + " " + driver.getLastName() + "!");
                return;
            }
        }
        System.out.println("Sorry! You've might've entered username or password wrong.");
    }

    public void addAGroupOfPassengers(){
        System.out.println("Consider that only admin could add group of drivers to drivers list.");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your username.");
        String username = deleteLastSpaces(scanner.nextLine());
        System.out.println("Please enter your password.");
        String password = deleteLastSpaces(scanner.nextLine());
        boolean allowed = admin.isUserAdmin(username, password);
        passengers = passengerAccess.getAllPassengers();
        if (allowed) {
            try {
                System.out.println("How many passengers would you like to add?");
                int numOfPassengers = Integer.parseInt(scanner.nextLine());
                addEachPassenger(numOfPassengers);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("You're not allowed to add passengers.");
        }
    }

    private void addEachPassenger(int numOfPassengers){
        for (int i = 0; i < numOfPassengers; i++){
            Passenger newPassenger = getPassenger();
            boolean existed = isPassengerExisted(newPassenger);
            boolean userNameTaken = isUserNameTaken(newPassenger);
            if (existed){
                System.out.println("Sorry! This passenger is already existed.");
            } else if (userNameTaken) {
                System.out.println("Sorry! username that you've entered is already taken. Please try again.");
            } else {
                boolean added = passengerAccess.savePassenger(passengers, newPassenger);
                if (added) {
                    passengers = passengerAccess.getAllPassengers();
                    System.out.println("This passenger " + newPassenger + " is added successfully!");
                }
            }
        }
    }

    private boolean isPassengerExisted(Passenger passenger){
        for (Passenger passenger1 : passengers)
            if (passenger1.equals(passenger))
                return true;

        return false;
    }

    private boolean isUserNameTaken(Passenger passenger){
        for (Passenger passenger1 : passengers)
            if (passenger1.getUsername().equals(passenger.getUsername()))
                return true;

        return false;
    }

    private Passenger getPassenger(){
        System.out.println("Please enter your username, password, firstName, lastName, " +
                "birthDate(year, month, day), phoneNumber, nationalCode");
        Scanner scanner = new Scanner(System.in);
        String inputLine = deleteLastSpaces(scanner.nextLine());
        String[] tokens = inputLine.split(" ");
        String username = tokens[0]; String password = tokens[1];
        String firstName = tokens[2]; String lastName = tokens[3];
        checkEmptyBuffer(username, password, firstName, lastName);
        int year = Integer.parseInt(tokens[4]);
        int month = Integer.parseInt(tokens[5]);
        int day = Integer.parseInt(tokens[6]);
        Date birthDate = new Date(year - 1900, month - 1, day);
        long phoneNumber = Long.parseLong(tokens[7]);
        long nationalCode = Long.parseLong(tokens[8]);
        return new Passenger(passengers.size() + 1, username, password, firstName, lastName,
                birthDate, phoneNumber, nationalCode);
    }

    public void passengerSignupOrLogin(){
        passengers = passengerAccess.getAllPassengers();
        System.out.println("Would you like to signup or login?");
        Scanner scanner = new Scanner(System.in);
        String choice = deleteLastSpaces(scanner.nextLine());
        if (choice.equalsIgnoreCase("signup")){
            try {
                int numOfPassengers = 1;
                addEachPassenger(numOfPassengers);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        } else if (choice.equalsIgnoreCase("login")){
            passengerLogin();;
        } else {
            System.out.println("Invalid choice! \nEnter signup if you want to create account.");
            System.out.println("Enter login, if you've already had an account. \nPlease try again.");
        }
    }

    private void passengerLogin(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your username.");
        String username = deleteLastSpaces(scanner.nextLine());
        System.out.println("Please enter your password.");
        String password = deleteLastSpaces(scanner.nextLine());
        for (Passenger passenger : passengers){
            if (passenger.getUsername().equals(username) && passenger.getPassword().equals(password)){
                System.out.println("Welcome back " + passenger.getFirstName() + " " + passenger.getLastName() + "!");
                return;
            }
        }
        System.out.println("Sorry! You've might've entered username or password wrong.");
    }

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
            System.out.println("You're not allowed to see drivers Information.");
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
