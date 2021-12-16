package ir.maktab58.onlinetaxisys.dao.singletonsessionfactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author Taban Soleymani
 */
public class SessionUtil {
    private static final SessionUtil instance = new SessionUtil();
    private final SessionFactory sessionFactory;

    public static SessionUtil getInstance() {
        return instance;
    }

    private SessionUtil() {
        Configuration configuration = new Configuration();
        configuration.configure("ir/maktab58/config/hibernate.cfg.xml");
        sessionFactory = configuration.buildSessionFactory();
    }

    public static Session getSession() {
        Session session = getInstance().sessionFactory.openSession();
        return session;
    }
}