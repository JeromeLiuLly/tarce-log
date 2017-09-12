package com.candao.trace.log.logger;

import org.slf4j.LoggerFactory;

import com.candao.trace.log.bean.LogEvent;
import com.candao.trace.log.bean.LogFlag;

public class MethodLogger extends ErrLogger {
	
	public static org.slf4j.Logger log = LoggerFactory.getLogger(MethodLogger.class);
	
	public static void info(LogEvent logEvent) {
		logEvent.flag = LogFlag.METHOD.getFlag();
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		logEvent.className = stack[2].getClassName();
		logEvent.methodName = stack[2].getMethodName();
		setLogIp(logEvent);
		setMDC(logEvent);
		log.info(logEvent == null ? null : logEvent.toString());
	}
	
	public static void info(String className, String methodName, String args, long costTime) {
		LogEvent logEvent = new LogEvent();
//		logEvent.step = ThreadLocalBean.getStepAndInc();
		logEvent.flag = LogFlag.METHOD.getFlag();
		logEvent.msg = args;
		logEvent.className = className;
		logEvent.methodName = methodName;
		logEvent.costTime = costTime;
		setLogIp(logEvent);
		setMDC(logEvent);
		log.info(logEvent.toString());
	}
	
	public static void info(String msg) {
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		LogEvent logEvent = new LogEvent();
//		logEvent.step = ThreadLocalBean.getStepAndInc();
		logEvent.flag = LogFlag.METHOD.getFlag();
		logEvent.msg = msg;
		logEvent.className = stack[2].getClassName();
		logEvent.methodName = stack[2].getMethodName();
		setLogIp(logEvent);
		setMDC(logEvent);
		log.info(logEvent.toString());
	}
}
