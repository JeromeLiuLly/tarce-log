package com.candao.trace.framework.bean.response;

import com.google.gson.JsonObject;

/**
 * 响应数据
 * 
 */
public class RspData {
	
	public RspData() {
		// 默认成功
		setStatus(RspStatus.SUCCESS);
	}
	
	public RspData(Object data) {
		this();
		this.data = data;
	}

	/**
	 * 响应状态 {link RspStatus}
	 */
	public int status;
	
	/**
	 * 响应状态描述
	 */
	public String msg;
	
	/**
	 * 响应具体数据
	 */
	public Object data;
	
	/**
	 * 服务器当前时间
	 */
	public long serverTime = System.currentTimeMillis();

	/**
	 * 设置响应状态（使用默认描述）
	 * @param rspStatus-{link RspStatus}
	 */
	public void setStatus(RspStatus rspStatus) {
		status = rspStatus.getStatus();
		msg = rspStatus.getMsg();
	}

	/**
	 * 设置响应状态（使用自定义描述）
	 * @param rspStatus-{link RspStatus}
	 * @param msg-自定义描述
	 */
	public void setStatus(RspStatus rspStatus, String msg) {
		status = rspStatus.getStatus();
		this.msg = msg;
	}

	/**
	 * 给data增加属性，支持Number、String、Boolean、Character
	 * @param name
	 * @param value
	 */
	public void dataAddProperty(String name, Object value) {
		if (data == null) {
			data = new JsonObject();
		}
		JsonObject jsonObject = (JsonObject) data;
		if (value instanceof Number) {
			jsonObject.addProperty(name, (Number) value);
		} else if (value instanceof String) {
			jsonObject.addProperty(name, (String) value);
		} else if (value instanceof Boolean) {
			jsonObject.addProperty(name, (Boolean) value);
		} else if (value instanceof Character) {
			jsonObject.addProperty(name, (Character) value);
		}
	}

	@Override
	public String toString() {
		return "RspData [status=" + status + ", msg=" + msg + ", data=" + data + "]";
	}
}
