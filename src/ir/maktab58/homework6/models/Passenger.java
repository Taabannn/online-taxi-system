package ir.maktab58.homework6.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Passenger {
    private int passengerId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private long phoneNumber;
    private long nationalCode;

    public Passenger(int passengerId, String username, String password, String firstName, String lastName,
                     Date birthDate, long phoneNumber, long nationalCode) {
        this.passengerId = passengerId;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.nationalCode = nationalCode;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
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

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String DateStr = dateFormat.format(birthDate);
        return "Passenger{" +
                "passengerId=" + passengerId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + DateStr +
                ", phoneNumber=" + phoneNumber +
                ", nationalCode=" + nationalCode +
                '}';
    }
}
