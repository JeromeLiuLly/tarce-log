package com.candao.trace.gateway.filter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestAttributes;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class SysErrorAttributes extends DefaultErrorAttributes {
	
	private void addStackTrace(Map<String, Object> errorAttributes, Throwable error) {
		StringWriter stackTrace = new StringWriter();
		error.printStackTrace(new PrintWriter(stackTrace));
		stackTrace.flush();
		errorAttributes.put("trace", stackTrace.toString());
	}
	
	private void addErrorDetails(Map<String, Object> errorAttributes,
			RequestAttributes requestAttributes, boolean includeStackTrace) {
		Throwable error = getError(requestAttributes);
		String exception = "";
//		String message = "";
		if (error != null) {
			while (error instanceof ServletException && error.getCause() != null) {
				error = ((ServletException) error).getCause();
			}
//			errorAttributes.put("exception", error.getClass().getName());
			if (error.getCause() != null) {
				exception = error.getCause().toString();
			} else {
				exception = error.toString();
			}
			errorAttributes.put("msg", exception);
			if (includeStackTrace) {
				addStackTrace(errorAttributes, error);
			}
		}
	}
	
	@Override
	public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
//		Map<String, Object> result = super.getErrorAttributes(requestAttributes, includeStackTrace);
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("status", 2);
		addErrorDetails(result, requestAttributes, includeStackTrace);
		result.put("serverTime", new Date());
		return result;
	}
}
