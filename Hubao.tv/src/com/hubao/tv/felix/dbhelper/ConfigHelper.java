package com.hubao.tv.felix.dbhelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/***
 * 读取配置类
 * 
 * @author Felix
 * 
 */
public class ConfigHelper {

	public static Logger logger = Logger.getLogger(ConfigHelper.class);

	/***
	 * 读取配置
	 * 
	 * @param resources
	 *            Properties配置文件源路径
	 * @return
	 */
	public Properties loadProperties(String resources) {

		InputStream ins = null;
		Properties properties = new Properties();
		File file = new File(resources);
		boolean bl = file.exists();
		try {
			FileInputStream fileInputStream = new FileInputStream(resources);
			ins = new BufferedInputStream(fileInputStream);
			// 加载配置文件
			properties.load(ins);
			return properties;
		} catch (IOException ex) {
			logger.error("读取配置方法loadProperties出现异常：" + ex.getMessage());
			return null;
		} finally {
			try {
				if (ins != null) {
					ins.close();
				}
			} catch (IOException ex) {
				logger.error("读取配置方法loadProperties关闭文件流时出现异常："
						+ ex.getMessage());
			}
		}
	}
}
