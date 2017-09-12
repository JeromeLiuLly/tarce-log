package com.candao.trace.framework.constants;

import java.util.HashMap;
import java.util.Map;

import com.candao.trace.framework.spring.bean.FastActionContext;
import com.candao.trace.framework.util.FastStringUtils;

/**
 * 操作触发方
 * 
 */
public enum OperateTriggerType {

	SYSTEM(0, "系统"),
	FOO_ACCOUNT(1, "管理账号"),
	SHOP_ACCOUNT(2, "商家账号"),
	HORSEMAN_APP_IOS(3, "骑手APP-ios"),
	HORSEMAN_APP_ANDROID(4, "骑手APP-android"),
	EXP_RIDER(5, "第三方配送"),
	EXT_ECP(6, "ECP");
	
	private static final String orderOperatorMsg_tpl = "【${operator}】${msg}";
	
	public int value;
	public String valueName;
	
	private OperateTriggerType(int value, String valueName){
		this.value = value;
		this.valueName = valueName;
	}
	
	/**
	 * 获取操作人
	 * @return
	 */
	public String getOrderOperator(){
		
		if(this == SHOP_ACCOUNT) return getCurrentLoginName();
		if(this == FOO_ACCOUNT) return getCurrentLoginName();
		
		return this.valueName;
	}
	
	/**
	 * 构建操作人信息
	 * @param msg
	 * @return
	 */
	public String builderOrderOperator(String msg){
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("operator", getOrderOperator());
		paramMap.put("msg", msg);
		
		return FastStringUtils.$format(orderOperatorMsg_tpl, paramMap);
	}
	
	private String getCurrentLoginName(){
		
		String r = FastStringUtils.EMPTY;
		
		FastActionContext atx = FastActionContext.getActionContext();
		if(atx != null) r = atx.getCurrentLoginName();
		
		return r;
	}
	
	public static OperateTriggerType get(int clientType){
		OperateTriggerType[] values = OperateTriggerType.values();
		for(OperateTriggerType t : values){
			if(t.value == clientType) return t;
		}
		return getDefault();
	}
	
	/**
	 * 获取默认触操作触发方
	 * @return
	 */
	public static OperateTriggerType getDefault(){
		return SYSTEM;
	}
	
}
