package com.QueryLayer.DatabaseSchemaEnums;


/**
 * Enum for MailMapper column
 */
public enum MailMapperColumn implements Column {
    ID("id"),
    USER_ID("user_id"),
    EMAIL("email");

    private final String columnName;

    MailMapperColumn(String columnName) {
        this.columnName = columnName;
    }
    @Override
    public String toString() {
        return getTable().getTableName() + "." + columnName;
    }

    @Override
    public Table getTable() {
        return Table.MAIL_MAPPER;
    }
}