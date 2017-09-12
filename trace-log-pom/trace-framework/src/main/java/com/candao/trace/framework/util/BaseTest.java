package com.candao.trace.framework.util;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.candao.trace.framework.bean.request.ReqFooData;
import com.candao.trace.framework.bean.response.RspData;

/**
 * 测试父类
 */
public class BaseTest {

	public void assertRspData(RspData r){
		Assert.assertTrue(r.status == 1);
	}
	
	public ReqFooData newReqData(Object obj){
		
		ReqFooData req = new ReqFooData();
		req.version = "1.0.0";
		req.content = JSON.toJSONString(obj);
		req.clientType = 1;
		req.langType = 1;
		req.token = "0909";
		
		return req;
	}
	
}
