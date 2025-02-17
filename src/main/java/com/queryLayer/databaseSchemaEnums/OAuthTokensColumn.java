package com.queryLayer.databaseSchemaEnums;

public enum OAuthTokensColumn implements Column {
    ID("id"),
    USER_ID("user_id"),
    REFRESH_TOKEN("refresh_token"),
    EMAIL("email"),
    PROVIDER("provider"),
    SYNC_INTERVAL("sync_interval"),
    LAST_SYNC("last_sync"),
    CREATED_AT("created_at"),
    UPDATED_AT("updated_at");

    private final String columnName;

    OAuthTokensColumn(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public Table getTable() {
        return Table.OAUTH_TOKENS;
    }

    @Override
    public String toString() {
        return getTable().getTableName() + "." + columnName;
    }

    public OAuthTokensColumn[] getAllColumns() {
        return OAuthTokensColumn.values();
    }
}