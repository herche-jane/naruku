package com.naruku.dbReserved.entity;


/**
 * 数据库基本信息 用于定义需要导出的数据
 * @author herche
 * @data 2022/10/26
 */
public  class DataBaseInfo {
	/**
	 * 数据库驱动
	 */
	private String dbDriver;
	
	/**
	 * 数据库地址
	 */
	private String dbUrl;
	
	/**
	 * 要导出的数据库名称
	 */
	private String dbName;
	
	/**
	 * 表名:不指定就全部输出 建议不要在这里指定，建议指定TableInfo
	 * 将会再下一个版本废弃这个属性
	 */
	@Deprecated
	private String tableName;
	
	/**
	 * 数据库登录用户名
	 */
	private String dbUserName;
	
	/**
	 * 数据库密码
	 */
	private String dbPassword;
	
	/**
	 * 文件导出目录
	 */
	private String fileUrl;
	
	/**
	 * 文件名称
	 */
	private String fileName;
	
	/**
	 * 线程数，线程池大小
	 */
	private Integer nThreads;
	
	/**
	 * dbReserved Type 可为空
	 */
	private String dbType;
	
	/**
	 * 当表数超过多少时候 创建线程池（默认8张 用户可以指定）
	 */
	private Integer maxTableWithThread = 8;
	
	
	/**
	 * 错误文件记录位置
	 */
	private String errorFilePath;
	
	/**
	 * 是否是Insert模式 默认是
	 * @return
	 */
	private boolean insert = true;
	
	/**
	 * 是否保留临时文件 默认保留
	 */
	private boolean deleteTemp = false;
	
	public String getDbDriver() {
		return dbDriver;
	}
	
	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}
	
	public String getDbUrl() {
		return dbUrl;
	}
	
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	
	public String getDbName() {
		return dbName;
	}
	
	
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	
	public String getDbUserName() {
		return dbUserName;
	}
	
	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}
	
	public String getDbPassword() {
		return dbPassword;
	}
	
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	
	public String getFileUrl() {
		return fileUrl;
	}
	
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
	/**
	 * 文件名 就是表名 + .sql
	 * @return
	 */
	public String getFileName() {
		return tableName + ".sql";
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Integer getnThreads() {
		return nThreads;
	}
	
	public void setnThreads(Integer nThreads) {
		this.nThreads = nThreads;
	}
	
	@Deprecated
	public String getTableName() {
		return tableName;
	}
	
	@Deprecated
	public void setTableName(String tableName) {
		this.tableName = tableName;
		this.fileName = tableName + ".sql";
	}
	
	public String getDbType() {
		return dbType;
	}
	
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	
	public boolean isInsert() {
		return insert;
	}
	
	public void setInsert(boolean insert) {
		this.insert = insert;
	}
	
	public Integer getMaxTableWithThread() {
		return maxTableWithThread;
	}
	
	public void setMaxTableWithThread(Integer maxTableWithThread) {
		this.maxTableWithThread = maxTableWithThread;
	}
	
	public String getErrorFilePath() {
		return errorFilePath;
	}
	
	public void setErrorFilePath(String errorFilePath) {
		this.errorFilePath = errorFilePath;
	}
	
	public boolean isDeleteTemp() {
		return deleteTemp;
	}
	
	public void setDeleteTemp(boolean deleteTemp) {
		this.deleteTemp = deleteTemp;
	}
}
