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
        Query<Passenger> passengerQuery = session.createQuery("from Passenger ", Passenger.class);
        passengerList = passengerQuery.getResultList();
        transaction.commit();
        session.close();
        return passengerList;
    }

    public List<Passenger> findPassengerByNationalCode(String nationalCode) {
        List<Passenger> passengerList;
        Session session = SessionUtil.getSession();
        Transaction transaction = session.beginTransaction();
        Query<Passenger> passengerQuery = session.createQuery("from Passenger p where p.nationalCode=:nationalCode", Passenger.class);
        passengerQuery.setParameter("nationalCode", nationalCode);
        passengerList = passengerQuery.getResultList();
        transaction.commit();
        session.close();
        return passengerList;
    }

    public List<Passenger> findPassengerByUsername(String username) {
        List<Passenger> passengerList;
        Session session = SessionUtil.getSession();
        Transaction transaction = session.beginTransaction();
        Query<Passenger> passengerQuery = session.createQuery("from Passenger p where p.username=:username", Passenger.class);
        passengerQuery.setParameter("username", username);
        passengerList = passengerQuery.getResultList();
        transaction.commit();
        session.close();
        return passengerList;
    }
}
