package com.candao.trace.gateway;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.ribbon.support.RibbonRequestCustomizer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.context.annotation.Bean;

import com.candao.trace.gateway.filter.ErrFilter;
import com.candao.trace.gateway.filter.LogPostFilter;
import com.candao.trace.gateway.filter.LogPreFilter;
import com.candao.trace.gateway.filter.LogRetFilter;
import com.candao.trace.gateway.filter.ReDispatchFilter;

/**
 *  API网关
 * 
 */
@EnableZuulProxy
@SpringCloudApplication
public class ApiGatewayApplication {

	@SuppressWarnings("rawtypes")
	@Autowired(required = false)
	private List<RibbonRequestCustomizer> requestCustomizers = Collections.emptyList();
	public static void main(String[] args) {
		new SpringApplicationBuilder(ApiGatewayApplication.class).web(true).run(args);
	}
	
	@Bean
	public LogPreFilter logPreFilter() {
		return new LogPreFilter();
	}
	
	@Bean
	public LogPostFilter logPostFilter() {
		return new LogPostFilter();
	}
	
	@Bean
	public ErrFilter errFilter() {
		return new ErrFilter();
	}
	
	@Bean
	public LogRetFilter logRetFilter() {
		return new LogRetFilter();
	}
	
	@Bean
	public ReDispatchFilter ribbonRoutingFilter(ProxyRequestHelper helper,
			RibbonCommandFactory<?> ribbonCommandFactory) {
		ReDispatchFilter filter = new ReDispatchFilter(helper, ribbonCommandFactory, this.requestCustomizers);
		return filter;
	}
}
