package ir.maktab58.homework6.dataaccess;

import ir.maktab58.homework6.exceptions.OnlineTaxiSysEx;
import ir.maktab58.homework6.exceptions.carexceptions.InvalidTypeOfVehicle;
import ir.maktab58.homework6.models.Driver;
import ir.maktab58.homework6.models.places.Coordinates;
import ir.maktab58.homework6.models.vehicles.*;

import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;

public class DriverDataBaseAccess extends DataBaseAccess{
    public DriverDataBaseAccess() {
        super();
    }

    public ArrayList<Driver> getAllDrivers() {
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select * from drivers");
                ArrayList<Driver> passengersList = new ArrayList<>();
                while (resultSet.next()) {
                    String dateStr = resultSet.getString("birth_date");
                    Date birthDate = convertStrToDate(dateStr);
                    long phoneNumber = Long.parseLong(resultSet.getString("phone_number"));
                    long nationalCode = Long.parseLong(resultSet.getString("national_code"));
                    Driver driver = getDriver(resultSet.getInt("driver_Id"),
                            resultSet.getString("username"), resultSet.getString("password"),
                            resultSet.getString("first_name"), resultSet.getString("last_name"),
                            birthDate, phoneNumber, nationalCode, resultSet.getString("vehicle_type"),
                            resultSet.getString("vehicle_model"), resultSet.getString("vehicle_color"),
                            resultSet.getString("vehicle_platenumber"), resultSet.getInt("state"),
                            resultSet.getInt("wallet"), resultSet.getString("current_location"));
                    passengersList.add(driver);
                }
                return passengersList;
            } catch (SQLException | NullPointerException | NumberFormatException | ArrayIndexOutOfBoundsException | OnlineTaxiSysEx exception){
                System.out.println(exception.getMessage());
            }
        }
        return null;
    }

    private Driver getDriver(int passengerId, String username, String password,
                             String firstName, String lastName, Date birthDate,
                             long phoneNumber, long nationalCode, String vehicleType,
                             String model, String color, String plateNumber, int state,
                             int wallet, String location){

        checkInputBuffers(username, password, firstName, lastName);
        VehicleInterface.checkInputBuffer(model, 500);
        VehicleInterface.checkInputBuffer(color, 500);
        VehicleInterface.validatePlateNumber(plateNumber, 500);
        VehicleInterface.validateTypeOfVehicle(vehicleType, 500);
        //TODO if you want to consider other types, your code goes here
        if (!vehicleType.equalsIgnoreCase(VehicleType.CAR.getVehicleType()))
            throw new InvalidTypeOfVehicle("This type of vehicle does not exist", 500);
        Car car = new Car(model, color, plateNumber);
        String[] locationXAndY = location.split(" ");
        int x = Integer.parseInt(locationXAndY[0]);
        int y = Integer.parseInt(locationXAndY[1]);
        Coordinates currentLocation = new Coordinates(x, y);
        if (state == 0)
            return new Driver(passengerId, username, password, firstName, lastName,
                    birthDate, phoneNumber, nationalCode, car, false, wallet, currentLocation);
        else
            return new Driver(passengerId, username, password, firstName, lastName,
                    birthDate, phoneNumber, nationalCode, car, true, wallet, currentLocation);
    }

    public boolean saveDriver(ArrayList<Driver> drivers, Driver driver) {
        if (connection != null) {
            try {
                int currentDriverId = drivers.size() + 1;
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String birthDateStr = dateFormat.format(driver.getBirthDate());
                Vehicle vehicle = driver.getVehicle();
                String typeOfVehicle = getTypeOfVehicle(vehicle);
                String sqlQuery = String.format("INSERT INTO drivers (driver_Id, username, password, first_name, last_name, birth_date, phone_number, national_code, vehicle_type, vehicle_model, vehicle_color, vehicle_platenumber, state, wallet, current_location) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
                setPreparedStatement(pstmt, currentDriverId, driver, birthDateStr, typeOfVehicle, vehicle);
                boolean result = pstmt.execute();
                return !result;
            } catch (SQLException | InvalidTypeOfVehicle exception){
                exception.getMessage();
            }
        }
        return false;
    }

    private String getTypeOfVehicle(Vehicle vehicle){
        if (vehicle instanceof Car)
            return VehicleType.CAR.getVehicleType();

        if (vehicle instanceof MotorCycle)
            return VehicleType.MOTOR_CYCLE.getVehicleType();

        if (vehicle instanceof Van)
            return VehicleType.VAN.getVehicleType();

        if (vehicle instanceof PickupTruck)
            return VehicleType.PICKUP_TRUCK.getVehicleType();

        throw new InvalidTypeOfVehicle("Type of vehicle that you've entered does not exist.", 400);
    }

    private void setPreparedStatement(PreparedStatement pstmt, int currentDriverId, Driver driver,
                                      String birthDateStr, String typeOfVehicle, Vehicle vehicle) throws SQLException {
        pstmt.setInt(1, currentDriverId);
        pstmt.setString(2, driver.getUsername());
        pstmt.setString(3, driver.getPassword());
        pstmt.setString(4, driver.getFirstName());
        pstmt.setString(5, driver.getLastName());
        pstmt.setString(6, birthDateStr);
        pstmt.setString(7, Long.toString(driver.getPhoneNumber()));
        pstmt.setString(8, Long.toString(driver.getNationalCode()));
        pstmt.setString(9, typeOfVehicle);
        pstmt.setString(10, vehicle.getModel());
        pstmt.setString(11, vehicle.getColor());
        pstmt.setString(12, vehicle.getPlateNumber());
        if (driver.isStateOfAttendance())
            pstmt.setInt(13, 1);
        else
            pstmt.setInt(13, 0);

        pstmt.setInt(14, driver.getWallet());
        pstmt.setString(15, driver.getCurrentLocation().toString());
    }
}
