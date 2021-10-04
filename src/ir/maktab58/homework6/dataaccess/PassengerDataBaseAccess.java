package ir.maktab58.homework6.dataaccess;

import ir.maktab58.homework6.exceptions.EmptyBufferException;
import ir.maktab58.homework6.models.Passenger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PassengerDataBaseAccess extends DataBaseAccess {
    public PassengerDataBaseAccess() {
        super();
    }

    public ArrayList<Passenger> getAllPassengers() {
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select * from passengers");
                ArrayList<Passenger> passengersList = new ArrayList<>();
                while (resultSet.next()) {
                    String dateStr = resultSet.getString("birth_date");
                    Date birthDate = convertStrToDate(dateStr);
                    long phoneNumber = Long.parseLong(resultSet.getString("phone_number"));
                    long nationalCode = Long.parseLong(resultSet.getString("national_code"));
                    Passenger passenger = getPassenger(resultSet.getInt("passenger_Id"),
                            resultSet.getString("username"), resultSet.getString("password"),
                            resultSet.getString("first_name"), resultSet.getString("last_name"),
                            birthDate, phoneNumber, nationalCode);
                    passengersList.add(passenger);
                }
                return passengersList;
            } catch (SQLException | NullPointerException | NumberFormatException | ArrayIndexOutOfBoundsException | EmptyBufferException exception){
                System.out.println(exception.getMessage());
            }
        }
        return null;
    }

    private Passenger getPassenger(int passengerId, String username, String password,
                           String firstName, String lastName, Date birthDate,
                           long phoneNumber, long nationalCode){

        checkInputBuffers(username, password, firstName, lastName);

        return new Passenger(passengerId, username, password, firstName, lastName,
                birthDate, phoneNumber, nationalCode);
    }

    public boolean savePassenger(ArrayList<Passenger> passengers, Passenger passenger) {
        if (connection != null) {
            try {
                int currentPassengerId = passengers.size() + 1;
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String birthDateStr = dateFormat.format(passenger.getBirthDate());
                String sqlQuery = String.format("INSERT INTO passengers (passenger_Id, username, password, first_name, last_name, birth_date, phone_number, national_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
                pstmt.setInt(1, currentPassengerId);
                pstmt.setString(2, passenger.getUsername());
                pstmt.setString(3, passenger.getPassword());
                pstmt.setString(4, passenger.getFirstName());
                pstmt.setString(5, passenger.getLastName());
                pstmt.setString(6, birthDateStr);
                pstmt.setString(7, Long.toString(passenger.getPhoneNumber()));
                pstmt.setString(8, Long.toString(passenger.getNationalCode()));
                boolean result = pstmt.execute();
                return !result;
            } catch (SQLException exception){
                exception.getMessage();
            }
        }
        return false;
    }
}
