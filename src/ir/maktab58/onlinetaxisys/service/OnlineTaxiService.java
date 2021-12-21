package ir.maktab58.onlinetaxisys.service;

import ir.maktab58.onlinetaxisys.enumeration.PaymentMode;
import ir.maktab58.onlinetaxisys.enumeration.StateOfAttendance;
import ir.maktab58.onlinetaxisys.enumeration.TravelStatus;
import ir.maktab58.onlinetaxisys.exceptions.OnlineTaxiSysEx;
import ir.maktab58.onlinetaxisys.models.Driver;
import ir.maktab58.onlinetaxisys.models.Passenger;
import ir.maktab58.onlinetaxisys.models.Travel;
import ir.maktab58.onlinetaxisys.models.Coordinate;
import ir.maktab58.onlinetaxisys.models.vehicle.Vehicle;
import ir.maktab58.onlinetaxisys.service.factory.VehicleFactory;
import ir.maktab58.onlinetaxisys.service.validator.NationalCodeValidator;
import ir.maktab58.onlinetaxisys.service.validator.UserAndPassValidator;

import java.util.ArrayList;
import java.util.List;

public class OnlineTaxiService implements OnlineTaxiInterface {
    private final PassengerService passengerService = new PassengerService();
    private final DriverService driverService = new DriverService();
    private final TravelService travelService = new TravelService();
    private static final long COST_PER_METER = 1000;
    List<Driver> drivers = new ArrayList<>();
    ArrayList<Passenger> passengers = new ArrayList<>();
    ArrayList<Travel> travels = new ArrayList<>();

    public List<Passenger> getAllPassengers() {
        return passengerService.getAllPassengers();
    }

    public List<Driver> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    public int addNewPassenger(String inputLine) {
        String[] tokens = inputLine.split(" ");
        String name = tokens[0];
        String family = tokens[1];
        String username = tokens[2];
        String password = tokens[3];
        String nationalCode = tokens[4];
        long initialBalance = Long.parseLong(tokens[5]);
        validateUserPassNationalCode(username, password, nationalCode);
        checkPassengerExisted(username, nationalCode);
        Passenger passenger = Passenger.builder()
                .withFirstName(name)
                .withLastName(family)
                .withUsername(username)
                .withPassword(password)
                .withNationalCode(nationalCode)
                .withBalance(initialBalance).build();
        return passengerService.saveNewPassenger(passenger);
    }

    private void checkPassengerExisted(String username, String nationalCode) {
        List<Passenger> passengerByUsername = passengerService.getPassengerByUsername(username);
        List<Passenger> passengerList = passengerService.getPassengerByNationalCode(nationalCode);
        if (passengerByUsername.size() != 0)
            throw OnlineTaxiSysEx.builder()
                    .message("Username is already taken")
                    .errorCode(400).build();
        if (passengerList.size() != 0)
            throw OnlineTaxiSysEx.builder()
                    .message("This national code is existed")
                    .errorCode(400).build();
    }

    public void validateUserPassNationalCode(String username, String password, String nationalCode) {
        boolean userAndPassValid = UserAndPassValidator.getSingletonInstance().isUserAndPassValid(username, password);
        if (!userAndPassValid)
            throw OnlineTaxiSysEx.builder()
                    .message("Invalid user or pass")
                    .errorCode(400).build();
        boolean nationalCodeValid = NationalCodeValidator.getSingletonInstance().isNationalCodeValid(nationalCode);
        if (!nationalCodeValid)
            throw OnlineTaxiSysEx.builder()
                    .message("Invalid national code")
                    .errorCode(400).build();
    }

    public int addNewDriver(String inputLine) {
        String[] tokens = inputLine.split(" ");
        String name = tokens[0];
        String family = tokens[1];
        String username = tokens[2];
        String password = tokens[3];
        String nationalCode = tokens[4];
        String vehicleType = tokens[5];
        String plateNumber = tokens[6];
        String model = tokens[7];
        String color = tokens[8];
        int x = Integer.parseInt(tokens[9]);
        int y = Integer.parseInt(tokens[10]);
        Coordinate currentLocation = new Coordinate(x, y);
        validateUserPassNationalCode(username, password, nationalCode);
        checkDriverExisted(username, nationalCode);
        Vehicle vehicle = VehicleFactory.getVehicleType(vehicleType, plateNumber, model, color);
        Driver driver = Driver.builder()
                .withFirstName(name)
                .withLastName(family)
                .withUsername(username)
                .withPassword(password)
                .withNationalCode(nationalCode)
                .withVehicle(vehicle)
                .withCurrentLocation(currentLocation).build();
        return driverService.saveNewDriver(driver);
    }

