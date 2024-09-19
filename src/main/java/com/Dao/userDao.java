package com.Dao;

import com.utils.DBConnection;
import com.models.User;
import java.sql.*;

public class userDao {
	
//    public User registerUser() {
//        Connection connection = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        boolean registrationSuccessful = false;
//    	
//    }
	public static User loginUser(String email, String password) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = DBConnection.getConnection();
        User user = null;
        
        String sql = "SELECT a.user_id, a.first_name, a.last_name, a.age, a.address, a.phone FROM userdata a JOIN MailMapper b ON a.user_id = b.user_id WHERE b.email = ? AND a.password = ?";
        ps = connection.prepareStatement(sql);
        
        ps.setString(1, email);
        ps.setString(2, password);

        rs = ps.executeQuery();
        if (rs.next()) {
        	user = new User(rs.getInt("user_id"),rs.getString("first_name"),rs.getString("last_name"),rs.getInt("age"),rs.getString("address"),rs.getString("phone"));
        }
        
        
        return user;
	}
}
