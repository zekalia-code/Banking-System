package com.jkash.database;

import java.sql.*;

public class DBConnection
{
    private static final String url = "jdbc:mysql://localhost:3306/bankingapp";
    private static final String user = "root";
    private static final String password = "";

    public static Connection getConnection()
            throws SQLException
    {
        return DriverManager.getConnection(url, user, password);
    }
}