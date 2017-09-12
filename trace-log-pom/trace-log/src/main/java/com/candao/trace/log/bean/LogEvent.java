package com.candao.trace.log.bean;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.Gson;

public class LogEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	public long time;

	/**
	 * traceId，每个请求唯一
	 */
	public String traceId;

	/**
	 * spanId
	 */
	public String spanId;
	
	/**
	 * 产生日志的服务器IP
	 */
	public String logIp;
	
	/**
	 * 用户请求ip
	 */
	public String clientIp;

	/**
	 * 日志所属系统
	 */
	public String sysName;

	/**
	 * 日志标识{LogFlag}
	 */
	public int flag;

	/**
	 * 日志顺序标识
	 */
	public int step;

	/**
	 * 日志产生时间(yyyy-MM-dd hh:mm:ss)
	 */
	public String createTime;
	public Date esCreateTime;

	/**
	 * 产生日志的类
	 */
	public String className;
	/**
	 * 产生日志的方法
	 */
	public String methodName;

	/**
	 * 日志级别 INFO ERROR
	 */
	public String logLever;

	/**
	 * 日志信息
	 */
	public Object msg;

	/**
	 * clientType
	 */
	public String clientType;

	/**
	 * 一次执行耗时（用于打印响应日志）
	 */
	public long costTime;
	
	/**
	 * 是否异常
	 */
	public boolean isErr;
	
	// TODO restUrl
	public String restUrl;
	
	// TODO 异常类名
	public String errName;
	
	// TODO 缓存key
	public String keyOrSql;
	
	// TODO 父spanId
	public String parentSpanId;
	
	// =============================================缓存&DB
	/**
	 * 缓存操作类型{CacheOpt}
	 */
	public byte cacheOpt;
	
	/**
	 * 缓存val大小
	 */
	public int cacheValSize;
	
	/**
	 * 缓存或DB连接数
	 */
	public int connCount;
	
	

//	/**
//	 * 解析Es数据
//	 * @param data
//	 * @return
//	 */
//	public static Log parseEsMap(Map<String, Object> data) {
//		if (data == null || data.isEmpty()) {
//			return null;
//		}
//		Log log = new Log();
//
//		Object type = data.get("type");
//		log.flag = parseFlag(type);
//
//		Object logtime = data.get("logtime");
//		log.createTime = logtime == null ? "" : logtime.toString();
//
//		Object level = data.get("level");
//		log.logLever = level == null ? "" : level.toString();
//
//		Object clientip = data.get("clientip");
//		log.ipAddr = clientip == null ? "" : clientip.toString();
//
//		Object logid = data.get("logid");
//		log.logId = logid == null ? "" : logid.toString();
//
//		Object step = data.get("step");
//		log.step = step == null ? 0 : (Integer) step;
//
//		String methodname = data.get("methodname") == null ? "" : data.get("methodname").toString();
//		if (methodname == null || methodname.isEmpty()) {
//			int index = methodname.lastIndexOf(".");
//			if (index != -1) {
//				log.className = methodname.substring(0, index);
//				log.methodName = methodname.substring(index + 1);
//			}
//		}
//
//		Object system = data.get("system");
//		log.sysName = system == null ? "" : system.toString();
//
//		Object serviceid = data.get("serviceid");
//		log.serviceId = serviceid == null ? 0 : (Integer) serviceid;
//
//		Object actionid = data.get("actionid");
//		log.actionId = actionid == null ? 0 : (Integer) actionid;
//
//		Object clienttype = data.get("clienttype");
//		log.clientType = clienttype == null ? 0 : (Integer) clienttype;
//
//		Object costtime = data.get("costtime");
//		log.costTime = costtime == null ? 0 : (Integer) costtime;
//
//		Object printmsg = data.get("printmsg");
//		log.message = printmsg;
//
//		Object eid = data.get("_id");
//		log.eid = eid == null ? "" : eid.toString();
//
//		return log;
//	}

	@Override
	public String toString() {
		/*
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<<<");
		
//		sb.append("[");
//		sb.append(this.traceId);
//		sb.append("]");
//		sb.append(" ");
//
//		sb.append("[");
//		sb.append(this.spanId);
//		sb.append("]");
//
//		sb.append(" ");

		sb.append("[");
		sb.append(this.logIp);
		sb.append("]");

		sb.append(" ");

		sb.append("[");
		sb.append(this.clientIp);
		sb.append("]");

		sb.append(" ");

//		sb.append("[");
//		sb.append(this.sysName);
//		sb.append("]");

		sb.append(" ");
		
		sb.append("[");
		sb.append(this.flag);
		sb.append("]");
		
		sb.append(" ");
		
		sb.append("[");
		sb.append(this.step);
		sb.append("]");
		
//		sb.append(" ");
		
//		sb.append("[");
//		sb.append(this.createTime);
//		sb.append("]");
		
		
		sb.append(" ");
		sb.append("[");
		sb.append(this.className);
		sb.append(".");
		sb.append(this.methodName);
		sb.append("]");

		sb.append(" ");
		
//		sb.append("[");
//		sb.append(this.logLever);
//		sb.append("]");
		
		sb.append(" ");
		
		sb.append("[");
		sb.append(this.message != null ? this.message.toString() : "");
		sb.append("]");
		
		sb.append(" ");
		
		sb.append("[");
		sb.append(this.clientType);
		sb.append("]");

		sb.append(" ");
		
		sb.append("[");
		sb.append(this.costTime);
		sb.append("]");
		
		sb.append(" ");
		sb.append("[");
		sb.append(this.isErr);
		sb.append("]");
		
		sb.append(" ");
		sb.append("[");
		sb.append(this.cacheOpt);
		sb.append("]");
		
		sb.append(" ");
		sb.append("[");
		sb.append(this.cacheValSize);
		sb.append("]");
		
		sb.append(" ");
		sb.append("[");
		sb.append(this.connCount);
		sb.append("]");

		sb.append(">>>");
		
		return sb.toString();
 */
		Gson gson = new Gson();
		return gson.toJson(gson.toJson(this));
	}
}
