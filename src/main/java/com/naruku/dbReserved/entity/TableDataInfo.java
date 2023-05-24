package com.naruku.dbReserved.entity;

import java.util.List;

/**
 * 数据 元数据
 * @author herche
 * @data 2022/10/26
 */
public  class TableDataInfo {
	// 一行的值
	private List<Object> value;
	
	// TODO  是否过滤这一行数据 0 否 1 是
	private String isFilter;
	
	// 是否是最后一行 0 否 1 是
	private String isLast;
	
	// 是否是第一行 0 否 1是
	private String isFirst;

	// 是否是空行 0 否 1是
	private String isNull;
	
	
}
