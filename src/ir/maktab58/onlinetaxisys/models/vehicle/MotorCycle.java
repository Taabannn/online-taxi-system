package ir.maktab58.onlinetaxisys.models.vehicle;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("motor-cycle")
@NoArgsConstructor
public class MotorCycle extends Vehicle {
    @Builder(setterPrefix = "with")
    public MotorCycle(String model, String color, String plateNumber) {
        super(model, color, plateNumber);
    }
}
