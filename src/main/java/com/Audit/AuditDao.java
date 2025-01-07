package com.Audit;

import java.sql.SQLException;

import com.QueryLayer.QueryBuilder;
import com.QueryLayer.QueryExecutor;
import com.QueryLayer.DatabaseSchemaEnums.*;
import com.exceptions.DaoException;
import com.exceptions.ErrorCode;
import com.exceptions.QueryExecutorException;

public class AuditDao {
	public static boolean insertRecord(String tableName, String actionType, int recordId, String oldData, String newData, long currTime) throws SQLException, DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor exe = new QueryExecutor();
		
		try {
			qb.insert(Table.AUDIT_LOG)
			.columns(AuditLogColumn.TABLE_NAME,AuditLogColumn.ACTION_TYPE,AuditLogColumn.RECORD_ID,AuditLogColumn.OLD_DATA,AuditLogColumn.NEW_DATA,AuditLogColumn.TIMESTAMP)
			.values(tableName,actionType,recordId,oldData,newData,currTime);
			
			int rc = exe.executeUpdate(qb);
			return rc>0;
		} catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to insert Record in audit log" + e.getMessage(), e);

		}
	}
}
