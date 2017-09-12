package com.candao.trace.demo.config;

/**
 * 该项目的所有cache key定义<br/>
 * 注意：最好每个项目的key以特定项目名为前缀，以免不同项目的cache key定义重复<br/>
 * 比如：订单项目： order_xx; 骑手项目：user_xx; 报表项目：report_
 * 
 */
public class CacheKeys {
	// DemoUser对象的key
	public static final String USER_ = "demo_user_";

	// DemoOrder对象的key
	public static final String ORDER_ = "demo_order_";
}