    private void checkDriverExisted(String username, String nationalCode) {
        List<Driver> driverByUsername = driverService.getDriverByUsername(username);
        List<Driver> driverList = driverService.getDriverByNationalCode(nationalCode);
        if (driverByUsername.size() != 0)
            throw OnlineTaxiSysEx.builder()
                    .message("Username is already taken")
                    .errorCode(400).build();
        if (driverList.size() != 0)
            throw OnlineTaxiSysEx.builder()
                    .message("This national code is existed")
                    .errorCode(400).build();
    }

    public int getPassengerId(String username, String password) {
        return passengerService.getPassengerIdByUserAndPass(username, password);
    }

    public int getDriverId(String username, String password) {
        return driverService.getDriverIdByUserAndPass(username, password);
    }

    public boolean getPassengerStateOfAttendance(int passengerId) {
        Passenger passenger = passengerService.getPassengerById(passengerId);
        return passenger.getStateOfAttendance().equals(StateOfAttendance.WAITING_FOR_TRAVEL);
    }

    public void depositPassengerWallet(int passengerId, long charge) {
        passengerService.updatePassengerWallet(passengerId, charge);
    }

    public long calculateCost(Coordinate sourceCoordinate, Coordinate desCoordinate) {
        double sqrt = Math.sqrt((desCoordinate.getY() - sourceCoordinate.getY()) ^ 2 + (desCoordinate.getX() - sourceCoordinate.getX()) ^ 2);
        return (long) (sqrt * COST_PER_METER);
    }

    public void assignDriverAndSave(int passengerId, PaymentMode paymentMode, Coordinate sourceCoordinate, Coordinate desCoordinate) {
        Passenger passenger = passengerService.getPassengerById(passengerId);
        drivers = driverService.getAllDrivers();
        Driver driver = findNearestDrive(sourceCoordinate, desCoordinate);
        driver.setStateOfAttendance(StateOfAttendance.IN_TRAVEL);
        passenger.setStateOfAttendance(StateOfAttendance.IN_TRAVEL);
        Travel travel = Travel.builder()
                .withDriver(driver)
                .withPassenger(passenger)
                .withIsPaid(false)
                .withSource(sourceCoordinate)
                .withDestination(desCoordinate)
                .withPaymentMode(paymentMode)
                .withStatus(TravelStatus.UNFINISHED).build();
        travelService.saveTravel(travel);
    }

    private Driver findNearestDrive(Coordinate sourceCoordinate, Coordinate desCoordinate) {
        if (drivers.size() == 0)
            throw OnlineTaxiSysEx.builder()
                    .message("There is no driver.")
                    .errorCode(500).build();
        Coordinate currentLocation = drivers.get(0).getCurrentLocation();
        //double distance = Math.sqrt();
        //drivers.forEach();
        return null;
    }


