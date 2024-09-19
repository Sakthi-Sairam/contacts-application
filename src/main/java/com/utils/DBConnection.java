package com.utils;

import java.sql.*;

public class DBConnection {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/DemoContacts";
    private static final String DB_USER = "root";
    private static final String DB_PASS = null;
    public static Connection getConnection() throws SQLException {
    	return DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
    }

}
