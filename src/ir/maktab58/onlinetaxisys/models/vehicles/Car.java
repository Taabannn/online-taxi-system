package ir.maktab58.onlinetaxisys.models.vehicles;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("car")
@NoArgsConstructor
public class Car extends Vehicle {
    @Builder(setterPrefix = "with")
    public Car(String model, String color, String plateNumber) {
        super(model, color, plateNumber);
    }
}
