package com.exceptions;

import java.sql.SQLException;

import com.mysql.cj.protocol.Message;

public class QueryExecutorException extends Exception{
	private static final long serialVersionUID = 1L;
	private ErrorCode code;

	public QueryExecutorException(String message) {
		super(message);
	}
	
	public QueryExecutorException(String message, Throwable clause) {
		super(message, clause);
	}

	public QueryExecutorException(ErrorCode code, String message, Throwable e) {
		super(message,e);
		this.code = code;
	}

	public QueryExecutorException(ErrorCode code, String message) {
		super(message);
		this.code = code;
	}

}
