package com.candao.trace.log.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CommonRequestParam implements Serializable{

	private static final long serialVersionUID = -8784702344417549068L;
	private static final String k_platformCode = "platformCode";
	private static final String k_platformId = "platformId";
	private static final String k_id = "id";
	private static final String k_loginName = "loginName";
	private static final String k_storeIds = "storeIds";
	private static final String k_orgIds = "orgIds";

	public static final String common_param_head_key = "common_param_head_key";
	
	// 公共参数数据
	public int langType;
	public int clientType;
	public String token;
	
	// 日志ID
	public String traceId;
	
	// 当前登录账号数据
	public String platformCode;
	public int platformId;
	public int accountId;
	public String loginName;
	
	public List<Integer> storeIds;
	public List<Integer> orgIds;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean setAccountData(Map map){
		
		if(map == null || map.isEmpty()) return false;
		
		if (map.containsKey(k_platformCode)) {
			this.platformCode = (String) map.get(k_platformCode);
		}
		if (map.containsKey(k_platformId)) {
			this.platformId = (int) map.get(k_platformId);
		}
		if (map.containsKey(k_id)) {
			this.accountId = (int) map.get(k_id);
		}
		if (map.containsKey(k_loginName)) {
			this.loginName = (String) map.get(k_loginName);
		}
		if (map.containsKey(k_storeIds)) {
			this.storeIds = (List<Integer>) map.get(k_storeIds);
		}
		if (map.containsKey(k_orgIds)) {
			this.orgIds = (List<Integer>) map.get(k_orgIds);
		}
		
		return true;
	}
}
