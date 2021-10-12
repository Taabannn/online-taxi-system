package ir.maktab58.homework6.dataaccess;

import ir.maktab58.homework6.exceptions.EmptyBufferException;
import ir.maktab58.homework6.models.Driver;
import ir.maktab58.homework6.models.Passenger;
import ir.maktab58.homework6.models.Travel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author Taban Soleymani
 */
public class TravelDataBaseAccess extends DataBaseAccess {
    public TravelDataBaseAccess() {
        super();
    }

    public boolean saveTravel(Travel travel){
        if (connection != null) {
            try {
                int currentTravelId = travel.getTravelId();
                String sqlQuery = String.format("INSERT INTO travel (travel_Id, fk_passenger_id, fk_driver_id, source, destination, cost, status, isPaid) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
                pstmt.setInt(1, currentTravelId);
                pstmt.setInt(2, travel.getPassenger().getPassengerId());
                pstmt.setInt(3, travel.getDriver().getDriverId());
                pstmt.setString(4, travel.getSource());
                pstmt.setString(5, travel.getDestination());
                pstmt.setInt(6, travel.getCost());
                if (travel.isStatus())
                    pstmt.setInt(7, 1);
                else
                    pstmt.setInt(7, 0);
                if (travel.isPaid())
                    pstmt.setInt(8, 1);
                else
                    pstmt.setInt(8, 0);
                if (travel.isPaymentMode())
                    pstmt.setInt(9, 1);
                else
                    pstmt.setInt(9, 0);
                boolean result = pstmt.execute();
                return !result;
            } catch (SQLException exception){
                System.out.println(exception.getMessage());
            }
        }
        return false;
    }

    public ArrayList<Travel> getAllTravels(ArrayList<Passenger> passengers, ArrayList<Driver> drivers) {
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select * from travel");
                ArrayList<Travel> travelsList = new ArrayList<>();
                while (resultSet.next()) {
                    boolean status = true;
                    boolean isPaid = true;
                    int temp = resultSet.getInt("status");
                    if (temp == 0)
                        status = false;
                    temp = resultSet.getInt("isPaid");
                    if (temp == 0)
                        isPaid = false;
                    boolean paymentMode = true;
                    temp = resultSet.getInt("payment_mode");
                    if (temp == 0)
                        paymentMode = false;
                    Passenger passenger = passengers.get(resultSet.getInt("fk_passenger_id") - 1);
                    Driver driver = drivers.get(resultSet.getInt("fk_driver_id") - 1);
                    Travel travel = new Travel(resultSet.getInt("travel_id"),
                            passenger, resultSet.getString("source"), resultSet.getString("destination"),
                            status, isPaid, paymentMode);
                    travel.setDriver(driver);
                    travelsList.add(travel);
                }
                return travelsList;
            } catch (SQLException | NullPointerException | NumberFormatException | ArrayIndexOutOfBoundsException | EmptyBufferException exception) {
                System.out.println(exception.getMessage());
            }
        }
        return null;
    }
}

