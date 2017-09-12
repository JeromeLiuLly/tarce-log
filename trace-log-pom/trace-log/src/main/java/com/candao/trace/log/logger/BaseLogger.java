package com.candao.trace.log.logger;




import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.MDC;

import com.candao.trace.log.ThreadLocalBean;
import com.candao.trace.log.bean.LogEvent;

public class BaseLogger {
	public static void setMDC(LogEvent logEvent) {
		MDC.put("cacheOpt", "" + logEvent.cacheOpt);
		MDC.put("cacheValSize", "" + logEvent.cacheValSize);
		MDC.put("className", logEvent.className == null ? "" : logEvent.className);
		MDC.put("clientIp", logEvent.clientIp);
		MDC.put("clientType", logEvent.clientType==null?"":logEvent.clientType);
		MDC.put("connCount", "" + logEvent.connCount);
		MDC.put("costTime", "" + logEvent.costTime);
		MDC.put("isErr", "" + logEvent.isErr);
		MDC.put("errName", logEvent.errName == null ? "" : logEvent.errName);
		MDC.put("flag", "" + logEvent.flag);
		MDC.put("ip", logEvent.logIp == null ? "" : logEvent.logIp);
		MDC.put("keyOrSql", logEvent.keyOrSql == null ? "" : logEvent.keyOrSql);
		MDC.put("methodName", logEvent.methodName == null ? "" : logEvent.methodName);
		MDC.put("restUrl", logEvent.restUrl == null ? "" : logEvent.restUrl);
		MDC.put("step", "" + logEvent.step);
		String mesg = "";
		if (logEvent.msg != null) {
			mesg = logEvent.msg.toString();
			mesg = mesg.replace("\\", "\\\\");
			mesg = mesg.replace("\"", "\\\"");
		}
		String traceId = MDC.get("X-B3-TraceId");
		if (traceId == null || "".equals(traceId)) {
			MDC.put("X-B3-TraceId", ThreadLocalBean.getTraceId());
		}
		MDC.put("mesg", mesg);
	}
	
	public static void setLogIp(LogEvent logEvent) {
		try {
			logEvent.logIp = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
}
