package com.queryLayer.DatabaseSchemaEnums;

/**
 * Interface for database columns providing consistent access to column properties
 */
public interface Column {
    Table getTable();
    Column[] getAllColumns();
}
