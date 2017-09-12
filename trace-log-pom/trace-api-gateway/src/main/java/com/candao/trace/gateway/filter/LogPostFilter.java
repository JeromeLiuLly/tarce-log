package com.candao.trace.gateway.filter;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.ROUTING_DEBUG_KEY;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.X_ZUUL_DEBUG_HEADER;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;

import com.candao.trace.gateway.exception.TokenException;
import com.candao.trace.gateway.util.GsonUtil;
import com.candao.trace.gateway.util.RestUriUtil;
import com.candao.trace.log.ThreadLocalBean;
import com.candao.trace.log.bean.LogEvent;
import com.candao.trace.log.bean.LogFlag;
import com.candao.trace.log.logger.Logger;
import com.google.common.reflect.TypeToken;
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.util.Pair;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.constants.ZuulConstants;
import com.netflix.zuul.constants.ZuulHeaders;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.netflix.zuul.util.HTTPRequestUtils;

public class LogPostFilter extends ZuulFilter {
	
	private static DynamicIntProperty INITIAL_STREAM_BUFFER_SIZE = DynamicPropertyFactory
			.getInstance()
			.getIntProperty(ZuulConstants.ZUUL_INITIAL_STREAM_BUFFER_SIZE, 8192);
	
	private ThreadLocal<byte[]> buffers = new ThreadLocal<byte[]>() {
		@Override
		protected byte[] initialValue() {
			return new byte[INITIAL_STREAM_BUFFER_SIZE.get()];
		}
	};
	
	private static DynamicBooleanProperty INCLUDE_DEBUG_HEADER = DynamicPropertyFactory
			.getInstance()
			.getBooleanProperty(ZuulConstants.ZUUL_INCLUDE_DEBUG_HEADER, false);
	
	private static DynamicBooleanProperty SET_CONTENT_LENGTH = DynamicPropertyFactory
			.getInstance()
			.getBooleanProperty(ZuulConstants.ZUUL_SET_CONTENT_LENGTH, false);
	
