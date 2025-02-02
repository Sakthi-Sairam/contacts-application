package com.queryLayer.DatabaseSchemaEnums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Enum for CategoryList columns
 */
public enum CategoryListColumn implements Column {
    ID("id"),
    CATEGORY_ID("categoryId"),
    MY_CONTACTS_ID("MyContactsID"),
	CREATED_AT("createdAt"),
	MODIFIED_AT("modifiedAt");

    private final String columnName;

    CategoryListColumn(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public Table getTable() {
        return Table.CATEGORY_LIST;
    }
    public Column[] getAllColumns(){
    	return CategoryListColumn.values();
    }

    @Override
    public String toString() {
        return getTable().getTableName() + "." + columnName;
    }
}
