package ir.maktab58.onlinetaxisys.exceptions;

import lombok.Builder;
import lombok.Getter;

public class OnlineTaxiSysEx extends RuntimeException {
    private final @Getter int errorCode;

    @Builder
    public OnlineTaxiSysEx(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
