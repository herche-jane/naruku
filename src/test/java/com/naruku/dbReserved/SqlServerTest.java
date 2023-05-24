package com.naruku.dbReserved;

import com.naruku.dbReserved.entity.DataBaseInfo;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlServerTest {
	
	@Test
	public void test() throws SQLException {
		DataBaseInfo dbInfo = new DataBaseInfo();
		dbInfo.setDbDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		dbInfo.setDbUserName("SA");
		dbInfo.setDbPassword("123456@zl");
		String url = "jdbc:sqlserver://192.168.5.151:1433;DatabaseName=SA";
		dbInfo.setDbUrl(url);
		Connection connection = DBConnection.getConnection(dbInfo);
		System.out.println(connection);
		String sql = "insert into a_language values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			// 获取pstmt对象
			PreparedStatement pstmt = connection.prepareStatement(sql);
// 设置？的值
			pstmt.setString(1, "aaaa");
			pstmt.setString(2, "bbbb");
			pstmt.setString(3, "cccc");
			pstmt.setString(4, "aaaa");
			pstmt.setString(5, "bbbb");
			pstmt.setString(6, "cccc");
			pstmt.setString(7, "aaaa");
			pstmt.setString(8, "bbbb");
			pstmt.setString(9, "cccc");
			pstmt.setString(10, "aaaa");
			pstmt.setString(11, "bbbb");
			pstmt.setString(12, "cccc");
			pstmt.setString(13, "aaaa");
			pstmt.setString(14, "bbbb");
			pstmt.setString(15, "cccc");
			pstmt.setString(16, "cccc");
			pstmt.setString(17, "aaaa");
			pstmt.setString(18, "bbbb");
			pstmt.setString(19, "cccc");
			pstmt.setString(20, "cccc");
			pstmt.setString(21, "aaaa");
			pstmt.setString(22, "bbbb");
			pstmt.setString(23, "cccc");
			pstmt.setString(24, "aaaa");
			pstmt.setString(25, "bbbb");
			pstmt.setString(26, "cccc");
			pstmt.setString(27, "aaaa");
			pstmt.setString(28, "bbbb");
			pstmt.setString(29, "cccc");
			pstmt.setString(30, "cccc");
			pstmt.setString(31, "aaaa");
			pstmt.setString(32, "bbbb");
			pstmt.setString(33, "cccc");
			pstmt.setString(34, "aaaa");
			pstmt.setString(35, "bbbb");
			pstmt.setString(36, "cccc");
			pstmt.setString(37, "aaaa");
			pstmt.setString(38, "bbbb");
			pstmt.setString(39, "cccc");
			pstmt.setString(40, "aaaa");
			pstmt.setString(41, "aaaa");
			pstmt.setString(42, "bbbb");
			pstmt.setString(43, "cccc");
			pstmt.setString(44, "aaaa");
			pstmt.setString(45, "bbbb");
			pstmt.setString(46, "cccc");
			pstmt.setString(47, "aaaa");
			pstmt.setString(48, "bbbb");
			pstmt.setString(49, "cccc");
			pstmt.setString(50, "aaaa");
		}
		long end = System.currentTimeMillis();
	}
}
