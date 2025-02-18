package com.queryLayer.databaseSchemaEnums;

public enum ContactsEmailColumn implements Column {
    CONTACT_EMAIL_ID("contact_email_id"),
    MY_CONTACTS_ID("MyContactsID"),
    EMAIL("email"),
    LABEL("label"),
    CREATED_AT("created_at"),
    UPDATED_AT("updated_at");

    private final String columnName;

    ContactsEmailColumn(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public Table getTable() {
        return Table.CONTACTS_EMAIL;
    }

    public Column[] getAllColumns() {
        return ContactsEmailColumn.values();
    }

    @Override
    public String toString() {
        return getTable().getTableName() + "." + columnName;
    }
}
