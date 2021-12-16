package ir.maktab58.homework6.models.vehicles;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("car")
@NoArgsConstructor
public class Car extends Vehicle {
    public Car(String model, String color, String plateNumber) {
        super(model, color, plateNumber);
    }
}
