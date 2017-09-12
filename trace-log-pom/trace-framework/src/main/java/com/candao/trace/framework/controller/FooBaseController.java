package com.candao.trace.framework.controller;

import java.util.List;

import com.candao.trace.framework.bean.DataStatus;
import com.candao.trace.framework.bean.response.RspData;

/**
 * FOO基础控制层（集成一些公共方法）
 *
 */
public class FooBaseController extends BaseController{

	/**
	 * 检查ids参数
	 * @param ids
	 * @return
	 */
	public RspData checkIds(List<Integer> ids){
		
		if(ids == null || ids.isEmpty()){
			return retFailRspData("ids is empty!");
		}
		
		return null;
	}
	
	/**
	 * 检查数据状态参数是否合法
	 * @param status
	 * @return
	 */
	public RspData checkStatus(int status){
		DataStatus dataStatus = DataStatus.getDataStatus(status);
		if(dataStatus == null){
			return retFailRspData("unavailable state!");
		}
		return null;
	}
	
	/**
	 * 检查ids及数据状态
	 * @param ids
	 * @param status
	 * @return
	 */
	public RspData checkIdsAndStatus(List<Integer> ids, int status){
		
		RspData r = checkIds(ids);
		if(r != null) return r;
		
		r = checkStatus(status);
		if(r != null) return r;
		
		return null;
	}
	
}
