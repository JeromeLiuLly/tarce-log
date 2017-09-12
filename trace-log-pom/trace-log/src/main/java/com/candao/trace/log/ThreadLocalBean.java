package com.candao.trace.log;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class ThreadLocalBean {

	private static ThreadLocal<Map<String, Object>> reqDataMap = new ThreadLocal<Map<String, Object>>();
	private static ThreadLocal<Long> timeMap = new ThreadLocal<Long>();
	// private static ThreadLocal<Integer> stepMap = new ThreadLocal<Integer>();
	private static ThreadLocal<String> traceIdMap = new ThreadLocal<String>();

	public static void init() {
	}

	public static void remove() {
		reqDataMap.remove();
		timeMap.remove();
		// stepMap.remove();
		traceIdMap.remove();
	}

	public static void setTraceId(String traceId) {
		traceIdMap.set(traceId);
	}

	public static String getTraceId() {
		String traceId = traceIdMap.get();
		if (traceId == null) {
			return "";
		}
		return traceId;
	}

	// public static int getStep() {
	// Integer step = stepMap.get();
	// if (step == null) {
	// return 0;
	// } else {
	// return step;
	// }
	// }
	//
	// public static void setStep(int step) {
	// stepMap.set(step);
	// }
	//
	// public static int getStepAndInc() {
	// Integer step = stepMap.get();
	// if (step == null) {
	// step = 1;
	// stepMap.set(step);
	// return step;
	// } else {
	// stepMap.set(step + 1);
	// return step + 1;
	// }
	// }

	public static void setReqData(String data) {
		reqDataMap.set(JSON.parseObject(data));
	}

	public static Map<String, Object> getReqData() {
		Map<String, Object> map = reqDataMap.get();
		if (map != null) {
			return map;
		} else {
			return new HashMap<String, Object>();
		}
	}

	public static void setReqTime(long reqTime) {
		timeMap.set(reqTime);
	}

	public static Long getReqTime() {
		return timeMap.get();
	}

}
