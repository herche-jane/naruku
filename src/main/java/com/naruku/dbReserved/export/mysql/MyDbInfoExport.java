package com.naruku.dbReserved.export.mysql;

import com.naruku.dbReserved.DBConnection;
import com.naruku.dbReserved.entity.DataBaseInfo;
import com.naruku.dbReserved.entity.TableInfo;
import com.naruku.dbReserved.export.AbstractDbInfoExport;
import com.naruku.utils.StringUtl;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.UUID;

/**
 * Mysql导出的相关方法
 *
 * @author herche
 * @data 2022/10/26
 */
public class MyDbInfoExport extends AbstractDbInfoExport {
	@Override
	public String getQueryAllTablesInfoSql(DataBaseInfo dataBaseInfo) {
		String querySql = null;
		if (StringUtils.hasText(dataBaseInfo.getDbName())) {
			querySql = "select TABLE_NAME,TABLE_COMMENT from information_schema.tables t where table_schema= '" + dataBaseInfo.getDbName() + "'";
		}
		return querySql;
	}
	
	@Override
	public String getQueryTableFieldsInfoSql(DataBaseInfo dataBaseInfo, TableInfo tableInfo) {
		String querySql = null;
		if (StringUtils.hasText(dataBaseInfo.getDbName()) && StringUtils.hasText(tableInfo.getTableName())) {
			querySql = "SELECT DISTINCT t.COLUMN_NAME FIELD_NAME, t.DATA_TYPE FIELD_TYPE, IFNULL(IFNULL(t.NUMERIC_PRECISION,t.CHARACTER_MAXIMUM_LENGTH),'NULL') FIELD_LENGTH,  IFNULL(t.NUMERIC_SCALE,0) FIELD_DECIMAL_LENGTH, if(t.IS_NULLABLE='YES','允许','不允许') FIELD_IS_EMPTY, if(t.COLUMN_KEY='PRI','是','否') FIELD_IS_PRIMARY_KEY, IF( 	t.EXTRA = 'auto_increment', 	'是', 	'否' ) FIELD_IS_AUTOINCERE, IFNULL(t.COLUMN_DEFAULT,'NULL') FIELD_DEFAULT_VALUE, IFNULL(t.COLUMN_COMMENT,'NULL') FIELD_DESC FROM information_schema.COLUMNS t WHERE TABLE_SCHEMA = '" + dataBaseInfo.getDbName() + "' AND TABLE_NAME = '" + tableInfo.getTableName() + "'";
		}
		return querySql;
	}
	
	@Override
	public String getQueryTableIndexInfoSql(DataBaseInfo dataBaseInfo, TableInfo tableInfo) {
		String querySql = null;
		if (StringUtils.hasText(dataBaseInfo.getDbName()) && StringUtils.hasText(tableInfo.getTableName())) {
			querySql = "SELECT IF (NON_UNIQUE = 0, 'Unique', 'Normal') INDEX_TYPE, INDEX_TYPE INDEX_STYLE, UPPER(INDEX_NAME) INDEX_NAME, UPPER(GROUP_CONCAT(COLUMN_NAME)) INDEX_FIELDS FROM 	information_schema.STATISTICS WHERE 	TABLE_SCHEMA = '" + dataBaseInfo.getDbName() + "' AND TABLE_NAME = '" + tableInfo.getTableName() + "' GROUP BY INDEX_NAME ORDER BY INDEX_TYPE desc";
		}
		return querySql;
	}
	
	@Override
	public String getTableDdl(DataBaseInfo dataBaseInfo, TableInfo tableInfo) throws Exception {
//		sqlFileWriter.append("DROP TABLE IF EXISTS `" + table + "`;\n");
		StringBuilder sql = new StringBuilder();
		sql.append("USE " + dataBaseInfo.getDbName() + ";\n");
		sql.append("SET NAMES " + (StringUtils.hasText(tableInfo.getOrderType()) ? tableInfo.getOrderType() : "utf8mb4") + ";\n");
		sql.append("SET FOREIGN_KEY_CHECKS = 0;\n");
		sql.append("DROP TABLE IF EXISTS `" + tableInfo.getTableName() + "`;\n");
		String querySql = "show create table `" + tableInfo.getTableName() + "`";
		Connection conn = null;
		Statement ps = null;
		ResultSet rs = null;
		//建表语句
		String ddl = "";
		try {
			conn = DBConnection.getConnection(dataBaseInfo);
			if (conn == null) {
				throw new Exception("获取数据库连接失败！");
			}
			ps = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery(querySql);
			while (rs.next()) {
				ddl = rs.getString(rs.getMetaData().getColumnName(2));
			}
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			DBConnection.closeConnection(conn, ps, rs);
		}
		return sql.append(ddl).toString();
	}
	
