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

import com.exceptions.ErrorCode;
import com.exceptions.QueryExecutorException;

public class DBConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DemoContacts";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private static BasicDataSource dataSource = new BasicDataSource();

    static {
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USER);
        dataSource.setPassword(DB_PASS);
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setMaxTotal(25);
    }

    public static Connection getConnection() throws QueryExecutorException {
			try {
				return dataSource.getConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
    }
}

