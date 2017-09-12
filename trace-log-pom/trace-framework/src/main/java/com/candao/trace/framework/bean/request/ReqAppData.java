package com.candao.trace.framework.bean.request;

/**
 * APP端请求数据
 * 
 */
public class ReqAppData extends ReqData {
	/**
	 * 请求token
	 */
	public String token;
	/**
	 * 骑手当前经度
	 */
	public double longitude;
	/**
	 * 骑手当前纬度
	 */
	public double latitude;
}
