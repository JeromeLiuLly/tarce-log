package com.candao.trace.log.logger;

import org.slf4j.LoggerFactory;

import com.candao.trace.log.bean.LogEvent;
import com.candao.trace.log.bean.LogFlag;

public class DBLogger extends ErrLogger {
	
	public static org.slf4j.Logger log = LoggerFactory.getLogger(DBLogger.class);
	
	public static void info(String msg) {
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		LogEvent logEvent = new LogEvent();
		logEvent.flag = LogFlag.DB.getFlag();
		logEvent.msg = msg;
		logEvent.className = stack[2].getClassName();
		logEvent.methodName = stack[2].getMethodName();
		setLogIp(logEvent);
		setMDC(logEvent);
		log.info(logEvent.toString());
	}
	
	public static void info(String sql, long costTime) {
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		LogEvent logEvent = new LogEvent();
		logEvent.keyOrSql = sql;
		logEvent.costTime = costTime;
		logEvent.className = stack[2].getClassName();
		logEvent.methodName = stack[2].getMethodName();
		setLogIp(logEvent);
		setMDC(logEvent);
		log.info(logEvent.toString());
	}
}
