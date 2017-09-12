package com.candao.trace.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.candao.trace.demo.api.bean.DemoUser;
import com.candao.trace.demo.service.DemoUserService;
import com.candao.trace.framework.bean.request.ReqAppData;
import com.candao.trace.framework.bean.response.RspData;
import com.candao.trace.framework.controller.BaseController;
import com.candao.trace.framework.util.ObjectUtil;
import com.candao.trace.log.aop.ControllerLog;
import com.candao.trace.log.logger.Logger;

/**
 * APP端请求Controller 示例 <br/>
 * 1. 继承至dms-framework中的BaseController <br/>
 * 2. 所有@RequestMapping 在该类中定义 <br/>
 * 3. 请求数据统一为：@RequestBody ReqAppData reqData (父类提供快速方法提取参数), POST形式<br/>
 * 4. 响应数据统一为：RspData对象(可调用父类方法快速构建该对象) <br/>
 * 5. 请求路径为/app/xx形式
 * 
 */
@RestController
@RequestMapping(value = "/app/demo")
@ControllerLog
@RefreshScope
public class DemoAppController extends BaseController {

	/*@Value("${field1}")
	public String field3;
	
	public String getField3() {
		return field3;
	}
	
	public void setField3(String field3) {
		this.field3 = field3;
	}*/
	
	@Autowired
	DemoUserService demoUserService;
	
	@Autowired
	private DiscoveryClient client;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(@RequestParam Integer a, @RequestParam Integer b) {
		ServiceInstance instance = client.getLocalServiceInstance();
		Integer r = a + b;
		System.out.println("/add, host:" + instance.getHost() + ", service_id:" + instance.getServiceId() + ", result:" + r);
		Logger.info("/add, host:" + instance.getHost() + ", service_id:" + instance.getServiceId() + ", result:" + r);
		return "From Service-A,测试动态更新升级,host:" + instance.getHost() + ", service_id:" + instance.getServiceId() +"Result is " + r;
	}

	@RequestMapping(value = "/user/", method = RequestMethod.POST)
	public RspData getUser(@RequestBody ReqAppData data) {
		// TODO 参数验证
		int uid = data.getAsInt("uid");
		DemoUser user = demoUserService.getUser(uid);
		return retSuccessRspData(user);
	}

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public RspData addUser(@RequestBody ReqAppData data) {
		// 从请求ReqAppData中获取参数
		String token = data.token;
		// 从请求ReqAppData中获取对象
		DemoUser user = data.toObject(DemoUser.class);
		System.out.println("token:" + token);
		System.out.println("registTime:" + user.getRegistTime());
		System.out.println("user:" + ObjectUtil.toString(user));
		
		// 添加用户发送确认短信(消息队列demo)
		demoUserService.sendConfiringSms(user);
		
		// TODO 参数验证
		return retSuccessRspData(user);
	}
}
