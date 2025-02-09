package com.dao;

import com.exceptions.DaoException;
import com.exceptions.ErrorCode;
import com.exceptions.QueryExecutorException;
import com.models.CategoryDetails;
import com.models.Contact;
import com.queryLayer.QueryBuilder;
import com.queryLayer.QueryExecutor;
import com.queryLayer.databaseSchemaEnums.CategoryDetailsColumn;
import com.queryLayer.databaseSchemaEnums.CategoryListColumn;
import com.queryLayer.databaseSchemaEnums.MyContactsDataColumn;
import com.queryLayer.databaseSchemaEnums.Table;

import java.util.List;

public class CategoriesDao {

    public static boolean createCategory(int userId, String categoryName) throws DaoException {
        QueryExecutor executor = new QueryExecutor();
        QueryBuilder qb = new QueryBuilder();
        long currTime = System.currentTimeMillis();
        try {
            qb.insert(Table.CATEGORY_DETAILS)
              .columns(CategoryDetailsColumn.CATEGORY_NAME, CategoryDetailsColumn.USER_ID, 
                       CategoryDetailsColumn.CREATED_AT, CategoryDetailsColumn.MODIFIED_AT)
              .values(categoryName, userId, currTime, currTime);

            int rowCount = executor.executeUpdate(qb);
            return rowCount > 0;
        } catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to create category: " + e.getMessage(), e);
        }
    }

    public static List<CategoryDetails> getCategoriesByUserId(int userId) throws DaoException {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
        try {
            qb.select(CategoryDetailsColumn.CATEGORY_ID, CategoryDetailsColumn.CATEGORY_NAME, 
                      CategoryDetailsColumn.CREATED_AT, CategoryDetailsColumn.MODIFIED_AT)
              .from(Table.CATEGORY_DETAILS)
              .where(CategoryDetailsColumn.USER_ID, "=", userId, true);

            return executor.executeQuery(qb, CategoryDetails.class);
        } catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to fetch categories: " + e.getMessage(), e);
        }
    }

    public static boolean addContactToCategory(int categoryId, int contactId) throws DaoException {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
        long currTime = System.currentTimeMillis();
        try {
            qb.insert(Table.CATEGORY_LIST)
              .columns(CategoryListColumn.CATEGORY_ID, CategoryListColumn.MY_CONTACTS_ID, 
                       CategoryListColumn.CREATED_AT, CategoryListColumn.MODIFIED_AT)
              .values(categoryId, contactId, currTime, currTime);

            return executor.executeUpdate(qb) > 0;
        } catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to add contact to category: " + e.getMessage(), e);
        }
    }

    public static boolean checkContactPresentInCategory(int categoryId, int contactId) throws DaoException {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
        try {
            qb.select(CategoryListColumn.CATEGORY_ID)
              .from(Table.CATEGORY_LIST)
              .where(CategoryListColumn.CATEGORY_ID, "=", categoryId, true)
              .and()
              .where(CategoryListColumn.MY_CONTACTS_ID, "=", contactId);

            return !executor.executeQuery(qb, CategoryDetails.class).isEmpty();
        } catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to check contact in category: " + e.getMessage(), e);
        }
    }

    public static boolean deleteCategory(int categoryId) throws DaoException {
        QueryExecutor executor = new QueryExecutor();
        QueryBuilder qb1 = new QueryBuilder();
        QueryBuilder qb2 = new QueryBuilder();
        try {
            qb1.delete(Table.CATEGORY_LIST)
               .where(CategoryListColumn.CATEGORY_ID, "=", categoryId, true);

            qb2.delete(Table.CATEGORY_DETAILS)
               .where(CategoryDetailsColumn.CATEGORY_ID, "=", categoryId, true);

            executor.transactionStart();
            executor.executeUpdate(qb1);
            int rowCount = executor.executeUpdate(qb2);
            executor.transactionEnd();

            return rowCount > 0;
        } catch (QueryExecutorException e) {
            try {
                executor.transactionEnd();
            } catch (QueryExecutorException ignored) {}
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to delete category: " + e.getMessage(), e);
        }
    }

    public static List<Contact> getContactsByCategoryId(int categoryId) throws DaoException {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
        try {
            qb.select(MyContactsDataColumn.MY_CONTACTS_ID, MyContactsDataColumn.ALIAS_FND_NAME,
                      MyContactsDataColumn.FRIEND_EMAIL, MyContactsDataColumn.PHONE,
                      MyContactsDataColumn.ADDRESS, MyContactsDataColumn.IS_ARCHIVED, 
                      MyContactsDataColumn.IS_FAVORITE, MyContactsDataColumn.CREATED_AT, 
                      MyContactsDataColumn.MODIFIED_AT)
              .from(Table.MY_CONTACTS_DATA)
              .join(Table.CATEGORY_LIST, CategoryListColumn.MY_CONTACTS_ID, 
                    MyContactsDataColumn.MY_CONTACTS_ID)
              .where(CategoryListColumn.CATEGORY_ID, "=", categoryId, true);

            return executor.executeQuery(qb, Contact.class);
        } catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to fetch contacts by category: " + e.getMessage(), e);
        }
    }

