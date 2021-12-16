package ir.maktab58.homework6.models.vehicles;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("pickup-truck")
@NoArgsConstructor
public class PickupTruck extends Vehicle {
    public PickupTruck(String model, String color, String plateNumber) {
        super(model, color, plateNumber);
    }
}
