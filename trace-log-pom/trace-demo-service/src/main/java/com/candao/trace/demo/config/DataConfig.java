package com.candao.trace.demo.config;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.candao.irms.framework.cache.RedisClient;
import com.candao.irms.framework.util.FileUtil;


/**
 * 数据配置初始化(先跑通，后面移除)
 * 
 */
@Component
public class DataConfig {

	@PostConstruct
	public void init() {
		try {
			// redis
			Properties properties = FileUtil.getConfigProperties("redis.properties");
			RedisClient.init(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
