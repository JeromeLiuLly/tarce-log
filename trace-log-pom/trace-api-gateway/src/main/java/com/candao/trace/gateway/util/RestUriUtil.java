package com.candao.trace.gateway.util;


public class RestUriUtil {
	
	/**
	 * 根据uri获取请求来源
	 * @param uri
	 * @return
	 */
	public static String getClientStr(String uri) {
		if (uri != null) {
			String[] arr = uri.split("[/]");
			if (arr.length > 3) {
				return arr[2];
			}
		}
		return null;
	}
	
}
