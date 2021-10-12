package ir.maktab58.homework6.models;

import ir.maktab58.homework6.models.places.Coordinates;
import ir.maktab58.homework6.models.places.Places;

import java.util.Objects;

/**
 * @author Taban Soleymani
 */
public class Travel {
    int travelId;
    Passenger passenger;
    Driver driver;
    String source; //[x, y]
    String destination; //[x, y]
    boolean status; //0: unfinished, //1: finished
    boolean isPaid;
    int cost;
    boolean paymentMode; //0: in cash, 1: withdraw passenger account

    public Travel(int travelId, Passenger passenger, String source, String destination, boolean status, boolean isPaid, boolean paymentMode) {
        this.travelId = travelId;
        this.passenger = passenger;
        this.source = source;
        this.destination = destination;
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

    public Coordinates calcCoordinate(String nameOfPlace){
        if (nameOfPlace.equalsIgnoreCase(Places.PLACE_A.getPlaceName()))
            return new Coordinates(3, 4);
        if (nameOfPlace.equalsIgnoreCase(Places.PLACE_B.getPlaceName()))
            return new Coordinates(6, 8);
        if (nameOfPlace.equalsIgnoreCase(Places.PLACE_C.getPlaceName()))
            return new Coordinates(9, 12);
        if (nameOfPlace.equalsIgnoreCase(Places.PLACE_D.getPlaceName()))
            return new Coordinates(12, 16);
        return null;
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

