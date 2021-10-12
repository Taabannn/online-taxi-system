package ir.maktab58.homework6.models;

import ir.maktab58.homework6.models.places.Coordinates;
import ir.maktab58.homework6.models.vehicles.Vehicle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Driver {
    private int driverId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private long phoneNumber;
    private long nationalCode;
    private Vehicle vehicle;
    private boolean stateOfAttendance;
    private int wallet;
    private Coordinates currentLocation;

    public Driver(int driverId, String username, String password, String firstName, String lastName, Date birthDate, long phoneNumber, long nationalCode, Vehicle vehicle, boolean stateOfAttendance, int wallet, Coordinates currentLocation) {
        this.driverId = driverId;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.nationalCode = nationalCode;
        this.vehicle = vehicle;
        this.stateOfAttendance = stateOfAttendance;
        this.wallet = wallet;
        this.currentLocation = currentLocation;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(long nationalCode) {
        this.nationalCode = nationalCode;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public boolean isStateOfAttendance() {
        return stateOfAttendance;
    }

    public void setStateOfAttendance(boolean stateOfAttendance) {
        this.stateOfAttendance = stateOfAttendance;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }

    public Coordinates getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Coordinates currentLocation) {
        this.currentLocation = currentLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return phoneNumber == driver.phoneNumber && nationalCode == driver.nationalCode && username.equals(driver.username) && password.equals(driver.password) && firstName.equals(driver.firstName) && lastName.equals(driver.lastName) && birthDate.equals(driver.birthDate) && vehicle.equals(driver.vehicle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverId, username, password, firstName, lastName, birthDate, phoneNumber, nationalCode, vehicle);
    }

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String DateStr = dateFormat.format(birthDate);
        final String driverState = stateOfAttendance ? "in travel" : "waiting for travel";
        return "Driver{" +
                "driverId=" + driverId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + DateStr +
                ", phoneNumber=" + phoneNumber +
                ", nationalCode=" + nationalCode +
                ", vehicle=" + vehicle +
                ", stateOfAttendance=" + driverState +
                ", wallet=" + wallet +
                ", currentLocation=" + currentLocation +
                '}';
    }
}
