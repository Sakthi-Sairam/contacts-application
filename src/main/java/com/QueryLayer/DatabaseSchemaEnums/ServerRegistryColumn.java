package com.QueryLayer.DatabaseSchemaEnums;

public enum ServerRegistryColumn implements Column{
    ID("id"),
    IP_ADDRESS("ip_address"),
    PORT_NUMBER("port_number"),
    REGISTERED_AT("registered_at"),
    LAST_HEARTBEAT("last_heartbeat");

    private final String columnName;

    ServerRegistryColumn(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

	@Override
	public Table getTable() {
		return Table.SERVER_REGISTRY;
	}
}
