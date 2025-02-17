package com.queryLayer.databaseSchemaEnums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ServerRegistryColumn implements Column{
    ID("id"),
    IP_ADDRESS("ip_address"),
    PORT_NUMBER("port_number"),
    CREATED_AT("created_at");

    private final String columnName;

    ServerRegistryColumn(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
    public Column[] getAllColumns(){
    	return ServerRegistryColumn.values();
    }

	@Override
	public Table getTable() {
		return Table.SERVER_REGISTRY;
	}
	
    @Override
    public String toString() {
        return getTable().getTableName() + "." + columnName;
    }
}
