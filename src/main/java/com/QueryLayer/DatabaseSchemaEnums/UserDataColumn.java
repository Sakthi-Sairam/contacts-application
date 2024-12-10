package com.QueryLayer.DatabaseSchemaEnums;

/**
 * Enum for userdata columns
 */
enum UserDataColumn implements Column {
    USER_ID("user_id"),
    PASSWORD("password"),
    FIRST_NAME("first_name"),
    LAST_NAME("last_name"),
    AGE("age"),
    ADDRESS("address"),
    PHONE("phone");

    private final String columnName;

    UserDataColumn(String columnName) {
        this.columnName = columnName;
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