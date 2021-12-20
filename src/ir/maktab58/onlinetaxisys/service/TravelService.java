package ir.maktab58.onlinetaxisys.service;

import ir.maktab58.onlinetaxisys.dao.TravelDao;
import ir.maktab58.onlinetaxisys.models.Travel;

/**
 * @author Taban Soleymani
 */
public class TravelService {
    private final TravelDao travelDao = new TravelDao();

    public void saveTravel(Travel travel) {
        travelDao.save(travel);
    }
}
