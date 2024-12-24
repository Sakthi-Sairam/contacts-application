package com.QueryLayer.DatabaseSchemaEnums;

/**
 * Enum representing all database tables
 */
public enum Table {
    CATEGORY_DETAILS("CategoryDetails"),
    CATEGORY_LIST("CategoryList"),
    MAIL_MAPPER("MailMapper"),
    MY_CONTACTS_DATA("MyContactsData"),
    SESSIONS("sessions"),
    USER_DATA("userdata"),
	SERVER_REGISTRY("server_registry"),
	AUDIT_LOG("audit_log");

    private final String tableName;

    Table(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}