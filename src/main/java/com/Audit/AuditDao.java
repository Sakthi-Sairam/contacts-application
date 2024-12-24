package com.Audit;

import java.sql.SQLException;

import com.QueryLayer.QueryBuilder;
import com.QueryLayer.QueryExecutor;
import com.QueryLayer.DatabaseSchemaEnums.*;

public class AuditDao {
	public static boolean insertRecord(String tableName, String actionType, int recordId, String oldData, String newData, long currTime) throws SQLException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor exe = new QueryExecutor();
		
		qb.insert(Table.AUDIT_LOG)
		.columns(AuditLogColumn.TABLE_NAME,AuditLogColumn.ACTION_TYPE,AuditLogColumn.RECORD_ID,AuditLogColumn.OLD_DATA,AuditLogColumn.NEW_DATA,AuditLogColumn.TIMESTAMP)
		.values(tableName,actionType,recordId,oldData,newData,currTime);
		
		int rc = exe.executeUpdate(qb);
		return rc>0;
	}
}
