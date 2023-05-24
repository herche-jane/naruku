package com.naruku.dbReserved.entity;

/**
 * 表字段 元数据 下个版本用于过滤数据
 * @author herche
 * @data 2022/10/26
 */
public  class TableFiledInfo {
	
	/**
	 * 字段名字
	 */
	private String fieldName;
	
	/**
	 * 类型
	 */
	private String fieldType;
	
	/**
	 * 字段长度
	 */
	private String fieldLength;
	
	/**
	 * 小数长度
	 */
	private String fieldDecimalLength;
	
	/**
	 * 是否允许为空
	 */
	private String fieldIsEmpty;
	
	/**
	 * 是否主键
	 */
	private String fieldIsPrimaryKey;
	
	/**
	 * 是否自增长
	 */
	private String fieldIsAutoIncere;
	
	/**
	 * 默认值
	 */
	private String fieldDefaultValue;
	
	/**
	 * 字段注释
	 */
	private String fieldDesc;
	
	public String getFieldName() {
		return fieldName;
	}
	
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getFieldType() {
		return fieldType;
	}
	
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	
	public String getFieldLength() {
		return fieldLength;
	}
	
	public void setFieldLength(String fieldLength) {
		this.fieldLength = fieldLength;
	}
	
	public String getFieldDecimalLength() {
		return fieldDecimalLength;
	}
	
	public void setFieldDecimalLength(String fieldDecimalLength) {
		this.fieldDecimalLength = fieldDecimalLength;
	}
	
	public String getFieldIsEmpty() {
		return fieldIsEmpty;
	}
	
	public void setFieldIsEmpty(String fieldIsEmpty) {
		this.fieldIsEmpty = fieldIsEmpty;
	}
	
	public String getFieldIsPrimaryKey() {
		return fieldIsPrimaryKey;
	}
	
	public void setFieldIsPrimaryKey(String fieldIsPrimaryKey) {
		this.fieldIsPrimaryKey = fieldIsPrimaryKey;
	}
	
	public String getFieldIsAutoIncere() {
		return fieldIsAutoIncere;
	}
	
	public void setFieldIsAutoIncere(String fieldIsAutoIncere) {
		this.fieldIsAutoIncere = fieldIsAutoIncere;
	}
	
	public String getFieldDefaultValue() {
		return fieldDefaultValue;
	}
	
	public void setFieldDefaultValue(String fieldDefaultValue) {
		this.fieldDefaultValue = fieldDefaultValue;
	}
	
	public String getFieldDesc() {
		return fieldDesc;
	}
	
	public void setFieldDesc(String fieldDesc) {
		this.fieldDesc = fieldDesc;
	}
}
