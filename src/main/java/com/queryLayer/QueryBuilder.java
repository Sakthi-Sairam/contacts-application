package com.queryLayer;

import com.queryLayer.databaseSchemaEnums.*;

public class QueryBuilder {

	public Query query;

	public QueryBuilder() {
		this.query = new Query();
	}

	// for select statements
	@Deprecated
	public QueryBuilder select() {
		query.setQueryType(QueryType.SELECT);
		query.appendQueryString("SELECT * ");
		return this;
	}
	
	public QueryBuilder select(Table... tables) {
		query.setQueryType(QueryType.SELECT);
		StringBuilder selectQuery = new StringBuilder("SELECT ");
		String prefix = "";
		for(Table table: tables) {
			Column[] columns = table.getPrimaryKeyColumn().getAllColumns();
			for (Column col : columns) {
				selectQuery.append(prefix);
				selectQuery.append(col.toString());
				prefix = ", ";
			}
		}
		query.appendQueryString(selectQuery.toString());
		return this;
	}
	
	@Deprecated
	public QueryBuilder select(Column... columns) {
		query.setQueryType(QueryType.SELECT);
		StringBuilder selectQuery = new StringBuilder("SELECT ");
		String prefix = "";
		for (Column col : columns) {
			selectQuery.append(prefix);
			selectQuery.append(col.toString());
			prefix = ", ";
		}
		query.appendQueryString(selectQuery.toString());
		return this;
	}

	public QueryBuilder from(Table table) {
		String queryString = " FROM " + table.getTableName();
		query.appendQueryString(queryString);
		query.addTable(table);
		return this;
	}

	public QueryBuilder limit(int limit) {
		String queryString =" LIMIT " + limit;
		query.appendQueryString(queryString);
		return this;
	}
	public QueryBuilder offset(int offset) {
		String queryString =" OFFSET " + offset;
		query.appendQueryString(queryString);
		return this;
	}

	@Deprecated
	public QueryBuilder where(Column col, String condition, Object value, boolean isWhere) {
		if (!isWhere)
			return where(col, condition, value);

		String queryString = " WHERE " + col.toString() + " " + condition + " ?";
		query.appendQueryString(queryString);
		query.addQueryParams(value);
		query.addCondition(col, condition, value);
		return this;
	}

	public QueryBuilder where(Column col, String condition, Object value) {
		StringBuilder currentQuery = query.getQueryString();
		
		if (currentQuery.indexOf("WHERE")==-1) {
			query.appendQueryString(" WHERE ");
		}
		String queryString = " " + col.toString() + " " + condition + " ?";
		query.appendQueryString(queryString);
		query.addQueryParams(value);
		query.addCondition(col, condition, value);
		return this;
	}

	public QueryBuilder and() {
		query.appendQueryString(" AND ");
		return this;
	}

	public QueryBuilder join(Table table, Column foreignKey1, Column foreignKey2) {
		String queryString = String.format(" JOIN %s ON %s = %s", table.getTableName(), foreignKey1.toString(),
				foreignKey2.toString());
		query.appendQueryString(queryString);
		return this;
	}

	// for insert statements
	public QueryBuilder insert(Table table) {
		query.setQueryType(QueryType.INSERT);
		String insertQuery = "INSERT INTO " + table.getTableName();
		query.appendQueryString(insertQuery);
		query.addTable(table);
		return this;
	}

	@Deprecated
	public QueryBuilder columns(Column... columns) {
		StringBuilder queryString = new StringBuilder(" (");
		String prefix = "";
		for (Column col : columns) {
			queryString.append(prefix).append(col.toString());
			prefix = ", ";
			query.addColumn(col);
		}
		queryString.append(")");
		query.appendQueryString(queryString.toString());
		return this;
	}

	@Deprecated
	public QueryBuilder values(Object... values) {
		StringBuilder queryString = new StringBuilder(" VALUES (");
		String prefix = "";
		for (Object val : values) {
			queryString.append(prefix).append("?");
			prefix = ", ";
			query.addValue(val);
			query.addQueryParams(val);
		}
		queryString.append(")");
		query.appendQueryString(queryString.toString());
		return this;
	}

	// for delete statements
	public QueryBuilder delete(Table table) {
		query.setQueryType(QueryType.DELETE);
		String deleteQuery = "DELETE FROM " + table.getTableName();
		query.appendQueryString(deleteQuery);
		query.addTable(table);
		return this;
	}

	// for update statements
	public QueryBuilder update(Table table) {
		query.setQueryType(QueryType.UPDATE);
		String updateQuery = "UPDATE " + table.getTableName();
		query.appendQueryString(updateQuery);
		query.addTable(table);
		return this;
	}

	public QueryBuilder set(Column column, Object value) {
		StringBuilder currentQuery = query.getQueryString();
		String queryString = "";
		
		if (currentQuery.indexOf(" SET ")==-1) {
			queryString += " SET ";
		} else {
			queryString += ", ";
		}
		queryString += column.toString() + " =  ?";
		query.appendQueryString(queryString);
		query.addColumn(column);
		query.addValue(value);
		query.addQueryParams(value);
		return this;
	}

	// returning the value
	public String build() {
		return query.getQueryString() + ";";
	}

	// helper method
	private String formatValue(Object value) {
		if (value == null) {
			return "NULL";
		}
		if (value instanceof String) {
			return "'" + value + "'";
		} else {
			return value.toString();
		}
	}

	public QueryBuilder orderBy(Column aliasFndName, boolean isAscending) {
		String queryString = " ORDER BY " + aliasFndName;
		if (!isAscending)
			queryString += " Desc";
		query.appendQueryString(queryString);
		return this;
	}


}
