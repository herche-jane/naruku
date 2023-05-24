package com.naruku.sax;

import com.naruku.exception.POIException;

import java.io.File;

public interface ExcelSaxReader<T> {
	
	// sheet r:Id前缀
	String RID_PREFIX = "rId";
	// sheet name前缀
	String SHEET_NAME_PREFIX = "sheetName:";
	
	/**
	 * 开始读取Excel
	 *
	 * @param file Excel文件
	 * @param idOrRidOrSheetName Excel中的sheet id或者rid编号或sheet名称，rid必须加rId前缀，例如rId1，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException POI异常
	 */
	T read(File file, String idOrRidOrSheetName) throws POIException;
	
	
	default T read(File file, int rid) throws POIException{
		return read(file, String.valueOf(rid));
	}
	
	void close();
}
