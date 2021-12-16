package ir.maktab58.onlinetaxisys.service;

import ir.maktab58.onlinetaxisys.dao.PassengerDao;
import ir.maktab58.onlinetaxisys.models.Driver;
import ir.maktab58.onlinetaxisys.models.Passenger;

import java.util.List;

/**
 * @author Taban Soleymani
 */
public class PassengerService {
    private final PassengerDao passengerDao = new PassengerDao();


    public List<Passenger> getAllPassengers() {
        return passengerDao.getAllPassengers();
    }

    public List<Passenger> getPassengerByNationalCode(String nationalCode) {
        return passengerDao.findPassengerByNationalCode(nationalCode);
    }

    public List<Passenger> getPassengerByUsername(String username) {
        return passengerDao.findPassengerByUsername(username);
    }

    public int saveNewPassenger(Passenger passenger) {
        return passengerDao.save(passenger);
    }
}
