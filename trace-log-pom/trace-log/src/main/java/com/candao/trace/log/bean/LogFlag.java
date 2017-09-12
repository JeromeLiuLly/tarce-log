package com.candao.trace.log.bean;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public enum LogFlag {

	/**
	 * 请求日志
	 */
	REQUEST(1, "REQUEST"),
	/**
	 * 响应日志
	 */
	RESPONSE(2, "RESPONSE"),
	/**
	 * 服务接口日志
	 */
	INNER_API(3, "INNER_API"),
	/**
	 * 方法日志
	 */
	METHOD(4, "METHOD"),
	/**
	 * 异常日志
	 */
	EXCEPTION(5, "EXCEPTION"),
	/**
	 * 缓存日志
	 */
	CACHE(6, "CACHE"),
	/**
	 * DB日志
	 */
	DB(7, "DB"),
	/**
	 * 业务日志
	 */
	BUSINESS(8, "BUSINESS"),
	/**
	 * 其他日志
	 */
	ORTHER(9, "ORTHER");

	private int flag;
	private String name;

	private LogFlag(int flag, String name) {
		this.flag = flag;
		this.name = name;
	}

	public int getFlag() {
		return this.flag;
	}

	public String getName() {
		return name;
	}

	public static LogFlag get(int flag) {
		switch (flag) {
		case 1:
			return REQUEST;
		case 2:
			return RESPONSE;
		case 3:
			return INNER_API;
		case 4:
			return METHOD;
		case 5:
			return EXCEPTION;
		case 6:
			return CACHE;
		case 7:
			return DB;
		case 8:
			return BUSINESS;
		case 9:
			return ORTHER;
		default:
			return ORTHER;
		}
	}

	public static String getAllJson() {
		LogFlag[] values = LogFlag.values();
		JsonArray arr = new JsonArray();
		JsonObject jsonObject = null;
		for (LogFlag sysName : values) {
			jsonObject = new JsonObject();
			jsonObject.addProperty("flag", sysName.getFlag());
			jsonObject.addProperty("name", sysName.getName());
			arr.add(jsonObject);
		}
		return arr.toString();
	}

}
