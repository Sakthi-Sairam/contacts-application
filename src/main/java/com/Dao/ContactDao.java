package com.Dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.models.Contact;
import com.utils.*;
public class ContactDao {
	public static boolean addContact(String friend_email, String alias_fnd_name, String phone, String address, int user_id, int isArchived, int isFavorite) throws SQLException {
		String sql = "INSERT INTO MyContactsData (user_id, friend_email, alias_fnd_name, phone, address, isArchived, isFavorite) VALUES (?, ?, ?, ?, ?, ?, ?)";
	    try (Connection connection = DBConnection.getConnection();
	            PreparedStatement ps = connection.prepareStatement(sql)) {
	           ps.setInt(1, user_id);
	           ps.setString(2, friend_email);
	           ps.setString(3, alias_fnd_name);
	           ps.setString(4, phone);
	           ps.setString(5, address);
	           ps.setInt(6, isArchived);
	           ps.setInt(7, isFavorite);
	           int rc = ps.executeUpdate();
	           return rc>0;
	       }
	    
		
	}

	public static List<Contact> getContactsByUserId(int userId) throws SQLException {

		String sql = "select MyContactsID, alias_fnd_name, friend_email, phone, address, isArchived, isFavorite from MyContactsData where user_id = ? order by alias_fnd_name";
		List<Contact> contacts = new ArrayList<>();
		
		try(Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setInt(1, userId);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Contact contact = new Contact(
						rs.getInt("MyContactsID"),
						rs.getString("alias_fnd_name"),
						rs.getString("friend_email"),
						rs.getString("phone"),
						rs.getString("address"),
						rs.getInt("isArchived"),
						rs.getInt("isFavorite")
					);
				contacts.add(contact);
				
			}
		}
		return contacts;
	}
	
	public static boolean deleteContact(int MyContactId) throws SQLException {
		String sql = "delete from MyContactsData where MyContactsID = ?";
		int rc=0;
		try(Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setInt(1, MyContactId);
			rc = ps.executeUpdate();
			
		}
        return rc>0;
	}
	
	public static boolean updateContact(Contact contact) throws SQLException {
		String sql = "update MyContactsData "
				+ "set friend_email=?, alias_fnd_name=?, phone=?, address=?, isArchived=?, isFavorite=? "
				+ "where MyContactsID=?";
		int rc=0;
		try(Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setString(1, contact.getFriend_email());
			ps.setString(2, contact.getAlias_name());
			ps.setString(3, contact.getPhone());
			ps.setString(4, contact.getAddress());
			ps.setInt(5, contact.getIsArchived());
			ps.setInt(6, contact.getIsFavorite());
			ps.setInt(7, contact.getMyContactsID());

			rc = ps.executeUpdate();
			
		}
        return rc>0;
	}
}
