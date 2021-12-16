package ir.maktab58.homework6.models;

public class Admin {
    private final String username = "root";
    private final String password = "61378";

    public boolean isUserAdmin(String enteredUsername, String enteredPassword){
        return enteredUsername.equals(username) && enteredPassword.equals(password);
    }
}
