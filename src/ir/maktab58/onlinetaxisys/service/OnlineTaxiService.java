package ir.maktab58.onlinetaxisys.service;

import ir.maktab58.onlinetaxisys.exceptions.OnlineTaxiSysEx;
import ir.maktab58.onlinetaxisys.models.Driver;
import ir.maktab58.onlinetaxisys.models.Passenger;
import ir.maktab58.onlinetaxisys.models.Travel;
import ir.maktab58.onlinetaxisys.models.places.Coordinates;
import ir.maktab58.onlinetaxisys.models.vehiclesfactory.Vehicle;
import ir.maktab58.onlinetaxisys.models.vehiclesfactory.VehicleFactory;
import ir.maktab58.onlinetaxisys.service.singletonvalidator.NationalCodeValidator;
import ir.maktab58.onlinetaxisys.service.singletonvalidator.UserAndPassValidator;

import java.util.ArrayList;
import java.util.List;

public class OnlineTaxiService implements OnlineTaxiInterface {
    private final PassengerService passengerService = new PassengerService();
    private final DriverService driverService = new DriverService();
    ArrayList<Driver> drivers = new ArrayList<>();
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
        boolean userAndPassValid = UserAndPassValidator.getInstance().isUserAndPassValid(username, password);
        if (!userAndPassValid)
            throw OnlineTaxiSysEx.builder()
                    .message("Invalid user or pass")
                    .errorCode(400).build();
        boolean nationalCodeValid = NationalCodeValidator.getInstance().isNationalCodeValid(nationalCode);
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
        Coordinates currentLocation = new Coordinates(x, y);
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

