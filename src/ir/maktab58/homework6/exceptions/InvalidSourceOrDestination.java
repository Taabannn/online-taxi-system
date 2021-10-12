package ir.maktab58.homework6.exceptions;

/**
 * @author Taban Soleymani
 */
public class InvalidSourceOrDestination extends OnlineTaxiSysEx{
    public InvalidSourceOrDestination(String message, int errorCode) {
        super(message, errorCode);
    }
}