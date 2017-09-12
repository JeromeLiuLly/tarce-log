package com.candao.trace.framework.util;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * 数值工具类
 * 
 */
public class FastNumberUtils extends NumberUtils{

	/**
	 * 金钱 分->元
	 * @param pennys
	 * @return
	 */
	public static double penny2Yuan(int pennys){
		return pennys / 100.0;
	}
	
}
