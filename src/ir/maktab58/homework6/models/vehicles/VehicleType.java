package ir.maktab58.homework6.models.vehicles;

public enum VehicleType {
    CAR("car"),
    MOTOR_CYCLE("motor-cycle"),
    PICKUP_TRUCK("pickup-truck"),
    VAN("van");

    private String vehicleType;

    VehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleType() {
        return vehicleType;
    }
}
