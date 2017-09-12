package com.candao.trace.framework.event;

/**
 * 智能监听器
 * 
 */
public abstract class WiseEventListener<TEvent> implements EventListener<TEvent> {

	protected abstract EventDispatcher<TEvent> dispatcher();

	public void listen() {
		dispatcher().addListener(this);
	}

	public void unlisten() {
		dispatcher().removeListener(this);
	}

}
