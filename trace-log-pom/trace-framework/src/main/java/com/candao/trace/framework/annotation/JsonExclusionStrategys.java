package com.candao.trace.framework.annotation;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * josn排除策略
 * 
 */
public class JsonExclusionStrategys {
	/**
	 * 排除标记有@JsonExcludable字段的策略
	 */
	public static ExclusionStrategy skipFieldExclusionStrategy = new ExclusionStrategy() {

		public boolean shouldSkipClass(Class<?> arg0) {
			return false;
		}

		public boolean shouldSkipField(FieldAttributes fa) {
			return fa.getAnnotation(JsonExcludable.class) != null;
		}
	};
}
