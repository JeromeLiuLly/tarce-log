package com.candao.trace.framework.spring.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.candao.trace.framework.spring.bean.FastActionContext;
import com.candao.trace.log.ThreadLocalBean;

/**
 * 参数过滤拦截
 * 
 */
public class FastActionParamInterceptor extends HandlerInterceptorAdapter{

	/**
	 * 处理之前
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		ThreadLocalBean.init();
		FastActionContext.createActionContext(request, response);
		ThreadLocalBean.setTraceId(FastActionContext.getActionContext().getTraceId());
		
		return super.preHandle(request, response, handler);
	}
	
	/**
	 * 处理之后
	 * @param request
	 * @param response
	 * @param handler
	 * @param modelAndView
	 * @throws Exception
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler, ModelAndView modelAndView) throws Exception {

		FastActionContext.destroyActionContext();
		ThreadLocalBean.remove();
		
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}
	
}
