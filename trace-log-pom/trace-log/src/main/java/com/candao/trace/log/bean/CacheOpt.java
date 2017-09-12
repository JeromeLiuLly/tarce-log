package com.candao.trace.log.bean;

public enum CacheOpt {
	
	GET((byte) 1),
	SET((byte) 2),
	DEL((byte) 3),
	OTHER((byte) 0);
	
	public byte flag;

	private CacheOpt(byte flag) {
		this.flag = flag;
	}
	
	public static CacheOpt getCacheOpt(byte flag) {
		switch (flag) {
		case 1:
			return GET;
		case 2:
			return SET;
		case 3:
			return DEL;
		default:
			return OTHER;
		}
	}
	
}
