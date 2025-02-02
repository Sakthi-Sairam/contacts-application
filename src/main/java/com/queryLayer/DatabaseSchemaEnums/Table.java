package com.queryLayer.DatabaseSchemaEnums;

import com.models.CategoryDetails;
import com.models.CategoryList;
import com.models.Contact;
import com.models.Email;
import com.models.OAuthToken;
import com.models.BaseModel;
import com.models.ServerRegistry;
import com.models.Session;
import com.models.User;

/**
 * Enum representing all database tables
 */

public enum Table {
    CATEGORY_DETAILS("CategoryDetails", CategoryDetailsColumn.class, CategoryDetailsColumn.CATEGORY_ID, CategoryDetails.class),
    CATEGORY_LIST("CategoryList", CategoryListColumn.class, CategoryListColumn.ID, CategoryList.class),
    MAIL_MAPPER("MailMapper", MailMapperColumn.class, MailMapperColumn.ID, Email.class),
    MY_CONTACTS_DATA("MyContactsData", MyContactsDataColumn.class, MyContactsDataColumn.MY_CONTACTS_ID, Contact.class),
    SESSIONS("sessions", SessionColumn.class, SessionColumn.SESSION_ID, Session.class),
    USER_DATA("userdata", UserDataColumn.class, UserDataColumn.USER_ID,User.class),
    SERVER_REGISTRY("server_registry", ServerRegistryColumn.class, ServerRegistryColumn.ID,ServerRegistry.class),
    AUDIT_LOG("audit_log", AuditLogColumn.class, AuditLogColumn.AUDIT_ID, null),
    OAUTH_TOKENS("oauth_tokens",OAuthTokensColumn.class,OAuthTokensColumn.ID, OAuthToken.class);

    private final String tableName;
    private final Class<? extends Column> columnEnum;
    private final Column primaryKeyColumn;
    private final Class<? extends BaseModel> model;

    Table(String tableName, Class<? extends Column> columnEnum, Column primaryKeyColumn, Class<? extends BaseModel> model) {
        this.tableName = tableName;
        this.columnEnum = columnEnum;
        this.primaryKeyColumn = primaryKeyColumn;
        this.model = model;
    }

    public String getTableName() {
        return tableName;
    }

    public Class<? extends Column> getColumnEnum() {
        return columnEnum;
    }

    public Column getPrimaryKeyColumn() {
        return primaryKeyColumn;
    }

	public Class<? extends BaseModel> getModel() {
		return model;
	}
	
	@Override
	public String toString() {
		return this.tableName;
	}
}
