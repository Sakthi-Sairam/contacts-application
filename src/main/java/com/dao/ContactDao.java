package com.dao;

import java.util.ArrayList;
import java.util.List;

import com.exceptions.DaoException;
import com.exceptions.ErrorCode;
import com.exceptions.QueryExecutorException;
import com.models.Contact;
import com.models.ContactEmail;
import com.queryLayer.Pair;
import com.queryLayer.QueryBuilder;
import com.queryLayer.QueryExecutor;
import com.queryLayer.databaseSchemaEnums.CategoryListColumn;
import com.queryLayer.databaseSchemaEnums.ContactsEmailColumn;
import com.queryLayer.databaseSchemaEnums.ContactsPhoneNumberColumn;
import com.queryLayer.databaseSchemaEnums.MyContactsDataColumn;
import com.queryLayer.databaseSchemaEnums.Table;

public class ContactDao {

	/**
	 * Adds a new contact to the database.
	 */
	public static boolean addContact(String email, String aliasName, String phone, String address, int userId,
			String resourceName, int isArchived, int isFavorite) throws DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();
		long currentTime = System.currentTimeMillis();
		try {
			executor.transactionStart();
			qb.insert(Table.MY_CONTACTS_DATA).set(MyContactsDataColumn.USER_ID, userId)
					.set(MyContactsDataColumn.ALIAS_FND_NAME, aliasName).set(MyContactsDataColumn.ADDRESS, address)
					.set(MyContactsDataColumn.IS_ARCHIVED, isArchived).set(MyContactsDataColumn.IS_FAVORITE, isFavorite)
					.set(MyContactsDataColumn.RESOURCE_NAME, resourceName)
					.set(MyContactsDataColumn.CREATED_AT, currentTime)
					.set(MyContactsDataColumn.MODIFIED_AT, currentTime);

			Pair result = executor.executeUpdateWithGeneratedKeys(qb);
			int contactId = result.getGeneratedKey();

			qb = new QueryBuilder();
			qb.insert(Table.CONTACTS_PHONE_NUMBER).set(ContactsPhoneNumberColumn.MY_CONTACTS_ID, contactId)
					.set(ContactsPhoneNumberColumn.PHONE_NUMBER, phone)
					.set(ContactsPhoneNumberColumn.CREATED_AT, currentTime)
					.set(ContactsPhoneNumberColumn.UPDATED_AT, currentTime);

			executor.executeUpdate(qb);

			qb = new QueryBuilder();
			qb.insert(Table.CONTACTS_EMAIL).set(ContactsEmailColumn.MY_CONTACTS_ID, contactId)
					.set(ContactsEmailColumn.EMAIL, email).set(ContactsEmailColumn.CREATED_AT, currentTime)
					.set(ContactsEmailColumn.UPDATED_AT, currentTime);

			executor.executeUpdate(qb);
			executor.transactionEnd();

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
			qb.select(Table.MY_CONTACTS_DATA, Table.CONTACTS_EMAIL, Table.CONTACTS_PHONE_NUMBER)
					.from(Table.MY_CONTACTS_DATA)
					.join(Table.CONTACTS_PHONE_NUMBER, MyContactsDataColumn.MY_CONTACTS_ID,
							ContactsPhoneNumberColumn.MY_CONTACTS_ID)
					.join(Table.CONTACTS_EMAIL, MyContactsDataColumn.MY_CONTACTS_ID, ContactsEmailColumn.MY_CONTACTS_ID)
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

			qb.select(Table.MY_CONTACTS_DATA)
			.from(Table.MY_CONTACTS_DATA)
			.where(MyContactsDataColumn.USER_ID, "=", userId).orderBy(MyContactsDataColumn.ALIAS_FND_NAME, true)
			.limit(limit).offset(offset);

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
			qb.select(Table.MY_CONTACTS_DATA, Table.CONTACTS_EMAIL, Table.CONTACTS_PHONE_NUMBER)
					.from(Table.MY_CONTACTS_DATA)
					.join(Table.CONTACTS_PHONE_NUMBER, MyContactsDataColumn.MY_CONTACTS_ID,
							ContactsPhoneNumberColumn.MY_CONTACTS_ID)
					.join(Table.CONTACTS_EMAIL, MyContactsDataColumn.MY_CONTACTS_ID, ContactsEmailColumn.MY_CONTACTS_ID)
					.where(MyContactsDataColumn.USER_ID, "=", userId).and()
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
		QueryBuilder deleteFromContactsPhoneNumber = new QueryBuilder();
		QueryBuilder deleteFromContactsEmail = new QueryBuilder();
		QueryBuilder deleteFromContacts = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();

		try {
			deleteFromCategoryList.delete(Table.CATEGORY_LIST).where(CategoryListColumn.MY_CONTACTS_ID, "=", contactId);
			deleteFromContactsPhoneNumber.delete(Table.CONTACTS_PHONE_NUMBER)
					.where(ContactsPhoneNumberColumn.MY_CONTACTS_ID, "=", contactId);
			deleteFromContactsEmail.delete(Table.CONTACTS_EMAIL).where(ContactsEmailColumn.MY_CONTACTS_ID, "=",
					contactId);
			deleteFromContacts.delete(Table.MY_CONTACTS_DATA).where(MyContactsDataColumn.MY_CONTACTS_ID, "=",
					contactId);

			executor.transactionStart();
			executor.executeUpdate(deleteFromCategoryList);
			executor.executeUpdate(deleteFromContactsPhoneNumber);
			executor.executeUpdate(deleteFromContactsEmail);
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
	public static boolean updateContact(int contactId, String aliasName, String address, int isArchived, int isFavorite)
			throws DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();
		long currentTime = System.currentTimeMillis();

		try {
			qb.update(Table.MY_CONTACTS_DATA).set(MyContactsDataColumn.ALIAS_FND_NAME, aliasName)
					.set(MyContactsDataColumn.ADDRESS, address).set(MyContactsDataColumn.IS_ARCHIVED, isArchived)
					.set(MyContactsDataColumn.IS_FAVORITE, isFavorite)
					.set(MyContactsDataColumn.MODIFIED_AT, currentTime)
					.where(MyContactsDataColumn.MY_CONTACTS_ID, "=", contactId);

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
			qb.select(Table.MY_CONTACTS_DATA, Table.CONTACTS_PHONE_NUMBER)
					.from(Table.MY_CONTACTS_DATA)
					.join(Table.CONTACTS_PHONE_NUMBER, MyContactsDataColumn.MY_CONTACTS_ID,
							ContactsPhoneNumberColumn.MY_CONTACTS_ID)
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
	
	public static List<ContactEmail> getContactEmailsByContactId(int contactId, int userId) throws DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();

		try {
			qb.select(Table.CONTACTS_EMAIL)
					.from(Table.CONTACTS_EMAIL)
					.join(Table.MY_CONTACTS_DATA, MyContactsDataColumn.MY_CONTACTS_ID, ContactsEmailColumn.MY_CONTACTS_ID)
					.where(MyContactsDataColumn.USER_ID, "=", userId).and()
					.where(MyContactsDataColumn.MY_CONTACTS_ID, "=", contactId);

			List<ContactEmail> results = executor.executeQuery(qb, ContactEmail.class);
			if (results.isEmpty()) {
				return null;
			}
			return results;
		} catch (QueryExecutorException e) {
			throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to retrieve contact: " + e.getMessage(),
					e);
		}
	}

	public static boolean addContactPhoneNumber(int contactId, String label, String phone) throws DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();
		long currentTime = System.currentTimeMillis();
		int rowCount=0;
		try {
			qb.insert(Table.CONTACTS_PHONE_NUMBER)
					.set(ContactsPhoneNumberColumn.MY_CONTACTS_ID, contactId)
					.set(ContactsPhoneNumberColumn.PHONE_NUMBER, phone)
					.set(ContactsPhoneNumberColumn.LABEL, label)
					.set(ContactsPhoneNumberColumn.CREATED_AT, currentTime)
					.set(ContactsPhoneNumberColumn.UPDATED_AT, currentTime);
			rowCount = executor.executeUpdate(qb);
		} catch (QueryExecutorException e) {
			throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to add Contact PhoneNumber" + e.getMessage(),e);
		}
		return rowCount > 0;
		
	}
	public static boolean addContactEmail(int contactId, String label, String email) throws DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();
		long currentTime = System.currentTimeMillis();
		int rowCount=0;
		try {
			qb.insert(Table.CONTACTS_EMAIL)
			.set(ContactsEmailColumn.MY_CONTACTS_ID, contactId)
			.set(ContactsEmailColumn.EMAIL, email)
			.set(ContactsEmailColumn.LABEL, label)
			.set(ContactsEmailColumn.CREATED_AT, currentTime)
			.set(ContactsEmailColumn.UPDATED_AT, currentTime);
			rowCount = executor.executeUpdate(qb);
		} catch (QueryExecutorException e) {
			throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to add Contact PhoneNumber" + e.getMessage(),e);
		}
		return rowCount > 0;
		
	}

	public static boolean deleteContactEmail(int contactEmailId) throws DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();
		int rowCount = 0;
		
		try {
			qb.delete(Table.CONTACTS_EMAIL).where(ContactsEmailColumn.CONTACT_EMAIL_ID, "=", contactEmailId);
			rowCount = executor.executeUpdate(qb);
		} catch (QueryExecutorException e) {
			throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to add Contact PhoneNumber" + e.getMessage(),e);
		}
		return rowCount > 0;
	}

	public static boolean deleteContactPhone(int contactPhoneNumberId) throws DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();
		int rowCount = 0;
		
		try {
			qb.delete(Table.CONTACTS_PHONE_NUMBER).where(ContactsPhoneNumberColumn.CONTACT_NUMBER_ID, "=", contactPhoneNumberId);
			rowCount = executor.executeUpdate(qb);
		} catch (QueryExecutorException e) {
			throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to add Contact PhoneNumber" + e.getMessage(),e);
		}
		return rowCount > 0;
	}

}
