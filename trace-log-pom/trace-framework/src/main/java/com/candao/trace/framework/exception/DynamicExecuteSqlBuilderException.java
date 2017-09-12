package com.candao.trace.framework.exception;

public class DynamicExecuteSqlBuilderException extends DBException {

	private static final long serialVersionUID = -8347468875156268518L;
	
	public DynamicExecuteSqlBuilderException() {
		this("DynamicUpdateEntityException");
	}

	public DynamicExecuteSqlBuilderException(String message) {
		super(message);
	}
	
}
