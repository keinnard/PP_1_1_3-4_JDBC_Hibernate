package jm.task.core.jdbc.util;

import java.sql.*;

public class Util {

    public static Connection getConnection(String url, String name, String password) throws SQLException {
        return DriverManager.getConnection(url, name, password);

    }

    public static Connection getLocalConnection() throws SQLException {
        Connection c = getConnection("jdbc:mysql://localhost:3306", "keinnard", "123Qwe");
        c.createStatement().executeUpdate("USE Kata");
        c.setAutoCommit(false);       
        return c;
    }

}
