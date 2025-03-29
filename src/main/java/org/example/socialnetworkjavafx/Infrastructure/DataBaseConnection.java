package org.example.socialnetworkjavafx.Infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private final String URL;
    private final String USER;
    private final String PASS;
    Connection con;

    public DataBaseConnection(String URL, String USER, String PASS) throws SQLException, ClassNotFoundException {
        this.URL = URL;
        this.USER = USER;
        this.PASS = PASS;
        Class.forName("org.postgresql.Driver");
        con = DriverManager.getConnection(URL, USER, PASS);
        System.out.println("Database connection established");
    }

    public Connection getConnection(){
        return con;
    }
}
