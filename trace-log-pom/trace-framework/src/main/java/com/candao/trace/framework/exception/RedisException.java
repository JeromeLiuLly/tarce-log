package com.candao.trace.framework.exception;

/**
 * Redis异常
 * 
 */
public class RedisException extends RuntimeException {
	private static final long serialVersionUID = -5205847518598975836L;

	public RedisException() {
		this("Redis Exception");
	}

	public RedisException(String message) {
		super(message);
	}

	public RedisException(Throwable cause) {
		super("Redis Exception", cause);
	}

	public RedisException(String message, Throwable cause) {
		super(message, cause);
	}

	public RedisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
