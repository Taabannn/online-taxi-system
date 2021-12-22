package ir.maktab58.onlinetaxisys.dao;

import ir.maktab58.onlinetaxisys.models.Driver;
import ir.maktab58.onlinetaxisys.utils.SessionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * @author Taban Soleymani
 */
public class DriverDao extends BaseDaoImpl<Driver> {
    public List<Driver> getAllDrivers() {
        List<Driver> driverList;
        Session session = SessionUtil.getSession();
        Transaction transaction = session.beginTransaction();
        Query<Driver> driverQuery = session.createQuery("from Driver ", Driver.class);
        driverList = driverQuery.getResultList();
        transaction.commit();
        session.close();
        return driverList;
    }

    public List<Driver> findDriverByUsername(String username) {
        List<Driver> driverList;
        Session session = SessionUtil.getSession();
        Transaction transaction = session.beginTransaction();
        Query<Driver> driverQuery = session.createQuery("from Driver d where d.username=:username", Driver.class);
        driverQuery.setParameter("username", username);
        driverList = driverQuery.getResultList();
        transaction.commit();
        session.close();
        return driverList;
    }

    public List<Driver> findDriverByNationalCode(String nationalCode) {
        List<Driver> driverList;
        Session session = SessionUtil.getSession();
        Transaction transaction = session.beginTransaction();
        Query<Driver> driverQuery = session.createQuery("from Driver d where d.nationalCode=:nationalCode", Driver.class);
        driverQuery.setParameter("nationalCode", nationalCode);
        driverList = driverQuery.getResultList();
        transaction.commit();
        session.close();
        return driverList;
    }

    public Driver findDriverByUserAndPass(String username, String password) {
        Driver driver;
        try {
            Session session = SessionUtil.getSession();
            Transaction transaction = session.beginTransaction();
            Query<Driver> query = session.createQuery("from Driver d where d.username=:username and d.password=:password", Driver.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            driver = query.getSingleResult();
            transaction.commit();
            session.close();
        } catch (NoResultException e) {
            driver = null;
        }
        return driver;
    }

    public Driver finDriverById(int driverId) {
        Driver driver;
        try {
            Session session = SessionUtil.getSession();
            Transaction transaction = session.beginTransaction();
            Query<Driver> query = session.createQuery("from Driver d where d.id=:id", Driver.class);
            query.setParameter("id", driverId);
            driver = query.getSingleResult();
            transaction.commit();
            session.close();
        } catch (NoResultException e) {
            driver = null;
        }
        return driver;
    }

    public List<Driver> getWaitingForTravelDrivers() {
        List<Driver> driverList;
        Session session = SessionUtil.getSession();
        Transaction transaction = session.beginTransaction();
        Query<Driver> driverQuery = session.createQuery("from Driver d where d.stateOfAttendance=:stateOfDriver", Driver.class);
        driverQuery.setParameter("stateOfDriver", "WAITING_FOR_TRAVEL");
        driverList = driverQuery.getResultList();
        transaction.commit();
        session.close();
        return driverList;
    }
}
