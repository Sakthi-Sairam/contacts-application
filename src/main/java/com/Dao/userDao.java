package com.Dao;

import com.utils.ContactsUtil;
import com.utils.DBConnection;
import com.QueryLayer.Pair;
import com.QueryLayer.QueryBuilder;
import com.QueryLayer.QueryExecutor;
import com.QueryLayer.DatabaseSchemaEnums.CategoryListColumn;
import com.QueryLayer.DatabaseSchemaEnums.MailMapperColumn;
import com.QueryLayer.DatabaseSchemaEnums.MyContactsDataColumn;
import com.QueryLayer.DatabaseSchemaEnums.Table;
import com.QueryLayer.DatabaseSchemaEnums.UserDataColumn;
import com.models.Email;
import com.models.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class userDao {
	
	/**
	 * checks if email already exists
	 * @param email user's email
	 * @return true if email exists, false otherwise
	 */

	public static boolean mailCheck(String email) {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
        qb.select(MailMapperColumn.EMAIL).from(Table.MAIL_MAPPER).where(MailMapperColumn.EMAIL, "=", email, true);
        List<Email> results = executor.executeQuery(qb, Email.class);
        return !results.isEmpty();

	}
	
	/**
	 * registers a new user
	 * 
	 * @return true if registration is successful, false otherwise
	 */
//    public static boolean registerUser(String password,String firstName, String lastName, String age, String address, String phone, String email ) {
//        boolean registrationSuccessful = false;
//        int userId;
//	    try (Connection connection = DBConnection.getConnection();){
//            String insertSQL = "INSERT INTO userdata (password, first_name, last_name, age, address, phone) VALUES (?, ?, ?, ?, ?, ?)";
//            PreparedStatement ps = connection.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);
//            ps.setString(1, password);
//            ps.setString(2, firstName);
//            ps.setString(3, lastName);
//            ps.setInt(4, (age != null && !age.isEmpty()) ? Integer.parseInt(age) : 0);
//            ps.setString(5, address);
//            ps.setString(6, phone);
//            System.out.print(ps);
//            
//            int rowsAffected = ps.executeUpdate();
//            ResultSet generatedKeys = ps.getGeneratedKeys();
//            userId = 0;
//            if (generatedKeys.next()) {
//                userId = generatedKeys.getInt(1);
//            }
//
//
//            String mailInsertSQL = "INSERT INTO MailMapper (user_id, email, isPrimary) VALUES (?, ?, true)";
//            ps = connection.prepareStatement(mailInsertSQL);
//            ps.setInt(1, userId);
//            ps.setString(2, email);
//            System.out.print(ps);
//            rowsAffected = ps.executeUpdate();
//
//            registrationSuccessful = (rowsAffected > 0);
//            ps.close();
//	    	
//	    } catch (SQLException e) {
//			e.printStackTrace();
//		}
//	    return registrationSuccessful;
//    	
//    }
	
	public static boolean registerUser(String password, String firstName, String lastName, String age, String address, String phone, String email) {
        try {
            QueryBuilder qb = new QueryBuilder();
            QueryExecutor executor = new QueryExecutor();
    		long currTime = System.currentTimeMillis();

            executor.transactionStart();
            // First, insert user data query
            qb.insert(Table.USER_DATA)
              .columns(UserDataColumn.PASSWORD, UserDataColumn.FIRST_NAME, UserDataColumn.LAST_NAME, 
                       UserDataColumn.AGE, UserDataColumn.ADDRESS, UserDataColumn.PHONE, UserDataColumn.CREATED_AT,UserDataColumn.MODIFIED_AT)
              .values(password, firstName, lastName, 
                      (age != null && !age.isEmpty()) ? Integer.parseInt(age) : 0, 
                      address, phone, currTime, currTime);
            
            // Execute and get the generated user ID
            Pair result = executor.executeUpdateWithGeneratedKeys(qb);
            int userId = result.getGeneratedKey();

            // Then, insert email query
            qb = new QueryBuilder();
            qb.insert(Table.MAIL_MAPPER)
              .columns(MailMapperColumn.USER_ID, MailMapperColumn.EMAIL, MailMapperColumn.IS_PRIMARY,MailMapperColumn.CREATED_AT,MailMapperColumn.MODIFIED_AT)
              .values(userId, email, true,currTime,currTime);
            
            int rowsAffected = executor.executeUpdate(qb);
            executor.transactionEnd();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public static boolean updatePassword(int user_id, String password) {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
        qb.update(Table.USER_DATA).set(UserDataColumn.PASSWORD, password).where(UserDataColumn.USER_ID, "=",user_id , true);
        List<User> results = executor.executeQuery(qb, User.class);
        return !results.isEmpty();
    }
    
    /**
     * logs in the user by email and password
     * 
     * @param email
     * @param password
     * @return user object if login successful, null otherwise
     */
    public static User loginUser(String email, String password) throws SQLException {
    	QueryBuilder qb = new QueryBuilder();
    	QueryExecutor executor = new QueryExecutor();
    	qb.select(UserDataColumn.USER_ID, UserDataColumn.FIRST_NAME, UserDataColumn.PASSWORD, UserDataColumn.LAST_NAME, UserDataColumn.AGE, UserDataColumn.ADDRESS, UserDataColumn.PHONE)
    	.from(Table.USER_DATA)
    	.join(Table.MAIL_MAPPER, UserDataColumn.USER_ID, MailMapperColumn.USER_ID)
    	.where(MailMapperColumn.EMAIL, "=", email, true);
    	
    	List<User> users = executor.executeQuery(qb, User.class);
    	if(users.isEmpty()) return null;
    	
    	User user = users.get(0);
    	System.out.println(user);
    	String storedHashedPassword = user.getPassword();
    	System.out.print(storedHashedPassword);
    	boolean isCorrectPassword = BCrypt.checkpw(password, storedHashedPassword);
	      if (isCorrectPassword) {
	    	  user.setPassword(null);
	    	  return user;
	      } else {
	    	  System.out.println("Password does not match.");
	      }
	      return null;
    	
    	
    }
//    public static User loginUser(String email, String password) throws SQLException {
//        ResultSet rs = null;
//        String sql = "SELECT a.user_id, a.first_name, a.password, a.last_name, a.age, a.address, a.phone FROM userdata a JOIN MailMapper b ON a.user_id = b.user_id WHERE b.email = ?";
//        User user = null;
//        String storedHashedPassword = null;
//        int user_id=0;
//        
//
//        try (Connection connection = DBConnection.getConnection(); 
//             PreparedStatement ps = connection.prepareStatement(sql)) {
//            
//            ps.setString(1, email);
//            rs = ps.executeQuery();
//            
//            if (rs.next()) {
//                storedHashedPassword = rs.getString("password");
//                user_id = rs.getInt("user_id");
//
//                boolean isCorrectPassword = BCrypt.checkpw(password, storedHashedPassword);
//                if (isCorrectPassword) {
//                    user = new User(
//                        rs.getInt("user_id"),
//                        rs.getString("first_name"),
//                        rs.getString("last_name"),
//                        rs.getInt("age"),
//                        rs.getString("address"),
//                        rs.getString("phone")
//                    );
//                } else {
//                    System.out.println("Password does not match.");
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            
//            if(password.equals(storedHashedPassword)) {
//            	System.out.println(user_id+" "+storedHashedPassword+" : "+password);
//            	updatePassword(user_id,BCrypt.hashpw(password, BCrypt.gensalt()));
//            	
//            	return loginUser(email,password);
//            }
//            
//        }
//
//        return user;
//    }
    
    /**
     * gets all emails for the user by user id
     * 
     * @param userId
     * @return list of emails
     */
    public static List<Email> getEmailsByUserId(int userId) {
    	QueryBuilder qb = new QueryBuilder();
    	QueryExecutor executor = new QueryExecutor();
        try {
            qb = new QueryBuilder();
            qb.select(MailMapperColumn.ID, MailMapperColumn.EMAIL, MailMapperColumn.IS_PRIMARY, MailMapperColumn.CREATED_AT,MailMapperColumn.MODIFIED_AT)
              .from(Table.MAIL_MAPPER)
              .where(MailMapperColumn.USER_ID, "=", userId, true);

            return executor.executeQuery(qb, Email.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
	
	
	/**
	 * adds a new email for the user
	 * 
	 * @param userId
	 * @param email
	 * @return true if email is added, false otherwise
	 */
    public static boolean addEmailByUserId(int userId, String email) throws SQLException {
    	QueryBuilder qb = new QueryBuilder();
    	QueryExecutor executor = new QueryExecutor();
		long curTime = System.currentTimeMillis();
    	
        qb.insert(Table.MAIL_MAPPER)
        .columns(MailMapperColumn.USER_ID,MailMapperColumn.EMAIL,MailMapperColumn.IS_PRIMARY,MailMapperColumn.CREATED_AT,MailMapperColumn.MODIFIED_AT)
        .values(userId,email,0,curTime,curTime);
        int rowsAffected = executor.executeUpdate(qb);
        return rowsAffected>0;
    	
    }
//    public static boolean addEmailByUserId(int userId, String email) {
//    	QueryBuilder qb = new QueryBuilder();
//    	QueryExecutor executor = new QueryExecutor();
//        try {
//            qb.insert(Table.MAIL_MAPPER)
//              .columns(MailMapperColumn.USER_ID, MailMapperColumn.EMAIL, MailMapperColumn.IS_PRIMARY)
//              .values(userId, email, 0);
//
//            int rowsAffected = executor.executeUpdate(qb);
//            return rowsAffected > 0;
//        } catch (Exception e) {
//        	System.out.println("this exeception is working");
//            e.printStackTrace();
//            return false;
//        }
//    }
	
	
	/**
	 * changes the primary email for the user
	 * 
	 * @param userId
	 * @param emailId
	 * @param primaryEmailId
	 * @return true if change is successful, false otherwise
	 */
    public static boolean changePrimaryEmail(int userId, int emailId, int primaryEmailId) throws SQLException{
        	QueryBuilder qb = new QueryBuilder();
        	QueryExecutor executor = new QueryExecutor();
        	// Start transaction
            executor.transactionStart();
            qb.update(Table.MAIL_MAPPER)
              .set(MailMapperColumn.IS_PRIMARY, 0).set(MailMapperColumn.MODIFIED_AT, System.currentTimeMillis())
              .where(MailMapperColumn.ID, "=", primaryEmailId, true);
            executor.executeUpdate(qb);

            // Set new primary email
            qb = new QueryBuilder();
            qb.update(Table.MAIL_MAPPER)
              .set(MailMapperColumn.IS_PRIMARY, 1).set(MailMapperColumn.MODIFIED_AT, System.currentTimeMillis())
              .where(MailMapperColumn.ID, "=", emailId, true);
            executor.executeUpdate(qb);

            // Commit transaction
            executor.transactionEnd();
            return true;
    }
	
	
	/**
	 * gets user details by user id
	 * 
	 * @param userId
	 * @return user object with details
	 */
//    public static User getUserById(int userId) {
//    	System.out.println("coming here to getUserById");
//        String sql = "SELECT user_id, first_name, last_name, age, address, phone FROM userdata WHERE user_id = ?";
//        User user = null;
//
//        try (Connection connection = DBConnection.getConnection();
//             PreparedStatement ps = connection.prepareStatement(sql)) {
//            
//            ps.setInt(1, userId);
//            ResultSet rs = ps.executeQuery();
//            
//            if (rs.next()) {
//                // Retrieve user details
//                user = new User(
//                    rs.getInt("user_id"),
//                    rs.getString("first_name"),
//                    rs.getString("last_name"),
//                    rs.getInt("age"),
//                    rs.getString("address"),
//                    rs.getString("phone")
//                );
//                
//                // Populate emails
////                List<Email> emails = getEmailsByUserId(userId);
////                user.setEmails(emails);
//                ContactsUtil.getContacts(user);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return user;
//    }
	
    public static User getUserById(int userId) {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
//        SELECT user_id, first_name, last_name, age, address, phone FROM userdata WHERE user_id = ?
//                                                                                                                                        select id, email, isPrimary from MailMapper where user_id = ?
        qb.select(UserDataColumn.USER_ID,UserDataColumn.FIRST_NAME,UserDataColumn.LAST_NAME,UserDataColumn.AGE,UserDataColumn.ADDRESS,UserDataColumn.PHONE,UserDataColumn.CREATED_AT,UserDataColumn.MODIFIED_AT, MailMapperColumn.ID,MailMapperColumn.EMAIL, MailMapperColumn.IS_PRIMARY, MailMapperColumn.CREATED_AT,MailMapperColumn.MODIFIED_AT)
          .from(Table.USER_DATA)
          .join(Table.MAIL_MAPPER, UserDataColumn.USER_ID, MailMapperColumn.USER_ID)
          .where(UserDataColumn.USER_ID, "=", userId, true);

        List<User> users = executor.executeQuery(qb, User.class);
        User user = users.get(0);
        ContactsUtil.getContacts(user);
        return user;
    }


}
