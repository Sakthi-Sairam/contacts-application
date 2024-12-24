//package com.utils;


/**
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
    
    
//    public static User getSession(HttpServletRequest request) {
//    	HttpSession session = request.getSession();
//    	User user = (User) session.getAttribute("user");
//    	return user;
//    }

}
*/

package com.utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

public class DBConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DemoContacts";
    private static final String DB_USER = "root";
    private static final String DB_PASS = null;

    private static BasicDataSource dataSource = new BasicDataSource();

    static {
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USER);
        dataSource.setPassword(DB_PASS);
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setMaxTotal(25);
    }

    public static Connection getConnection()  {
        try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
    }
}

