package com.candao.trace.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.trace.demo.api.bean.DemoUser;
import com.candao.trace.demo.queue.UserSmsSender;

@Service
public class DemoSmsService {
	@Autowired
	UserSmsSender userSmsSender;
	public void sendSms(DemoUser user) {
		userSmsSender.send(user);
	}
}
