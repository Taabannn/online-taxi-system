package ir.maktab58.homework6.exceptions;

public class OnlineTaxiSysEx extends RuntimeException {
    int errorCode;

    public OnlineTaxiSysEx(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
