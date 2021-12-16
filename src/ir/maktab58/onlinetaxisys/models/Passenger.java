package ir.maktab58.onlinetaxisys.models;

import ir.maktab58.onlinetaxisys.enumeration.StateOfAttendance;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int passengerId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    @Temporal(TemporalType.DATE)
    private Date birthDate;
    private String phoneNumber;
    private String nationalCode;
    private long balance;
    @Enumerated(EnumType.STRING)
    private StateOfAttendance stateOfAttendance = StateOfAttendance.WAITING_FOR_TRAVEL;

    @Builder(setterPrefix = "with")
    public Passenger(int passengerId, String username, String password, String firstName, String lastName, Date birthDate, String phoneNumber, String nationalCode, long balance, StateOfAttendance stateOfAttendance) {
        this.passengerId = passengerId;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.nationalCode = nationalCode;
        this.balance = balance;
        this.stateOfAttendance = stateOfAttendance;
    }
}
