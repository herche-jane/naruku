package com.naruku.dbReserved.export;

import com.naruku.dbReserved.DBConnection;
import com.naruku.dbReserved.entity.DataBaseInfo;
import com.naruku.dbReserved.entity.TableInfo;
import com.naruku.dbReserved.export.mysql.MyDbInfoExport;
import com.naruku.dbReserved.utils.WriterUtils;
import com.naruku.exception.UnkownDataSourceException;
import com.naruku.utils.StringUtl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

/**
 * DB导出SQL工具类 调用
 *
 * @author herche
 * @data 2022/10/27
 */
public class DBExportSqlUtil {
	private static final Logger logger = LoggerFactory.getLogger(DBExportSqlUtil.class);
	
	/**
	 * 如果库中指定了表信息，那么以表中为准 如果Table Info是空，那么直接使用库信息 这个时候dataBaseInfo中tableName必须是有值得 没有表值得话 直接返回空
	 *
	 * @param dataBaseInfo 库信息
	 * @param tableInfo    表信息
	 * @return 表信息 带上了表的 info
	 */
	public static TableInfo exportSql(DataBaseInfo dataBaseInfo, TableInfo tableInfo) {
		Boolean insert = true;
		// 判空
		if (!ObjectUtils.isEmpty(tableInfo) && StringUtl.hasText(tableInfo.getTableName())) {
			dataBaseInfo.setTableName(tableInfo.getTableName());
			logger.info("------开始导出sql------" + tableInfo.getTableName());
			insert = tableInfo.getInsert();
			dataBaseInfo.setInsert(insert);
		} else {
			if (ObjectUtils.isEmpty(dataBaseInfo.getTableName())) {
				return null;
			}
			logger.info("------开始导出sql------" + dataBaseInfo.getTableName());
			tableInfo = new TableInfo();
			tableInfo.setInsert(true);
			tableInfo.setTableName(dataBaseInfo.getTableName());
			tableInfo.setCreate(true);
			tableInfo.setInsert(true);
		}
		// 看看是否是insert
		if (dataBaseInfo.isInsert()) {
			return exportSqlWithInsert(dataBaseInfo, tableInfo);
		} else {
			try {
				return exportSqlWithOutInsert(dataBaseInfo, tableInfo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static List<String> getTableCount(DataBaseInfo dataBaseInfo, List<TableInfo> tableInfos,String filter) {
		logger.info("开始获取:" + dataBaseInfo.getDbName() + "数据库的总表数");
		List<String> filters = new ArrayList<>();
		if (StringUtils.hasText(filter)){
			filters = Arrays.asList(filter.split(","));
		}
		DbInfoExport dbInfoExport = getType(dataBaseInfo);
		List<String> tablesName = new ArrayList<>();
		if (!ObjectUtils.isEmpty(tableInfos)){
			logger.info("表一共有:" + tableInfos.size() +"张");
			logger.info("------开始过滤------");
			for (TableInfo tableInfo : tableInfos) {
				if (!filters.contains(tableInfo.getTableName())){
					tablesName.add(tableInfo.getTableName());
				}
			}
			logger.info("过滤完毕，表还剩:" + tablesName.size() + "张");
			return tablesName;
		}
		if (!StringUtils.hasText(dataBaseInfo.getTableName())) {
			String sql = dbInfoExport.getQueryAllTablesInfoSql(dataBaseInfo);
			// 执行这个sql
			Connection conn = null;
			Statement ps = null;
			ResultSet rs = null;
			
			try {
				conn = DBConnection.getConnection(dataBaseInfo);
				if (conn == null) {
					throw new Exception("获取数据库连接失败！");
				}
				ps = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = ps.executeQuery(sql);
				// 元数据
				logger.info("------开始过滤------");
				while (rs.next()) {
					if (!filters.contains(rs.getString("TABLE_NAME")))
					tablesName.add(rs.getString("TABLE_NAME"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		logger.info("过滤完毕，表还剩:" + tablesName.size() + "张");
		logger.info(dataBaseInfo.getDbName() + "数据库的总表数为：" + tablesName.size());
		return tablesName;
	}
	
	
	public static List<String> getTableCount(DataBaseInfo dataBaseInfo) {
		logger.info("开始获取:" + dataBaseInfo.getDbName() + "数据库的总表数");
		DbInfoExport dbInfoExport = getType(dataBaseInfo);
		List<String> tablesName = new ArrayList<>();
		if (!StringUtils.hasText(dataBaseInfo.getTableName())) {
			String sql = dbInfoExport.getQueryAllTablesInfoSql(dataBaseInfo);
			// 执行这个sql
			Connection conn = null;
			Statement ps = null;
			ResultSet rs = null;
			
			try {
				conn = DBConnection.getConnection(dataBaseInfo);
				if (conn == null) {
					throw new Exception("获取数据库连接失败！");
				}
				ps = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = ps.executeQuery(sql);
				// 元数据
				while (rs.next()) {
					tablesName.add(rs.getString("TABLE_NAME"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		logger.info(dataBaseInfo.getDbName() + "数据库的总表数为：" + tablesName.size());
		return tablesName;
	}
	
	private static TableInfo exportSqlWithOutInsert(DataBaseInfo dataBaseInfo, TableInfo tableInfo) throws IOException {
		FileOutputStream out = new FileOutputStream(dataBaseInfo.getFileUrl() + "/" + dataBaseInfo.getFileName(), true);
		//指定写入内容编码格式, 只有用OutputStreamWriter才能指定编码格式, 这个是FilterWriter没有的.
		BufferedWriter bufWriter = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
		//将写文件指针移到文件尾。
//		raf.seek(raf.length());
		DbInfoExport dbInfoExport = getType(dataBaseInfo);
		String tableName = dataBaseInfo.getTableName();
		// 如果表名不为空
		if (StringUtils.hasText(tableName)) {
			if (ObjectUtils.isEmpty(dbInfoExport)) {
				return null;
			}
			try {
				String sql = dbInfoExport.getTableDdl(dataBaseInfo, tableInfo);
				bufWriter.write(sql);
				tableInfo.setTableInitSql(sql);
				return tableInfo;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				bufWriter.close();
				out.close();
			}
		}
		return null;
	}
	
	/**
	 * 全库遍历 使用异步任务去实现 如果在库中指定了表 那么使用，没有指定的话则全库扫描
	 *
	 * @param dataBaseInfo 数据库信息
	 * @return 表信息
	 */
	public static TableInfo exportSqlWithInsert(DataBaseInfo dataBaseInfo, TableInfo tableInfo) {
		DbInfoExport dbInfoExport = getType(dataBaseInfo);
		String tableName = dataBaseInfo.getTableName();
		// 如果表名不为空
		if (StringUtils.hasText(tableName)) {
			if (ObjectUtils.isEmpty(dbInfoExport)) {
				return null;
			}
			try {
				String sql = dbInfoExport.getTableData(dataBaseInfo, tableInfo);
//				tableInfo.setTableInitSql(sql);
				return tableInfo;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static DbInfoExport getType(DataBaseInfo dataBaseInfo) {
		String dbType = dataBaseInfo.getDbType();
		DbInfoExport dbInfoExport = null;
		// 如果用户不传，根据驱动获取
		if (!StringUtils.hasText(dbType)) {
			dbType = DbInfoExport.getDbTypeByInfo(dataBaseInfo);
		}
		if ("MySql".equalsIgnoreCase(dbType)) {
			dbInfoExport = new MyDbInfoExport();
		}
		if (dataBaseInfo == null) {
			throw new UnkownDataSourceException("未匹配到合适的数据源");
		}
		// TODO 后续补上 Oracle 和 SQLSERVER
		logger.info("该数据库类型是：" + dbType);
		return dbInfoExport;
	}
	
	/**
	 * 单张表的导出
	 *
	 * @param dataBaseInfo
	 * @param tableInfo
	 */
	public static void export(DataBaseInfo dataBaseInfo, TableInfo tableInfo) {
		if ((!ObjectUtils.isEmpty(tableInfo) && StringUtils.hasText(tableInfo.getTableName())) || StringUtils.hasText(dataBaseInfo.getTableName())) {
			logger.info("准备导出：" + dataBaseInfo.getFileUrl() + "/" + dataBaseInfo.getFileName() + "文件");
			tableInfo = DBExportSqlUtil.exportSql(dataBaseInfo, tableInfo);
			logger.info("------开始------");
			// 拿到init SQL 导出
//			if (StringUtils.hasText(tableInfo.getTableInitSql())){
//				try {

//					boolean b = WriterUtils.writeToFile(tableInfo.getTableInitSql(), dataBaseInfo.getFileUrl() + "/" + dataBaseInfo.getFileName());
//					if (!b){
//						 TODO 导出失败
//						throw new IOException("导出" + dataBaseInfo.getFileUrl() + "/" + dataBaseInfo.getFileName() + "失败！");
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
			logger.info("导出：" + dataBaseInfo.getFileUrl() + "/" + dataBaseInfo.getFileName() + "文件成功吗？" + Files.exists(Paths.get(dataBaseInfo.getFileUrl() + "/" + dataBaseInfo.getFileName()), NOFOLLOW_LINKS));
		}
	}
	
	/**
	 * error.log 所有失败的表，以’,‘形式隔开
	 *
	 * @param errorFilePath
	 * @param dataBaseInfo
	 */
	public static void writeErrorLog(String errorFilePath, DataBaseInfo dataBaseInfo) {
		String errorLog = null;
		if (!Files.exists(Paths.get(errorFilePath), NOFOLLOW_LINKS)) {
			synchronized (DBExportSqlUtil.class) {
				if (!Files.exists(Paths.get(errorFilePath), NOFOLLOW_LINKS)) {
					errorLog = dataBaseInfo.getTableName();
					try {
						WriterUtils.writeAddFile(errorLog, errorFilePath);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
			}
		}
		try {
			WriterUtils.writeAddFile("," + errorLog, errorFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


