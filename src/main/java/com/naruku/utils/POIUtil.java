package com.naruku.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class POIUtil {
	private final static String xls = "xls";
	private final static String xlsx = "xlsx";
	
	/**
	 * 读入excel文件，解析后返回
	 * @param file
	 * @throws IOException
	 */
	public static List<String[]> readExcel(File file){
		//文件路径
		XSSFWorkbook wb = null;
		try {
			wb = new XSSFWorkbook(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		//获取第一个工作表
		Sheet sheet = wb.getSheetAt(0);
		int lastRowNum = 0;
		Row row = sheet.getRow(lastRowNum);
		System.out.println(row);
		Cell cell = row.getCell(0);
		System.err.println(cell);
		return null;
	}
	
	public static void main(String[] args) {
		readExcel(new File("E:\\herche_work_qigao\\test\\33333.xlsx"));
	}
	
}
