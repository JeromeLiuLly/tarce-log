package com.candao.trace.log.logger;

import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.candao.trace.log.bean.LogEvent;
import com.candao.trace.log.bean.LogFlag;

public class Logger extends ErrLogger {
	
	public static org.slf4j.Logger log = LoggerFactory.getLogger(Logger.class);
	
	public static void info(LogEvent logEvent) {
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		logEvent.className = stack[2].getClassName();
		logEvent.methodName = stack[2].getMethodName();
		setLogIp(logEvent);
		setMDC(logEvent);
		log.info(logEvent == null ? null : logEvent.toString());
		MDC.clear();
	}
	
	public static void info(String msg) {
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		LogEvent logEvent = new LogEvent();
		logEvent.flag = LogFlag.BUSINESS.getFlag();
		logEvent.msg = msg;
		logEvent.className = stack[2].getClassName();
		logEvent.methodName = stack[2].getMethodName();
		setMDC(logEvent);
		setLogIp(logEvent);
		log.info(logEvent.toString());
		MDC.clear();
	}
	
}
