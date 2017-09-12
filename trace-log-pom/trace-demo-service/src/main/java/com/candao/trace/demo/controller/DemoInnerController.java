package com.candao.trace.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.candao.trace.demo.api.DemoFeignClient;
import com.candao.trace.demo.api.bean.DemoOrder;
import com.candao.trace.demo.api.bean.DemoUser;
import com.candao.trace.demo.service.DemoOrderService;
import com.candao.trace.demo.service.DemoUserService;
import com.candao.trace.log.aop.ControllerLog;

/**
 * 内部系统调用 Controller示例<br/>
 * 1. 基础dms-xx-api里面的xxFeignClient
 * 2. @RequestMapping 在xxFeignClient里面定义
 * 3. 该类方法参数需用@PathVariable 标记
 * 4. 请求路径为/inner/xx形式
 * 
 */
@RestController
@ControllerLog
public class DemoInnerController implements DemoFeignClient {
	@Autowired
	DemoUserService demoUserService;

	@Autowired
	DemoOrderService demoOrderService;

	@Override
	public DemoUser getUser(@PathVariable(value = "id") Integer uid) {
		if (uid <= 0) {
			// TODO 如何返回异常
		}
		return demoUserService.getUser(uid);
	}

	@Override
	public DemoOrder getOrder(@PathVariable(value = "id") Integer orderId) {
		return demoOrderService.getOrder(orderId);
	}

}
