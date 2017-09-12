package com.candao.trace.framework.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.candao.trace.framework.util.FastStringUtils;

/**
 * 精简数据封装
 * @author alan.zhang
 * @version 1.0.0 2017年7月6日 下午4:38:33
 */
public class MiniData implements Serializable{

	private static final long serialVersionUID = 3553992120118330893L;
	
	public MiniData(){}
	
	public MiniData(Integer id, String name){
		this.id = id;
		this.name = name;
	}
	
	public MiniData(Integer id, Integer pid, String name){
		this.id = id;
		this.pid = pid;
		this.name = name;
	}
	
	public MiniData(Integer id, String name, Integer pid, String pidName){
		this.id = id;
		this.name = name;
		this.pid = pid;
		this.pidName = pidName;
	}
	
	private Integer id;
	private Integer pid;
	private String pidName;
	private String name;

	/**
	 * 数据转换
	 * @param obj
	 * @return
	 */
	public static MiniData toMiniData(Object obj){
		MiniData m = null;
		if(obj != null){
			m = new MiniData();
			BeanUtils.copyProperties(obj, m);
		}
		return m;
	}
	
	/**
	 * 数据转换
	 * @param objs
	 * @return
	 */
	public static List<MiniData> toMiniDatas(List<?> objs){
		List<MiniData> r = new ArrayList<MiniData>();
		if(FastStringUtils.isNotEmpty(objs)){
			for(Object o : objs){
				r.add(toMiniData(o));
			}
		}
		return r;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public String getPidName() {
		return pidName;
	}

	public void setPidName(String pidName) {
		this.pidName = pidName;
	}
	
}
