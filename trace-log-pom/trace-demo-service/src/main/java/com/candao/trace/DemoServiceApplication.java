package com.candao.trace;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.candao.trace.framework.jpa.factory.BaseRepositoryFactoryBean;

/**
 * DMS Demo
 * 
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableJpaRepositories(basePackages = { "com.candao.trace" }, repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)//指定自定义的工厂类
public class DemoServiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		new SpringApplicationBuilder(DemoServiceApplication.class).web(true).run(args);
	}

	@Override
	public void run(String... strings) throws Exception {
		//实体类型发送MQ
		/*System.out.println("测试用户发送");
		DemoUser demoUser = new DemoUser();
		demoUser.setId(5);
		demoUser.setName("西门庆");
		demoUser.setPhone("15080800000");
		demoUser.setStatus(1);

		UserSmsSender userSmsSender = new UserSmsSender();
		userSmsSender.send(demoUser);*/
	}
}