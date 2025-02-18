package com.dao;

import java.util.List;

import com.exceptions.DaoException;
import com.exceptions.ErrorCode;
import com.exceptions.QueryExecutorException;
import com.models.CategoryDetails;
import com.models.Contact;
import com.models.ContactEmail;
import com.models.ContactPhoneNumber;
import com.queryLayer.QueryBuilder;
import com.queryLayer.QueryExecutor;
import com.queryLayer.databaseSchemaEnums.CategoryDetailsColumn;
import com.queryLayer.databaseSchemaEnums.ContactsEmailColumn;
import com.queryLayer.databaseSchemaEnums.ContactsPhoneNumberColumn;
import com.queryLayer.databaseSchemaEnums.MyContactsDataColumn;
import com.queryLayer.databaseSchemaEnums.Table;
import com.queryLayer.databaseSchemaEnums.UserDataColumn;

public class AuthorizationDao {
	public static boolean isUserAuthorizedToDeleteContactNumber(int userId, int phoneNumberId) throws DaoException {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
        try {
			qb.select(Table.CONTACTS_PHONE_NUMBER).from(Table.CONTACTS_PHONE_NUMBER)
			.join(Table.MY_CONTACTS_DATA, MyContactsDataColumn.MY_CONTACTS_ID, ContactsPhoneNumberColumn.MY_CONTACTS_ID)
			.join(Table.USER_DATA, UserDataColumn.USER_ID, MyContactsDataColumn.USER_ID)
			.where(UserDataColumn.USER_ID,"=",userId).and()
			.where(ContactsPhoneNumberColumn.CONTACT_NUMBER_ID, "=", phoneNumberId);
			
			List<ContactPhoneNumber> results = executor.executeQuery(qb, ContactPhoneNumber.class);
			return !results.isEmpty();
		} catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to check authorization for contacts phone " + e.getMessage(), e);
		}

	}
	
	public static boolean isUserAuthorizedToDeleteContactEmail(int userId, int contactEmailId) throws DaoException {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
        try {
			qb.select(Table.CONTACTS_EMAIL).from(Table.CONTACTS_EMAIL)
			.join(Table.MY_CONTACTS_DATA, MyContactsDataColumn.MY_CONTACTS_ID, ContactsEmailColumn.MY_CONTACTS_ID)
			.join(Table.USER_DATA, UserDataColumn.USER_ID, MyContactsDataColumn.USER_ID)
			.where(UserDataColumn.USER_ID,"=",userId).and()
			.where(ContactsEmailColumn.CONTACT_EMAIL_ID, "=", contactEmailId);
			
			List<ContactEmail> results = executor.executeQuery(qb, ContactEmail.class);
			return !results.isEmpty();
		} catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to check authorization for contacts email " + e.getMessage(), e);
		}

	}
	
	public static boolean isUserAuthorizedForCrudOnContacts(int userId, int contactId) throws DaoException {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
        try {
			qb.select(Table.MY_CONTACTS_DATA).from(Table.MY_CONTACTS_DATA)
			.join(Table.USER_DATA, UserDataColumn.USER_ID, MyContactsDataColumn.USER_ID)
			.where(UserDataColumn.USER_ID,"=",userId).and()
			.where(MyContactsDataColumn.MY_CONTACTS_ID,"=",contactId);
			
			List<Contact> results = executor.executeQuery(qb, Contact.class);
			return !results.isEmpty();
		} catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to check authorization for contacts " + e.getMessage(), e);
		}

	}
	
	public static boolean isUserAuthorizedForCategory(int userId, int categoryId) throws DaoException {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
        try {
			qb.select(Table.CATEGORY_DETAILS).from(Table.CATEGORY_DETAILS)
			.where(CategoryDetailsColumn.USER_ID,"=",userId).and()
			.where(CategoryDetailsColumn.CATEGORY_ID,"=",categoryId);
			
			List<CategoryDetails> results = executor.executeQuery(qb, CategoryDetails.class);
			return !results.isEmpty();
		} catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to check authorization for categories " + e.getMessage(), e);
		}

	}

}
