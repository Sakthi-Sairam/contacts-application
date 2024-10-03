package com.utils;

import java.sql.*;

import com.models.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class DBConnection {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/DemoContacts";
    private static final String DB_USER = "root";
    private static final String DB_PASS = null;
    public static Connection getConnection() throws SQLException {
    	return DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
    }
    
    
    public static User getSession(HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	User user = (User) session.getAttribute("user");
    	return user;
    }

}
