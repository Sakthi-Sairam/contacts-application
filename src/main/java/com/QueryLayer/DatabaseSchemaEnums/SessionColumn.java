package com.QueryLayer.DatabaseSchemaEnums;

/**
 * Enum for sessions columns
 */
public enum SessionColumn implements Column {
    SESSION_ID("sessionId"),
    USER_ID("user_id"),
    LAST_ACCESSED_TIME("lastAccessedTime"),
    CREATED_AT("createdAt");

    private final String columnName;

    SessionColumn(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public Table getTable() {
        return Table.SESSIONS;
    }

    @Override
    public String toString() {
        return getTable().getTableName() + "." + columnName;
    }
}