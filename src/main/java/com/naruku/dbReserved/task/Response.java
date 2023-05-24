package com.naruku.dbReserved.task;

import com.naruku.dbReserved.entity.DataBaseInfo;

/**
 * 响应类 单个任务的响应类
 * @author herche
 * @date 2022/10/26
 */
public class Response {
	
	public boolean success;
	public String tableName;
	public String errorMsg;
	public DataBaseInfo dataBaseInfo;
	
	public boolean isSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public String getFileName() {
		return tableName;
	}
	
	public void setTableName(String fileName) {
		this.tableName = fileName;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public DataBaseInfo getDataBaseInfo() {
		return dataBaseInfo;
	}
	
	public void setDataBaseInfo(DataBaseInfo dataBaseInfo) {
		this.dataBaseInfo = dataBaseInfo;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}
