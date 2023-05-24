package com.naruku.excl;

import com.naruku.handler.RowHandler;
import com.naruku.sax.ExcelSaxReader;
import org.apache.poi.poifs.filesystem.FileMagic;

import java.io.File;

public class ExcelUtil {
	
	/**
	 * xls的ContentType
	 */
	public static final String XLS_CONTENT_TYPE = "application/vnd.ms-excel";
	
	/**
	 * xlsx的ContentType
	 */
	public static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	
	/**
	 * 通过Sax方式读取Excel，同时支持03和07格式
	 *
	 * @param path       Excel文件路径
	 * @param rid        sheet rid，-1表示全部Sheet, 0表示第一个Sheet
	 * @param rowHandler 行处理器
	 * @since 3.2.0
	 */
	public static void readBySax(String path, int rid, RowHandler rowHandler) {
		readBySax(new File(path), rid, rowHandler);
	}
	
	
	/**
	 * 通过Sax方式读取Excel，同时支持03和07格式
	 *
	 * @param file       Excel文件
	 * @param rid        sheet rid，-1表示全部Sheet, 0表示第一个Sheet
	 * @param rowHandler 行处理器
	 * @since 3.2.0
	 */
	public static void readBySax(File file, int rid, RowHandler rowHandler) {
		boolean isXlsx = file.getName().endsWith(".xlsx") && !file.getName().endsWith(".xls");
		final ExcelSaxReader<?> reader = ExcelSaxUtil.createSaxReader(isXlsx, rowHandler);
		reader.read(file, rid);
	}
	
}
