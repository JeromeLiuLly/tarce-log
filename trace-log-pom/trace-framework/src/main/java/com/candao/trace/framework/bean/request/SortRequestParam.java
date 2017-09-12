package com.candao.trace.framework.bean.request;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.candao.trace.framework.util.FastStringUtils;

/**
 * 
 * 
 * @author lion.chen
 * @version 1.0.0 2017年7月26日 上午11:11:13
 */
public class SortRequestParam implements Serializable{

	private static final long serialVersionUID = 6738808266042446712L;

	/**
	 * 排序字段
	 */
	public String field;
	
	/**
	 * 是否升序，否则降序
	 */
	public boolean asc;

	public SortRequestParam(){}
	
	public SortRequestParam(String field, boolean asc){
		this.field = field;
		this.asc = asc;
	}
	
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj != null && obj instanceof SortRequestParam){
			return FastStringUtils.equalsIgnoreCase(((SortRequestParam)obj).field, this.field);
		}
		
		return super.equals(obj);
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public boolean isAsc() {
		return asc;
	}

	public void setAsc(boolean asc) {
		this.asc = asc;
	}
	
}