	private boolean useServlet31 = true;

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@SuppressWarnings("serial")
	@Override
	public Object run() {
//		System.out.println("LogPostFilter run");
		RequestContext ctx = RequestContext.getCurrentContext();
		ctx.getResponse().setCharacterEncoding("UTF-8");
		boolean isErr = false;
		String errName = "";
		String retStr = "";
		
		// 异常
		Throwable ex = ctx.getThrowable();
		
		String callback = ctx.getRequest().getParameter("callback");
		
		// 先判断zuul是否有异常抛出
		if (ex != null) {
			errName = ex.getClass().getName();
			isErr = true;
			LinkedHashMap<String, Object> retMap = new LinkedHashMap<String, Object>();
			retMap.put("status", 2);
			ZuulException zuulException = findZuulException(ex);
			zuulException.printStackTrace();
			// TODO 这里考虑要不要返回最底层的异常
			Throwable causeEx = zuulException.getCause();
			if (causeEx != null) {
				if (causeEx instanceof TokenException) {
					retMap.put("msg", causeEx.getMessage());
					retMap.put("status", 3);
				} else {
					retMap.put("msg", causeEx.toString());
				}
			} else {
				retMap.put("msg", zuulException.toString());
			}
			retStr = GsonUtil.gson.toJson(retMap);
		} else {
			InputStream in = ctx.getResponseDataStream();
			StringBuffer sb = new StringBuffer();
			if (in != null) {
				byte[] b = new byte[4096];
				try {
					for (int n; (n = in.read(b)) != -1;) {
						sb.append(new String(b, 0, n));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			Map<String, Object> map = null;
			boolean isJson = false;
			
			if(sb.length() > 0){
				try {
					map = GsonUtil.gson.fromJson(sb.toString(), new TypeToken<LinkedHashMap<String, Object>>(){}.getType());
					isJson = true;
				} catch (Exception e) {
					map = new LinkedHashMap<String, Object>();
				}
			}
			
			isErr = isJson && map.containsKey("exception");
			
			if (isErr) { // 异常
				ctx.getResponse().setStatus(200);
				errName = map.get("exception").toString();
				LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
				result.put("status", 2);
				result.put("msg", map.get("exception") + ": " + map.get("message"));
				retStr = GsonUtil.gson.toJson(result);
			} else { // 正常
				if (isJson) {
					retStr = GsonUtil.gson.toJson(map);//sb.toString();
				} else {
					retStr = sb.toString();
				}
			}
		}
		try {
			System.out.println("rsp data = " + retStr);
			if (callback != null && !"".equals(callback)) {
				retStr = callback + "(" + retStr + ")";
			}
			if (ex != null) {
				ctx.setResponseBody(retStr);
				try {
					OutputStream outStream = ctx.getResponse().getOutputStream();
					if (RequestContext.getCurrentContext().getResponseBody() != null) {
						String body = RequestContext.getCurrentContext().getResponseBody();
						writeResponse(
								new ByteArrayInputStream(
										body.getBytes(ctx.getResponse().getCharacterEncoding())),
										outStream);
//						addResponseHeaders();
//						writeResponse();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				ctx.setResponseDataStream(new ByteArrayInputStream(retStr.getBytes("UTF-8")));
				addResponseHeaders();
				writeResponse();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		HttpServletRequest req = ctx.getRequest();
		
		log(isErr, errName, retStr, req);
		return null;
	}

	private void log(boolean isErr, String errName, String retStr, HttpServletRequest req) {
		try {
			long reqTime = ThreadLocalBean.getReqTime();
			LogEvent logEvent = new LogEvent();
			logEvent.flag = LogFlag.RESPONSE.getFlag();
			logEvent.clientIp = req.getRemoteAddr();
			logEvent.costTime = System.currentTimeMillis() - reqTime;
			logEvent.msg = retStr;
			logEvent.isErr = isErr;
			String uri = req.getRequestURI();
			logEvent.restUrl = uri.toString();
			logEvent.clientType = RestUriUtil.getClientStr(uri);
			logEvent.errName = errName;
	//		logEvent.step = ThreadLocalBean.getStepAndInc();
			if (!req.getRequestURI().startsWith("/log")) {
				Logger.info(logEvent);
			}
			ThreadLocalBean.remove();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void writeResponse(InputStream zin, OutputStream out) throws Exception {
		byte[] bytes = buffers.get();
		int bytesRead = -1;
		while ((bytesRead = zin.read(bytes)) != -1) {
			out.write(bytes, 0, bytesRead);
		}
	}
	
	ZuulException findZuulException(Throwable throwable) {
		if (throwable.getCause() instanceof ZuulRuntimeException) {
			// this was a failure initiated by one of the local filters
			return (ZuulException) throwable.getCause().getCause();
		}

		if (throwable.getCause() instanceof ZuulException) {
			// wrapped zuul exception
			return (ZuulException) throwable.getCause();
		}

		if (throwable instanceof ZuulException) {
			// exception thrown by zuul lifecycle
			return (ZuulException) throwable;
		}

		// fallback, should never get here
		return new ZuulException(throwable, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, null);
	}
	
	private void writeResponse() throws Exception {
		RequestContext context = RequestContext.getCurrentContext();
		// there is no body to send
		if (context.getResponseBody() == null
				&& context.getResponseDataStream() == null) {
			return;
		}
		HttpServletResponse servletResponse = context.getResponse();
		if (servletResponse.getCharacterEncoding() == null) { // only set if not set
			servletResponse.setCharacterEncoding("UTF-8");
		}
		OutputStream outStream = servletResponse.getOutputStream();
		InputStream is = null;
		try {
			if (RequestContext.getCurrentContext().getResponseBody() != null) {
				String body = RequestContext.getCurrentContext().getResponseBody();
				writeResponse(
						new ByteArrayInputStream(
								body.getBytes(servletResponse.getCharacterEncoding())),
						outStream);
				return;
			}
			boolean isGzipRequested = false;
			final String requestEncoding = context.getRequest()
					.getHeader(ZuulHeaders.ACCEPT_ENCODING);

			if (requestEncoding != null
					&& HTTPRequestUtils.getInstance().isGzipped(requestEncoding)) {
				isGzipRequested = true;
			}
			is = context.getResponseDataStream();
			InputStream inputStream = is;
			if (is != null) {
				if (context.sendZuulResponse()) {
					// if origin response is gzipped, and client has not requested gzip,
					// decompress stream
					// before sending to client
					// else, stream gzip directly to client
					if (context.getResponseGZipped() && !isGzipRequested) {
						// If origin tell it's GZipped but the content is ZERO bytes,
						// don't try to uncompress
						final Long len = context.getOriginContentLength();
						if (len == null || len > 0) {
							try {
								inputStream = new GZIPInputStream(is);
							}
							catch (java.util.zip.ZipException ex) {
//								log.debug(
//										"gzip expected but not "
//												+ "received assuming unencoded response "
//												+ RequestContext.getCurrentContext()
//												.getRequest().getRequestURL()
//												.toString());
								inputStream = is;
							}
						}
						else {
							// Already done : inputStream = is;
						}
					}
					else if (context.getResponseGZipped() && isGzipRequested) {
						servletResponse.setHeader(ZuulHeaders.CONTENT_ENCODING, "gzip");
					}
					writeResponse(inputStream, outStream);
				}
			}
		}
		finally {
			/**
			* Closing the wrapping InputStream itself has no effect on closing the underlying tcp connection since it's a wrapped stream. I guess for http
			* keep-alive. When closing the wrapping stream it tries to reach the end of the current request, which is impossible for infinite http streams. So
			* instead of closing the InputStream we close the HTTP response.
			*
			* @author Johannes Edmeier
			*/
			try {
				Object zuulResponse = RequestContext.getCurrentContext()
						.get("zuulResponse");
				if (zuulResponse instanceof Closeable) {
					((Closeable) zuulResponse).close();
				}
				outStream.flush();
				// The container will close the stream for us
			}
			catch (IOException ex) {
//			log.warn("Error while sending response to client: " + ex.getMessage());
			}
		}
	}
	
	private void addResponseHeaders() {
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletResponse servletResponse = context.getResponse();
		if (INCLUDE_DEBUG_HEADER.get()) {
			@SuppressWarnings("unchecked")
			List<String> rd = (List<String>) context.get(ROUTING_DEBUG_KEY);
			if (rd != null) {
				StringBuilder debugHeader = new StringBuilder();
				for (String it : rd) {
					debugHeader.append("[[[" + it + "]]]");
				}
				servletResponse.addHeader(X_ZUUL_DEBUG_HEADER, debugHeader.toString());
			}
		}
		List<Pair<String, String>> zuulResponseHeaders = context.getZuulResponseHeaders();
		if (zuulResponseHeaders != null) {
			for (Pair<String, String> it : zuulResponseHeaders) {
				servletResponse.addHeader(it.first(), it.second());
			}
		}
		// Only inserts Content-Length if origin provides it and origin response is not
		// gzipped
		if (SET_CONTENT_LENGTH.get()) {
			Long contentLength = context.getOriginContentLength();
			if ( contentLength != null && !context.getResponseGZipped()) {
				if(useServlet31) {
					servletResponse.setContentLengthLong(contentLength);
				} else {
					//Try and set some kind of content length if we can safely convert the Long to an int
					if (isLongSafe(contentLength)) {
						servletResponse.setContentLength(contentLength.intValue());
					}
				}
			}
		}
	}
	
	private boolean isLongSafe(long value) {
		return value <= Integer.MAX_VALUE && value >= Integer.MIN_VALUE;
	}
}
