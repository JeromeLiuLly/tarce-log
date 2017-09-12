package com.candao.trace.demo.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.candao.trace.demo.api.bean.DemoOrder;
import com.candao.trace.demo.api.bean.DemoUser;

/**
 * Demo服务的FeignClient
 * 
 */
@FeignClient("demo-service")
@RestController
@RequestMapping(value = "/inner/demo")
@Component
public interface DemoFeignClient {

	/**
	 * 获取指定id的DemoUser对象
	 * @param uid-用户id
	 * @return
	 */
	@RequestMapping(value = "/user/{id}")
	DemoUser getUser(@PathVariable(value = "id") Integer uid);

	/**
	 * 获取指定id的DemoOrder对象
	 * @param orderId-订单对象
	 * @return
	 */
	@RequestMapping(value = "/order/{id}")
	DemoOrder getOrder(@PathVariable(value = "id") Integer orderId);
}
