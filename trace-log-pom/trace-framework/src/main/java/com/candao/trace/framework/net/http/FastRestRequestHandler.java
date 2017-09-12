package com.candao.trace.framework.net.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;

/**
 * rest接口请求处理器
 */
public abstract class FastRestRequestHandler {
	
	public static final String CHARSET_UTF8 = "UTF-8";
	
	public static final String CONTENT_TYPE_KEY = "Content-Type";
	public static final String USER_AGENT_KEY = "User-Agent"; 
	public static final String CONTENT_TYPE_JSON = "application/json";
	
	public static final String USER_AGENT_SERVER = "serverClient";
	public static final String USER_AGENT_ANDROID = "androidClient";
	
	public boolean isShowResultLog = true;
	public boolean isShowRequestLog = true;
	
	/**
	 * 设置请求配置
	 * @param request
	 */
	public void setConfig(HttpRequestBase request){}
	
	/**
	 * 设置head内容
	 * @param request
	 */
	public void setHead(HttpRequestBase request){}
	
	public boolean preHandle(org.apache.http.client.HttpClient httpClient, HttpRequestBase request){
		
		return true;
	}
	
	public void postHandle(org.apache.http.client.HttpClient httpClient, HttpRequestBase request, HttpResponse response, HttpResult httpResult){
		
	}
	
	public ContentType getHttpEntityContentType(){
		return null;
	}
	
	public String getEncoding(){
		return null;
	}
	
	public final String getCharsetEncoding(){
		
		String encoding = getEncoding();
		
		return encoding == null ? CHARSET_UTF8 : encoding;
	}
	
	public void onError(Exception e){
		e.printStackTrace();
	}
	
	public boolean isShowResultLog() {
		return isShowResultLog;
	}

	public void setShowResultLog(boolean isShowResultLog) {
		this.isShowResultLog = isShowResultLog;
	}

	public boolean isShowRequestLog() {
		return isShowRequestLog;
	}

	public void setShowRequestLog(boolean isShowRequestLog) {
		this.isShowRequestLog = isShowRequestLog;
	}

}
