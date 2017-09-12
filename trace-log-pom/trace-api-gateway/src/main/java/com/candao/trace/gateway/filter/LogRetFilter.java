package com.candao.trace.gateway.filter;

import org.springframework.cloud.netflix.zuul.filters.post.SendResponseFilter;

public class LogRetFilter extends SendResponseFilter {

	@Override
	public int filterOrder() {
		return super.filterOrder() + 1;
	}

	@Override
	public Object run() {

		super.run();
		
		return null;
	}
}
