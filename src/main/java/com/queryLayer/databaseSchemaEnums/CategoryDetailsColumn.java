package com.queryLayer.databaseSchemaEnums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Enum for CategoryDetail columns
 */
public enum CategoryDetailsColumn implements Column {
    CATEGORY_ID("categoryId"),
    CATEGORY_NAME("categoryName"),
    USER_ID("user_id"),
	CREATED_AT("createdAt"),
	MODIFIED_AT("modifiedAt");

    private final String columnName;

    CategoryDetailsColumn(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public Table getTable() {
        return Table.CATEGORY_DETAILS;
    }
    public Column[] getAllColumns(){
    	return CategoryDetailsColumn.values();
    }

    @Override
    public String toString() {
        return getTable().getTableName() + "." + columnName;
    }
}

