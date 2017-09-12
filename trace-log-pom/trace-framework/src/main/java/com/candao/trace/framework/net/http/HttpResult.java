package com.candao.trace.framework.net.http;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

/**
 * 请求结果
 * 
 */
public class HttpResult {
	
	/**
	 * 请求代码，如200、500、503等
	 */
	public int statusCode;

	/**
	 * 结果内容
	 */
	public String content;

	/** 执行出现异常的异常信息 */
	public String errorMsg;
	
	public HttpResponse response;

	public HttpResult(){}
	
	public HttpResult(int statusCode){
		this.statusCode = statusCode;
	}
	
	/**
	 * 请求是否正常
	 * @return
	 */
	public boolean isOk() {
		return statusCode == HttpStatus.SC_OK;
	}

	@Override
	public String toString() {
		return "HttpResult [statusCode=" + statusCode + ", content=" + content + ", errorMsg=" + errorMsg + "]";
	}
	
	
}
