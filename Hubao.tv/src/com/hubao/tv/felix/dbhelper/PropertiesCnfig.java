package com.hubao.tv.felix.dbhelper;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.hubao.tv.felix.httphelper.HttpHelper;

 

/**
 * 读取配置
 * 
 * @author Felix
 * 
 */
public class PropertiesCnfig {

	private static Logger logger = Logger.getLogger(PropertiesCnfig.class);

	/***
	 * 加载配置
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public Properties LoadPropertie() {
		ConfigHelper pCnfig = new ConfigHelper();
		Properties pro = null;
		try {
			// 获取class绝对路径
			String path = HttpHelper.Decode(this.getClass().getClassLoader()
					.getResource("").getPath());
			// 截取WEB-INF目录
			int num = path.indexOf("WEB-INF");
			// 截取拼接处所有目录F
			path = path.substring(0, num + "WEB-INF".length());
			String source = path + "/resources/coreconfig.properties";
			pro = pCnfig.loadProperties(source);
		} catch (Exception e) {
			logger.error("LoadPropertie方法中出现异常："+e);
		}
		return pro;
	}
}
