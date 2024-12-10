package com.QueryLayer.DatabaseSchemaEnums;

/**
 * Enum for CategoryList columns
 */
public enum CategoryListColumn implements Column {
    ID("id"),
    CATEGORY_ID("categoryId"),
    MY_CONTACTS_ID("MyContactsID");

    private final String columnName;

    CategoryListColumn(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public Table getTable() {
        return Table.CATEGORY_LIST;
    }

    @Override
    public String toString() {
        return getTable().getTableName() + "." + columnName;
    }
}
