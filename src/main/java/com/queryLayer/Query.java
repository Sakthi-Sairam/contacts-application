package com.queryLayer;

import java.util.*;

import com.queryLayer.databaseSchemaEnums.Column;
import com.queryLayer.databaseSchemaEnums.Table;

public class Query {
	private String queryString;
	private int numberOfCols;
	private QueryType queryType;
	private List<Object> values;
	private List<Column> columns;
	private List<Table> tables;
	private List<Condition> conditions;
	private int autoGeneratedKey;

	public Query() {
		values = new ArrayList<>();
		tables = new ArrayList<>();
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	public int getNumberOfCols() {
		return numberOfCols;
	}

	public void setNumberOfCols(int numberOfCols) {
		this.numberOfCols = numberOfCols;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryType(QueryType queryType) {
		this.queryType = queryType;
	}

	public QueryType getQueryType() {
		return this.queryType;
	}

	public void addParameter(Object param) {
		this.values.add(param);
	}

	public List<Object> getParameters() {
		return values;
	}

	public Object[] getParametersArray() {
		return values.toArray();
	}

	public void addTable(Table table) {
		if (this.tables == null)
			this.tables = new ArrayList<>();
		this.tables.add(table);
	}

	public void addColumn(Column col) {
		if (this.columns == null)
			this.columns = new ArrayList<>();
		this.columns.add(col);
	}

	public void addValue(Object val) {
		if (this.values == null)
			this.values = new ArrayList<>();
		this.values.add(val);
	}

	public int getAutoGeneratedKey() {
		return this.autoGeneratedKey;
	}

	public void setAutoGeneratedKey(int autoGeneratedKey) {
		this.autoGeneratedKey = autoGeneratedKey;
	}

	public void addCondition(Column col, String condition, Object value) {
		if (this.conditions == null)
			this.conditions = new ArrayList<>();
		this.conditions.add(new Condition(col, value, condition));
	}

	public List<Condition> getConditions() {
		return this.conditions;
	}
}