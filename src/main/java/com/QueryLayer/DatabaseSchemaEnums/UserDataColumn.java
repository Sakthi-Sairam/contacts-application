package com.QueryLayer.DatabaseSchemaEnums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Enum for UserData columns
 */
public enum UserDataColumn implements Column {
    USER_ID("user_id"),
    PASSWORD("password"),
    FIRST_NAME("first_name"),
    LAST_NAME("last_name"),
    AGE("age"),
    ADDRESS("address"),
    PHONE("phone"),
	CREATED_AT("createdAt"),
	MODIFIED_AT("modifiedAt");

    private final String columnName;

    UserDataColumn(String columnName) {
        this.columnName = columnName;
    }
    @Override
    public Column[] getAllColumns(){
    	return UserDataColumn.values();
    }
    @Override
    public Table getTable() {
        return Table.USER_DATA;
    }
    
    @Override
    public String toString() {
        return getTable().getTableName() + "." + columnName;
    }
}
