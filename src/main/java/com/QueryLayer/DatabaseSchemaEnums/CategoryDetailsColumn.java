package com.QueryLayer.DatabaseSchemaEnums;

/**
 * Enum for CategoryDetail columns
 */
public enum CategoryDetailsColumn implements Column {
    CATEGORY_ID("categoryId"),
    CATEGORY_NAME("categoryName"),
    USER_ID("user_id");

    private final String columnName;

    CategoryDetailsColumn(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public Table getTable() {
        return Table.CATEGORY_DETAILS;
    }

    @Override
    public String toString() {
        return getTable().getTableName() + "." + columnName;
    }
}

