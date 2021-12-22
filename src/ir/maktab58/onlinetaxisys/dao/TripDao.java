package ir.maktab58.onlinetaxisys.dao;

import ir.maktab58.onlinetaxisys.models.Trip;
import ir.maktab58.onlinetaxisys.utils.SessionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * @author Taban Soleymani
 */
public class TripDao extends BaseDaoImpl<Trip> {
    public Trip findTripByDriverId(int driverId) {
        Trip trip;
        try {
            Session session = SessionUtil.getSession();
            Transaction transaction = session.beginTransaction();
            Query<Trip> query = session.createQuery("from Trip t where t.driver.id=:id and t.status=:status", Trip.class);
            query.setParameter("id", driverId);
            query.setParameter("status", "UNFINISHED");
            trip = query.getSingleResult();
            transaction.commit();
            session.close();
        } catch (NoResultException e) {
            trip = null;
        }
        return trip;
    }

    public List<Trip> getAllOngoingTravel() {
        List<Trip> trips;
        Session session = SessionUtil.getSession();
        Transaction transaction = session.beginTransaction();
        Query<Trip> query = session.createQuery("from Trip t where t.status=:status", Trip.class);
        query.setParameter("status", "UNFINISHED");
        trips = query.getResultList();
        transaction.commit();
        session.close();
        return trips;
    }
}
