package com.candao.trace.framework.util;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.log4j.Logger;
import org.springframework.util.Base64Utils;

/**
 * DES加密解密工具
 * 
 */
public class FastDESUtils {

	private final static Logger LOG = Logger.getLogger(FastDESUtils.class);
	private final static String DES = "DES";

	/**
	 * 根据键值进行加密
	 * @param data
	 * @param key 
	 * @return
	 * @throws Throwable
	 */
	public static String encrypt(String data, String key) {
		
		String result = data;
		
		if(check(data, key)){
			try{
				byte[] bt = encrypt(data.getBytes(), key.getBytes());
				result = Base64Utils.encodeToString(bt);
			}catch(Throwable e){
				//e.printStackTrace();
				LOG.error("====>> DESUtils.encrypt error:" + e.getMessage());
			}
		}
		
		return result;
	}

	/**
	 * 根据键值进行解密
	 * @param data
	 * @param key
	 * @return
	 * @throws IOException
	 * @throws Throwable
	 */
	public static String decrypt(String data, String key) {
		
		String result = data;

		if(check(data, key)){
			try{
				byte[] buf = Base64Utils.decodeFromString(data);
				byte[] bt = decrypt(buf, key.getBytes());
				result = new String(bt);
			}catch(Throwable e){
				//e.printStackTrace();
				LOG.error("====>> DESUtils.decrypt error:" + e.getMessage());
			}
		}
		
		return result;
	}

	/**
	 * 验证key
	 * @param key
	 * @return
	 */
	private static boolean check(String data, String key){
		
		if(key == null){
			LOG.error("====>> DESUtils.check DES key is null!!");
			return false;
		}
		
		if(key.length() < 8){
			LOG.error("====>> DESUtils.check DES key length must be greater than or equal to 8!!");
			return false;
		}
		
		if(data == null){
			LOG.error("====>> DESUtils.check DES data is null!!");
			return false;
		}
		
		return true;
	}
	
	/**
	 * 根据键值进行加密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] key) throws Throwable {
		
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);

		// 用密钥初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}

	/**
	 * 根据键值进行解密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] data, byte[] key) throws Throwable {
		
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(DES);

		// 用密钥初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}
	
}
