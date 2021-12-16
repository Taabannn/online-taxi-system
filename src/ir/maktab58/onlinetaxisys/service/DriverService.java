package ir.maktab58.onlinetaxisys.service;

import ir.maktab58.onlinetaxisys.dao.DriverDao;
import ir.maktab58.onlinetaxisys.models.Driver;
import ir.maktab58.onlinetaxisys.models.Passenger;

import java.util.List;

/**
 * @author Taban Soleymani
 */
public class DriverService {
    private final DriverDao driverDao = new DriverDao();

    public List<Driver> getAllDrivers() {
        return driverDao.getAllDrivers();
    }
}
