package com.candao.trace.log.logger;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.LoggerFactory;

import com.candao.trace.log.bean.LogEvent;
import com.candao.trace.log.bean.LogFlag;

public class ErrLogger extends BaseLogger{
	
	public static org.slf4j.Logger log = LoggerFactory.getLogger(ErrLogger.class);
	
	public static void error(String keyOrSql, String msg, Throwable t) {
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		LogEvent logEvent = new LogEvent();
		String[] errClassArr = t.getClass().toString().split(" ");
		if (errClassArr.length >= 2) {
			logEvent.errName = t.getClass().toString().split(" ")[1];
		} else {
			logEvent.errName = t.getClass().toString().split(" ")[0];
		}
		logEvent.flag = LogFlag.EXCEPTION.getFlag();
		logEvent.logLever = "ERROR";
		logEvent.keyOrSql = keyOrSql;
		StringWriter writer = new StringWriter();
		t.printStackTrace(new PrintWriter(writer, true));
		String lineSeparator = System.getProperty("line.separator", "\n");
		logEvent.msg = msg == null ? "" : msg.replace(lineSeparator, "</br>") + writer.toString().replace(lineSeparator, "<br/>");
		logEvent.className = stack[2].getClassName();
		logEvent.methodName = stack[2].getMethodName();
		setLogIp(logEvent);
		setMDC(logEvent);
		log.error(logEvent.toString());
	}
	
	public static void error(String msg, Throwable t) {
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		LogEvent logEvent = new LogEvent();
		String[] errClassArr = t.getClass().toString().split(" ");
		if (errClassArr.length >= 2) {
			logEvent.errName = t.getClass().toString().split(" ")[1];
		} else {
			logEvent.errName = t.getClass().toString().split(" ")[0];
		}
		logEvent.flag = LogFlag.EXCEPTION.getFlag();
		logEvent.logLever = "ERROR";
		StringWriter writer = new StringWriter();
		t.printStackTrace(new PrintWriter(writer, true));
		String lineSeparator = System.getProperty("line.separator", "\n");
		logEvent.msg = msg == null ? "" : msg.replace(lineSeparator, "<br/>") + writer.toString().replace(lineSeparator, "<br/>");
		logEvent.className = stack[2].getClassName();
		logEvent.methodName = stack[2].getMethodName();
		setLogIp(logEvent);
		setMDC(logEvent);
		log.error(logEvent.toString());
	}
	
	public static void error(Throwable t) {
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		LogEvent logEvent = new LogEvent();
		String[] errClassArr = t.getClass().toString().split(" ");
		if (errClassArr.length >= 2) {
			logEvent.errName = t.getClass().toString().split(" ")[1];
		} else {
			logEvent.errName = t.getClass().toString().split(" ")[0];
		}
		logEvent.flag = LogFlag.EXCEPTION.getFlag();
		logEvent.logLever = "ERROR";
		StringWriter writer = new StringWriter();
		t.printStackTrace(new PrintWriter(writer, true));
		String lineSeparator = System.getProperty("line.separator", "\n");
		logEvent.msg = writer.toString().replace(lineSeparator, "<br/>");
		logEvent.className = stack[2].getClassName();
		logEvent.methodName = stack[2].getMethodName();
		setLogIp(logEvent);
		setMDC(logEvent);
		log.error(logEvent.toString());
	}
}
