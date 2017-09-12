package com.candao.trace;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EnableAspectJAutoProxy
@ComponentScan(basePackages={"com.candao.trace"}) 
public class DemoServiceApplication2 {

	public static void main(String[] args) {
		new SpringApplicationBuilder(DemoServiceApplication2.class).web(true).run(args);
	}
	
}
