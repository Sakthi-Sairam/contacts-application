package com.dao;

import java.util.ArrayList;
import java.util.List;

import com.exceptions.DaoException;
import com.exceptions.ErrorCode;
import com.exceptions.QueryExecutorException;
import com.models.Contact;
import com.queryLayer.Pair;
import com.queryLayer.QueryBuilder;
import com.queryLayer.QueryExecutor;
import com.queryLayer.databaseSchemaEnums.CategoryListColumn;
import com.queryLayer.databaseSchemaEnums.MyContactsDataColumn;
import com.queryLayer.databaseSchemaEnums.Table;

public class ContactDao {

	/**
	 * Adds a new contact to the database.
	 */
	public static boolean addContact(String friendEmail, String aliasName, String phone, String address, int userId,
			int isArchived, int isFavorite) throws DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();
		long currentTime = System.currentTimeMillis();
		try {
			qb.insert(Table.MY_CONTACTS_DATA).set(MyContactsDataColumn.USER_ID, userId)
					.set(MyContactsDataColumn.FRIEND_EMAIL, friendEmail)
					.set(MyContactsDataColumn.ALIAS_FND_NAME, aliasName).set(MyContactsDataColumn.PHONE, phone)
					.set(MyContactsDataColumn.ADDRESS, address).set(MyContactsDataColumn.IS_ARCHIVED, isArchived)
					.set(MyContactsDataColumn.IS_FAVORITE, isFavorite).set(MyContactsDataColumn.CREATED_AT, currentTime)
					.set(MyContactsDataColumn.MODIFIED_AT, currentTime);

			Pair result = executor.executeUpdateWithGeneratedKeys(qb);
			int rowCount = result.getRowCount();
			if (rowCount == 0) {
				throw new DaoException(ErrorCode.DATA_NOT_FOUND, "No contact was added to the database.");
			}
			return rowCount > 0;
		} catch (QueryExecutorException e) {
			throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to add contact: " + e.getMessage(), e);
		}
	}

	/**
	 * Retrieves all contacts for a specific user.
	 */
	public static List<Contact> getContactsByUserId(int userId) throws DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();
		List<Contact> contacts = new ArrayList<>();
		try {
			qb.select(Table.MY_CONTACTS_DATA).from(Table.MY_CONTACTS_DATA)
					.where(MyContactsDataColumn.USER_ID, "=", userId)
					.orderBy(MyContactsDataColumn.ALIAS_FND_NAME, true);

			contacts = executor.executeQuery(qb, Contact.class);
			return contacts;
		} catch (QueryExecutorException e) {
			throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to retrieve contacts: " + e.getMessage(),
					e);
		}
	}
	
	/**
	 * Retrieves all contacts for a specific user page wise
	 */
	public static List<Contact> getContactsByUserId(int userId, int limit, int offset) throws DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();
		List<Contact> contacts = new ArrayList<>();
		try {
			qb.select(Table.MY_CONTACTS_DATA).from(Table.MY_CONTACTS_DATA)
					.where(MyContactsDataColumn.USER_ID, "=", userId)
					.orderBy(MyContactsDataColumn.ALIAS_FND_NAME, true)
					.limit(limit)
					.offset(offset);

			contacts = executor.executeQuery(qb, Contact.class);
			return contacts;
		} catch (QueryExecutorException e) {
			throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to retrieve contacts: " + e.getMessage(),
					e);
		}
	}

	/**
	 * Retrieves favorite contacts for a user.
	 */
	public static List<Contact> getFavoriteContactsByUserId(int userId) throws DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();
		try {
			qb.select(MyContactsDataColumn.MY_CONTACTS_ID, MyContactsDataColumn.ALIAS_FND_NAME,
					MyContactsDataColumn.FRIEND_EMAIL, MyContactsDataColumn.PHONE, MyContactsDataColumn.ADDRESS,
					MyContactsDataColumn.IS_ARCHIVED, MyContactsDataColumn.IS_FAVORITE, MyContactsDataColumn.CREATED_AT,
					MyContactsDataColumn.MODIFIED_AT).from(Table.MY_CONTACTS_DATA)
					.where(MyContactsDataColumn.USER_ID, "=", userId, true).and()
					.where(MyContactsDataColumn.IS_FAVORITE, "=", 1).orderBy(MyContactsDataColumn.ALIAS_FND_NAME, true);

			return executor.executeQuery(qb, Contact.class);
		} catch (QueryExecutorException e) {
			throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED,
					"Failed to retrieve favorite contacts: " + e.getMessage(), e);
		}
	}

	/**
	 * Deletes a contact by its ID.
	 */
	public static boolean deleteContact(int contactId) throws DaoException {
		QueryBuilder deleteFromCategoryList = new QueryBuilder();
		QueryBuilder deleteFromContacts = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();

		try {
			deleteFromCategoryList.delete(Table.CATEGORY_LIST).where(CategoryListColumn.MY_CONTACTS_ID, "=", contactId,
					true);

			deleteFromContacts.delete(Table.MY_CONTACTS_DATA).where(MyContactsDataColumn.MY_CONTACTS_ID, "=", contactId,
					true);

			executor.transactionStart();
			executor.executeUpdate(deleteFromCategoryList);
			int affectedRows = executor.executeUpdate(deleteFromContacts);
			executor.transactionEnd();

			return affectedRows > 0;
		} catch (Exception e) {
			try {
				executor.transactionEnd();
			} catch (QueryExecutorException ignored) {
			}
			throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to delete contact: " + e.getMessage(), e);
		}
	}

	/**
	 * Updates an existing contact.
	 */
	public static boolean updateContact(Contact contact) throws DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();
		long currentTime = System.currentTimeMillis();

		try {
			qb.update(Table.MY_CONTACTS_DATA).set(MyContactsDataColumn.FRIEND_EMAIL, contact.getFriend_email())
					.set(MyContactsDataColumn.ALIAS_FND_NAME, contact.getAlias_name())
					.set(MyContactsDataColumn.PHONE, contact.getPhone())
					.set(MyContactsDataColumn.ADDRESS, contact.getAddress())
					.set(MyContactsDataColumn.IS_ARCHIVED, contact.getIsArchived())
					.set(MyContactsDataColumn.IS_FAVORITE, contact.getIsFavorite())
					.set(MyContactsDataColumn.MODIFIED_AT, currentTime)
					.where(MyContactsDataColumn.MY_CONTACTS_ID, "=", contact.getMyContactsID(), true);

			return executor.executeUpdate(qb) > 0;
		} catch (QueryExecutorException e) {
			throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to update contact: " + e.getMessage(), e);
		}
	}

	/**
	 * Retrieves a contact by its ID and user ID.
	 */
	public static Contact getContactByContactId(int contactId, int userId) throws DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();

		try {
			qb.select(Table.MY_CONTACTS_DATA).from(Table.MY_CONTACTS_DATA)
					.where(MyContactsDataColumn.USER_ID, "=", userId).and()
					.where(MyContactsDataColumn.MY_CONTACTS_ID, "=", contactId);

			List<Contact> results = executor.executeQuery(qb, Contact.class);
			if (results.isEmpty()) {
				return null;
			}
			return results.get(0);
		} catch (QueryExecutorException e) {
			throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to retrieve contact: " + e.getMessage(),
					e);
		}
	}

	// OAUTH ADDING OF CONTACT
	public static boolean addContact(String friendEmail, String aliasName, String phone, String address, int userId,
			String resourceName, int isArchived, int isFavorite) throws DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();
		long currentTime = System.currentTimeMillis();
		try {
			qb.insert(Table.MY_CONTACTS_DATA).set(MyContactsDataColumn.USER_ID, userId)
					.set(MyContactsDataColumn.FRIEND_EMAIL, friendEmail)
					.set(MyContactsDataColumn.ALIAS_FND_NAME, aliasName).set(MyContactsDataColumn.PHONE, phone)
					.set(MyContactsDataColumn.ADDRESS, address).set(MyContactsDataColumn.IS_ARCHIVED, isArchived)
					.set(MyContactsDataColumn.IS_FAVORITE, isFavorite).set(MyContactsDataColumn.CREATED_AT, currentTime)
					.set(MyContactsDataColumn.MODIFIED_AT, currentTime)
					.set(MyContactsDataColumn.RESOURCE_NAME, resourceName);

			Pair result = executor.executeUpdateWithGeneratedKeys(qb);
			int rowCount = result.getRowCount();
			if (rowCount == 0) {
				throw new DaoException(ErrorCode.INVALID_INPUT, "No contact was added to the database.");
			}
			return rowCount > 0;
		} catch (QueryExecutorException e) {
			throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to add contact: " + e.getMessage(), e);
		}
	}
}
