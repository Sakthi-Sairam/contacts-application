package com.Dao;

import com.utils.DBConnection;
import com.models.Email;
import com.models.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class userDao {
	
	public static boolean mailCheck(String email) {
		String checkEmailSQL = "SELECT email FROM MailMapper WHERE email = ?";
        ResultSet rs = null;
        boolean result = false;
	    try (Connection connection = DBConnection.getConnection(); 
		         PreparedStatement ps = connection.prepareStatement(checkEmailSQL)) {
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next())
                result = true; 
	    	
	    } catch (SQLException e) {
			e.printStackTrace();
		}
	    return result;
        
	}
	
    public static boolean registerUser(String password,String firstName, String lastName, String age, String address, String phone, String email ) {
        boolean registrationSuccessful = false;
        int userId;
	    try (Connection connection = DBConnection.getConnection();){
            String insertSQL = "INSERT INTO userdata (password, first_name, last_name, age, address, phone) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, password);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setInt(4, (age != null && !age.isEmpty()) ? Integer.parseInt(age) : 0);
            ps.setString(5, address);
            ps.setString(6, phone);
            System.out.print(ps);
            
            int rowsAffected = ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            userId = 0;
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
            }


            String mailInsertSQL = "INSERT INTO MailMapper (user_id, email, isPrimary) VALUES (?, ?, true)";
            ps = connection.prepareStatement(mailInsertSQL);
            ps.setInt(1, userId);
            ps.setString(2, email);
            System.out.print(ps);
            rowsAffected = ps.executeUpdate();

            registrationSuccessful = (rowsAffected > 0);
            ps.close();
	    	
	    } catch (SQLException e) {
			e.printStackTrace();
		}
	    return registrationSuccessful;
    	
    }
    
    public static boolean updatePassword(int user_id, String password) throws SQLException {
		String sql = "update userdata set password=? where user_id=?";
		int rc = 0;
		try(Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setString(1, password);
			ps.setInt(2, user_id);

			
			rc = ps.executeUpdate();
		}
		return rc>0;
    	
    }
    
    public static User loginUser(String email, String password) throws SQLException {
        ResultSet rs = null;
        String sql = "SELECT a.user_id, a.first_name, a.password, a.last_name, a.age, a.address, a.phone FROM userdata a JOIN MailMapper b ON a.user_id = b.user_id WHERE b.email = ?";
        User user = null;
        String storedHashedPassword = null;
        int user_id=0;
        

        try (Connection connection = DBConnection.getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql)) {
            
            ps.setString(1, email);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                storedHashedPassword = rs.getString("password");
                user_id = rs.getInt("user_id");
                System.out.println("Stored Hashed Password: " + storedHashedPassword);

                boolean isCorrectPassword = BCrypt.checkpw(password, storedHashedPassword);
                if (isCorrectPassword) {
                    user = new User(
                        rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getString("address"),
                        rs.getString("phone")
                    );
                } else {
                    System.out.println("Password does not match."); // Debug log
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("Password validation failed: " + e.getMessage());
            
            if(password.equals(storedHashedPassword)) {
            	System.out.println(user_id+" "+storedHashedPassword+" : "+password);
            	updatePassword(user_id,BCrypt.hashpw(password, BCrypt.gensalt()));
            	
            	return loginUser(email,password);
            }
            
        }

        return user;
    }

	public static List<Email> getEmailsByUserId(int userId) throws SQLException {

		String sql = "select id, email, isPrimary from MailMapper where user_id = ?";
		List<Email> emails = new ArrayList<>();
		
		try(Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setInt(1, userId);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Email email = new Email(
						rs.getInt("id"),
						rs.getString("email"),
						rs.getInt("isPrimary")
					);
				emails.add(email);
				
			}
		}
		return emails;
	}
	
	public static boolean addEmailByUserId(int userId, String email) throws SQLException {

		String sql = "insert into MailMapper (user_id,email,isPrimary) values (?,?,0)";
		int rc = 0;
		try(Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setInt(1, userId);
			ps.setString(2, email);

			
			rc = ps.executeUpdate();
		}
		return rc>0;
	}
	
	public static boolean changePrimaryEmail(int userId, int emailId, int primaryEmailId) throws SQLException {
	    String sql1 = "UPDATE MailMapper SET isPrimary = 0 WHERE id = ?";
	    String sql2 = "UPDATE MailMapper SET isPrimary = 1 WHERE id = ?";
	    Connection connection = null;
	    PreparedStatement ps1 = null;
	    PreparedStatement ps2 = null;
	    boolean isSuccess = false;
	    System.out.println(primaryEmailId+" : "+emailId);

	    try {
	        connection = DBConnection.getConnection();
	        connection.setAutoCommit(false);

	        ps1 = connection.prepareStatement(sql1);
	        ps1.setInt(1, primaryEmailId);
	        int rc1 = ps1.executeUpdate();

	        ps2 = connection.prepareStatement(sql2);
	        ps2.setInt(1, emailId);
	        int rc2 = ps2.executeUpdate();

	        if (rc1 > 0 && rc2 > 0) {
	            connection.commit(); 
	            isSuccess = true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        if (connection != null) {
	            try {
	                connection.rollback(); 
	            } catch (SQLException ex) {
	                ex.printStackTrace(); 
	            }
	        }
	    } finally {
	        if (ps1 != null) {
	            ps1.close();
	        }
	        if (ps2 != null) {
	            ps2.close();
	        }
	        if (connection != null) {
	            connection.close();
	        }
	    }
	    return isSuccess;
	}


}
