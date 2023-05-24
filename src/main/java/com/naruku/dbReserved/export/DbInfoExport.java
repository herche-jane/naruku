package com.naruku.dbReserved.export;

import com.naruku.dbReserved.entity.DataBaseInfo;
import com.naruku.dbReserved.entity.TableInfo;

/**
 * 导出基接口
 *
 * @author herche
 * @data 2022/10/26
 */
public interface DbInfoExport {
	/**
	 * 获取数据库所有表名
	 *
	 * @param dataBaseInfo
	 * @return
	 */
	public String getQueryAllTablesInfoSql(DataBaseInfo dataBaseInfo);
	
	
	/**
	 * 根据表名，获取数据库表的字段信息
	 *
	 * @param dataBaseInfo
	 * @param tableInfo
	 * @return
	 */
	public String getQueryTableFieldsInfoSql(DataBaseInfo dataBaseInfo, TableInfo tableInfo);
	
	/**
	 * 根据表名，获取表的索引信息
	 *
	 * @param dataBaseInfo
	 * @param tableInfo
	 * @return
	 */
	public String getQueryTableIndexInfoSql(DataBaseInfo dataBaseInfo, TableInfo tableInfo);
	
	/**
	 * 根据表名，获取表的ddl建表语句
	 *
	 * @param dataBaseInfo
	 * @param tableInfo
	 * @return
	 */
	public String getTableDdl(DataBaseInfo dataBaseInfo, TableInfo tableInfo) throws Exception;
	
	
	/**
	 * 根据表名，导出表结构和表数据
	 *
	 * @param dataBaseInfo
	 * @param tableInfo
	 * @return
	 */
	public String getTableData(DataBaseInfo dataBaseInfo, TableInfo tableInfo) throws Exception;
	
	
	
	
	public static String getDbTypeByInfo(DataBaseInfo dataBaseInfo){
		String dbDriver = dataBaseInfo.getDbDriver();
		if (dbDriver.contains("mysql"))return "mysql";
		else if (dbDriver.contains("oracle"))return "oracle";
		else if (dbDriver.contains("sqlserver"))return "sql server";
		else if(dbDriver.contains("h2"))return "h2";
		return null;
		
	}
}
