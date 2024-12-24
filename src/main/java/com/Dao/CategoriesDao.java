package com.Dao;

import com.QueryLayer.QueryBuilder;
import com.QueryLayer.QueryExecutor;
import com.QueryLayer.DatabaseSchemaEnums.CategoryDetailsColumn;
import com.QueryLayer.DatabaseSchemaEnums.CategoryListColumn;
import com.QueryLayer.DatabaseSchemaEnums.MyContactsDataColumn;
import com.QueryLayer.DatabaseSchemaEnums.Table;
import com.models.CategoryDetails;
import com.models.Contact;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriesDao {

    public static boolean createCategory(int userId, String categoryName) throws SQLException {
        QueryExecutor executor = new QueryExecutor();
        QueryBuilder qb = new QueryBuilder();
		long currTime = System.currentTimeMillis();

        qb.insert(Table.CATEGORY_DETAILS)
          .columns(CategoryDetailsColumn.CATEGORY_NAME, CategoryDetailsColumn.USER_ID, CategoryDetailsColumn.CREATED_AT,CategoryDetailsColumn.MODIFIED_AT)
          .values(categoryName, userId, currTime, currTime);

        int rowCount = executor.executeUpdate(qb);
        return rowCount > 0;
    }

//    public static List<CategoryDetails> getCategoriesByUserId(int userId) throws SQLException {
//        QueryBuilder qb = new QueryBuilder();
//        QueryExecutor executor = new QueryExecutor();
//        List<CategoryDetails> categories = new ArrayList<>();
//
//        qb.select(CategoryDetailsColumn.CATEGORY_ID, CategoryDetailsColumn.CATEGORY_NAME)
//          .from(Table.CATEGORY_DETAILS)
//          .where(CategoryDetailsColumn.USER_ID, "=", userId, true);
//
//        List<Object[]> results = executor.executeQuery(qb);
//        for (Object[] row : results) {
//            categories.add(new CategoryDetails((Integer) row[0], (String) row[1]));
//        }
//
//        return categories;
//    }
    
    public static List<CategoryDetails> getCategoriesByUserId(int userId) throws SQLException {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();

        qb.select(CategoryDetailsColumn.CATEGORY_ID, CategoryDetailsColumn.CATEGORY_NAME, CategoryDetailsColumn.CREATED_AT, CategoryDetailsColumn.MODIFIED_AT)
          .from(Table.CATEGORY_DETAILS)
          .where(CategoryDetailsColumn.USER_ID, "=", userId, true);

        List<CategoryDetails> categories = executor.executeQuery(qb, CategoryDetails.class);
        
        // Optional: Add logging or additional error checking
        if (categories == null) {
            System.err.println("No categories found for user ID: " + userId);
            return new ArrayList<>(); // Return empty list instead of null
        }
        
        return categories;
    }


    public static boolean addContactToCategory(int categoryId, int contactId) throws SQLException {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
		long currTime = System.currentTimeMillis();

        qb.insert(Table.CATEGORY_LIST)
          .columns(CategoryListColumn.CATEGORY_ID, CategoryListColumn.MY_CONTACTS_ID, CategoryListColumn.CREATED_AT, CategoryListColumn.MODIFIED_AT)
          .values(categoryId, contactId, currTime, currTime);

        int rowCount = executor.executeUpdate(qb);
        return rowCount > 0;
    }

//    public static boolean checkContactPresentInCategory(int categoryId, int contactId) throws SQLException {
//        QueryBuilder qb = new QueryBuilder();
//        QueryExecutor executor = new QueryExecutor();
//
//        qb.select(CategoryListColumn.CATEGORY_ID)
//          .from(Table.CATEGORY_LIST)
//          .where(CategoryListColumn.CATEGORY_ID, "=", categoryId, true)
//          .and()
//          .where(CategoryListColumn.MY_CONTACTS_ID, "=", contactId);
//
//        List<Object[]> results = executor.executeQuery(qb);
//        if(results.size()==0) return false;
//        return true;
//    }
    public static boolean checkContactPresentInCategory(int categoryId, int contactId) throws SQLException {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();

        qb.select(CategoryListColumn.CATEGORY_ID)
          .from(Table.CATEGORY_LIST)
          .where(CategoryListColumn.CATEGORY_ID, "=", categoryId, true)
          .and()
          .where(CategoryListColumn.MY_CONTACTS_ID, "=", contactId);

        List<CategoryDetails> results = executor.executeQuery(qb, CategoryDetails.class);

        // Check if any result exists
        return !results.isEmpty();
    }

    public static boolean deleteCategory(int categoryId) throws SQLException {
        QueryExecutor executor = new QueryExecutor();

        // Delete from CategoryList first
        QueryBuilder qb1 = new QueryBuilder();
        qb1.delete(Table.CATEGORY_LIST)
           .where(CategoryListColumn.CATEGORY_ID, "=", categoryId, true);

        // Delete from CategoryDetails
        QueryBuilder qb2 = new QueryBuilder();
        qb2.delete(Table.CATEGORY_DETAILS)
           .where(CategoryDetailsColumn.CATEGORY_ID, "=", categoryId, true);

        int rowCount1 = executor.executeUpdate(qb1);
        int rowCount2 = executor.executeUpdate(qb2);

        return rowCount1 > 0 || rowCount2 > 0;
    }

//    public static List<Contact> getContactsByCategoryId(int categoryId) throws SQLException {
//        QueryBuilder qb = new QueryBuilder();
//        QueryExecutor executor = new QueryExecutor();
//
//        qb.select(MyContactsDataColumn.MY_CONTACTS_ID, MyContactsDataColumn.ALIAS_FND_NAME,
//        		MyContactsDataColumn.FRIEND_EMAIL, MyContactsDataColumn.PHONE,
//        		MyContactsDataColumn.ADDRESS)
//          .from(Table.MY_CONTACTS_DATA)
//          .join(Table.CATEGORY_LIST, CategoryListColumn.MY_CONTACTS_ID,
//        		  MyContactsDataColumn.MY_CONTACTS_ID)
//          .where(CategoryListColumn.CATEGORY_ID, "=", categoryId, true);
//
//        List<Object[]> results = executor.executeQuery(qb);
//        List<Contact> contacts = new ArrayList<>();
//
//        for (Object[] row : results) {
//            contacts.add(new Contact(
//                (Integer) row[0],
//                (String) row[1],
//                (String) row[2],
//                (String) row[3],
//                (String) row[4],
//                0,
//                0
//            ));
//        }
//
//        return contacts;
//    }
    public static List<Contact> getContactsByCategoryId(int categoryId) throws SQLException {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();

        qb.select(MyContactsDataColumn.MY_CONTACTS_ID, MyContactsDataColumn.ALIAS_FND_NAME,
        		MyContactsDataColumn.FRIEND_EMAIL, MyContactsDataColumn.PHONE,
        		MyContactsDataColumn.ADDRESS, MyContactsDataColumn.IS_ARCHIVED, MyContactsDataColumn.IS_FAVORITE, MyContactsDataColumn.CREATED_AT, MyContactsDataColumn.MODIFIED_AT)
          .from(Table.MY_CONTACTS_DATA)
          .join(Table.CATEGORY_LIST, CategoryListColumn.MY_CONTACTS_ID,
        		  MyContactsDataColumn.MY_CONTACTS_ID)
          .where(CategoryListColumn.CATEGORY_ID, "=", categoryId, true);

        List<Contact> contacts = executor.executeQuery(qb, Contact.class);
        System.out.println(contacts);

        return contacts;
    }
}
