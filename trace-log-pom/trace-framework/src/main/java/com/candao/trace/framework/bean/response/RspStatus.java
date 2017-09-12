package com.candao.trace.framework.bean.response;

/**
 * 响应状态
 * 
 */
public enum RspStatus {

	/** 1 操作成功 */
	SUCCESS(1, "操作成功"),
	/** -1 操作失败 */
	FAIL(-1, "操作失败"),

	/** 3 参数错误 */
	PARAM_ERR(2, "参数错误"),
	/** 4 token失效 */
	TOKEN_ERROR(3, "您的账号已失效，请重新登陆");

	private final int status;
	private final String msg;

	// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
	RspStatus(int status, String msg) {
		this.status = status;
		this.msg = msg;
	}

	public int getStatus() {
		return status;
	}

	public String getMsg() {
		return msg;
	}
}
