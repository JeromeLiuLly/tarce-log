package com.candao.trace.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 服务注册中心
 * 
 * @author 刘练源
 * @version 0.0.1
 */
@EnableEurekaServer
@SpringBootApplication
public class ServiceRegistryCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRegistryCenterApplication.class, args);
	}

}