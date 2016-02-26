package com.hubao.tv.felix.dbhelper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;



/***
 * 基层的数据库者，就是用来查询数据库啦
 * 
 * @author Felix
 * 
 */

public class DBconner {

	public static Logger logger = Logger.getLogger(DBconner.class);
	public String paths = this.getClass().getClassLoader().getResource("")
			.getPath();
	DBconnetionPoolManager cpManager = new DBconnetionPoolManager();

	/***
	 * 执行查询方法，实现比较简单但是使用比较灵活
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public ResultSet executeQuery(String sql, LinkedList<Object> params) {
		DBconnetionPoolManager cpManager = new DBconnetionPoolManager();
		PreparedStatement pstmt = null;
		try {
			Connection conn = cpManager.getConnection();

			pstmt = conn.prepareStatement(sql);
			if (params != null) {
				int i = 1;
				for (Object p : params) {
					pstmt.setObject(i++, p);
				}
			}
			return pstmt.executeQuery();
		} catch (Exception ex) {
			logger.error("数据库连接类DBconner中executeQuery方法出现异常：" + ex.getMessage());
		}
		return null;
	}

	/***
	 * 执行增删改方法，实现比较简单但是使用比较灵活
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public boolean executeUpdateQuery(String sql, LinkedList<Object> params) {
		PreparedStatement pstmt = null;
		try {

			Connection conn = cpManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			if (params != null) {
				int i = 1;
				for (Object p : params) {
					pstmt.setObject(i++, p);
				}
			}
			int num = pstmt.executeUpdate();
			cpManager.returnConnection(conn);
			return num > 0 ? true : false;
		} catch (Exception ex) {
			logger.error("数据库连接类DBconner中executeUpdateQuery方法出现异常："
					+ ex.getMessage());
			return false;
		}
	}

	/***
	 * 执行批量的增删改方法
	 * 
	 * @param paramvalueSQL
	 *            带有参数的需要执行的SQL语句几个
	 * @return 执行状态（成功与否）
	 * @throws SQLException
	 */
	public boolean executeListUpdateAndSaveQuery(List<String> paramvalueSQL)
			throws Exception {
		Connection conn = null;
		try {

			conn = cpManager.getConnection();
			if (conn != null) {
				Statement pstmt = null;
				boolean flag = false;

				/****关闭事务自动提交*****/
				conn.setAutoCommit(false);
				pstmt = conn.createStatement();
				for (int i = 0; i < paramvalueSQL.size(); i++) {
					//批量执行的SQL语句（必须要填充参数的）
					pstmt.addBatch(paramvalueSQL.get(i));
				}

				pstmt.executeBatch();
				conn.commit();
				/****打开事务自动提交*****/
				conn.setAutoCommit(true);
				/* 释放连接池 */
				cpManager.returnConnection(conn);
				return true;
			} else {
				logger.error("获取数据库连接失败，请检查数据库连接字符串");
				return false;
			}
		} catch (Exception ex) {
			// TODO: handle exception
			conn.rollback();// 会抛出异常，要捕获
			logger.error("通用的增删改查方法executeListUpdateAndSaveQuery出现异常"
					+ ex.getMessage());
			return false;
		}
	}

	/***
	 * 执行增删改方法
	 * 
	 * @param sql
	 *            执行的语句
	 * @param params
	 *            sql参数列表
	 * @return 执行状态（成功与否）
	 * @throws SQLException
	 */
	public boolean executeUpdateAndSaveQuery(String sql, List<Object> params) {

		try {

			Connection conn = cpManager.getConnection();
			if (conn != null) {
				PreparedStatement pstmt = null;
				boolean flag = false;
				int result = -1;
				pstmt = conn.prepareStatement(sql);
				int index = 1;
				if (params != null && !params.isEmpty()) {
					for (int i = 0; i < params.size(); i++) {
						pstmt.setObject(index++, params.get(i));
					}
				}
				result = pstmt.executeUpdate();
				/* 释放连接池 */
				cpManager.returnConnection(conn);
				flag = result > 0 ? true : false;
				return flag;
			} else {
				logger.error("获取数据库连接失败，请检查数据库连接字符串");
				return false;
			}
		} catch (Exception ex) {
			// TODO: handle exception
			logger.error("通用的增删改查方法executeUpdateAndSaveQuery出现异常"
					+ ex.getMessage());
			return false;
		}
	}

