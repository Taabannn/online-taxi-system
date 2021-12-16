package ir.maktab58.homework6.models.vehicles;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("van")
@NoArgsConstructor
public class Van extends Vehicle {
    public Van(String model, String color, String plateNumber) {
        super(model, color, plateNumber);
    }
}