    /*
    private boolean isVehicleRegistered(Driver newDriver){
        for (Driver driver : drivers){
            if (driver.getVehicle().equals(newDriver.getVehicle())){
                return true;
            }
        }
        return false;
    }

    private void driverSignup(){
        Driver newDriver = getNewDriver();
        boolean existed = drivers.contains(newDriver);
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
                showDriverMenu(newDriver);
            }
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
                showDriverMenu(driver);
                return;
            }
        }
        System.out.println("Sorry! You've might've entered username or password wrong.");
    }

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

    private void changeTravelPayment(Travel travel){
        System.out.println("Are sure to make isPaid state?");
        Scanner scanner = new Scanner(System.in);
        if (deleteLastSpaces(scanner.nextLine()).equalsIgnoreCase("Yes")) {
            travel.setPaid(true);
            int result = travelAccess.updateIsPaidState(travel);
            if (result == 1){
                System.out.println("Your request was handled successfully.");
            } else {
                System.out.println("Sorry! Unable to response your request, please try again.");
            }
        }
    }

    @Override
    public void addAGroupOfPassengers(){
        boolean allowed = isUserAllowed("add", "passengers");
        passengers = passengerAccess.getAllPassengers();
        if (allowed) {
            try {
                System.out.println("How many passengers would you like to add?");
                Scanner scanner = new Scanner(System.in);
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
            boolean existed = passengers.contains(newPassenger);
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

    private boolean isUserNameTaken(Passenger passenger){
        for (Passenger passenger1 : passengers)
            if (passenger1.getUsername().equals(passenger.getUsername()))
                return true;

        return false;
    }

    private Passenger getPassenger(){
        System.out.println("Please enter your username, password, firstName, lastName, " +
                "birthDate(year, month, day), phoneNumber, nationalCode, initialBalance");
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
        int initialBalance = Integer.parseInt(tokens[9]);
        return new Passenger(passengers.size() + 1, username, password, firstName, lastName,
                birthDate, phoneNumber, nationalCode, initialBalance, false);
    }

    @Override
    public void passengerSignupOrLogin(){
        passengers = passengerAccess.getAllPassengers();
        System.out.println("Would you like to signup or login?");
        Scanner scanner = new Scanner(System.in);
        String choice = deleteLastSpaces(scanner.nextLine());
        try {
            if (choice.equalsIgnoreCase("signup")) {
                passengerSignup();
            } else if (choice.equalsIgnoreCase("login")) {
                passengerLogin();
            } else {
                System.out.println("Invalid choice! \nEnter signup if you want to create account.");
                System.out.println("Enter login, if you've already had an account. \nPlease try again.");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void passengerSignup(){
        Passenger newPassenger = getPassenger();
        boolean existed = passengers.contains(newPassenger);
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
                showPassengerMenu(newPassenger);
            }
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
                showPassengerMenu(passenger);
                return;
            }
        }
        System.out.println("Sorry! You've might've entered username or password wrong.");
    }

    private void showPassengerMenu(Passenger passenger){
        boolean stateOfAttendance = passenger.isStateOfAttendance();
        Scanner scanner = new Scanner(System.in);
        if(stateOfAttendance)
            System.out.println("You're still in travel.");
        while (!stateOfAttendance) {
            System.out.println("**********Passenger Menu**********");
            System.out.println("1) Apply for a new travel (pay cash)");
            System.out.println("2) Apply for a new travel (pay from your balance)");
            System.out.println("3) Deposit your balance");
            System.out.println("4) Back to main menu");
            String choice = deleteLastSpaces(scanner.nextLine());
            if (choice.equals("1")) {
                applyForTravelPayCash(passenger);
            } else if (choice.equals("2")) {
                applyForTravelPayFromYourBalance(passenger);
            } else if (choice.equals("3")) {
                depositPassengerWallet(passenger);
            } else if (choice.equals("4")) {
                break;
            } else {
                System.out.println("Invalid input command. Your choice must be an integer between 1 to 4.");
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

    private void depositYourBalanceForATravel(Travel travel, Passenger passenger){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Your balance is not enough. You should deposit your account: " + (travel.getCost() - passenger.getBalance()));
        while (true) {
            System.out.println("Do you want to continue? Yes/No");
            String choice = deleteLastSpaces(scanner.nextLine());
            if (choice.equalsIgnoreCase("No"))
                return;
            else if (choice.equalsIgnoreCase("Yes")) {
                System.out.println("You have not deposited your account.");
                System.out.println("How much money do you want to deposit?");
                int amountOfIncrease = Integer.parseInt(deleteLastSpaces(scanner.nextLine()));
                passengerAccess.updatePassengerBalance(passenger.getPassengerId(), amountOfIncrease + passenger.getBalance());
                passenger.setBalance(passenger.getBalance() + amountOfIncrease);
                if (travel.getCost() <= passenger.getBalance()) {
                    System.out.println("Your balance is enough.");
                    travel.setPaid(true);
                    passenger.setBalance(passenger.getBalance() - travel.getCost());
                    boolean isAdded = travelAccess.saveTravel(travel);
                    if (isAdded) {
                        System.out.println("Your travel is registered successfully. Please wait for a driver.");
                        travels = travelAccess.getAllTravels(passengers, drivers);
                    } else {
                        System.out.println("Your request has not registered. Please try later!");
                        return;
                    }
                    updateAndInsertNewTravel(passenger, travel);
                    break;
                } else {
                    System.out.println("Your balance is not enough yet. You should deposit your account: " + (travel.getCost() - passenger.getBalance()));
                    continue;
                }
            }else {
                System.out.println("Invalid Input! Please try again.");
            }
        }
    }

    private void updateAndInsertNewTravel(Passenger passenger, Travel travel){
        passengerAccess.updatePassengerBalance(passenger.getPassengerId(), passenger.getBalance());
        passengers = passengerAccess.getAllPassengers();
        travel.getDriver().setWallet(travel.getDriver().getWallet() + travel.getCost());
        driversAccess.updateDriverWallet(travel.getDriver());
        travel.getDriver().setStateOfAttendance(true);
        driversAccess.updateDriverStateOfAttendance(travel.getDriver());
        //drivers = driversAccess.getAllDrivers();
    }

    private void applyForTravelPayCash(Passenger passenger){
        Travel travel = getSourceAndDestination(passenger, false);
        boolean isAdded = travelAccess.saveTravel(travel);
        if (isAdded){
            System.out.println("Cost of travel is: " + travel.getCost());
            travels = travelAccess.getAllTravels(passengers, drivers);
        } else
            System.out.println("Your request has not registered. Please try later!");
    }

    private Travel getSourceAndDestination(Passenger passenger, boolean paymentMode){
        passengers = passengerAccess.getAllPassengers();
        drivers = driversAccess.getAllDrivers();
        travels = travelAccess.getAllTravels(passengers, drivers);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter source: ");
        String source = deleteLastSpaces(scanner.nextLine());
        System.out.println("Please enter destination: ");
        String destination = deleteLastSpaces(scanner.nextLine());
        if (source.length() == 0 || destination.length() == 0)
            throw new EmptyBufferException("Source or destination can't be empty buffer", 400);
        return new Travel(travels.size() + 1, passenger, drivers, source, destination, false, false, paymentMode);
    }

    private void depositPassengerWallet(Passenger passenger){
        System.out.println("How much money do you want to deposit?");
        Scanner scanner = new Scanner(System.in);
        int amount = Integer.parseInt(deleteLastSpaces(scanner.nextLine()));
        int result = passengerAccess.updatePassengerBalance(passenger.getPassengerId(), passenger.getBalance() + amount);
        if (result == 1) {
            System.out.println(passenger.getFirstName() + " Your balance is updated successfully!");
            System.out.println("Now your balance is " + (amount + passenger.getBalance()) + ".");
            passengers = passengerAccess.getAllPassengers();
        } else {
            System.out.println("Sorry! sth wrong was happened. Please try again.");
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
