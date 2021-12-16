package ir.maktab58.onlinetaxisys.service;

import ir.maktab58.onlinetaxisys.dao.PassengerDao;
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
}
