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

    public List<Passenger> getPassengerByNationalCode(String nationalCode) {
        return passengerDao.findPassengerByNationalCode(nationalCode);
    }

    public List<Passenger> getPassengerByUsername(String username) {
        return passengerDao.findPassengerByUsername(username);
    }

    public int saveNewPassenger(Passenger passenger) {
        return passengerDao.save(passenger);
    }

    public int getPassengerIdByUserAndPass(String username, String password) {
        Passenger passenger = passengerDao.findUserByUserAndPass(username, password);
        if (passenger != null)
            return passenger.getPassengerId();
        return 0;
    }

    public Passenger getPassengerById(int passengerId) {
        return passengerDao.findPassengerById(passengerId);
    }

    public void updatePassengerWallet(int passengerId, long charge) {
        Passenger passengerById = passengerDao.findPassengerById(passengerId);
        passengerById.setBalance(passengerById.getPassengerId() + charge);
        passengerDao.update(passengerById);
    }
}
