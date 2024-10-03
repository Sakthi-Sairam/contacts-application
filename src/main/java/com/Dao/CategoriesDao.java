package com.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.models.*;
import com.utils.DBConnection;

public class CategoriesDao {
    

    public static boolean createCategory(int userId, String categoryName) {
        String sql = "INSERT INTO CategoryDetails (categoryName, user_id) VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, categoryName);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<CategoryDetails> getCategoriesByUserId(int userId) {
        String sql = "SELECT categoryId, categoryName FROM CategoryDetails WHERE user_id = ?";
        List<CategoryDetails> categories = new ArrayList<>();
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CategoryDetails CategoryDetails = new CategoryDetails(rs.getInt("categoryId"), rs.getString("categoryName"));
                categories.add(CategoryDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return categories;
    }
    

    public static boolean addContactToCategory(int categoryId, int contactId) {
        String sql = "INSERT INTO CategoryList (categoryId, MyContactsID) VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.setInt(2, contactId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean checkContactPresentInCategory(int categoryId, int contactId) {
        String sql = "select * from CategoryList where categoryId=? and MyContactsID=?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.setInt(2, contactId);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean deleteCategory(int categoryId) throws SQLException {
    	String sql1 = "delete from CategoryList where categoryId=?";
    	String sql2 = "delete from CategoryDetails where categoryId=?";
    	int rc;
    	 
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps1 = connection.prepareStatement(sql1);
        		PreparedStatement ps2 = connection.prepareStatement(sql2)) {
            ps1.setInt(1, categoryId);
            ps1.executeUpdate();
            ps2.setInt(1, categoryId);
            rc = ps2.executeUpdate();
        }
		return rc>0;
    	
    }

    public static List<Contact> getContactsByCategoryId(int categoryId) {
        String sql = "SELECT c.MyContactsID, c.alias_fnd_name, c.friend_email, c.phone, c.address FROM MyContactsData c INNER JOIN CategoryList cl ON c.MyContactsID = cl.MyContactsID WHERE cl.categoryId = ?";
        List<Contact> contacts = new ArrayList<>();
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Contact contact = new Contact(
                    rs.getInt("MyContactsID"),
                    rs.getString("alias_fnd_name"),
                    rs.getString("friend_email"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    0,
                    0
                );
                contacts.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return contacts;
    }
}
