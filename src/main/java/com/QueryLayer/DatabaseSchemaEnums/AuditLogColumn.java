package com.QueryLayer.DatabaseSchemaEnums;

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

    @Override
    public String toString() {
        return getTable().getTableName() + "." + columnName;
    }
}
