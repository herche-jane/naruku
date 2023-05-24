package com.naruku.dbReserved.entity;

import java.util.List;

/**
 * 表 元数据
 * @author herche
 * @data 2022/10/26
 */
public  class TableInfo {
	
	/**
	 * 表名
	 */
	private String tableName;
	
	/**
	 * 字符集
	 */
	private String orderType= "";
	/**
	 * 存储引擎
	 */
	private String storageEngine= "";
	
	/**
	 * 表描述
	 */
	private String tableDesc;
	
	/**
	 * 表初始化脚本
	 */
	private String tableInitSql;
	
	/**
	 * 表字段信息
	 */
	private List<TableFiledInfo> tableFiledInfoList;
	
	/**
	 * 表索引信息
	 */
	private List<TableIndexInfo> tableIndexInfoList;
	
	
	/**
	 * 表数据信息
	 */
	private List<TableDataInfo> tableDataInfoList;
	
	
	/**
	 * 是否需要 create 建表语句，默认需要
	 */
	public Boolean create = true;
	/**
	 * 是否需要 insert 语句，默认需要
	 */
	public Boolean insert = true;
	
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getOrderType() {
		return orderType;
	}
	
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	public String getStorageEngine() {
		return storageEngine;
	}
	
	public void setStorageEngine(String storageEngine) {
		this.storageEngine = storageEngine;
	}
	
	public String getTableDesc() {
		return tableDesc;
	}
	
	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}
	
	public String getTableInitSql() {
		return tableInitSql;
	}
	
	public void setTableInitSql(String tableInitSql) {
		this.tableInitSql = tableInitSql;
	}
	
	public List<TableFiledInfo> getTableFiledInfoList() {
		return tableFiledInfoList;
	}
	
	public void setTableFiledInfoList(List<TableFiledInfo> tableFiledInfoList) {
		this.tableFiledInfoList = tableFiledInfoList;
	}
	
	public List<TableIndexInfo> getTableIndexInfoList() {
		return tableIndexInfoList;
	}
	
	public void setTableIndexInfoList(List<TableIndexInfo> tableIndexInfoList) {
		this.tableIndexInfoList = tableIndexInfoList;
	}
	
	
	public List<TableDataInfo> getTableDataInfoList() {
		return tableDataInfoList;
	}
	
	public void setTableDataInfoList(List<TableDataInfo> tableDataInfoList) {
		this.tableDataInfoList = tableDataInfoList;
	}
	
	public Boolean getCreate() {
		return create;
	}
	
	public void setCreate(Boolean create) {
		this.create = create;
	}
	
	public Boolean getInsert() {
		return insert;
	}
	
	public void setInsert(Boolean insert) {
		this.insert = insert;
	}
}
