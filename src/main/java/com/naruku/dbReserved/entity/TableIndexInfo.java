package com.naruku.dbReserved.entity;

/**
 * 表索引
 * @author herche
 * @data 2022/10/26
 */
public  class TableIndexInfo {
	/**
	 * 索引类型
	 */
	private String indexType;
	
	/**
	 * 索引方式
	 */
	private String indexStyle;
	
	/**
	 * 索引名称
	 */
	private String indexName;
	
	/**
	 * 索引字段
	 */
	private String indexFields;
	
	public String getIndexType() {
		return indexType;
	}
	
	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}
	
	public String getIndexStyle() {
		return indexStyle;
	}
	
	public void setIndexStyle(String indexStyle) {
		this.indexStyle = indexStyle;
	}
	
	public String getIndexName() {
		return indexName;
	}
	
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	
	public String getIndexFields() {
		return indexFields;
	}
	
	public void setIndexFields(String indexFields) {
		this.indexFields = indexFields;
	}
}
