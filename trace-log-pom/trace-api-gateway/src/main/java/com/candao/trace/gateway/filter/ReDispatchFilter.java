package com.candao.trace.gateway.filter;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.REQUEST_ENTITY_KEY;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.RETRYABLE_KEY;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.ROUTE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_ID_KEY;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.support.RibbonRequestCustomizer;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonRoutingFilter;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.MultiValueMap;

import com.alibaba.fastjson.JSON;
import com.candao.trace.gateway.exception.TokenException;
import com.candao.trace.gateway.http.HttpService;
import com.candao.trace.gateway.util.GsonUtil;
import com.candao.trace.log.ThreadLocalBean;
import com.candao.trace.log.bean.CommonRequestParam;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.netflix.zuul.http.ServletInputStreamWrapper;
public class ReDispatchFilter extends RibbonRoutingFilter {

	private boolean useServlet31 = true;
	
	@Autowired
	public HttpService httpService;
	@Autowired
	public Tracer tracer;
	
	@SuppressWarnings("rawtypes")
	public ReDispatchFilter(ProxyRequestHelper helper, RibbonCommandFactory<?> ribbonCommandFactory, List<RibbonRequestCustomizer> requestCustomizers) {
		super(helper, ribbonCommandFactory, requestCustomizers);
		try {
			HttpServletRequest.class.getMethod("getContentLengthLong");
		} catch(NoSuchMethodException e) {
			useServlet31 = false;
		}
	}

	public ReDispatchFilter(RibbonCommandFactory<?> ribbonCommandFactory) {
		this(new ProxyRequestHelper(), ribbonCommandFactory, null);
	}
	
	@Override
	public String filterType() {
		return ROUTE_TYPE;
	}

	@Override
	public Object run() {
		
		RequestContext context = RequestContext.getCurrentContext();
		String requestURI = context.getRequest().getRequestURI();
		
		// 第三方请求
		if (requestURI.startsWith("/external/")) {
			// ecp请求特殊处理
			if(requestURI.startsWith("/external/v1")){
				try{
					return super.run();
				}catch(Exception e){
					context.getResponse().addHeader("resultCode", "-1011");
					try {
						context.getResponse().sendError(503);
					} catch (IOException e1) {
					}
				}
			}
		}
		
		this.helper.addIgnoredHeaders();
		
		try {
			RibbonCommandContext commandContext = buildCommandContext(context);
			ClientHttpResponse response = forward(commandContext);
			setResponse(response);
			return response;
		}
		catch (ZuulException ex) {
			throw new ZuulRuntimeException(ex);
		}
		catch (Exception ex) {
			throw new ZuulRuntimeException(ex);
		}
	}

	protected InputStream getRequestBody(HttpServletRequest request) {
		InputStream requestEntity = null;
		try {
			requestEntity = (InputStream) RequestContext.getCurrentContext()
					.get(REQUEST_ENTITY_KEY);
			if (requestEntity == null) {
				requestEntity = request.getInputStream();
				if ("GET".equalsIgnoreCase(request.getMethod()) && request.getParameter("data") != null) {
					requestEntity = new ServletInputStreamWrapper(request.getParameter("data").getBytes());
				}
			}
		}
		catch (IOException ex) {
//			log.error("Error during getRequestBody", ex);
			ex.printStackTrace();
		}
		return requestEntity;
	}
	
	protected RibbonCommandContext buildCommandContext(RequestContext context) {
		
		HttpServletRequest request = context.getRequest();
		MultiValueMap<String, String> headers = this.helper.buildZuulRequestHeaders(request);
		MultiValueMap<String, String> params = this.helper.buildZuulRequestQueryParams(request);
		String verb = getVerb(request);
		
		boolean isTurnToPost = false;
		if (verb.equalsIgnoreCase("GET") && request.getParameter("data") != null) {
			isTurnToPost = true;
			headers.add("content-type", "application/json");
			verb = "POST";
		}
		
		String uri = this.helper.buildZuulRequestURI(request);
		
		try {
			
			Map<String, Object> reqData = ThreadLocalBean.getReqData();
			
			CommonRequestParam crp = JSON.parseObject(JSON.toJSONString(reqData), CommonRequestParam.class);
			
			if (crp != null) {
				
				crp.traceId = tracer.getCurrentSpan().traceIdString();
				
				if (crp.clientType > 0 && !httpService.isIgnoreToken(request.getRequestURI())) {
					
					// 将用户信息设到头部
					if (crp.token != null && !"".equals(crp.token)) {
						
						@SuppressWarnings({"rawtypes"})
						Map map = httpService.getAccountByToken(crp.token, crp.clientType);
						
						boolean setAccountData = crp.setAccountData(map);
						
						System.out.println("map from getAccountByToken = " + GsonUtil.gson.toJson(map));
						
						if (!setAccountData)  throw new TokenException("token失效");
						
					} else {
						throw new TokenException("token失效");
					}
				}
				headers.add(CommonRequestParam.common_param_head_key, GsonUtil.gson.toJson(crp));
			}
		} catch (TokenException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
//		long contentLength = -1;
		InputStream requestEntity = getRequestBody(request);
		if (request.getContentLength() < 0 && !verb.equalsIgnoreCase("GET")) {
			context.setChunkedRequestBody();
		}

		String serviceId = (String) context.get(SERVICE_ID_KEY);
		Boolean retryable = (Boolean) context.get(RETRYABLE_KEY);

//		String[] restArrays = uri.split("/");
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < restArrays.length; i++) {
//			sb.append("/");
//			sb.append(restArrays[i]);
//			if (i==0) {
//				sb.append("/");
//				sb.append("app");
//			}
//		}
//		uri = sb.toString();
		uri = uri.replace("//", "/");

		long contentLength = useServlet31 ? request.getContentLengthLong(): request.getContentLength();
		if (isTurnToPost) {
			contentLength = request.getParameter("data").getBytes().length;
		}

		return new RibbonCommandContext(serviceId, verb, uri, retryable, headers, params,
				requestEntity, this.requestCustomizers, contentLength);
	}
	
}
