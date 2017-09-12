package com.candao.trace.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记不序列化到json的注释
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface JsonExcludable {
	/**
	 * 属性序列化成JSON串时是否过滤(默认过滤)
	 * @return
	 */
	boolean read() default true;
	/**
	 * 写入数据库时是否过滤(默认过滤)
	 * @return
	 */
	boolean write() default true;
}
