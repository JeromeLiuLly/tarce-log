package com.candao.trace.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.candao.trace.demo.api.bean.DemoUser;
import com.candao.trace.user.service.DemoService;

/**
 * 
 * 
 * @author jameslei
 * @version 1.0.0 2017年4月28日 上午11:34:59
 */
@RestController
@RequestMapping(value="/demo")
public class DemoController {
	
	@Autowired
	DemoService demoService;
	
	@RequestMapping(value = "/{uid}")
	public DemoUser getUser(@PathVariable(value = "uid") Integer uid) {
		return demoService.getUser(uid);
	}
}
