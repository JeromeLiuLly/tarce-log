package com.candao.trace.framework.exception;

/**
 * 业务异常
 * 
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1164740090193837439L;
	
	public int code;
	public String msg;
	
	public BusinessException(int code, String msg){
		super(msg);
		this.code = code;
		this.msg = msg;
	}
	
}
