package com.candao.trace.framework.exception;

/**
 * 数据库异常
 * 
 */
public class DBException extends RuntimeException {
	private static final long serialVersionUID = -3128648316504571901L;

	public DBException() {
		this("DatabaseFailure");
	}

	public DBException(String message) {
		super(message);
	}

	public DBException(Throwable cause) {
		super("DatabaseFailure", cause);
	}

	public DBException(String message, Throwable cause) {
		super(message, cause);
	}

	public DBException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
