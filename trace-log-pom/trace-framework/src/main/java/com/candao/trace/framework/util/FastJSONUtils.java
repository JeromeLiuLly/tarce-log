package com.candao.trace.framework.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class FastJSONUtils {

	public static final String V_UNDEFINED = "undefined"; 
	
	public static final String A_FUN_REGEX = "^function\\s{0,}\\((.|\\s)+$";
	
	public static <T> T parseObject(Object source, Class<T> clazz){
		
		if(source != null && clazz != null){
			return JSON.parseObject(JSON.toJSONString(source), clazz);
		}
		
		return null;
	}
	
	public static boolean isAnonymousFun(String fun){
		
		return fun != null && fun.trim().matches(A_FUN_REGEX);
	}
	
	public static JSONObject isObj(String data){
		try{
			return JSONObject.parseObject(data);
		}catch(Exception e){
		}
		return null;
	}
	
	public static JSONArray isArray(String data){
		try{
			return JSONObject.parseArray(data);
		}catch(Exception e){
		}
		return null;
	}
	
	public static boolean isEmpty(JSONObject json){
		
		return json == null || json.isEmpty();
	}
	
	public static boolean isEmpty(JSONArray array){
		
		return array == null || array.isEmpty();
	}
	
	public static JSONObject getJSONObject(Object obj){
		return obj != null ? JSONObject.parseObject(JSONObject.toJSONString(obj)) : new JSONObject();
	}
	
	public static JSONArray getJSONArray(List<?> list){
		return list != null ? JSONObject.parseArray(JSONObject.toJSONString(list)) : new JSONArray();
	}
	
	public static JSONArray getJSONArray(Object obj){
		
		String jsonStf = JSONObject.toJSONString(obj);
		
		return isArray(jsonStf);
	}
	
	public static JSONArray getJSONArray(Object[] array){
		return array != null ? JSONObject.parseArray(JSONObject.toJSONString(array)) : new JSONArray();
	}
	
	public static String getString(String key, JSONObject json){
		
		return getString(key, json, StringUtils.EMPTY);
	}
	
	public static String getString(String key, JSONObject json, String defaultValue){
		
		if (StringUtils.isEmpty(key) || json == null || !json.containsKey(key)) {
			return defaultValue;
		}
		
		String bv = String.valueOf(json.get(key));
		
		return StringUtils.isNotEmpty(bv) ? bv : defaultValue;
	}
	
	public static int getInt(String key, JSONObject json){
		
		return getInt(key, json, 0);
	}
	
	public static int getInt(String key, JSONObject json, int defaultValue){
		
		if (StringUtils.isEmpty(key) || json == null || !json.containsKey(key)) {
			return defaultValue;
		}
		
		String bv = String.valueOf(json.get(key));
		
		return FastStringUtils.isNum(bv) ? Double.valueOf(bv).intValue() : defaultValue;
	}
	
	public static long getLong(String key, JSONObject json){
		
		return getLong(key, json, 0l);
	}
	
	public static long getLong(String key, JSONObject json, long defaultValue){
		
		if (StringUtils.isEmpty(key) || json == null || !json.containsKey(key)) {
			return defaultValue;
		}
		
		String bv = String.valueOf(json.get(key));
		
		return FastStringUtils.isNum(bv) ? Double.valueOf(bv).longValue(): defaultValue;
	}
	
	public static double getDouble(String key, JSONObject json){
		
		return getDouble(key, json, 0);
	}
	
	public static double getDouble(String key, JSONObject json, double defaultValue){
		
		if (StringUtils.isEmpty(key) || json == null || !json.containsKey(key)) {
			return defaultValue;
		}
		
		String bv = String.valueOf(json.get(key));
		
		return FastStringUtils.isNum(bv) ? Double.valueOf(bv) : defaultValue;
	}
	
	/**
	 * 获取boolean类型
	 * @param key String
	 * @param json JSONObject
	 * @return boolean
	 */
	public static boolean getBoolean(String key, JSONObject json) {

		return getBoolean(key, json, false);
	}

	/**
	 * 获取boolean类型
	 * @param key String
	 * @param json JSONObject
	 * @param defaultValue boolean
	 * @return boolean
	 */
	public static boolean getBoolean(String key, JSONObject json, boolean defaultValue) {

		if (StringUtils.isEmpty(key) || json == null || !json.containsKey(key)) {
			return defaultValue;
		}

		String bv = String.valueOf(json.get(key));

		return FastStringUtils.isBoolean(bv) ? Boolean.valueOf(bv) : defaultValue;
	}

}
