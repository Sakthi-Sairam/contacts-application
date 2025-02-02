package com.queryLayer;

public class Pair {
    int rowCount;
    int generatedKey;
    Pair(int rowCount, int generatedKey) {
        this.rowCount = rowCount;
        this.generatedKey = generatedKey;
    }
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public int getGeneratedKey() {
		return generatedKey;
	}
	public void setGeneratedKey(int generatedKey) {
		this.generatedKey = generatedKey;
	}
}