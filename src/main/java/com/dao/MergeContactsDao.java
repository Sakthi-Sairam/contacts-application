package com.dao;

import java.util.List;

import com.exceptions.DaoException;
import com.exceptions.ErrorCode;
import com.exceptions.QueryExecutorException;
import com.queryLayer.QueryBuilder;
import com.queryLayer.QueryExecutor;
import com.queryLayer.databaseSchemaEnums.ContactsEmailColumn;
import com.queryLayer.databaseSchemaEnums.ContactsPhoneNumberColumn;
import com.queryLayer.databaseSchemaEnums.MyContactsDataColumn;
import com.queryLayer.databaseSchemaEnums.Table;

public class MergeContactsDao {

	public static void updateContactPhoneNumbersAndEmails(int primaryContactId, List<Integer> secondaryContactIds) throws DaoException {
		QueryExecutor executor = new QueryExecutor();
		long currentTime = System.currentTimeMillis();
		QueryBuilder phoneNumberQuery = new QueryBuilder();
		QueryBuilder emailQuery = new QueryBuilder();

		try {
			phoneNumberQuery.update(Table.CONTACTS_PHONE_NUMBER)
				.set(ContactsPhoneNumberColumn.MY_CONTACTS_ID, primaryContactId)
				.set(ContactsPhoneNumberColumn.UPDATED_AT, currentTime);
			
			emailQuery.update(Table.CONTACTS_EMAIL)
				.set(ContactsEmailColumn.MY_CONTACTS_ID, primaryContactId)
				.set(ContactsEmailColumn.UPDATED_AT, currentTime);

			
			int size = secondaryContactIds.size();
			for(int i = 0; i < size; i++) {
				
				int secondaryContactId = secondaryContactIds.get(i);
				
				phoneNumberQuery.where(ContactsPhoneNumberColumn.MY_CONTACTS_ID, "=", secondaryContactId);
				emailQuery.where(ContactsEmailColumn.MY_CONTACTS_ID, "=", secondaryContactId);
				
				if(i != size-1) {
					phoneNumberQuery.or();
					emailQuery.or();
				}
			}

			executor.executeUpdate(emailQuery);
			executor.executeUpdate(phoneNumberQuery);

		} catch (QueryExecutorException e) {
			throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to update contact: " + e.getMessage(), e);
		}
	}

	public static void deleteDuplicateContactData(List<Integer> secondaryContactIds) throws DaoException {
		QueryBuilder deleteFromContactsQuery = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();
		try {
			deleteFromContactsQuery.delete(Table.MY_CONTACTS_DATA);
			
			int size = secondaryContactIds.size();
			for(int i = 0; i < size; i++) {
				
				int secondaryContactId = secondaryContactIds.get(i);
				
				deleteFromContactsQuery.where(MyContactsDataColumn.MY_CONTACTS_ID, "=", secondaryContactId);
				
				if(i != size-1) {
					deleteFromContactsQuery.or();
				}
			}
			executor.executeUpdate(deleteFromContactsQuery);
		} catch (Exception e) {
			throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to update contact: " + e.getMessage(), e);
		}
		
	}
	

}
