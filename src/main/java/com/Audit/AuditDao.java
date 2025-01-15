package com.Audit;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import com.QueryLayer.Condition;
import com.QueryLayer.QueryBuilder;
import com.QueryLayer.QueryExecutor;
import com.QueryLayer.DatabaseSchemaEnums.*;
import com.exceptions.DaoException;
import com.exceptions.ErrorCode;
import com.exceptions.QueryExecutorException;
import com.models.BaseModel;

public class AuditDao {
	public static boolean insertRecord(String tableName, String actionType, String oldData, String newData) throws DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor exe = new QueryExecutor();
		long currTime = System.currentTimeMillis();
		
		try {
			qb.insert(Table.AUDIT_LOG)
			.columns(AuditLogColumn.TABLE_NAME,AuditLogColumn.ACTION_TYPE,AuditLogColumn.OLD_DATA,AuditLogColumn.NEW_DATA,AuditLogColumn.TIMESTAMP)
			.values(tableName,actionType,oldData,newData,currTime);
			
			int rc = exe.executeUpdate(qb);
			return rc>0;
		} catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to insert Record in audit log" + e.getMessage(), e);

		}
		
	}
	public static BaseModel selectRecord(Table table, QueryBuilder sqlQuery) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor exe = new QueryExecutor();
        Column[] allColumns = ((Column[]) table.getColumnEnum().getMethod("values").invoke(null));
        System.out.println("all columns are " + Arrays.toString(allColumns));
		qb.select(allColumns)
			.from(table);
		
	    List<Condition> conditions = sqlQuery.query.getConditions();
	    int len = conditions.size();
		for(int i=0;i<len;i++) {
			if(i!=0 && i!=len-1) qb.and();
			Column column = conditions.get(i).getColumn();
			String operator = conditions.get(i).getOperator();
			Object value = conditions.get(i).getValue();
			qb.where(column,operator,value, i==0);
		}
		try {
			List<BaseModel> result = (List<BaseModel>) exe.executeQuery(qb, table.getModel());
			if(result.size()==0) return null;
			return result.get(0);
		} catch (QueryExecutorException e) {
			throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "select failed in audit dao", e);
		}
	}
}
