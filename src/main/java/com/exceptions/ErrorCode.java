package com.exceptions;

public enum ErrorCode {
 	DATABASE_CONNECTION_ERROR("Error connecting to the database"),
    QUERY_EXECUTION_FAILED("Query execution failed"),
    INVALID_INPUT("Invalid input provided"),
    DATA_NOT_FOUND("Data not found"),
    UNAUTHORIZED_ACCESS("Unauthorized access"),
    TRANSACTION_ERROR("Transaction error"),
    DATABASE_ERROR("Database error");
    private final String description;

	private ErrorCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
    

}
