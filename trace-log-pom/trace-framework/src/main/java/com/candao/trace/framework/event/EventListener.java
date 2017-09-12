package com.candao.trace.framework.event;

/**
 * 事件监听器
 * 
 */
public interface EventListener<TEvent> {
	
	/**
	 * 事件执行
	 * @param event
	 */
	void onEvent(TEvent event);
	
}
