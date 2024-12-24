package com.QueryLayer;

import com.QueryLayer.DatabaseSchemaEnums.*;

public class QueryBuilder {

    private Query query;

    public QueryBuilder() {
        this.query = new Query();
    }

    // for select statements
    public QueryBuilder select(Column... columns) {
        query.setQueryType(QueryType.SELECT);
        query.setNumberOfCols(columns.length);
        StringBuilder selectQuery = new StringBuilder("SELECT ");
        String prefix = "";
        for (Column col : columns) {
            selectQuery.append(prefix);
            selectQuery.append(col.toString());
            prefix = ", ";
        }
        query.setQueryString(selectQuery.toString());
        return this;
    }

    public QueryBuilder from(Table table) {
        String queryString = query.getQueryString() + " FROM " + table.getTableName();
        query.setQueryString(queryString);
        return this;
    }

    public QueryBuilder limit(int limit) {
        String queryString = query.getQueryString() + " LIMIT " + limit;
        query.setQueryString(queryString);
        return this;
    }

    public QueryBuilder where(Column col, String condition, Object value, boolean isWhere) {
        if (!isWhere) return where(col, condition, value);
        
        String queryString = query.getQueryString();
        queryString += " WHERE " + col.toString() + " " + condition + " " + formatValue(value);
        query.setQueryString(queryString);
        return this;
    }

    public QueryBuilder where(Column col, String condition, Object value) {
        String queryString = query.getQueryString();
        queryString += " " + col.toString() + " " + condition + " " + formatValue(value);
        query.setQueryString(queryString);
        return this;
    }

    public QueryBuilder and() {
        query.setQueryString(query.getQueryString() + " AND ");
        return this;
    }

    public QueryBuilder join(Table table, Column foreignKey1, Column foreignKey2) {
        String queryString = query.getQueryString();
        queryString += String.format(" JOIN %s ON %s = %s", table.getTableName(), foreignKey1.toString(), foreignKey2.toString());
        query.setQueryString(queryString);
        return this;
    }

    // for insert statements
    public QueryBuilder insert(Table table) {
        query.setQueryType(QueryType.INSERT);
        String insertQuery = "INSERT INTO " + table.getTableName();
        query.setQueryString(insertQuery);
        return this;
    }

    public QueryBuilder columns(Column... columns) {
        StringBuilder queryString = new StringBuilder(query.getQueryString()).append(" (");
        String prefix = "";
        for (Column col : columns) {
            queryString.append(prefix).append(col.toString());
            prefix = ", ";
        }
        queryString.append(")");
        query.setQueryString(queryString.toString());
        return this;
    }

    public QueryBuilder values(Object... values) {
        StringBuilder queryString = new StringBuilder(query.getQueryString() + " VALUES (");
        String prefix = "";
        for (Object val : values) {
            queryString.append(prefix).append(formatValue(val));
            prefix = ", ";
        }
        queryString.append(")");
        query.setQueryString(queryString.toString());
        return this;
    }

    // for delete statements
    public QueryBuilder delete(Table table) {
        query.setQueryType(QueryType.DELETE);
        String deleteQuery = "DELETE FROM " + table.getTableName();
        query.setQueryString(deleteQuery);
        return this;
    }
    
 // for update statements
    public QueryBuilder update(Table table) {
        query.setQueryType(QueryType.UPDATE);
        String updateQuery = "UPDATE " + table.getTableName();
        query.setQueryString(updateQuery);
        return this;
    }

    public QueryBuilder set(Column column, Object value) {
        String queryString = query.getQueryString();
        if (!queryString.contains(" SET ")) {
            queryString += " SET ";
        } else {
            queryString += ", ";
        }
        queryString += column.toString() + " = " + formatValue(value);
        query.setQueryString(queryString);
        return this;
    }


    // returning the value
    public String build() {
        return query.getQueryString() + ";";
    }

    public int getNumberOfCols() {
        return query.getNumberOfCols();
    }

    // helper method
    private String formatValue(Object value) {
        if (value instanceof String) {
            return "'" + value + "'";
        } else {
            return value.toString();
        }
    }

	public QueryBuilder orderBy(MyContactsDataColumn aliasFndName, boolean isAscending) {
		String queryString = query.getQueryString();
		queryString += " ORDER BY "+aliasFndName;
		if(!isAscending) queryString += " Desc";
		query.setQueryString(queryString);
		return this;
	}
}
