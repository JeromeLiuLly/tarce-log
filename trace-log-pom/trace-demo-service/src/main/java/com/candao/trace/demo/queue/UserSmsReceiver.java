package com.candao.trace.demo.queue;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import com.candao.trace.demo.api.bean.DemoUser;
import com.candao.trace.framework.util.ObjectUtil;

/**
 * 消息接收器，加@EnableBinding(ProcessConfig.class)注解以监听通道
 *
 */
@EnableBinding(ProcessConfig.class)
public class UserSmsReceiver {

	@StreamListener(ProcessConfig.USER_SMS_INPUT)
	public void receive(Message<DemoUser> message) {
		DemoUser user = message.getPayload();
		System.out.println("消息消费。。。user = " + ObjectUtil.toString(user));
		// 这里实现真正的发送短信逻辑
		System.out.println("发送短信。。。");
	}
	
}
