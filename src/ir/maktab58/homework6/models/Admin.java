package ir.maktab58.homework6.models;

public class Admin {
    private final String username = "root";
    private final String password = "61378";

    public boolean isUserAdmin(String enteredUsername, String enteredPassword){
        if (!username.equals(enteredUsername) || !password.equals(enteredPassword)){
            System.out.println("Sorry! Invalid username or password, please try again.");
            return false;
        }
        System.out.println("Welcome back " + username + ". You've logged in successfully.");
        return true;
    }
}
