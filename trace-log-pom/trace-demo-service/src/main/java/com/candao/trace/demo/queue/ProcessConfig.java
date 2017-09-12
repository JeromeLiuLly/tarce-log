package com.candao.trace.demo.queue;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 消息通道配置
 *
 */
public interface ProcessConfig {
	String USER_SMS_INPUT = "user_sms_input";
	String USER_SMS_OUTPUT = "user_sms_output";
	
	@Input(USER_SMS_INPUT)
	SubscribableChannel input();
	
	@Output(USER_SMS_OUTPUT)
	MessageChannel output();
}
