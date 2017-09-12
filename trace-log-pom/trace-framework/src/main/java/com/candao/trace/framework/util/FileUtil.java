package com.candao.trace.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

/**
 * 文件操作工具类
 * 
 */
public class FileUtil {
	public static final String SEPARATOR = File.separator;

	public static String standardizeDirPath(String path) {
		return standardizeDirPath(new File(path));
	}

	public static String standardizeDirPath(File dir) {
		String formatedPath = dir.getPath();
		return formatedPath.isEmpty() ? formatedPath : formatedPath + SEPARATOR;
	}

	public static String getChildDirPath(String standardParentPath, String childPath) {
		return standardizeDirPath(standardParentPath + childPath);
	}

	public static File getChildFile(String standardParentPath, String childPath) {
		return new File(getChildFilePath(standardParentPath, childPath));
	}

	public static String getChildFilePath(String standardParentPath, String childPath) {
		return standardParentPath + childPath;
	}

	/**
	 * 读取配置文件
	 * @param fullPath-文件全路径（包括文件名）
	 * @return
	 */
	public static Properties getConfigProperties(String fileName) {
		File file = null;
		String env = System.getProperty("candao.root");
		Properties properties = new Properties();
		InputStream in = null;
		try {
			// 如果配置了自定义的candao.root，则从该目录读配置文件
			if (!StringUtil.isNullOrEmpty(env)) {
				file = new File(env + "WEB-INF/", fileName);
				in = new FileInputStream(file);
			} else {
				ClassPathResource res = new ClassPathResource(fileName);
				in = res.getInputStream();
			}
			properties.load(in);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return properties;
	}
}
