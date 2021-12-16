package ir.maktab58.homework6.models.vehicles;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("motor-cycle")
@NoArgsConstructor
public class MotorCycle extends Vehicle {
    public MotorCycle(String model, String color, String plateNumber) {
        super(model, color, plateNumber);
    }
}
