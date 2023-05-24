package com.naruku.dbReserved;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.naruku.dbReserved.entity.DataBaseInfo;

import java.sql.*;

/**
 * 数据库连接（获取、关闭）
 * @author herche
 * @data 2022/10/26
 */
public class DBConnection {
	private static DruidDataSource druidDataSource = null;
	private static Object lockObj = new Object();
	
	public static DruidDataSource getDs(DataBaseInfo dbInfo) throws SQLException {
		if (druidDataSource == null){
			synchronized (lockObj){
				if (druidDataSource == null){
					druidDataSource = new DruidDataSource();
					druidDataSource.setDriverClassName(dbInfo.getDbDriver());
					druidDataSource.setUsername(dbInfo.getDbUserName());
					druidDataSource.setUrl(dbInfo.getDbUrl());
					druidDataSource.setPassword(dbInfo.getDbPassword());
				}
			}
		}
		return druidDataSource;
	}
	/**
	 *
	 * @param dbInfo ,数据库连接相关信息
	 * @return
	 */
	public static Connection getConnection(DataBaseInfo dbInfo) throws SQLException {
		if (druidDataSource == null){
			synchronized (lockObj){
				if (druidDataSource == null){
					druidDataSource = new DruidDataSource();
					druidDataSource.setDriverClassName(dbInfo.getDbDriver());
					druidDataSource.setUsername(dbInfo.getDbUserName());
					druidDataSource.setUrl(dbInfo.getDbUrl());
					druidDataSource.setPassword(dbInfo.getDbPassword());
				}
			}
		}
		DruidPooledConnection connection = druidDataSource.getConnection();
		connection.setAutoCommit(false);
		return connection;
	}
	
	/**
	 *  关闭数据源连接
	 * @param conn(Connection)
	 * @param st(Statement)
	 * @param rs(ResultSet)
	 */
	public static void closeConnection(Connection conn, Statement st, ResultSet rs){
		try {
			if (rs != null) {
				rs.close();
			}
			if (st != null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
}
