package com.candao.trace.framework.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * token生成工具
 */
public class TokenGenerator {

	private static final TokenGenerator instance = new TokenGenerator();
	private static final String algorithm_md5 = "MD5";

	private long previous;

	private TokenGenerator() {
	}

	public static TokenGenerator getInstance() {
		return instance;
	}

	/**
	 * 生成新的token
	 * @param key
	 * @return
	 */
	public String getToken(String key) {
		return getToken(key, true);
	}

	/**
	 * 生成新的token
	 * @param key
	 * @param timeChange
	 * @return
	 */
	public synchronized String getToken(String key, boolean timeChange) {
		try {
			long current = System.currentTimeMillis();
			if (current == previous)
				current++;
			previous = current;
			MessageDigest md = MessageDigest.getInstance(algorithm_md5);
			md.update(key.getBytes());
			if (timeChange) {
				byte now[] = (new Long(current)).toString().getBytes();
				md.update(now);
			}
			return toHex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	/**
	 * 32位md5加密
	 * @param str
	 */
	public String get32MD5(String str) {
		StringBuilder buf = new StringBuilder("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest();

			int i;

			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return buf.toString();// 32位的加密
	}

	/**
	 * 16位md5加密
	 * @param str
	 */
	public String get16MD5(String str) {
		return get32MD5(str).substring(8, 24);// 16位的加密
	}

	private String toHex(byte buffer[]) {
		StringBuffer sb = new StringBuffer(buffer.length * 2);
		for (int i = 0; i < buffer.length; i++) {
			sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
			sb.append(Character.forDigit(buffer[i] & 15, 16));
		}
		return sb.toString();
	}

}
