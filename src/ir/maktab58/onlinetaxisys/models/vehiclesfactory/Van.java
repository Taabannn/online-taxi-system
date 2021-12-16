package ir.maktab58.onlinetaxisys.models.vehiclesfactory;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("van")
@NoArgsConstructor
public class Van extends Vehicle {
    @Builder(setterPrefix = "with")
    public Van(String model, String color, String plateNumber) {
        super(model, color, plateNumber);
    }
}
