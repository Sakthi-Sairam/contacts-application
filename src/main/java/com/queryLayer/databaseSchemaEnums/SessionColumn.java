package com.queryLayer.databaseSchemaEnums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public Column[] getAllColumns(){
    	return SessionColumn.values();
    }

    @Override
    public String toString() {
        return getTable().getTableName() + "." + columnName;
    }
}