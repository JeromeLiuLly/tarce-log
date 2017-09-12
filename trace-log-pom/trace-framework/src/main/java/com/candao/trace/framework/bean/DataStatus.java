package com.candao.trace.framework.bean;

/**
 * 数据状态 <br/>
 * 停用：0 <br/>
 * 启用：1 <br/>
 * 删除：3
 * 
 */
public enum DataStatus {
	
	/** 停用 */
	DISABLE((byte) 0, "停用"),
	
	/** 启用 */
	ENABLE((byte) 1, "启用"),

	/** 删除 */
	DELETE((byte) 2, "删除");

	public byte value;
	public String valueName;

	// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
	DataStatus(byte value, String name) {
		this.value = value;
		this.valueName = name;
	}

	public byte getValue() {
		return value;
	}

	/**
	 * 根据status获取DataStatus <br/>
	 * @param status-停用：0；启用：1；删除：2
	 * @return
	 */
	public static DataStatus getDataStatus(Byte status) {
		if(status == null) return null;
		return getDataStatus(status.byteValue());
	}
	
	/**
	 * 根据status获取DataStatus <br/>
	 * @param status-停用：0；启用：1；删除：2
	 * @return
	 */
	public static DataStatus getDataStatus(byte status) {
		if (status == 0) {
			return DISABLE;
		} else if (status == 1) {
			return ENABLE;
		} else if (status == 2) {
			return DELETE;
		}
		return null;
	}

	/**
	 * 根据status获取DataStatus <br/>
	 * @param status-停用：0；启用：1；删除：2
	 * @return
	 */
	public static DataStatus getDataStatus(int status) {
		return getDataStatus((byte) status);
	}

	/**
	 * 是否相同
	 * @param type
	 * @return
	 */
	public boolean isEq(int type) {
		return this.value == type;
	}
}