package com.queryLayer.databaseSchemaEnums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum AuditLogColumn implements Column{
		AUDIT_ID("audit_id"),
		TABLE_NAME("table_name"),
		ACTION_TYPE("action_type"),
		RECORD_ID("record_id"),
		OLD_DATA("old_data"),
		NEW_DATA("new_data"),
		TIMESTAMP("timestamp");
	
    private final String columnName;

	AuditLogColumn(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public Table getTable() {
        return Table.AUDIT_LOG;
    }
    
    public Column[] getAllColumns(){
    	return AuditLogColumn.values();
    }

    @Override
    public String toString() {
        return getTable().getTableName() + "." + columnName;
    }
}
