package com.candao.trace.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class PropertiesLoader {
	
	public static Properties loadProperties(String dirORfile) {
		Properties props = new Properties();
		try {
			URL resoucePath = PropertiesLoader.class.getClassLoader()
					.getResource(dirORfile);

			File file = new File(resoucePath.getPath());

			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					File f = files[i];
					InputStream in = null;
					try {
						in = new FileInputStream(f);
						props.load(in);
					} catch (Exception e) {
						DebugUtil.print(String.format(
								"loadProperties：%sfaild!",
								new Object[] { file.getAbsolutePath() }), e);
					} finally {
						try {
							in.close();
						} catch (Exception e1) {
							DebugUtil.print(String.format(
									"close file:%s faild!",
									new Object[] { file.getAbsolutePath() }),
									e1);
						}

					}

				}

			} else if (file.isFile()) {
				InputStream in = null;
				try {
					in = new FileInputStream(file);
					props.load(in);
				} catch (Exception e) {
					DebugUtil.print(String.format("loadProperties：%sfaild!",
							new Object[] { file.getAbsolutePath() }), e);
				} finally {
					try {
						in.close();
					} catch (Exception e1) {
						DebugUtil.print(String.format("关闭配置文件：%s输入流faild!",
								new Object[] { file.getAbsolutePath() }), e1);
					}
				}
			}
		} catch (Exception e) {
			DebugUtil.print("PropertiesLoader.loadProperties() faild!dirORfile:"
					+ dirORfile, e);
		}

		return props;
	}
}
