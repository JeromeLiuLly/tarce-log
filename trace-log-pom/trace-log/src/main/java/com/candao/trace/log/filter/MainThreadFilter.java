package com.candao.trace.log.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

import com.google.gson.Gson;

public class MainThreadFilter extends Filter<ILoggingEvent> {

	public static final Gson gson = new Gson();

	@Override
	public FilterReply decide(ILoggingEvent event) {
		if ("main".equals(event.getThreadName())) {
			return FilterReply.DENY;
		} else {
			return FilterReply.ACCEPT;
		}
	}

}
