package com.Dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.QueryLayer.QueryBuilder;
import com.QueryLayer.QueryExecutor;
import com.QueryLayer.DatabaseSchemaEnums.CategoryDetailsColumn;
import com.QueryLayer.DatabaseSchemaEnums.CategoryListColumn;
import com.QueryLayer.DatabaseSchemaEnums.MyContactsDataColumn;
import com.QueryLayer.DatabaseSchemaEnums.Table;
import com.models.Contact;
import com.utils.*;
public class ContactDao {
//	public static boolean addContact(String friend_email, String alias_fnd_name, String phone, String address, int user_id, int isArchived, int isFavorite) throws SQLException {
//		String sql = "INSERT INTO MyContactsData (user_id, friend_email, alias_fnd_name, phone, address, isArchived, isFavorite) VALUES (?, ?, ?, ?, ?, ?, ?)";
//	    try (Connection connection = DBConnection.getConnection();
//	            PreparedStatement ps = connection.prepareStatement(sql)) {
//	           ps.setInt(1, user_id);
//	           ps.setString(2, friend_email);
//	           ps.setString(3, alias_fnd_name);
//	           ps.setString(4, phone);
//	           ps.setString(5, address);
//	           ps.setInt(6, isArchived);
//	           ps.setInt(7, isFavorite);
//	           int rc = ps.executeUpdate();
//	           return rc>0;
//	       }
//	}
	public static boolean addContact(String friend_email, String alias_fnd_name, String phone, String address, int user_id, int isArchived, int isFavorite) throws SQLException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor exe = new QueryExecutor();
		long time = System.currentTimeMillis();
		qb.insert(Table.MY_CONTACTS_DATA)
		.columns(MyContactsDataColumn.USER_ID,MyContactsDataColumn.FRIEND_EMAIL,MyContactsDataColumn.ALIAS_FND_NAME,MyContactsDataColumn.PHONE,MyContactsDataColumn.ADDRESS,MyContactsDataColumn.IS_ARCHIVED,MyContactsDataColumn.IS_FAVORITE,MyContactsDataColumn.CREATED_AT,MyContactsDataColumn.MODIFIED_AT)
		.values(user_id,friend_email,alias_fnd_name,phone,address,isArchived,isFavorite,time,time);
		int rc = exe.executeUpdate(qb);
		return rc>0;
	}

//	public static List<Contact> getContactsByUserId(int userId) throws SQLException {
//
//		String sql = "select MyContactsID, alias_fnd_name, friend_email, phone, address, isArchived, isFavorite from MyContactsData where user_id = ? order by alias_fnd_name";
//		List<Contact> contacts = new ArrayList<>();
//		
//		try(Connection connection = DBConnection.getConnection();
//				PreparedStatement ps = connection.prepareStatement(sql)){
//			ps.setInt(1, userId);
//			
//			ResultSet rs = ps.executeQuery();
//			while(rs.next()) {
//				Contact contact = new Contact(
//						rs.getInt("MyContactsID"),
//						rs.getString("alias_fnd_name"),
//						rs.getString("friend_email"),
//						rs.getString("phone"),
//						rs.getString("address"),
//						rs.getInt("isArchived"),
//						rs.getInt("isFavorite")
//					);
//				contacts.add(contact);
//				
//			}
//		}
//		return contacts;
//	}
    public static List<Contact> getContactsByUserId(int userId) throws SQLException {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
        List<Contact> contacts = new ArrayList<>();

        qb.select(MyContactsDataColumn.MY_CONTACTS_ID, MyContactsDataColumn.ALIAS_FND_NAME, MyContactsDataColumn.FRIEND_EMAIL, MyContactsDataColumn.PHONE, MyContactsDataColumn.ADDRESS, MyContactsDataColumn.IS_ARCHIVED, MyContactsDataColumn.IS_FAVORITE, MyContactsDataColumn.CREATED_AT,MyContactsDataColumn.MODIFIED_AT)
          .from(Table.MY_CONTACTS_DATA)
          .where(MyContactsDataColumn.USER_ID, "=", userId, true)
          .orderBy(MyContactsDataColumn.ALIAS_FND_NAME,true);

        contacts = executor.executeQuery(qb, Contact.class);
        return contacts;
    }
    
	public static boolean deleteContact(int MyContactId) throws SQLException {
		QueryBuilder qb1 = new QueryBuilder();
		QueryBuilder qb2 = new QueryBuilder();
		QueryExecutor exe = new QueryExecutor();
		qb1.delete(Table.CATEGORY_LIST).where(CategoryListColumn.MY_CONTACTS_ID, "=", MyContactId, true);
		qb2.delete(Table.MY_CONTACTS_DATA).where(MyContactsDataColumn.MY_CONTACTS_ID, "=", MyContactId, true);
		
		exe.transactionStart();
		int rc1 = exe.executeUpdate(qb1);
		int rc2 = exe.executeUpdate(qb2);
		exe.transactionEnd();
		return rc2>0;


	}
	
	
