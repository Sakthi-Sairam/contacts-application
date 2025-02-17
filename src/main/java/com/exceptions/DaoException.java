package com.exceptions;

public class DaoException extends Exception{
	private static final long serialVersionUID = 1L;
	private final ErrorCode code;

	public DaoException(ErrorCode code,String message) {
		super(message);
		this.code = code;
	}
	
	public DaoException(ErrorCode code,String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}
}