	@Override
	public String getTableData(DataBaseInfo dataBaseInfo, TableInfo tableInfo) throws Exception {
		if (tableInfo == null) {
			tableInfo = new TableInfo();
			tableInfo.setTableName(dataBaseInfo.getTableName());
		}
		StringBuilder insertSql = new StringBuilder();
		String ddlSql = getTableDdl(dataBaseInfo, tableInfo);
		// 直接写
		// 遇到不规范形式的表，直接将其名改掉
		FileOutputStream out = null;
		BufferedWriter bufWriter = null;
		try {
			out = new FileOutputStream((dataBaseInfo.getFileUrl() + "/" + dataBaseInfo.getFileName()).replace(" ",""), true);
			//指定写入内容编码格式, 只有用OutputStreamWriter才能指定编码格式, 这个是FilterWriter没有的.
			bufWriter =  new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
		}catch (Exception e){
			e.printStackTrace();
			out = new FileOutputStream(dataBaseInfo.getFileUrl() + "/" + "不规则表" + UUID.randomUUID().toString().replace("-",""),true);
			//指定写入内容编码格式, 只有用OutputStreamWriter才能指定编码格式, 这个是FilterWriter没有的.
			bufWriter =  new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
		}
	
		bufWriter.write(ddlSql);
		bufWriter.flush();
		insertSql.append(ddlSql);
		String selectSql = "SELECT * FROM " + dataBaseInfo.getDbName() + "." + tableInfo.getTableName();
		Connection conn = null;
		Statement ps = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConnection(dataBaseInfo);
			if (conn == null) {
				throw new Exception("获取数据库连接失败！");
			}
			ps = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery(selectSql);
			// 元数据
			ResultSetMetaData metaData = rs.getMetaData();
			while (rs.next()) {
				int columnCount = metaData.getColumnCount();
				StringBuilder field = new StringBuilder();
				StringBuilder data = new StringBuilder();
				for (int i = 1; i <= columnCount; i++) {
					String fieldName = metaData.getColumnName(i);
					String value = rs.getString(i);
					String valueStr = StringUtl.toStringOrNull(value);
					field.append("`").append(fieldName).append("`").append(", ");
					if (!ObjectUtils.isEmpty(valueStr)) {
						// 值包含 ' 转义处理
						valueStr = StringUtl.replace(valueStr, "'", "\\'");
						// boolean 值处理
						if (StringUtl.equals("true", valueStr)) {
							data.append("b'1'");
						} else if (StringUtl.equals("false", valueStr)) {
							data.append("b'0'");
						} else {
							data.append("'").append(valueStr).append("'");
						}
					} else {
						data.append("NULL");
					}
					data.append(", ");
				}
//				insertSql.append(";INSERT INTO `" + tableInfo.getTableName() + "`(");
//				String fieldStr = field.toString().substring(0, field.length() - 2);
//				insertSql.append(fieldStr);
//				insertSql.append(") VALUES (");
//				String dataStr = data.toString().substring(0, data.length() - 2);
//				insertSql.append(dataStr);
//				insertSql.append(")\n");
				bufWriter.write(";INSERT INTO `" + tableInfo.getTableName() + "`(");
				bufWriter.write(field.toString().substring(0, field.length() - 2));
				bufWriter.write(") VALUES (");
				bufWriter.write(data.toString().substring(0, data.length() - 2));
				bufWriter.write(")\n");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBConnection.closeConnection(conn, ps, rs);
			bufWriter.close();
			out.close();
		}
		return insertSql.toString();
	}
	
	
}
