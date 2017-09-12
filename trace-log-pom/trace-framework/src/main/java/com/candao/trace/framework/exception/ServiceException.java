package com.candao.trace.framework.exception;

/**
 * 参数异常
 * 
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 2979629890710776829L;
	
	/**
	 * 自定义异常类型
	 */
	public int code;
	/**
	 * 自定义异常信息
	 */
	public String msg;
	/**
	 * 自定义额外数据
	 */
	public Object[] args;

	public ServiceException() {
		super();
	}

	public ServiceException(Throwable e) {
		super(e);
		msg = e.getMessage();
		code = 2;
	}

	/**
	 * 异常信息
	 * @param msg-异常信息
	 */
	public ServiceException(String msg, Object... args) {
		this(2, msg, args);
	}

	public ServiceException(int code, String msg, Object... args) {
		super(msg);
		this.code = code;
		this.msg = msg;
		this.args = args;
	}
}
