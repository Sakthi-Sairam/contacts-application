package com.QueryLayer.DatabaseSchemaEnums;

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
    public Table getTable() {
        return Table.USER_DATA;  // Ensure that USER_DATA is defined in Table enum
    }

    @Override
    public String toString() {
        return getTable().getTableName() + "." + columnName;
    }
}
