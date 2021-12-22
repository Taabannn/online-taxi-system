package ir.maktab58.onlinetaxisys.service;

import ir.maktab58.onlinetaxisys.enumeration.PaymentMode;
import ir.maktab58.onlinetaxisys.enumeration.StateOfAttendance;
import ir.maktab58.onlinetaxisys.enumeration.TripStatus;
import ir.maktab58.onlinetaxisys.exceptions.OnlineTaxiSysEx;
import ir.maktab58.onlinetaxisys.models.Driver;
import ir.maktab58.onlinetaxisys.models.Passenger;
import ir.maktab58.onlinetaxisys.models.Trip;
import ir.maktab58.onlinetaxisys.models.Coordinate;
import ir.maktab58.onlinetaxisys.models.vehicle.Vehicle;
import ir.maktab58.onlinetaxisys.service.factory.VehicleFactory;
import ir.maktab58.onlinetaxisys.service.validator.NationalCodeValidator;
import ir.maktab58.onlinetaxisys.service.validator.UserAndPassValidator;

import java.util.ArrayList;
import java.util.List;

public class OnlineTaxiService implements OnlineTaxi {
    private final PassengerService passengerService = new PassengerService();
    private final DriverService driverService = new DriverService();
    private final TripService tripService = new TripService();
    private static final long COST_PER_METER = 1000;
    List<Driver> drivers = new ArrayList<>();
    ArrayList<Passenger> passengers = new ArrayList<>();
    ArrayList<Trip> travels = new ArrayList<>();

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
        Coordinate currentLocation = Coordinate.builder()
                                    .withX(x)
                                    .withY(y).build();
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

    public void assignDriverAndSave(int passengerId, PaymentMode paymentMode, Coordinate sourceCoordinate, Coordinate desCoordinate, boolean isPaid) {
        Passenger passenger = passengerService.getPassengerById(passengerId);
        drivers = driverService.getWaitingForTravelDrivers();
        Driver driver = findNearestDriver(sourceCoordinate, desCoordinate);
        driver.setStateOfAttendance(StateOfAttendance.IN_TRAVEL);
        passenger.setStateOfAttendance(StateOfAttendance.IN_TRAVEL);
        long cost = calculateCost(sourceCoordinate, desCoordinate);
        Trip travel = Trip.builder()
                .withDriver(driver)
                .withPassenger(passenger)
                .withIsPaid(isPaid)
                .withSource(sourceCoordinate)
                .withDestination(desCoordinate)
                .withPaymentMode(paymentMode)
                .withStatus(TripStatus.UNFINISHED)
                .withCost(cost).build();
        tripService.saveTrip(travel);
        driverService.updateDriverStateOfAttendance(driver);
        passengerService.updatePassengerStateOfAttendance(passenger);
    }

    private Driver findNearestDriver(Coordinate sourceCoordinate, Coordinate desCoordinate) {
        if (drivers.size() == 0)
            throw OnlineTaxiSysEx.builder()
                    .message("There is no driver.")
                    .errorCode(500).build();
        Driver assignedDriver = drivers.get(0);
        Coordinate currentLocation = drivers.get(0).getCurrentLocation();
        double minDistance = Math.sqrt((sourceCoordinate.getX() - currentLocation.getX())^2 + (sourceCoordinate.getY() - currentLocation.getY())^2);
        for (Driver driver : drivers) {
            currentLocation = driver.getCurrentLocation();
            double distance = Math.sqrt((sourceCoordinate.getX() - currentLocation.getX())^2 + (sourceCoordinate.getY() - currentLocation.getY())^2);
            if (distance < minDistance)
                assignedDriver = driver;
        }
        return assignedDriver;
    }

    public boolean getDriverStateOfAttendance(int driverId) {
        Driver driver = driverService.getDriverById(driverId);
        return driver.getStateOfAttendance().equals(StateOfAttendance.IN_TRAVEL);
    }

    public void confirmTravelHasFinished(int driverId) {
        Trip trip = tripService.findTripByDriverId(driverId);
        trip.setStatus(TripStatus.FINISHED);
        tripService.updateTripStatus(trip);
        Driver driver = trip.getDriver();
        driver.setStateOfAttendance(StateOfAttendance.WAITING_FOR_TRAVEL);
        Passenger passenger = trip.getPassenger();
        passenger.setStateOfAttendance(StateOfAttendance.WAITING_FOR_TRAVEL);
        driverService.updateDriverStateOfAttendance(driver);
        passengerService.updatePassengerStateOfAttendance(passenger);
    }

    public boolean isTravelPaymentModeCash(int driverId) {
        Trip trip = tripService.findTripByDriverId(driverId);
        return trip.getPaymentMode().equals(PaymentMode.CASH);
    }

    public void updateIsPaidInTravel(int driverId) {
        Trip tripByDriverId = tripService.findTripByDriverId(driverId);
        tripByDriverId.setPaid(true);
        tripService.updateTripIsPaid(tripByDriverId);
    }

    public long getPassengerBalance(int passengerId) {
        return passengerService.getPassengerById(passengerId).getBalance();
    }


    /*
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
