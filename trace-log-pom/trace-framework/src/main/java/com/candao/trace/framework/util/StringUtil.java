package com.candao.trace.framework.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串帮助类
 * 
 */
public class StringUtil {
	/**
	 * 判断字符串是否为null或空
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}

	/**
	 * 判断字符串不为null或者空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		return !isNullOrEmpty(str);
	}
	
	/**
	 * 判断字符串是否为null或空字符串
	 * @param str
	 * @return tt
	 */
	public static boolean isNullOrBlank(String str) {
		return str == null || "".equals(str.trim());
	}

	/**
	 * 如果str为null则返回空字符串
	 * @param str
	 * @return
	 */
	public static String getEmptyIfNull(String str) {
		return str == null ? "" : str;
	}

	/**
	 * 将指定的字符串s按照splitTag符号分割成list
	 * @param s-需要分割的字符串
	 * @param splitTag-分割符
	 * @return
	 */
	public static List<String> splitToList(String s, String splitTag) {
		if (s == null || splitTag == null) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		String[] arr = s.split("[" + splitTag + "]");
		for (String ss : arr) {
			if (!isNullOrEmpty(ss)) {
				list.add(ss);
			}
		}
		return list;
	}

	/**
	 * 将List<T>转化成以splitTag为分割符拼接的字符串
	 * @param list-需转换的list
	 * @param splitTag-分割符
	 * @return
	 */
	public static <T> String listToString(List<T> list, String splitTag) {
		if (list == null || list.isEmpty() || isNullOrBlank(splitTag)) {
			return "";
		}
		String s = "";
		for (T t : list) {
			s += String.valueOf(t) + splitTag;
		}
		if (s.endsWith(splitTag)) {// 移除最后一个分隔符
			s = s.substring(0, s.lastIndexOf(splitTag));
		}
		return s;
	}
	
	/**
	 * 将字符串转换成Integer集合
	 * @param s 待拆分字符串
	 * @param splitTag 拆分符号
	 * @return
	 * @author guoden 2016年7月19日
	 */
	public static List<Integer> splitToIntegerList(String s, String splitTag) {
		if (s == null || splitTag == null) {
			return null;
		}
		List<Integer> ids = new ArrayList<Integer>();
		for (String str : s.split(splitTag)) {
			if (!isNullOrBlank(str)) {
				ids.add(Integer.valueOf(str));
			}
		}
		return ids;
	}

	/**
	 * 删除emoji等字符
	 * @param str
	 * @return
	 * @author Guoden
	 */
	public static String removeNonBmpUnicode(String str) {
		if (str == null) {
			return null;
		}
		str = str.replaceAll("[^\\u0000-\\uFFFF]", "");
		return str;
	}

	/**
	 * 验证邮箱
	 * @param emails 多个用","分割
	 * @return
	 */
	public static boolean checkEmails(String emails) {
		List<String> list = StringUtil.splitToList(emails, ",");// 多个用",分割"
		for (String email : list) {
			if (!checkEmail(email)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 验证邮箱
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 判断字符串是否为整数(大部分情况可行)
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("-?[0-9]+");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断字符串是否为浮点数(大部分情况可行)
	 * @param str
	 * @return
	 */
	public static boolean isFloat(String str) {
		Pattern pattern = Pattern.compile("-?[0-9]+\\.[0-9]+");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断字符串是否为数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		if (StringUtil.isNullOrBlank(str) || str.contains(" ")) {// 空字符串或包含空格
			return false;
		} else {
			String[] arr = str.split("[.]");
			if (arr.length > 0 && arr[0].indexOf("0") == 0 && arr[0].length() > 1) {// 0开头并且整数部分位数大于1的过滤掉
				return false;
			}
		}
		boolean isExist = false;
		for (int i = str.length(); --i >= 0;) {
			char c = str.charAt(i);
			if (!Character.isDigit(c)) {
				// 只能包含一个点,并且点不能在开头或者结尾
				if (!isExist && ".".equals(String.valueOf(c)) && i != 0 && i != str.length()) {
					isExist = true;
					continue;
				}
				// 负数允许通过
				if ("-".equals(String.valueOf(c)) && i == 0 && str.length() > 1) {
					continue;
				}
				return false;
			} else if (((c < 'A') || (c > 'Z')) && ((c < 'a') || (c > 'z')) && ((c < '0') || (c > '9'))) {// 过滤半角英文数字
				return false;
			}
		}
		return true;
	}
}
