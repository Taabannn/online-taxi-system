package ir.maktab58.onlinetaxisys.models;

import ir.maktab58.onlinetaxisys.enumeration.PaymentMode;
import ir.maktab58.onlinetaxisys.enumeration.TravelStatus;
import ir.maktab58.onlinetaxisys.models.places.Coordinates;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author Taban Soleymani
 */
@Entity
@Data
@EqualsAndHashCode
@ToString
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int travelId;
    @OneToOne
    private Passenger passenger;
    @OneToOne
    private Driver driver;
    @OneToOne
    private Coordinates source;
    @OneToOne
    private Coordinates destination;
    @Enumerated(EnumType.STRING)
    private TravelStatus status;
    private boolean isPaid;
    private long cost;
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    @Builder(setterPrefix = "with")
    public Travel(int travelId, Passenger passenger, Driver driver, Coordinates source, Coordinates destination, TravelStatus status, boolean isPaid, long cost, PaymentMode paymentMode) {
        this.travelId = travelId;
        this.passenger = passenger;
        this.driver = driver;
        this.source = source;
        this.destination = destination;
        this.status = status;
        this.isPaid = isPaid;
        this.cost = cost;
        this.paymentMode = paymentMode;
    }
}

