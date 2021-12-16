package ir.maktab58.homework6.enumeration;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum VehicleType {
    CAR("car"),
    MOTOR_CYCLE("motor-cycle"),
    PICKUP_TRUCK("pickup-truck"),
    VAN("van");

    private @Getter String vehicleType;

    VehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
