package com.queryLayer.databaseSchemaEnums;

public enum ContactsPhoneNumberColumn implements Column {
    CONTACT_NUMBER_ID("contact_number_id"),
    MY_CONTACTS_ID("MyContactsID"),
    PHONE_NUMBER("phone_number"),
    LABEL("label"),
    CREATED_AT("created_at"),
    UPDATED_AT("updated_at");

    private final String columnName;

    ContactsPhoneNumberColumn(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public Table getTable() {
        return Table.CONTACTS_PHONE_NUMBER;
    }

    public Column[] getAllColumns() {
        return ContactsPhoneNumberColumn.values();
    }

    @Override
    public String toString() {
        return getTable().getTableName() + "." + columnName;
    }
}
