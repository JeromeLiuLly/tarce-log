package com.candao.trace.gateway.exception;

public class TokenException extends RuntimeException {

	private static final long serialVersionUID = -7869690599565826120L;
	
	public int status = 3;
	public String msg;
	
	public TokenException() {
		super("token失效");
		this.msg = "token失效";
	}

	public TokenException(Throwable e) {
		super(e);
		msg = e.getMessage();
	}
	
	/**
	 * 异常信息
	 * @param msg-异常信息
	 */
	public TokenException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
}
