package com.candao.trace.framework.jpa;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

/**
 * 
 * JSON bean
 * 
 * @author lion.chen
 * @version 1.0.0 2017年8月4日 上午11:21:43
 */
public class JSONEntity implements Serializable{

	private static final long serialVersionUID = 464164840017728229L;

	public JSON toJSONObject(){
		return JSON.parseObject(toString());
	}
	
	public JSON toJSONArray(){
		return JSON.parseArray(toString());
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
