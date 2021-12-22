package ir.maktab58.onlinetaxisys.models;

public class Admin {
    private static final Admin admin = new Admin();

    private Admin() {
    }

    public static Admin getInstance() {
        return admin;
    }

    public boolean isUserAdmin(String enteredUsername, String enteredPassword){
        String username = "root";
        String password = "61378";
        return enteredUsername.equals(username) && enteredPassword.equals(password);
    }
}