//	public static boolean deleteContact(int MyContactId) throws SQLException {
//	    String sql1 = "delete from CategoryList where MyContactsID =?";
//	    String sql2 = "delete from MyContactsData where MyContactsID = ?";
//	    Connection connection = null;
//	    PreparedStatement ps1 = null;
//	    PreparedStatement ps2 = null;
//	    boolean isSuccess = false;
//
//	    try {
//	        connection = DBConnection.getConnection();
//	        connection.setAutoCommit(false);
//
//	        ps1 = connection.prepareStatement(sql1);
//	        ps1.setInt(1, MyContactId);
//	        int rc1 = ps1.executeUpdate();
//
//	        ps2 = connection.prepareStatement(sql2);
//	        ps2.setInt(1, MyContactId);
//	        int rc2 = ps2.executeUpdate();
//
////	        if (rc1 > 0 && rc2 > 0) {
//	        if (rc2 > 0) {
//	            connection.commit(); 
//	            isSuccess = true;
//	        }
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	        if (connection != null) {
//	            try {
//	                connection.rollback(); 
//	            } catch (SQLException ex) {
//	                ex.printStackTrace(); 
//	            }
//	        }
//	    }finally {
//	        if (ps1 != null) {
//	            ps1.close();
//	        }
//	        if (ps2 != null) {
//	            ps2.close();
//	        }
//	        if (connection != null) {
//	            connection.close();
//	        }
//	    }
//		return isSuccess;
//	}
	
//	public static boolean updateContact(Contact contact) throws SQLException {
//		String sql = "update MyContactsData "
//				+ "set friend_email=?, alias_fnd_name=?, phone=?, address=?, isArchived=?, isFavorite=? "
//				+ "where MyContactsID=?";
//		int rc=0;
//		try(Connection connection = DBConnection.getConnection();
//				PreparedStatement ps = connection.prepareStatement(sql)){
//			ps.setString(1, contact.getFriend_email());
//			ps.setString(2, contact.getAlias_name());
//			ps.setString(3, contact.getPhone());
//			ps.setString(4, contact.getAddress());
//			ps.setInt(5, contact.getIsArchived());
//			ps.setInt(6, contact.getIsFavorite());
//			ps.setInt(7, contact.getMyContactsID());
//
//			rc = ps.executeUpdate();
//			
//		}
//        return rc>0;
//	}
	public static boolean updateContact(Contact contact) throws SQLException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor exe = new QueryExecutor();
		long curTime = System.currentTimeMillis();
		   qb.update(Table.MY_CONTACTS_DATA)
		      .set(MyContactsDataColumn.FRIEND_EMAIL, contact.getFriend_email())
		      .set(MyContactsDataColumn.ALIAS_FND_NAME, contact.getAlias_name())
		      .set(MyContactsDataColumn.PHONE, contact.getPhone())
		      .set(MyContactsDataColumn.ADDRESS, contact.getAddress())
		      .set(MyContactsDataColumn.IS_ARCHIVED, contact.getIsArchived())
		      .set(MyContactsDataColumn.IS_FAVORITE, contact.getIsFavorite())
		      .set(MyContactsDataColumn.MODIFIED_AT, curTime)
		      .where(MyContactsDataColumn.MY_CONTACTS_ID, "=", contact.getMyContactsID(), true);

		    return exe.executeUpdate(qb) > 0;
	}

}
