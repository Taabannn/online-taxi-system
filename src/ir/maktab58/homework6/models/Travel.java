package ir.maktab58.homework6.models;

import ir.maktab58.homework6.exceptions.InvalidSourceOrDestination;
import ir.maktab58.homework6.models.places.Coordinates;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Taban Soleymani
 */
public class Travel {
    private int travelId;
    private Passenger passenger;
    private Driver driver;
    private String source; //[x, y]
    private String destination; //[x, y]
    private boolean status; //0: unfinished, //1: finished
    private boolean isPaid;
    private int cost;
    private boolean paymentMode; //0: in cash, 1: withdraw passenger account
    private ArrayList<Integer> rejectedDrivers = new ArrayList<>();

    public Travel(int travelId, Passenger passenger, Driver driver, String source, String destination, boolean status, boolean isPaid, boolean paymentMode) {
        this.travelId = travelId;
        this.passenger = passenger;
        this.driver = driver;
        this.source = source;
        this.destination = destination;
        this.status = status;
        this.isPaid = isPaid;
        this.paymentMode = paymentMode;
    }

    public Travel(int travelId, Passenger passenger, ArrayList<Driver> drivers, String source, String destination, boolean status, boolean isPaid, boolean paymentMode) {
        this.travelId = travelId;
        this.passenger = passenger;
        this.source = source;
        this.destination = destination;
        this.driver = findTheDriver(drivers);
        this.status = status;
        this.isPaid = isPaid;
        this.paymentMode = paymentMode;
        calcCostOfTravel();
    }

    public int getTravelId() {
        return travelId;
    }

    public void setTravelId(int travelId) {
        this.travelId = travelId;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public boolean isPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(boolean paymentMode) {
        this.paymentMode = paymentMode;
    }

    public void calcCostOfTravel(){
        Coordinates source = calcCoordinate(this.source);
        Coordinates destination = calcCoordinate(this.destination);
        int x = source.getX() - destination.getX();
        int y = source.getY() - destination.getY();
        int distance = (int) Math.sqrt(x*x + y*y);
        cost = distance*1000;
    }

    public Coordinates calcCoordinate(String addressOfPlace){
        if (addressOfPlace.length() < 3)
            throw new  InvalidSourceOrDestination("Format of source or destination is wrong.", 400);
        return new Coordinates(addressOfPlace);
    }

    public Driver findTheDriver(ArrayList<Driver> drivers){
        Coordinates sourceCoordinate = calcCoordinate(source);
        Driver firstDriver= drivers.get(0);
        int driverIndex = 0;
        Coordinates driverLocation = firstDriver.getCurrentLocation();
        int x = sourceCoordinate.getX() - driverLocation.getX();
        int y = sourceCoordinate.getY() - driverLocation.getY();
        int minDistance = (int) Math.sqrt(x*x + y*y);
        for (Driver driver1 : drivers) {
            driverLocation = driver1.getCurrentLocation();
            x = sourceCoordinate.getX() - driverLocation.getX();
            y = sourceCoordinate.getY() - driverLocation.getY();
            int distance = (int) Math.sqrt(x*x + y*y);
            if (distance < minDistance) {
                minDistance = distance;
                driverIndex = drivers.indexOf(driver1);
            }
        }
        return drivers.get(driverIndex);
    }

    private int findTheFirstIndex(){
        int firstIndex = 0;
        for (Integer rejectedDriver : rejectedDrivers) {
            if (rejectedDriver == firstIndex)
                firstIndex++;
        }

        return firstIndex;
    }

    public void assignNewDriver(int index, ArrayList<Driver> drivers) {
        rejectedDrivers.add(index);
        Coordinates sourceCoordinate = calcCoordinate(source);
        int driverIndex = findTheFirstIndex();
        Driver firstDriver = drivers.get(driverIndex);
        Coordinates driverLocation = firstDriver.getCurrentLocation();
        int x = sourceCoordinate.getX() - driverLocation.getX();
        int y = sourceCoordinate.getY() - driverLocation.getY();
        int minDistance = (int) Math.sqrt(x*x + y*y);
        for (Driver driver1 : drivers) {
            for (Integer index1 : rejectedDrivers) {
                if (drivers.indexOf(driver1) != index1){
                    driverLocation = driver1.getCurrentLocation();
                    x = sourceCoordinate.getX() - driverLocation.getX();
                    y = sourceCoordinate.getY() - driverLocation.getY();
                    int distance = (int) Math.sqrt(x * x + y * y);
                    if (distance < minDistance) {
                        minDistance = distance;
                        driverIndex = drivers.indexOf(driver1);
                    }
                }
            }
        }
        this.driver = drivers.get(driverIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Travel travel = (Travel) o;
        return status == travel.status && cost == travel.cost && passenger.equals(travel.passenger) && driver.equals(travel.driver) && source.equals(travel.source) && destination.equals(travel.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passenger, driver, source, destination, status, cost);
    }

    @Override
    public String toString() {
        return "Travel{" +
                "travelId=" + travelId +
                ", passenger=" + passenger +
                ", driver=" + driver +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", status=" + status +
                ", cost=" + cost +
                '}';
    }
}

