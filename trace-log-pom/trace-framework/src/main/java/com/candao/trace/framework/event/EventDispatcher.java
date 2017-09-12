package com.candao.trace.framework.event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 事件分发器
 * 
 */
public class EventDispatcher<TEvent> {
	private Set<EventListener<TEvent>> listeners;

	public EventDispatcher() {
		init();
	}

	public void init() {
		listeners = new HashSet<EventListener<TEvent>>();
	}

	/**
	 * 释放所有监听器
	 */
	public void dispose() {
		listeners = null;
	}

	/**
	 * 增加监听
	 * @param listener
	 * @return true if did not already has the specified listener
	 */
	public boolean addListener(EventListener<TEvent> listener) {
		return listeners.add(listener);
	}

	/**
	 * 删除监听
	 * @param listener
	 * @return true if has the specified listener
	 */
	public boolean removeListener(EventListener<TEvent> listener) {
		return listeners.remove(listener);
	}

	/**
	 * 是否有监听器
	 * @return
	 */
	public boolean hasListeners() {
		return listeners != null && !listeners.isEmpty();
	}

	/**
	 * 分发事件
	 */
	public void dispatch(TEvent event) {
		if (!hasListeners()) {
			return;
		}
		ArrayList<EventListener<TEvent>> listeners = new ArrayList<EventListener<TEvent>>(this.listeners);
		for (EventListener<TEvent> listener : listeners) {
			try {
				listener.onEvent(event);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
}
