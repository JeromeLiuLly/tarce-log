package com.candao.trace.demo.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.candao.trace.demo.api.bean.DemoUser;
import com.candao.trace.framework.util.ObjectUtil;

/**
 * 消息发送器
 *
 */
@Component
public class UserSmsSender {

	@Autowired
	ProcessConfig processConfig;
	
	public void send(DemoUser user) {
		if (user != null) {
			processConfig.output().send(MessageBuilder.withPayload(user).build());
			System.out.println("消息生产。。。user = " + ObjectUtil.toString(user));
		}else{
			System.out.println("空对象");
		}
	}

}
