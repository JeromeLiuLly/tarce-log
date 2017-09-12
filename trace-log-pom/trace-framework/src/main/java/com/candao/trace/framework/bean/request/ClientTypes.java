package com.candao.trace.framework.bean.request;

/**
 * 客户端类型
 * 
 */
public enum ClientTypes {
	/** 停用 */
	APP((byte) 1),

	/** 启用 */
	FOO((byte) 2),

	/** 删除 */
	EXTERNAL((byte) 3);

	private final byte value;

	// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
	ClientTypes(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return value;
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
