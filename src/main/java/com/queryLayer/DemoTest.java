package com.queryLayer;

import com.queryLayer.databaseSchemaEnums.MyContactsDataColumn;
import com.queryLayer.databaseSchemaEnums.Table;

public class DemoTest {
	public static void main(String args[]) {
		QueryBuilder qb = new QueryBuilder();
		qb.insert(Table.MY_CONTACTS_DATA)
	    .set(MyContactsDataColumn.USER_ID, 1)
	    .set(MyContactsDataColumn.FRIEND_EMAIL, "gmaail@fm.omc")
	    .set(MyContactsDataColumn.ALIAS_FND_NAME, "name")
	    .set(MyContactsDataColumn.PHONE, 1234567890)
	    .set(MyContactsDataColumn.ADDRESS, "address for me")
	    .set(MyContactsDataColumn.IS_ARCHIVED, 0)
	    .set(MyContactsDataColumn.IS_FAVORITE, 1)
	    .set(MyContactsDataColumn.CREATED_AT, System.currentTimeMillis())
	    .set(MyContactsDataColumn.MODIFIED_AT, System.currentTimeMillis());
		System.out.println(qb.build());
	}
}
