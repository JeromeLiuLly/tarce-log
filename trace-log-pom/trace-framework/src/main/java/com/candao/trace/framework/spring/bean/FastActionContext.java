package com.candao.trace.framework.spring.bean;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.MDC;

import com.alibaba.fastjson.JSONObject;
import com.candao.trace.framework.util.FastStringUtils;
import com.candao.trace.log.bean.CommonRequestParam;

/**
 * 当前线程存储数据ActionContext
 * 
 */
public class FastActionContext {

	private static final ThreadLocal<FastActionContext> actionContextThreadLocal = new ThreadLocal<FastActionContext>();
	
	/**
     * 当前对象只给自己实例
     */
    private FastActionContext(){}
	
    private HttpServletRequest request;
    private HttpServletResponse response;
    private CommonRequestParam commonRequestParam;
    
    public static FastActionContext getActionContext() {
        return actionContextThreadLocal.get();
    }
    
    public static void createActionContext(HttpServletRequest request, HttpServletResponse response){
    	
    	FastActionContext ctx = new FastActionContext();
        
    	ctx.request = request;
        ctx.response = response;
        
        if(request != null){
        	String headerValue = request.getHeader(CommonRequestParam.common_param_head_key);
        	if(FastStringUtils.isNotEmpty(headerValue)){
        		ctx.commonRequestParam = JSONObject.parseObject(headerValue, CommonRequestParam.class);
        	}
        }
        
        actionContextThreadLocal.set(ctx);
        try {
	        String traceId = MDC.get("X-B3-TraceId");
			if(null == traceId || "".equals(traceId)) {
				MDC.put("X-B3-TraceId", ctx.getTraceId());
			}
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    public static void destroyActionContext() {
        actionContextThreadLocal.remove();
    }

    /**
     * 获取当前客户端请求ClientType
     * @return
     */
    public int getCurrentClientType(){
    	return commonRequestParam != null ? commonRequestParam.clientType : 0;
    }
    
    /**
     * 获取当前用户token
     * @return
     */
    public String getCurrentToken(){
    	return commonRequestParam != null ? commonRequestParam.token : FastStringUtils.EMPTY;
    }
    
    /**
     * 获取当前用户所属运营商ID
     * @return
     */
    public int getCurrentPlatformId(){
    	return commonRequestParam != null ? commonRequestParam.platformId : -1;
    }
    
    /**
     * 获取当前用户ID
     * @return
     */
    public int getCurrentAccountId(){
    	return commonRequestParam != null ? commonRequestParam.accountId : 0;
    }
    
    /**
     * 获取当前用户登录名
     * @return
     */
    public String getCurrentLoginName(){
    	return commonRequestParam != null ? commonRequestParam.loginName : FastStringUtils.EMPTY;
    }
    
    /**
     * 获取当前账号单个门店ID
     * @return
     */
    public Integer getCurrentOnlyStoreId(){
    	
    	List<Integer> currentStoreIds = getCurrentStoreIds();
    	
    	return FastStringUtils.isNotEmpty(currentStoreIds) ? currentStoreIds.get(0) : 0;
    }
    
    /**
     * 获取当前用户所关联门店ID
     * @return
     */
    public List<Integer> getCurrentStoreIds(){
    	return commonRequestParam != null ? commonRequestParam.storeIds : new ArrayList<Integer>();
    }
    
    /**
     * 获取当前用户所关联组织ID
     * @return
     */
    public List<Integer> getCurrentOrgIds(){
    	return commonRequestParam != null ? commonRequestParam.orgIds : new ArrayList<Integer>();
    }
    
    /**
     * 获取当前用户运营商编码
     * @return
     */
    public String getCurrentPlatformCode(){
    	return commonRequestParam != null ? commonRequestParam.platformCode : FastStringUtils.EMPTY;
    }
    
    public String getTraceId(){
    	return commonRequestParam != null ? commonRequestParam.traceId : FastStringUtils.EMPTY;    	
    }
    
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public HttpSession getHttpSession() {
        return request == null ? null : request.getSession();
    }

	public CommonRequestParam getCommonRequestParam() {
		return commonRequestParam;
	}

	public void setCommonRequestParam(CommonRequestParam commonRequestParam) {
		this.commonRequestParam = commonRequestParam;
	}
	
}
