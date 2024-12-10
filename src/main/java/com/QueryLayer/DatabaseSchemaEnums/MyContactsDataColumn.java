package com.QueryLayer.DatabaseSchemaEnums;

/**
 * Enum for MyContactsData columns
 */
public enum MyContactsDataColumn implements Column {
    MY_CONTACTS_ID("MyContactsID"),
    USER_ID("user_id"),
    FRIEND_EMAIL("friend_email"),
    ALIAS_FND_NAME("alias_fnd_name"),
    PHONE("phone"),
    ADDRESS("address"),
    IS_ARCHIVED("isArchived"),
    IS_FAVORITE("isFavorite"),
    CREATED_AT("created_at");

    private final String columnName;

    MyContactsDataColumn(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public Table getTable() {
        return Table.MY_CONTACTS_DATA;
    }

    @Override
    public String toString() {
        return getTable().getTableName() + "." + columnName;
    }
}
