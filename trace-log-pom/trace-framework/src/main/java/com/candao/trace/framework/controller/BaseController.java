package com.candao.trace.framework.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.candao.trace.framework.annotation.JsonExclusionStrategys;
import com.candao.trace.framework.bean.FastDateFormat;
import com.candao.trace.framework.bean.response.RspData;
import com.candao.trace.framework.bean.response.RspStatus;
import com.candao.trace.framework.constants.OperateTriggerType;
import com.candao.trace.framework.jpa.BaseIdEntity;
import com.candao.trace.framework.spring.bean.FastActionContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 基础Controller<br/>
 * 对外的controller继承该类
 * 
 */
public class BaseController {
	
	// .serializeNulls() 设置null 键名返回
	// 序列化时排除有@JsonExcludable注释的字段
	protected static final Gson gson = new GsonBuilder().setExclusionStrategies(JsonExclusionStrategys.skipFieldExclusionStrategy).disableHtmlEscaping()
			.setDateFormat(FastDateFormat.DATE_TIME.value).create();

	protected String toJson(Object src) {
		return gson.toJson(src);
	}

	// **********************************http常用数据返回方法***************************************
	/**
	 * 返回一条消息给客户端，消息内容为单个boolean
	 */
	protected String retBoolString(boolean value) throws Exception {
		return String.valueOf(value);
	}

	/**
	 * 返回一个成功的RspData
	 * @param data-具体响应数据
	 * @return
	 */
	protected RspData retSuccessRspData(Object data) {
		return new RspData(data);
	}

	/**
	 * 返回一个无具体数据的成功的RspData<br/>
	 * @return
	 */
	protected RspData retSuccessRspData() {
		return new RspData();
	}

	/**
	 * 返回一个String类型成功的 rspDate
	 * @param data
	 * @return
	 */
	protected RspData retSuccessStrDate(String data){
		return new RspData(data);
	}

	/**
	 * 返回一个无具体数据的成功的RspData<br/>
	 * @return
	 */
	protected RspData retSuccessRspData(String msg) {
		RspData rspData = new RspData();
		rspData.setStatus(RspStatus.SUCCESS, msg);
		return rspData;
	}

	/**
	 * 返回一个失败的RspData<br/>
	 * 默认为RspStatus.FAIL失败类型
	 * @param msg-自定义失败信息
	 * @return
	 */
	protected RspData retFailRspData(String msg) {
		RspData rspData = new RspData();
		rspData.setStatus(RspStatus.FAIL, msg);
		return rspData;
	}

	/**
	 * 返回一个失败的RspData<br/>
	 * @param status-自定义状态
	 * @param msg-自定义信息
	 * @return
	 */
	protected RspData retFailRspData(int status, String msg) {
		RspData rspData = new RspData();
		rspData.status = status;
		rspData.msg = msg;
		return rspData;
	}

	/**
	 * 返回一个失败的RspData<br/>
	 * @param rspStatus-{link RspStatus}
	 * @return
	 */
	protected RspData retFailRspData(RspStatus rspStatus) {
		RspData rspData = new RspData();
		rspData.setStatus(rspStatus);
		return rspData;
	}

	/**
	 * 返回一个自定义的RspData
	 * @param status-响应状态
	 * @param msg-响应信息
	 * @param data-具体响应数据
	 * @return
	 */
	protected RspData retRspData(int status, String msg, Object data) {
		RspData rspData = new RspData();
		rspData.status = status;
		rspData.msg = msg;
		rspData.data = data;
		return rspData;
	}


	/**
	 * 根据字符串 regex 分割符号，组装List集合，用于ID查询
	 * @param str
	 * @param regex
	 * @return
	 */
	protected List<Integer> regexString(String str, String regex){
		String[] strArray = str.split(regex);
		List<Integer> list = new ArrayList<>();
		for (String s : strArray) {
			 list.add(Integer.parseInt(s));
		}
		return list;
	}
	
	public HttpServletResponse getResponse() {
		
		FastActionContext ctx = FastActionContext.getActionContext();
		
		return ctx != null ? ctx.getResponse() : null;
	}
	
	/**
	 * 获取http请求对象
	 * @return
	 */
	public HttpServletRequest getRequest(){
		
		FastActionContext ctx = FastActionContext.getActionContext();
		
		return ctx != null ? ctx.getRequest() : null;
	}
	
	/**
	 * 根据ClientType获取当前操作触发方
	 * @return
	 */
	public OperateTriggerType getCurrentOperateTriggerType(){
		
		return OperateTriggerType.get(getCurrentClientType());
	}
	
	/**
     * 获取当前客户端请求ClientType
     * @return
     */
    public int getCurrentClientType(){
    	
    	FastActionContext ctx = FastActionContext.getActionContext();
    	
    	return ctx != null ? ctx.getCurrentClientType() : 0;
    }
    
    /**
     * 获取当前用户token
     * @return
     */
    public String getCurrentToken(){
    	
    	FastActionContext ctx = FastActionContext.getActionContext();
    	
    	return ctx != null ? ctx.getCurrentToken() : null;
    }
    
    /**
     * 获取当前用户所属运营商ID
     * @return
     */
    public int getCurrentPlatformId(){
    	
    	FastActionContext ctx = FastActionContext.getActionContext();
    	
    	return ctx != null ? ctx.getCurrentPlatformId() : 0;
    }
    
    /**
     * 获取当前用户ID
     * @return
     */
    public int getCurrentAccountId(){
    	
    	FastActionContext ctx = FastActionContext.getActionContext();
    	
    	return ctx != null ? ctx.getCurrentAccountId() : 0;
    }
    
    /**
     * 获取当前用户登录名
     * @return
     */
    public String getCurrentLoginName(){
    	
    	FastActionContext ctx = FastActionContext.getActionContext();
    	
    	return ctx != null ? ctx.getCurrentLoginName() : null;
    }
    
    /**
     * 获取当前用户运营商编码
     * @return
     */
    public String getCurrentPlatformCode(){
    	
    	FastActionContext ctx = FastActionContext.getActionContext();
    	
    	return ctx != null ? ctx.getCurrentPlatformCode() : null;
    }
	
    /**
     * 获取当前账号单个门店ID
     * @return
     */
    public Integer getCurrentOnlyStoreId(){
    	
    	FastActionContext ctx = FastActionContext.getActionContext();
    	
    	return ctx != null ? ctx.getCurrentOnlyStoreId() : null;
    }
    
    /**
     * 获取当前用户所关联门店ID
     * @return
     */
    public List<Integer> getCurrentStoreIds(){
    	
    	FastActionContext ctx = FastActionContext.getActionContext();
    	
    	return ctx != null ? ctx.getCurrentStoreIds() : null;
    }
    
    /**
     * 获取当前用户所关联组织ID
     * @return
     */
    public List<Integer> getCurrentOrgIds(){
    	
    	FastActionContext ctx = FastActionContext.getActionContext();
	
    	return ctx != null ? ctx.getCurrentOrgIds() : null;
    }
    
    /**
	 * 是否为系统运营商
	 * @param platformId
	 * @return
	 */
	public static boolean isSysPlatform(Integer platformId){
		
		return BaseIdEntity.isSysPlatform(platformId);
	}
	
	/**
	 * 是否为系统运营商
	 * @return
	 */
	public static boolean isSysPlatform(){
		
		return BaseIdEntity.isSysPlatform();
	}
}
