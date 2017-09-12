package com.candao.trace.gateway.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.candao.trace.gateway.util.RestUriUtil;
import com.candao.trace.log.ThreadLocalBean;
import com.candao.trace.log.bean.LogEvent;
import com.candao.trace.log.bean.LogFlag;
import com.candao.trace.log.logger.Logger;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class LogPreFilter extends ZuulFilter {
	
	@Override
	public boolean shouldFilter() {
		return true;
	}
	@Override
	public String filterType() {
		return "pre";
	}
	@Override
	public int filterOrder() {
		return 0;
	}
	
	@Override
	public Object run() {
		
		ThreadLocalBean.init();
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest req = ctx.getRequest();
		String uri = req.getRequestURI();
		
		String reqData = getReqData(req);

		System.out.println(new Date());
		System.out.println("req data = " + req.getRequestURL() + "?data=" + reqData);
		
		log(ctx, req, uri, reqData);
		
		return null;
	}
	
	/**
	 * 获取请求数据
	 * @param req
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String getReqData(HttpServletRequest req){
		
		String reqData = "";
		String method = req.getMethod();
		
		if ("post".equalsIgnoreCase(method) || "put".equalsIgnoreCase(method)) {
			StringBuffer sb = new StringBuffer();
			try {
				if (req.getContentLength() > 0) {
					BufferedReader reader = req.getReader();
					String valueString = null;
					while ((valueString = reader.readLine()) != null) {
						sb.append(valueString);
					}
					reqData = sb.toString();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			ThreadLocalBean.setReqData(reqData);
		} else {
			reqData = req.getQueryString();
			if(reqData != null) URLDecoder.decode(reqData);
			ThreadLocalBean.setReqData(req.getParameter("data"));
		}
		
		return reqData;
	}
	
	private void log(RequestContext ctx, HttpServletRequest req, String uri, String reqData) {
		try {
			LogEvent logEvent = new LogEvent();
	//		logEvent.step = ThreadLocalBean.getStepAndInc();
			logEvent.time = System.currentTimeMillis();
			logEvent.clientIp = ctx.getRequest().getRemoteAddr();
			logEvent.flag = LogFlag.REQUEST.getFlag();
			logEvent.msg = reqData;
			logEvent.restUrl = uri.toString();
			String clientStr = RestUriUtil.getClientStr(uri);
			logEvent.clientType = clientStr;
			ThreadLocalBean.setReqTime(logEvent.time);
			if (!req.getRequestURI().startsWith("/log")) {
				Logger.info(logEvent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
