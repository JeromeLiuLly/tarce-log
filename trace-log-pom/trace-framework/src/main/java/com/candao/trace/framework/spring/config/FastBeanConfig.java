package com.candao.trace.framework.spring.config;

import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.candao.trace.framework.spring.bean.FastActionContext;
import com.candao.trace.log.bean.CommonRequestParam;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 设置json转化器
 * 
 */
@Configuration
public class FastBeanConfig {

	/**
	 * json数据转换器
	 * @return
	 */
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		
		FastJsonHttpMessageConverter fastConvert = new FastJsonHttpMessageConverter();
		
		fastConvert.setFeatures(SerializerFeature.PrettyFormat, 
					SerializerFeature.WriteNullStringAsEmpty,
					SerializerFeature.WriteNullNumberAsZero,
					SerializerFeature.WriteNullBooleanAsFalse);	
		
		return new HttpMessageConverters((HttpMessageConverter<?>) fastConvert);
	}
	
	/**
	 * Feign请求公共参数设置
	 * @return
	 */
	@Bean
	public RequestInterceptor getCommonFeignRequestInterceptor(){
		return new RequestInterceptor(){
			@Override
			public void apply(RequestTemplate template) {
				FastActionContext ctx = FastActionContext.getActionContext();
				if(ctx != null){
					template.header(CommonRequestParam.common_param_head_key, JSONObject.toJSONString(ctx.getCommonRequestParam()));
				}
			}
		};
	}

}
