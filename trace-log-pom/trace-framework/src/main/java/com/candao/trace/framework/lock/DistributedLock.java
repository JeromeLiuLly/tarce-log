package com.candao.trace.framework.lock;

import java.util.concurrent.TimeUnit;

import com.candao.trace.framework.cache.RedisClient;
import com.candao.trace.framework.callback.ISimpleCallback;
import com.candao.trace.framework.util.FastDateUtils;

/**
 * 分布式锁
 */
public class DistributedLock {

	/** 默认超时秒数 5分钟，够了吧，一笔业务在5分钟之类应该会完成吧 */
	private static final int DEFAULT_EXPIRE_SECONDS = 1 * 60 * 1;

	/** 睡眠毫秒数，重新获取锁 */
	private static final long GET_LOCK_SLEEP_MILLISECONDS = 200;

	private DistributedLock() {
	}

	/**
	 * 分布式串行执行任务
	 * @param key 加锁的任务key
	 * @param call 任务
	 * @param keyExpireSeconds 任务key的失效时间,单位为秒 (可以理解为：比如你传值为120，如果你的任务120秒还没执行完毕，会自动释放锁，同 一任务key的任务，会获取锁，进行执行)
	 * @param maxGetLockSeconds 获取锁的最大等待秒数(如果传为120，则在120秒后还没获取到锁，便自动跳出方法，如果传的值小于等于0 ，则为第一次拿不到锁，便马上退出执行）
	 * @return
	 */
	public static Object execute(String key, ISimpleCallback call, int keyExpireSeconds, int maxGetLockSeconds) {

		// 不能删除锁（如果是直接跳出的，那就不能删除锁）
		boolean canNotDeleteKey = false;

		// 每次睡眠200毫秒，那1秒钟，就要睡眠5次
		long maxSleepCount = maxGetLockSeconds * 5L;

		// 已经睡眠的次数
		long hasSleepCount = 0;

		try {

			while (true) {

				if (RedisClient.setnx(key, key, keyExpireSeconds)) {

					System.out.println("[" + FastDateUtils.getDefaultCurTimeStr() + "][" + Thread.currentThread().getName() + "]" + "成功获取到key[" + key + "]锁");
					return call.execute();
				}

				if (maxGetLockSeconds <= 0) {
					System.out.println("[" + FastDateUtils.getDefaultCurTimeStr() + "][" + Thread.currentThread().getName() + "]" + "key[" + key + "]已经被锁定，直接跳出");
					// 不能删除锁
					canNotDeleteKey = true;
					return null;
				}

				if (hasSleepCount >= maxSleepCount) {

					System.out.println("[" + FastDateUtils.getDefaultCurTimeStr() + "][" + Thread.currentThread().getName() + "]" + "key[" + key + "]已经被锁定，已经过了获取锁的最大等待秒数:"
							+ maxGetLockSeconds + ",直接跳出");
					// 不能删除锁
					canNotDeleteKey = true;
					return null;
				}

				System.out.println("[" + FastDateUtils.getDefaultCurTimeStr() + "][" + Thread.currentThread().getName() + "]" + "key[" + key + "]已经被锁定，睡眠" + GET_LOCK_SLEEP_MILLISECONDS
						+ "毫秒重新获取锁");
				try {
					TimeUnit.MILLISECONDS.sleep(GET_LOCK_SLEEP_MILLISECONDS);
					hasSleepCount++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		} finally {

			if (!canNotDeleteKey) {
				// 执行完毕，把key给删除
				System.out.println("[" + FastDateUtils.getDefaultCurTimeStr() + "][" + Thread.currentThread().getName() + "]delete key[" + key + "]");
				RedisClient.delKey(key);
			}

		}

	}

	/**
	 * 分布式串行执行任务
	 * @param key 加锁的任务key
	 * @param call 任务
	 * @param keyExpireSeconds 任务key的失效时间,单位为秒 可以理解为：比如你传值为120，如果你的任务120秒还没执行完毕，会自动释放锁，同 一任务key的任务，会获取锁，进行执行
	 * @return
	 */
	public static Object execute(String key, ISimpleCallback call, int keyExpireSeconds) {
		return execute(key, call, keyExpireSeconds, Integer.MAX_VALUE);
	}

	/**
	 * 分布式串行执行任务
	 * @param key 加锁的任务key
	 * @param call 任务
	 * @return
	 */
	public static Object execute(String key, ISimpleCallback call) {
		return execute(key, call, DEFAULT_EXPIRE_SECONDS, Integer.MAX_VALUE);
	}

}