//	public static CategoryDetails getCategoriesByCategoryId(int categoryId, int userId) throws DaoException {
//		QueryBuilder qb = new QueryBuilder();
//		QueryExecutor executor = new QueryExecutor();
//		
//		try {
//			qb.select(CategoryDetailsColumn.CATEGORY_ID, CategoryDetailsColumn.CATEGORY_NAME, 
//			        CategoryDetailsColumn.CREATED_AT, CategoryDetailsColumn.MODIFIED_AT)
//			.from(Table.CATEGORY_DETAILS)
//			.where(CategoryDetailsColumn.USER_ID, "=", userId, true).and()
//			.where(CategoryDetailsColumn.CATEGORY_ID, "=", categoryId);
//			List<CategoryDetails> results = executor.executeQuery(qb, CategoryDetails.class);
//			if (results.isEmpty()) {
//				return null;
//			}
//			return results.get(0);
//		} catch (QueryExecutorException e) {
//            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to retrieve category: " + e.getMessage(), e);
//		}
//	}  select * from CategoryDetails a join CategoryList b on a.categoryId = b.categoryId join MyContactsData c on b.MyContactsID = c.MyContactsID where a.categoryId=36;

	public static CategoryDetails getCategoriesByCategoryId(int categoryId, int userId) throws DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();
		
		try {
			qb.select(CategoryDetailsColumn.CATEGORY_ID, CategoryDetailsColumn.CATEGORY_NAME, 
			        CategoryDetailsColumn.CREATED_AT, CategoryDetailsColumn.MODIFIED_AT,
			        MyContactsDataColumn.MY_CONTACTS_ID, MyContactsDataColumn.ALIAS_FND_NAME,
					MyContactsDataColumn.FRIEND_EMAIL, MyContactsDataColumn.PHONE, MyContactsDataColumn.ADDRESS,
					MyContactsDataColumn.IS_ARCHIVED, MyContactsDataColumn.IS_FAVORITE, MyContactsDataColumn.CREATED_AT,
					MyContactsDataColumn.MODIFIED_AT, MyContactsDataColumn.RESOURCE_NAME)
			.from(Table.CATEGORY_DETAILS)
			.join(Table.CATEGORY_LIST, CategoryListColumn.CATEGORY_ID, CategoryDetailsColumn.CATEGORY_ID)
			.join(Table.MY_CONTACTS_DATA, MyContactsDataColumn.MY_CONTACTS_ID, CategoryListColumn.MY_CONTACTS_ID)
			.where(CategoryDetailsColumn.USER_ID, "=", userId, true).and()
			.where(CategoryDetailsColumn.CATEGORY_ID, "=", categoryId);
			List<CategoryDetails> results = executor.executeQuery(qb, CategoryDetails.class);
			if (results.isEmpty()) {
				return null;
			}
			return results.get(0);
		} catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to retrieve category: " + e.getMessage(), e);
		}
	}
}
