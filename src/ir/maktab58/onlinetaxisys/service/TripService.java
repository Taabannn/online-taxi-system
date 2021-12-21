package ir.maktab58.onlinetaxisys.service;

import ir.maktab58.onlinetaxisys.dao.TripDao;
import ir.maktab58.onlinetaxisys.models.Trip;

/**
 * @author Taban Soleymani
 */
public class TripService {
    private final TripDao tripDao = new TripDao();

    public void saveTrip(Trip travel) {
        tripDao.save(travel);
    }
}
