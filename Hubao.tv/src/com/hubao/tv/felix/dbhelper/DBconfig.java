package com.hubao.tv.felix.dbhelper;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.hubao.tv.felix.httphelper.HttpHelper;



/***
 * 读取JDBC连接配置的库
 * @author Felix
 *
 */
public class DBconfig {


	private static Logger logger = Logger.getLogger(DBconfig.class);
	private Properties prop = null;

	/***
	 * 构造函数中初始化配置
	 */
	public DBconfig() {
		ConfigHelper chelp=new  ConfigHelper();
		//获取class绝对路径
		String path;
		try {
			path = HttpHelper.Decode(this.getClass().getClassLoader().getResource("").getPath());
			//截取WEB-INF目录
			 int num =path.indexOf("WEB-INF");   
			//截取拼接处所有目录
			 path=path.substring(0, num+"WEB-INF".length());  
			 String source=path+"/resources/coreconfig.properties";
			 this.prop = chelp.loadProperties(source);
		} catch (Exception e) {
		   logger.error("DBconfig构造方法初始化出现异常："+e);
		}

	}

	/***
	 * 获取JDBC连接
	 * @return
	 */
	public  String getJdbcUrl()
	{
		String url="";
		try {
			url=this.prop.getProperty("jdbc.url");
		} catch (Exception ex) {
			// TODO: handle exception
			logger.error("获取数据库连接密码(数据库连接节点jdbc.url未设置):"+ex);
		}
		return url;
	}

	
	
	
	/***
	 * 获取数据库用户名
	 * @return
	 */
	public String getUsername()
	{

		String username="";
		try {
			username=	  this.prop.getProperty("jdbc.username");
		} catch (Exception ex) {
			// TODO: handle exception
			logger.error("获取数据库连接密码(数据库连接节点jdbc.username未设置):"+ex);
		}
		return username;
	}

	/***
	 * 获取数据库连接密码
	 * @return
	 */
	public String getPassword()
	{
		String password="";
		try {
			password= this.prop.getProperty("jdbc.password");
		} catch (Exception ex) {
			// TODO: handle exception
			logger.error("获取数据库连接密码(数据库连接节点jdbc.password未设置):"+ex);
		}
		return    password;
	}

	/***
	 * 获取数据库最大连接数
	 * @return
	 */
	public int getMaxConnectionCount()
	{
		int maxConnectionCount = 0;
		String maxConnectionCountString = this.prop.getProperty("jdbc.maxConnectionCount");
		try {
			maxConnectionCount = Integer.parseInt(maxConnectionCountString);
		} catch (Exception ex) {
			logger.error("获取数据库最大连接数(节点名称：jdbc.maxConnectionCount)出现异常:"+ex);
		}
		return maxConnectionCount;
	}

	/***
	 * 获取数据库最小连接数
	 * @return
	 */
	public int getMinConnectionCount()
	{
		int minConnectionCount = 0;
		String minConnectionCountString = this.prop.getProperty("jdbc.minConnectionCount");
		try {
			minConnectionCount = Integer.parseInt(minConnectionCountString);
		} catch (Exception ex) {
			logger.error("获取数据库最大连接数(节点名称：jdbc.minConnectionCount)出现异常:"+ex);
		}
		return minConnectionCount;
	}

	/***
	 * 获取数据库等待超时时间
	 * @return
	 */
	public int getConnectionTimeout()
	{
		int ConnectionTimeout = 0;
		String connectionTimeoutString = this.prop.getProperty("jdbc.connectionTimeout");
		try {
			ConnectionTimeout = Integer.parseInt(connectionTimeoutString);
		} catch (Exception ex) {
			logger.error("获取数据库最大连接数(节点名称：jdbc.connectionTimeout)出现异常:"+ex);
		}	    
		return ConnectionTimeout;
	}

	/**
	 * 获取测试表的名称
	 * @return
	 */
	public String getTestTableName()
	{
		String tTableName = "";	
		try {
			tTableName = this.prop.getProperty("jdbc.testtablename");
		} catch (Exception ex) {
			logger.error("获取数据库测试表的名称(节点名称：jdbc.testtablename)出现异常:"+ex);
		}	    
		return tTableName;
	}

}
