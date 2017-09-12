package com.candao.trace.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动化ID赋值
 * 
 * @Retention 用于描述注解的生命周期
 * @Target 用于描述注解的使用范围
 * 
 * @author jeromeLiu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface AutoIdGenerator {

}
