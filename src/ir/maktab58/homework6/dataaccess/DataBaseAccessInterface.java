package ir.maktab58.homework6.dataaccess;

import ir.maktab58.homework6.exceptions.EmptyBufferException;

import java.util.Date;

public interface DataBaseAccessInterface {
    default Date convertStrToDate(String dateStr){
        String[] dateElements = dateStr.split("-");
        int year = Integer.parseInt(dateElements[0]) - 1900;
        int month = Integer.parseInt(dateElements[1]) - 1;
        int day = Integer.parseInt(dateElements[2]);
        return new Date(year, month, day);
    }

    default void checkInputBuffers(String username, String password, String firstName, String lastName){
        if (username.length() == 0)
            throw new EmptyBufferException("username can't be a zero-length string", 500);

        if (password.length() == 0)
            throw new EmptyBufferException("password can't be a zero-length string", 500);

        if (firstName.length() == 0)
            throw new EmptyBufferException("firstname can't be a zero-length string", 500);

        if (lastName.length() == 0)
            throw new EmptyBufferException("lastname can't be a zero-length string", 500);
    }
}
