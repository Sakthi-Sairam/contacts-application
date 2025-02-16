
package com.utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import com.exceptions.QueryExecutorException;

public class DBConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DemoContacts";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private static BasicDataSource dataSource = new BasicDataSource();

    static {
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

