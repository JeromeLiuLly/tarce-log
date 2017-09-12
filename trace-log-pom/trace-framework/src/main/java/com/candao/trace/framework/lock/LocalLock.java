package com.candao.trace.framework.lock;

import java.util.HashMap;
import java.util.Map;

/**
 * 本地锁
 * 
 */
public class LocalLock {
	// 锁对象集合
	public static Map<String, Object> objectMap = new HashMap<String, Object>();

	/**
	 * 获取一个指定的对象锁
	 * @param key
	 * @return
	 */
	public synchronized static Object getLockObject(String key) {
		Object obj = objectMap.get(key);
		if (obj == null) {
			obj = new Object();
			objectMap.put(key, obj);
		}
		return obj;
	}

	/**
	 * 释放对象锁
	 * @param key
	 */
	public static void disposeLock(String key) {
		objectMap.remove(key);
	}
}
