package ir.maktab58.homework6.exceptions.carexceptions;

import ir.maktab58.homework6.exceptions.OnlineTaxiSysEx;

public class InvalidTypeOfVehicle extends OnlineTaxiSysEx {
    public InvalidTypeOfVehicle(String message, int errorCode) {
        super(message, errorCode);
    }
}
