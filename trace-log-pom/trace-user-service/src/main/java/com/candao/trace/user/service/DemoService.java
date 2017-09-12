package com.candao.trace.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.candao.trace.demo.api.DemoFeignClient;
import com.candao.trace.demo.api.bean.DemoUser;
import com.candao.trace.log.logger.Logger;

@Component
public class DemoService {
	
	@Autowired
	private DemoFeignClient demoFeignClient;

	public DemoUser getUser(int uid) {
		// 从dms-demo-service服务中获取DemoUser对象
		DemoUser user = demoFeignClient.getUser(uid);
		user.setName("name from demo 2");
		Logger.info("调用demo接口");
		return user;
	}

}
