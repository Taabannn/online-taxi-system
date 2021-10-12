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
                            birthDate, phoneNumber, nationalCode, resultSet.getInt("balance"), resultSet.getInt("state_of_attendance"));
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
                                   long phoneNumber, long nationalCode, int balance, int stateOfAttendance){

        checkInputBuffers(username, password, firstName, lastName);
        if (stateOfAttendance == 0)
            return new Passenger(passengerId, username, password, firstName, lastName,
                    birthDate, phoneNumber, nationalCode, balance, false);
        else
            return new Passenger(passengerId, username, password, firstName, lastName,
                    birthDate, phoneNumber, nationalCode, balance, true);
    }

    public boolean savePassenger(ArrayList<Passenger> passengers, Passenger passenger) {
        if (connection != null) {
            try {
                int currentPassengerId = passengers.size() + 1;
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String birthDateStr = dateFormat.format(passenger.getBirthDate());
                String sqlQuery = String.format("INSERT INTO passengers (passenger_Id, username, password, first_name, last_name, birth_date, phone_number, national_code, balance, state_of_attendance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
                pstmt.setInt(1, currentPassengerId);
                pstmt.setString(2, passenger.getUsername());
                pstmt.setString(3, passenger.getPassword());
                pstmt.setString(4, passenger.getFirstName());
                pstmt.setString(5, passenger.getLastName());
                pstmt.setString(6, birthDateStr);
                pstmt.setString(7, Long.toString(passenger.getPhoneNumber()));
                pstmt.setString(8, Long.toString(passenger.getNationalCode()));
                pstmt.setInt(9, passenger.getBalance());
                if (passenger.isStateOfAttendance())
                    pstmt.setInt(10, 1);
                else
                    pstmt.setInt(10, 0);
                boolean result = pstmt.execute();
                return !result;
            } catch (SQLException exception){
                System.out.println(exception.getMessage());
            }
        }
        return false;
    }

    public int updatePassengerBalance(int passengerId, int amount){
        if (connection != null) {
            try {
                String sqlQuery = String.format("UPDATE passengers SET balance = %d WHERE passenger_id = %d",
                        amount, passengerId);
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                int result = preparedStatement.executeUpdate(sqlQuery);
                return result;
            }
            catch (SQLException ex){
                System.out.println(ex.getMessage());
            }
        }
        return 0;
    }
}

