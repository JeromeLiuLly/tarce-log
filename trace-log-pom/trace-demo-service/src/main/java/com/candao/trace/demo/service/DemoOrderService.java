package com.candao.trace.demo.service;

import org.springframework.stereotype.Component;

import com.candao.trace.demo.api.bean.DemoOrder;
import com.candao.trace.demo.config.CacheKeys;
import com.candao.trace.framework.callback.IDataGeter;
import com.candao.trace.framework.data.DataReader;

/**
 * Demo Order服务逻辑
 * 
 */
@Component
public class DemoOrderService {

	/**
	 * 获取指定id的Order对象
	 * @param orderId-订单id
	 * @return
	 */
	public DemoOrder getOrder(int orderId) {
		String cacheKey = CacheKeys.ORDER_ + orderId;
		// 调用数据获取封装方法，内部以规范cache与db数据获取流程
		DemoOrder data = DataReader.getDataFromRedisOrDataGeter(cacheKey, DemoOrder.class, new IDataGeter<DemoOrder>() {
			@Override
			public DemoOrder getData() {
				// TODO 一般从db中获取数据，这里示例为直接构建对象
				DemoOrder data = new DemoOrder();
				data.setId(1);
				data.setOrderNo("888888888");
				data.setPrice(66.6f);
				return data;
			}
		});
		return data;
	}
}
