package com.candao.trace.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.candao.trace.demo.api.bean.DemoUser;
import com.candao.trace.demo.service.DemoUserService;
import com.candao.trace.framework.bean.request.ReqFooData;
import com.candao.trace.framework.bean.response.RspData;
import com.candao.trace.framework.controller.BaseController;

/**
 * 管理后台端请求Controller 示例<br/>
 * 1. 继承至dms-framework中的BaseController <br/>
 * 2. 所有@RequestMapping 在该类中定义 <br/>
 * 3. 请求数据统一为：@RequestBody ReqFooData reqData (父类提供快速方法提取参数), POST形式<br/>
 * 4. 响应数据统一为：RspData对象(可调用父类方法快速构建该对象) <br/>
 * 5. 请求路径为/foo/xx形式
 * 
 */
@RestController
@RequestMapping(value = "/foo/demo")
public class DemoFooController extends BaseController {

	@Autowired
	DemoUserService demoUserService;
	
	@Autowired
	private DiscoveryClient client;
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(@RequestParam Integer a, @RequestParam Integer b) {
		ServiceInstance instance = client.getLocalServiceInstance();
		Integer r = a + b;
		System.out.println("/add, host:" + instance.getHost() + ", service_id:" + instance.getServiceId() + ", result:" + r);
		return "From Service-A,foo,host:" + instance.getHost() + ", service_id:" + instance.getServiceId() +"Result is " + r;
	}

	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public RspData getUser(@RequestBody ReqFooData reqData) {
		// TODO 参数验证
		int uid = reqData.getAsInt("uid");
		DemoUser user = demoUserService.getUser(uid);
		return retSuccessRspData(user);
	}
}
