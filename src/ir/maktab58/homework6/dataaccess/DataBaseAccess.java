package ir.maktab58.homework6.dataaccess;

import java.sql.*;

public abstract class DataBaseAccess implements DataBaseAccessInterface {
    protected static Connection connection = null;

    public DataBaseAccess() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinetaxisys",
                    "root", "61378");
        } catch (SQLException | ClassNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
