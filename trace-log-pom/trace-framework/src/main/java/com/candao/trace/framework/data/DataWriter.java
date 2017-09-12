package com.candao.trace.framework.data;

import java.util.List;

import com.candao.trace.framework.cache.RedisClient;
import com.candao.trace.framework.callback.IDataGeter;
import com.candao.trace.framework.callback.IDataSeter;
import com.candao.trace.framework.lock.LocalLock;

/**
 * 数据写入/更新器<br/>
 * 封装常用数据写入/更新处理规则
 * 
 */
public class DataWriter {
	private static final String PRE_LOCK_KEY = "DataWriter:";

	/**
	 * 插入单个数据成功后，写Redis
	 * @param cacheKey-缓存key
	 * @param dataGeter-一般为DB插入操作
	 * @return
	 */
	public static <T> T insertDataSynRedis(String cacheKey, IDataGeter<T> dataGeter) {
		T data = dataGeter.getData();
		if (data != null) {
			RedisClient.setValue(cacheKey, data);
		}
		return data;
	}

	/**
	 * 设置单个数据成功后，刷新Redis(这里的做法是直接移除key，等下次get的时候再获取)
	 * @param cacheKey--缓存key
	 * @param dataSeter--一般为DB更新操作
	 * @return
	 */
	public static boolean updateDataAndDelRedisKey(String cacheKey, IDataSeter dataSeter) {
		Object lockObject = LocalLock.getLockObject(PRE_LOCK_KEY + cacheKey);
		synchronized (lockObject) {
			boolean rs = dataSeter.setData();
			if (rs) {
				RedisClient.delKey(cacheKey);
			}
			return rs;
		}
	}

	/**
	 * 批量设置数据成功后，刷新Redis(这里的做法是直接移除key，等下次get的时候再获取)
	 * @param cacheKeys--缓存key集合
	 * @param dataSeter--一般为DB更新操作
	 * @return
	 */
	public static boolean batchUpdateDataAndDelRedisKey(List<String> cacheKeys, IDataSeter dataSeter) {
		Object lockObject = LocalLock.getLockObject(PRE_LOCK_KEY + String.join(",", cacheKeys));
		synchronized (lockObject) {
			boolean rs = dataSeter.setData();
			if (rs) {
				for (String cacheKey : cacheKeys) {
					RedisClient.delKey(cacheKey);
				}
			}
			return rs;
		}
	}

	/**
	 * 设置单个数据成功后，刷新Redis(这里的做法是直接移除key，等下次get的时候再获取)
	 * @param cacheKey--缓存key
	 * @param dataSeter--一般为DB更新操作
	 * @return
	 */
	public static <T> T updateDataAndSynRedis(String cacheKey, IDataGeter<T> dataGeter) {
		Object lockObject = LocalLock.getLockObject(PRE_LOCK_KEY + cacheKey);
		synchronized (lockObject) {
			T data = dataGeter.getData();
			if (data != null) {
				RedisClient.setValue(cacheKey, data);
			}
			return data;
		}
	}

}
