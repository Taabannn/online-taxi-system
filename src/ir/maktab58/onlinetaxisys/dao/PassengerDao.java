package ir.maktab58.onlinetaxisys.dao;

import ir.maktab58.onlinetaxisys.dao.singletonsessionfactory.SessionUtil;
import ir.maktab58.onlinetaxisys.models.Passenger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * @author Taban Soleymani
 */
public class PassengerDao extends BaseDaoInterfaceImpl<Passenger> {
    public List<Passenger> getAllPassengers() {
        List<Passenger> passengerList;
        Session session = SessionUtil.getSession();
        Transaction transaction = session.beginTransaction();
        Query<Passenger> passengerQuery = session.createQuery("from Passenger", Passenger.class);
        passengerList = passengerQuery.getResultList();
        transaction.commit();
        session.close();
        return passengerList;
    }

}
