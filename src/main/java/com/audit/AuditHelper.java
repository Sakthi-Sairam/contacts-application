package com.audit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.exceptions.DaoException;
import com.models.BaseModel;
import com.queryLayer.*;
import com.queryLayer.databaseSchemaEnums.Column;
import com.queryLayer.databaseSchemaEnums.Table;
import com.utils.JsonFormatter;

public class AuditHelper {
	BaseModel oldDataModel;
	public void audit(QueryBuilder qb) {
		
		Table table = qb.query.getTables().get(0);
		if(table==Table.AUDIT_LOG || table==Table.SESSIONS || table==Table.SERVER_REGISTRY || table==Table.OAUTH_TOKENS) return;
		
		switch (qb.query.getQueryType()) {
		case QueryType.INSERT:
			auditInsert(qb);
			break;
		case QueryType.UPDATE:
			auditUpdate(qb);
			break;
		case QueryType.DELETE:
			auditDelete(qb);
			break;

		default:
			break;
		}
	}
	
	public void beforeExecute(QueryBuilder qb) {
		Table table = qb.query.getTables().get(0);
		if(table==Table.AUDIT_LOG || table==Table.SESSIONS || table==Table.SERVER_REGISTRY) return;
//		if(qb.query.getQueryType() != QueryType.UPDATE) return;
		System.out.println(qb.query.getQueryType());
		try {
			oldDataModel = AuditDao.selectRecord(table, qb);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	private void auditDelete(QueryBuilder qb) {
		if(oldDataModel==null) return;//checking
		Table table = qb.query.getTables().get(0);
		JsonFormatter oldJsonData = jsonModelMapperHelper(oldDataModel);
		oldJsonData.put(table.getPrimaryKeyColumn().toString(), (int)oldDataModel.getPrimaryKeyValue());
		try {
			AuditDao.insertRecord(table.toString(), "DELETE",oldJsonData.toString(), null);
		} catch (DaoException e) {
			e.printStackTrace();
		}
	}

	private void auditUpdate(QueryBuilder qb) {
		
		if(oldDataModel==null) return;//checking
		
		List<Column> columns = qb.query.getColumns();
		List<Object> values = qb.query.getValues();
		JsonFormatter newData = jsonHelper(columns, values);
        JsonFormatter oldDataUpdatedFields = new JsonFormatter();
        JsonFormatter newDataUpdatedFields = new JsonFormatter();
        Column primaryKeyColumn = qb.query.getTables().get(0).getPrimaryKeyColumn();

        compareAndGetUpdatedFields(newData, oldDataUpdatedFields, newDataUpdatedFields, primaryKeyColumn);

        if (!newDataUpdatedFields.isEmpty()) {
            try {
                AuditDao.insertRecord(qb.query.getTables().get(0).toString(), "UPDATE", oldDataUpdatedFields.toString(), newDataUpdatedFields.toString());
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }

	}


    private void compareAndGetUpdatedFields(Map<String, Object> newDataMap, JsonFormatter oldDataUpdatedFields, JsonFormatter newDataUpdatedFields, Column primaryKeyColumn) {
        try {
            for (Field field : oldDataModel.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object oldValue = field.get(oldDataModel);
                String columnName = field.getAnnotation(com.models.Column.class).name();
                
                if(columnName.equals(primaryKeyColumn.toString())){
                	newDataUpdatedFields.put(columnName, oldValue);
                    oldDataUpdatedFields.put(columnName, oldValue);
                }
                
                if (newDataMap.containsKey(columnName) && !areEqual(oldValue, newDataMap.get(columnName))) {
                    newDataUpdatedFields.put(columnName, newDataMap.get(columnName));
                    oldDataUpdatedFields.put(columnName, oldValue);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

//        return recordId;
    }


    private boolean areEqual(Object oldValue, Object newValue) {
        return oldValue == null ? newValue == null : oldValue.equals(newValue);
    }


	private void auditInsert(QueryBuilder qb) {
		Table table = qb.query.getTables().get(0);
		if(table==Table.AUDIT_LOG || table==Table.SESSIONS || table==Table.SERVER_REGISTRY) return;
		
		List<Column> columns = qb.query.getColumns();
		List<Object> values = qb.query.getValues();
		JsonFormatter newData = jsonHelper(columns, values);
		newData.put(table.getPrimaryKeyColumn().toString(), qb.query.getAutoGeneratedKey());
		try {
			AuditDao.insertRecord(table.toString(), "INSERT" , null, newData.toString());
		} catch (DaoException e) {
			e.printStackTrace();
		}
		
	}

	private JsonFormatter jsonHelper(List<Column> columns, List<Object> values) {
		JsonFormatter json = new JsonFormatter();
		for(int i=0;i<columns.size();i++) {
			json.put(columns.get(i).toString(), values.get(i));
		}
		return json;
	}
	
	private JsonFormatter jsonModelMapperHelper(BaseModel model) {
		JsonFormatter json = new JsonFormatter();
		for(Field field : model.getClass().getDeclaredFields()) {
			field.setAccessible(true);
            com.models.Column columnAnnotation = field.getAnnotation(com.models.Column.class);
            if(columnAnnotation == null) continue;
            String columnName = columnAnnotation.name();
            try {
				json.put(columnName, field.get(model));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
		}
		return json;
	}
	
}
