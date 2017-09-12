package com.candao.trace.framework.util;


/**
 * 调试工具类
 * 
 */
public class DebugUtil {
	private static final String PREFIX_SEP = ":";
	// 元素分隔符
	private static final String FIELD_SEP = ",";

	/**
	 * 打印信息
	 * @param fields-需要打印的字段
	 */
	public static void print(Object... fields) {
		print(null, fields);
	}

	/**
	 * 打印信息
	 * @param prefix-前缀字符串
	 * @param fields-需要打印的字段
	 */
	public static void print(String prefix, Object... fields) {
		StringBuilder sb = null;
		if (StringUtil.isNullOrEmpty(prefix)) {
			sb = new StringBuilder();
		} else {
			sb = new StringBuilder(prefix + PREFIX_SEP);
		}
		for (Object field : fields) {
			sb.append(field);
			sb.append(FIELD_SEP);
		}
		sb.deleteCharAt(sb.length() - 1);
		System.out.println(sb.toString());
	}
}
