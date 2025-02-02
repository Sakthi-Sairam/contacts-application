package com.dao;

import com.exceptions.DaoException;
import com.exceptions.ErrorCode;
import com.exceptions.QueryExecutorException;
import com.models.Email;
import com.models.User;
import com.queryLayer.Pair;
import com.queryLayer.QueryBuilder;
import com.queryLayer.QueryExecutor;
import com.queryLayer.DatabaseSchemaEnums.MailMapperColumn;
import com.queryLayer.DatabaseSchemaEnums.Table;
import com.queryLayer.DatabaseSchemaEnums.UserDataColumn;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class UserDao {
	
	/**
	 * checks if email already exists
	 * @param email user's email
	 * @return true if email exists, false otherwise
	 * @throws DaoException 
	 */

	public static boolean mailCheck(String email) throws DaoException {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
        try {
			qb.select(MailMapperColumn.EMAIL).from(Table.MAIL_MAPPER).where(MailMapperColumn.EMAIL, "=", email, true);
			List<Email> results = executor.executeQuery(qb, Email.class);
			return !results.isEmpty();
		} catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to check mail " + e.getMessage(), e);
		}

	}
	
	/**
	 * registers a new user
	 * 
	 * @return true if registration is successful, false otherwise
	 * @throws DaoException 
	 */

	
	public static boolean registerUser(String password, String firstName, String lastName, String age, String address, String phone, String email) throws DaoException {
	    QueryBuilder qb = new QueryBuilder();
	    QueryExecutor executor = new QueryExecutor();
		long currTime = System.currentTimeMillis();

		try {
            executor.transactionStart();
            qb.insert(Table.USER_DATA)
              .columns(UserDataColumn.PASSWORD, UserDataColumn.FIRST_NAME, UserDataColumn.LAST_NAME, 
                       UserDataColumn.AGE, UserDataColumn.ADDRESS, UserDataColumn.PHONE, UserDataColumn.CREATED_AT,UserDataColumn.MODIFIED_AT)
              .values(password, firstName, lastName, 
                      (age != null && !age.isEmpty()) ? Integer.parseInt(age) : 0, 
                      address, phone, currTime, currTime);
            
            // Execute and get the generated user ID
            Pair result = executor.executeUpdateWithGeneratedKeys(qb);
            int userId = result.getGeneratedKey();

            qb = new QueryBuilder();
            qb.insert(Table.MAIL_MAPPER)
              .columns(MailMapperColumn.USER_ID, MailMapperColumn.EMAIL, MailMapperColumn.IS_PRIMARY,MailMapperColumn.CREATED_AT,MailMapperColumn.MODIFIED_AT)
              .values(userId, email, true,currTime,currTime);
            
            int rowsAffected = executor.executeUpdate(qb);
            executor.transactionEnd();
            
            return rowsAffected > 0;
        } catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to register user" + e.getMessage(), e);
        }
    }
    
    
    public static boolean updatePassword(int user_id, String password) throws DaoException {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
        try {
			qb.update(Table.USER_DATA).set(UserDataColumn.PASSWORD, password).where(UserDataColumn.USER_ID, "=",user_id , true);
			List<User> results = executor.executeQuery(qb, User.class);
			return !results.isEmpty();
		} catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to update password " + e.getMessage(), e);

		}
    }
    
    /**
     * logs in the user by email and password
     * 
     * @param email
     * @param password
     * @return user object if login successful, null otherwise
     * @throws DaoException 
     */
    public static User loginUser(String email, String password) throws DaoException{
    	QueryBuilder qb = new QueryBuilder();
    	QueryExecutor executor = new QueryExecutor();
    	try {
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
		} catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to login user" + e.getMessage(), e);
		}
    	
    	
    }
    
    /**
     * gets all emails for the user by user id
     * 
     * @param userId
     * @return list of emails
     * @throws DaoException 
     */
    public static List<Email> getEmailsByUserId(int userId) throws DaoException {
    	QueryBuilder qb = new QueryBuilder();
    	QueryExecutor executor = new QueryExecutor();
        try {
            qb = new QueryBuilder();
            qb.select(MailMapperColumn.ID, MailMapperColumn.EMAIL, MailMapperColumn.IS_PRIMARY, MailMapperColumn.CREATED_AT,MailMapperColumn.MODIFIED_AT)
              .from(Table.MAIL_MAPPER)
              .where(MailMapperColumn.USER_ID, "=", userId, true);

            return executor.executeQuery(qb, Email.class);
        } catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to get emails" + e.getMessage(), e);
        }
    }
	
	
	/**
	 * adds a new email for the user
	 * 
	 * @param userId
	 * @param email
	 * @return true if email is added, false otherwise
	 * @throws DaoException 
	 */
    public static boolean addEmailByUserId(int userId, String email) throws DaoException {
    	QueryBuilder qb = new QueryBuilder();
    	QueryExecutor executor = new QueryExecutor();
		long curTime = System.currentTimeMillis();
    	
        try {
			qb.insert(Table.MAIL_MAPPER)
			.columns(MailMapperColumn.USER_ID,MailMapperColumn.EMAIL,MailMapperColumn.IS_PRIMARY,MailMapperColumn.CREATED_AT,MailMapperColumn.MODIFIED_AT)
			.values(userId,email,0,curTime,curTime);
			int rowsAffected = executor.executeUpdate(qb);
			return rowsAffected>0;
		} catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to add email" + e.getMessage(), e);

		}
    	
    }
	
	
	/**
	 * changes the primary email for the user
	 * 
	 * @param userId
	 * @param emailId
	 * @param primaryEmailId
	 * @return true if change is successful, false otherwise
	 * @throws DaoException 
	 */
    public static boolean changePrimaryEmail(int userId, int emailId, int primaryEmailId) throws DaoException{
        	QueryBuilder qb = new QueryBuilder();
        	QueryExecutor executor = new QueryExecutor();
        	try {
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
			} catch (QueryExecutorException e) {
	            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to change primary email " + e.getMessage(), e);

			}
    }
	
	
	/**
	 * gets user details by user id
	 * 
	 * @param userId
	 * @return user object with details
	 * @throws DaoException 
	 */
	
    public static User getUserById(int userId) throws DaoException {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
//        SELECT user_id, first_name, last_name, age, address, phone FROM userdata WHERE user_id = ?
//                                                                                                                                        select id, email, isPrimary from MailMapper where user_id = ?
        try {
			qb.select(UserDataColumn.USER_ID,UserDataColumn.FIRST_NAME,UserDataColumn.LAST_NAME,UserDataColumn.AGE,UserDataColumn.ADDRESS,UserDataColumn.PHONE,UserDataColumn.CREATED_AT,UserDataColumn.MODIFIED_AT, MailMapperColumn.ID,MailMapperColumn.EMAIL, MailMapperColumn.IS_PRIMARY, MailMapperColumn.CREATED_AT,MailMapperColumn.MODIFIED_AT)
			  .from(Table.USER_DATA)
			  .join(Table.MAIL_MAPPER, UserDataColumn.USER_ID, MailMapperColumn.USER_ID)
			  .where(UserDataColumn.USER_ID, "=", userId, true);

			List<User> users = executor.executeQuery(qb, User.class);
			User user = users.get(0);
//        ContactsUtil.getContacts(user);
			return user;
		} catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to get the user" + e.getMessage(), e);

		}
    }


}