    /*
    private void showDriverMenu(Driver driver){
        passengers = passengerAccess.getAllPassengers();
        drivers = driversAccess.getAllDrivers();
        travels = travelAccess.getAllTravels(passengers, drivers);
        boolean stateOfAttendance = driver.isStateOfAttendance();
        Scanner scanner = new Scanner(System.in);
        if(!stateOfAttendance)
            System.out.println("You're still waiting for travel.");
        boolean confirmed = calcPassengerStateOfAttendance(driver);
        if (!confirmed)
            confirmed = confirmOrCancelTravel(driver);
        while (stateOfAttendance && confirmed) {
            System.out.println("**********Driver Menu**********");
            System.out.println("1) Confirm passenger payment");
            System.out.println("2) Travel is finished");
            System.out.println("3) Back to main menu");
            String choice = deleteLastSpaces(scanner.nextLine());
            if (choice.equals("1")) {
                confirmPassengerPayment(driver);
            } else if (choice.equals("2")) {
                confirmTravelIsFinished(driver);
            } else if (choice.equals("3")) {
                break;
            } else {
                System.out.println("Invalid input command. Your choice must be an integer between 1 to 3.");
            }
        }
    }

    private boolean confirmOrCancelTravel(Driver driver){
        System.out.println("First of all you must confirmed assigned travel.");
        System.out.println("Do you want to cancel or confirm travel?");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().trim();
        if (choice.equalsIgnoreCase("confirm")){
            for (Travel travel : travels) {
                if (travel.getDriver().equals(driver) && !travel.isStatus()){
                    travel.getPassenger().setStateOfAttendance(true);
                    passengerAccess.updatePassengerStateOfAttendance(travel.getPassenger());
                }
            }
            return true;
        } else if(choice.equalsIgnoreCase("cancel")) {
            for (Travel travel : travels) {
                if (travel.getDriver().equals(driver) && !travel.isStatus()){
                    driver.setStateOfAttendance(false);
                    driversAccess.updateDriverStateOfAttendance(driver);
                    if (travel.isPaymentMode() && travel.isPaid()){
                        driver.setWallet(driver.getWallet() - travel.getCost());
                        driversAccess.updateDriverWallet(driver);
                    }
                    travel.assignNewDriver(drivers.indexOf(driver), drivers);
                    travelAccess.updateDriver(travel);
                    driversAccess.updateDriverStateOfAttendance(travel.getDriver());
                    if (travel.isPaymentMode() && travel.isPaid()){
                        driver.setWallet(travel.getDriver().getWallet() + travel.getCost());
                        driversAccess.updateDriverWallet(travel.getDriver());
                    }
                }
            }
            return false;
        } else {
            System.out.println("Invalid Input, please try later.");
            return false;
        }
    }

    private boolean calcPassengerStateOfAttendance (Driver driver){
        for (Travel travel : travels) {
            if (travel.getDriver().equals(driver) && !travel.isStatus()) {
                return travel.getPassenger().isStateOfAttendance();
            }
        }
        return false;
    }

    private void confirmTravelIsFinished(Driver driver){
        passengers = passengerAccess.getAllPassengers();
        drivers = driversAccess.getAllDrivers();
        travels = travelAccess.getAllTravels(passengers, drivers);
        for (Travel travel : travels) {
            if (travel.getDriver().equals(driver) && !travel.isStatus()){
                if (travel.isPaid()) {
                    changeDriverAndPassengerStatus(travel, travel.getDriver());
                    return;
                }
                System.out.println("Sorry. You didn't receive payments, so you can't finish travel.");
            }
        }
    }

    private void changeDriverAndPassengerStatus(Travel travel, Driver driver){
        System.out.println("Are you sure to finish travel?");
        Scanner scanner = new Scanner(System.in);
        if (deleteLastSpaces(scanner.nextLine()).equalsIgnoreCase("Yes")) {
            travel.setStatus(true);
            int result = travelAccess.updateStatusOfTravel(travel);
            if (result == 1){
                driver.setStateOfAttendance(false);
                driversAccess.updateDriverStateOfAttendance(driver);
                driver.setCurrentLocation(new Coordinates(travel.getDestination()));
                driversAccess.updateDriverCurrentLocation(driver);
                travel.getPassenger().setStateOfAttendance(false);
                passengerAccess.updatePassengerStateOfAttendance(travel.getPassenger());
                System.out.println("Your travel is finished successfully.");
            } else
                System.out.println("Sorry! unable to process. Please try again.");
        }
    }

    private void confirmPassengerPayment(Driver driver){
        passengers = passengerAccess.getAllPassengers();
        drivers = driversAccess.getAllDrivers();
        travels = travelAccess.getAllTravels(passengers, drivers);
        for (Travel travel : travels) {
            if (travel.getDriver().equals(driver)){
                if (!travel.isPaymentMode()) {
                    if (travel.isPaid()) {
                        System.out.println("You already confirmed that travel is paid.");
                        return;
                    }
                    changeTravelPayment(travel);
                }
            }
        }
    }

    private void applyForTravelPayFromYourBalance(Passenger passenger){
        Travel travel = getSourceAndDestination(passenger, true);
        if (travel.getCost() <= passenger.getBalance()){
            System.out.println("Your balance is enough.");
            travel.setPaid(true);
            passenger.setBalance(passenger.getBalance() - travel.getCost());
            boolean isAdded = travelAccess.saveTravel(travel);
            if (isAdded){
                System.out.println("Cost of travel is: " + travel.getCost());
                travels = travelAccess.getAllTravels(passengers, drivers);
            } else {
                System.out.println("Your request has not registered. Please try later!");
                return;
            }
            updateAndInsertNewTravel(passenger, travel);
        } else {
            depositYourBalanceForATravel(travel, passenger);
        }
    }

    @Override
    public void showOngoingTravels() {
        boolean allowed = isUserAllowed("show", "ongoing travels");
        if (allowed) {
            passengers = passengerAccess.getAllPassengers();
            drivers = driversAccess.getAllDrivers();
            ArrayList<Travel> ongoingTravels = travelAccess.getOngoingTravels(passengers, drivers);
            if (ongoingTravels.size() == 0) {
                System.out.println("There is no ongoing travel to show.");
                return;
            }
            for (Travel ongoingTravel : ongoingTravels) {
                System.out.println(ongoingTravel);
            }
        } else
            System.out.println("You are not allowed to see ongoing travels list.");
        passengers = passengerAccess.getAllPassengers();
        drivers = driversAccess.getAllDrivers();
        ArrayList<Travel> ongoingTravels = travelAccess.getOngoingTravels(passengers, drivers);
        if (ongoingTravels.size() == 0){
            System.out.println("There is no ongoing travel to show.");
            return;
        }
    }*/
}
