package com.candao.trace.log.logger;

import org.slf4j.LoggerFactory;

import com.candao.trace.log.bean.CacheOpt;
import com.candao.trace.log.bean.LogEvent;
import com.candao.trace.log.bean.LogFlag;

public class CacheLogger extends ErrLogger {
	
	public static org.slf4j.Logger log = LoggerFactory.getLogger(CacheLogger.class);
	
	public static void info(String msg) {
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		LogEvent logEvent = new LogEvent();
		logEvent.flag = LogFlag.CACHE.getFlag();
		logEvent.msg = msg;
		logEvent.className = stack[2].getClassName();
		logEvent.methodName = stack[2].getMethodName();
		setLogIp(logEvent);
		setMDC(logEvent);
		log.info(logEvent.toString());
	}
	
	public static void info(String key, CacheOpt opt, long costTime) {
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		LogEvent logEvent = new LogEvent();
		logEvent.flag = LogFlag.CACHE.getFlag();
//		logEvent.step = ThreadLocalBean.getStepAndInc();
		logEvent.keyOrSql = key;
		logEvent.cacheOpt = opt.flag;
		logEvent.costTime = costTime;
		logEvent.className = stack[2].getClassName();
		logEvent.methodName = stack[2].getMethodName();
		setLogIp(logEvent);
		setMDC(logEvent);
		log.info(logEvent.toString());
	}
}
