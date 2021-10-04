package ir.maktab58.homework6.models.vehicles;

import ir.maktab58.homework6.exceptions.EmptyBufferException;
import ir.maktab58.homework6.exceptions.carexceptions.InvalidTypeOfVehicle;
import ir.maktab58.homework6.exceptions.carexceptions.InvalidPlateNumberEx;

public interface VehicleInterface {
    static void checkInputBuffer(String modelOrColor, int errorCode){
        if (modelOrColor.length() == 0)
            throw new EmptyBufferException("The input buffer can't be empty.", errorCode);
    }

    /*void validateModel(String model);

    void validateColor(String Color);*/

    static void validatePlateNumber(String plateNumber, int errorCode) {
        if (plateNumber.length() != 6)
            throw new InvalidPlateNumberEx("The plateNumber ust have 6 chars.", errorCode);

        for (int i = 0; i < plateNumber.length(); i++){
            if (i == 2) {
                if (!isValidChar(plateNumber.charAt(i)))
                    throw new InvalidPlateNumberEx("The plateNumber is not valid", errorCode);
                continue;
            }

            if (!isItInteger(plateNumber.charAt(i)))
                throw new InvalidPlateNumberEx("The plateNumber is not valid", errorCode);
        }
    }

    private static boolean isItInteger(char inputChar){
        return (inputChar >= 49) && (inputChar <= 57);
    }

    private static boolean isValidChar(char inputChar){
        return (inputChar >= 65) && (inputChar <= 90);
    }

    static void validateTypeOfVehicle(String vehicleType, int errorCode){
        if (!vehicleType.equalsIgnoreCase(VehicleType.CAR.getVehicleType()) &&
            !vehicleType.equalsIgnoreCase(VehicleType.PICKUP_TRUCK.getVehicleType()) &&
            !vehicleType.equalsIgnoreCase(VehicleType.VAN.getVehicleType()) &&
            !vehicleType.equalsIgnoreCase(VehicleType.MOTOR_CYCLE.getVehicleType())){
            throw new InvalidTypeOfVehicle("This type of vehicle is not existed", errorCode);
        }
    }
}
