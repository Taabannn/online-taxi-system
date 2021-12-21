package ir.maktab58.onlinetaxisys.models.vehicle;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("pickup-truck")
@NoArgsConstructor
public class PickupTruck extends Vehicle {
    @Builder(setterPrefix = "with")
    public PickupTruck(String model, String color, String plateNumber) {
        super(model, color, plateNumber);
    }
}
