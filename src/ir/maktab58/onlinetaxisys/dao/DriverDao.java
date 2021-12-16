package ir.maktab58.onlinetaxisys.dao;

import ir.maktab58.onlinetaxisys.dao.singletonsessionfactory.SessionUtil;
import ir.maktab58.onlinetaxisys.models.Driver;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * @author Taban Soleymani
 */
public class DriverDao extends BaseDaoInterfaceImpl<Driver> {
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
}