	/***
	 * 执行增删改方法
	 * 
	 * @param Pro
	 *            执行的存储过程
	 * @param params
	 *            sql参数列表
	 * @return 执行状态（成功与否）
	 * @throws SQLException
	 */
	public boolean executeUpdateAndSaveQueryByProcedure(String Pro,
			List<Object> params) {
		try {

			Connection conn = cpManager.getConnection();
			PreparedStatement pstmt = null;
			boolean flag = false;
			int result = -1;
			pstmt = conn.prepareCall(Pro);
			int index = 1;
			if (params != null && !params.isEmpty()) {
				for (int i = 0; i < params.size(); i++) {
					pstmt.setObject(index++, params.get(i));
				}
			}
			result = pstmt.executeUpdate();
			/* 释放连接池 */
			cpManager.returnConnection(conn);
			flag = result > 0 ? true : false;
			return flag;
		} catch (Exception ex) {
			// TODO: handle exception
			logger.error("通用的增删改查方法executeUpdateAndSaveQueryByPro出现异常"
					+ ex.getMessage());
			return false;
		}
	}

	
	/**
	 * 查询单条记录（获取别名）
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> QueryOneResultBySmallName(String sql, List<Object> params)
			throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();

		ResultSet resultSet;
		try {
			Connection conn = cpManager.getConnection();
			PreparedStatement pstmt = null;
			int index = 1;
			pstmt = conn.prepareStatement(sql);
			if (params != null && !params.isEmpty()) {
				for (int i = 0; i < params.size(); i++) {
					pstmt.setObject(index++, params.get(i));
				}
			}
			resultSet = pstmt.executeQuery();// 返回查询结果
			ResultSetMetaData metaData = resultSet.getMetaData();
			int col_len = metaData.getColumnCount();
			while (resultSet.next()) {
				for (int i = 0; i < col_len; i++) {
					/** 列名 **/
					String cols_name = metaData.getColumnLabel(i + 1);
					/** 列值 **/
					Object cols_value = resultSet.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
			}
			cpManager.returnConnection(conn);
			return map;

		} catch (Exception ex) {
			// TODO: handle exception
			logger.error("查询单条记录方法QueryOneResultByAlias方法出现异常" + ex.getMessage());
			return null;
		}
	}
	
	/**
	 * 查询单条记录
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> QueryOneResult(String sql, List<Object> params)
			throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();

		ResultSet resultSet;
		try {
			Connection conn = cpManager.getConnection();
			PreparedStatement pstmt = null;
			int index = 1;
			pstmt = conn.prepareStatement(sql);
			if (params != null && !params.isEmpty()) {
				for (int i = 0; i < params.size(); i++) {
					pstmt.setObject(index++, params.get(i));
				}
			}
			resultSet = pstmt.executeQuery();// 返回查询结果
			ResultSetMetaData metaData = resultSet.getMetaData();
			int col_len = metaData.getColumnCount();
			while (resultSet.next()) {
				for (int i = 0; i < col_len; i++) {
					/** 列名 **/
					String cols_name = metaData.getColumnName(i + 1);
					/** 列值 **/
					Object cols_value = resultSet.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
			}
			cpManager.returnConnection(conn);
			return map;

		} catch (Exception ex) {
			// TODO: handle exception
			logger.error("查询单条记录方法QueryOneResult方法出现异常" + ex.getMessage());
			return null;
		}
	}

	/**
	 * 查询多条记录
	 * 
	 * @param pro
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> QueryListResultByProcedure(String pro,
			List<Object> params) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int index = 1;
		try {
			Connection conn = cpManager.getConnection();
			ResultSet resultSet;
			PreparedStatement pstmt = null;
			pstmt = conn.prepareStatement(pro);
			if (params != null && !params.isEmpty()) {
				for (int i = 0; i < params.size(); i++) {
					pstmt.setObject(index++, params.get(i));
				}
			}
			resultSet = pstmt.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();
			int cols_len = metaData.getColumnCount();
			while (resultSet.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < cols_len; i++) {
					String cols_name = metaData.getColumnName(i + 1);
					Object cols_value = resultSet.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
			cpManager.returnConnection(conn);
			return list;
		} catch (Exception ex) {
			// TODO: handle exception
			logger.error("通用查询多条记录的SQL方法QueryListResult出现异常" + ex.getMessage());
			return null;
		}
	}

	/**
	 * 查询多条记录(获取字段原名)
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> QueryListResult(String sql,
			List<Object> params) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int index = 1;

		try {
			Connection conn = cpManager.getConnection();
			ResultSet resultSet;
			PreparedStatement pstmt = null;
			pstmt = conn.prepareStatement(sql);
			if (params != null && !params.isEmpty()) {
				for (int i = 0; i < params.size(); i++) {
					pstmt.setObject(index++, params.get(i));
				}
			}
			resultSet = pstmt.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();
			int cols_len = metaData.getColumnCount();
			while (resultSet.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < cols_len; i++) {
					String cols_name = metaData.getColumnName(i + 1);
					Object cols_value = resultSet.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
			cpManager.returnConnection(conn);
			return list;
		} catch (Exception ex) {
			// TODO: handle exception
			logger.error("通用查询多条记录的SQL方法QueryListResult出现异常" + ex.getMessage());
			return null;
		}
	}
	
	
	/**
	 * 查询数据库多条记录(获取字段别名)
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> QueryListResultBySmallName(String sql,
			List<Object> params) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int index = 1;

		try {
			Connection conn = cpManager.getConnection();
			ResultSet resultSet;
			PreparedStatement pstmt = null;
			pstmt = conn.prepareStatement(sql);
			if (params != null && !params.isEmpty()) {
				for (int i = 0; i < params.size(); i++) {
					pstmt.setObject(index++, params.get(i));
				}
			}
			resultSet = pstmt.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();
			int cols_len = metaData.getColumnCount();
			while (resultSet.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < cols_len; i++) {
					String cols_name = metaData.getColumnLabel(i + 1);
					Object cols_value = resultSet.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
			cpManager.returnConnection(conn);
			return list;
		} catch (Exception ex) {
			// TODO: handle exception
			logger.error("通用查询多条记录的SQL方法QueryListResultByAlias出现异常" + ex.getMessage());
			return null;
		}
	}

	/**
	 * 通过反射机制查询单条记录
	 * 
	 * @param sql
	 * @param params
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public <T> T QueryOneResultRefResult(String sql, List<Object> params,
			Class<T> cls) {
		T resultObject = null;
		int index = 1;

		try {
			Connection conn = cpManager.getConnection();
			ResultSet resultSet;
			PreparedStatement pstmt = null;
			pstmt = conn.prepareStatement(sql);
			if (params != null && !params.isEmpty()) {
				for (int i = 0; i < params.size(); i++) {
					pstmt.setObject(index++, params.get(i));
				}
			}
			resultSet = pstmt.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();
			int cols_len = metaData.getColumnCount();
			while (resultSet.next()) {
				// 通过反射机制创建一个实例
				resultObject = cls.newInstance();
				for (int i = 0; i < cols_len; i++) {
					String cols_name = metaData.getColumnName(i + 1);
					Object cols_value = resultSet.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}
					Field field = cls.getDeclaredField(cols_name);
					field.setAccessible(true); // 打开javabean的访问权限
					field.set(resultObject, cols_value);
				}
			}
			cpManager.returnConnection(conn);
			return resultObject;
		} catch (Exception ex) {
			// TODO: handle exception
			logger.error("通用的查询单个实例方法QueryOneResultRefResult出现异常:"
					+ ex.getMessage());
			return null;
		}

	}

	/**
	 * 通过反射机制查询多条记录
	 * 
	 * @param sql
	 * @param params
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> QueryListRefResult(String sql, List<Object> params,
			Class<T> cls) {
		List<T> list = new ArrayList<T>();
		int index = 1;

		try {
			Connection conn = cpManager.getConnection();
			ResultSet resultSet;
			PreparedStatement pstmt = null;
			pstmt = conn.prepareStatement(sql);
			if (params != null && !params.isEmpty()) {
				for (int i = 0; i < params.size(); i++) {
					pstmt.setObject(index++, params.get(i));
				}
			}
			resultSet = pstmt.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();
			int cols_len = metaData.getColumnCount();
			while (resultSet.next()) {
				// 通过反射机制创建一个实例
				T resultObject = cls.newInstance();
				for (int i = 0; i < cols_len; i++) {
					String cols_name = metaData.getColumnName(i + 1);
					Object cols_value = resultSet.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}
					Field field = cls.getDeclaredField(cols_name);
					field.setAccessible(true); // 打开javabean的访问权限
					field.set(resultObject, cols_value);
				}
				list.add(resultObject);
			}
			cpManager.returnConnection(conn);
			return list;
		} catch (Exception ex) {
			// TODO: handle exception
			logger.error("通用的查询多条记录方法QueryListRefResult出现异常:" + ex.getMessage());
			return null;
		}
	}

	/***
	 * 测试连接池是否正常的
	 * 
	 * @param str
	 * @throws Exception
	 */
	public static void main(String[] str) throws Exception {

		// DBconnetionPoolManager cpManager = new DBconnetionPoolManager();
		// connection = cpManager.getConnection();
		// DBconner dc = new DBconner();
		// ResultSet rSet = dc.executeQuery("select * from user_info", null);
		// logger.debug(rSet.next());

		String sql = "select * from t_User";
		DBconner dc = new DBconner();
		List<Map<String, Object>> wlist = dc.QueryListResultBySmallName(
				sql, null);
		for (int i = 0; i < wlist.size(); i++) {
	            String a=  wlist.get(i).get("name").toString();
		}

		/***
		 * connection = cpManager.getConnection(); DBconner dc1 = new
		 * DBconner(); ResultSet rSet1 =
		 * dc.executeQuery("select * from user_info", null);
		 * cpManager.returnConnection(connection); logger.debug(rSet1.next());
		 * 
		 * connection = cpManager.getConnection(); DBconner dc2 = new
		 * DBconner(); ResultSet rSet2 =
		 * dc.executeQuery("select * from user_info", null);
		 * logger.debug(rSet.next());
		 */
		//
		// int num =dc.paths.indexOf("WEB-INF");
		// dc.paths=dc.paths.substring(0, num+"WEB-INF".length());
		//
		// File file=new File(dc.paths+"/resources/promesage.properties");
		//
		// logger.debug(dc.paths+"/resources/promesage.properties");
		// logger.debug(file.exists());

	}

}
