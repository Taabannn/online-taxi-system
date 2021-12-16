package ir.maktab58.onlinetaxisys.models.vehiclesfactory;

import ir.maktab58.onlinetaxisys.enumeration.VehicleType;
import ir.maktab58.onlinetaxisys.exceptions.OnlineTaxiSysEx;

/**
 * @author Taban Soleymani
 */
public class VehicleFactory {
    public static Vehicle getVehicleType(String type, String plateNumber, String model, String color) {
        Vehicle vehicle;
        if (type.equals(VehicleType.CAR.getVehicleType()))
            vehicle = Car.builder()
                    .withColor(color)
                    .withModel(model)
                    .withPlateNumber(plateNumber).build();

        else if (type.equals(VehicleType.VAN.getVehicleType()))
            vehicle = Van.builder()
                    .withColor(color)
                    .withModel(model)
                    .withPlateNumber(plateNumber).build();

        else if (type.equals(VehicleType.PICKUP_TRUCK.getVehicleType()))
            vehicle = PickupTruck.builder()
                    .withColor(color)
                    .withModel(model)
                    .withPlateNumber(plateNumber).build();

        else if (type.equals(VehicleType.MOTOR_CYCLE.getVehicleType()))
            vehicle = MotorCycle.builder()
                    .withColor(color)
                    .withModel(model)
                    .withPlateNumber(plateNumber).build();
        else
            throw OnlineTaxiSysEx.builder()
                    .message("Entered type for vehicle type is not valid.")
                    .errorCode(400).build();
        return vehicle;
    }
}
