package com.queryLayer;

import com.queryLayer.databaseSchemaEnums.Column;

public class Condition {
	private Column column;
	private Object value;
	private String operator;
	public Condition(Column column, Object value, String operator) {
		super();
		this.column = column;
		this.value = value;
		this.operator = operator;
	}
	public Column getColumn() {
		return column;
	}
	public void setColumn(Column column) {
		this.column = column;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
}
