package com.candao.trace.gateway.filter;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;

import com.candao.trace.gateway.util.GsonUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class ErrFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public String filterType() {
		return "error";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		Throwable ex = ctx.getThrowable();
		if (ex != null) {
			LinkedHashMap<String, Object> retMap = new LinkedHashMap<String, Object>();
			retMap.put("status", 2);
			retMap.put("msg", ex);
			try {
				ctx.setResponseDataStream(new ByteArrayInputStream(GsonUtil.gson.toJson(retMap).getBytes("UTF-8")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
