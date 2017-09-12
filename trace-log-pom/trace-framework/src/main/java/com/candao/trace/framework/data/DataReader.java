package com.candao.trace.framework.data;

import java.util.List;

import com.candao.trace.framework.cache.RedisClient;
import com.candao.trace.framework.callback.IDataGeter;
import com.candao.trace.framework.callback.IListDataGeter;
import com.candao.trace.framework.exception.DBException;
import com.candao.trace.framework.exception.RedisException;
import com.candao.trace.framework.lock.LocalLock;

/**
 * 数据读取器<br/>
 * 封装常用数据读取处理规则
 * 
 */
public class DataReader {

	/**
	 * 获取redis中获取指定data，如果redis中不存在，则从DataGeter接口中获取，并且写入redis<br/>
	 * @param cacheKey-缓存key
	 * @param clazz-获取目标类型
	 * @param dataGeter-数据获取器，原始数据源，一般从db中取数据
	 * @return
	 */
	public static <T> T getDataFromRedisOrDataGeter(String cacheKey, Class<T> clazz, IDataGeter<T> dataGeter) {
		try {
			Object lockObject = LocalLock.getLockObject(cacheKey);
			synchronized (lockObject) {// 为提升性能 这里不使用分布式锁，在分布式大并发环境下可能在不同服务器会被同时执行多次，但不影响业务流程
				T data = RedisClient.getValue(cacheKey, clazz);
				if (data == null) {// Redis中不存在，则从数据源中加载
					try {
						data = dataGeter.getData();
					} catch (DBException e) {
						e.printStackTrace();// DB异常
						return data;
					}
					if (data != null) {
						try {
							RedisClient.setValue(cacheKey, data, RedisClient.DEFAULT_EXPIRE_SECONDS);
						} catch (RedisException e) {
							e.printStackTrace();// Redis异常只记录不中断程序
						}
					}
				}
				return data;
			}
		} finally {
			LocalLock.disposeLock(cacheKey);
		}
	}

	/**
	 * 获取redis中获取指定list，如果redis中不存在，则从ListDataGeter接口中获取，并且写入redis<br/>
	 * @param cacheKey-缓存key
	 * @param clazz-获取目标类型
	 * @param dataGeter-数据获取器，原始数据源，一般从db中取数据
	 * @return
	 */
	public static <T> List<T> getListFromRedisOrDataGeter(String cacheKey, Class<T> clazz, IListDataGeter<T> dataGeter) {
		try {
			Object lockObject = LocalLock.getLockObject(cacheKey);
			synchronized (lockObject) {// 为提升性能 这里不使用分布式锁，在分布式大并发环境下可能在不同服务器会被同时执行多次，但不影响业务流程
				List<T> list = RedisClient.getAllList(cacheKey, clazz);
				if (list == null) {
					try {
						list = dataGeter.getList();
					} catch (DBException e) {
						e.printStackTrace();
						return list;
					}
					if (list != null) {
						try {
							RedisClient.delKey(cacheKey);
							RedisClient.setList(cacheKey, list);
						} catch (RedisException e) {
							e.printStackTrace();// Redis异常只记录不中断程序
						}
					}
				}
				return list;
			}
		} finally {
			LocalLock.disposeLock(cacheKey);
		}
	}
	
	/**
	 * 获取redis map中获取指定data，如果redis中不存在，则从DataGeter接口中获取，并且写入redis<br/>
	 * @param hashCacheKey-hashmap的key
	 * @param cacheKey-具体数据key
	 * @param clazz-目标类型（T对象类型）
	 * @param dataGeter-数据获取器，原始数据源，一般从db中取数据
	 * @return
	 */
	public static <T> T getDataFromRedisMapOrDataGeter(String hashCacheKey, String cacheKey, Class<T> clazz, IDataGeter<T> dataGeter) {
		try {
			Object lockObject = LocalLock.getLockObject(cacheKey);
			synchronized (lockObject) {// 为提升性能 这里不使用分布式锁
				T data = RedisClient.getValueFromHashMap(hashCacheKey, cacheKey, clazz);
				if (data == null) {
					try {
						data = dataGeter.getData();
					} catch (DBException e) {
						e.printStackTrace();
						return data;
					}
					if (data != null) {
						try {
							RedisClient.addToHashMap(hashCacheKey, cacheKey, data);
						} catch (RedisException e) {
							e.printStackTrace();// Redis异常只记录不中断程序
						}
					}
				}
				return data;
			}
		} finally {
			LocalLock.disposeLock(cacheKey);
		}
	}
}
