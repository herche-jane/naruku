package com.naruku.utils;

import com.naruku.excl.ExcelUtil;
import com.naruku.handler.reader.ReadRowsHandler;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadUtils {
	public static List<List<Object>> readRows(String filePath, int startRows, int endRows) {
//		if (startRows == endRows) {
//			// 使用poi读取
//			try {
//				read(filePath,startRows);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		ReadRowsHandler readRowsHandler = new ReadRowsHandler(startRows, endRows);
		ExcelUtil.readBySax(new File(filePath), -1, readRowsHandler);
		return readRowsHandler.getRows();
	}
	
	public static List<List<Object>> read(String filePath, int startRows) throws IOException {
		if (!StringUtils.hasText(filePath)) {
			return null;
		}
		//获取文件路径
		File file = new File(filePath);
		//获取文件后缀
		String finishFile = filePath.substring(filePath.lastIndexOf(".") + 1);
		InputStream inputStream = new FileInputStream(file);
		//这里需要注意XSSFWorkbook能够处理xlsx的文件
		//而HSSFWorkbook能够处理xls的文件，不然会报错，这样写是为了更好的兼容处理两种格式
		Workbook workbook;
		if ("xlsx".equals(finishFile)) {
			workbook = new XSSFWorkbook(inputStream);
		} else {
			workbook = new HSSFWorkbook(inputStream);
		}
		//这里是读取第几个sheet
		Sheet sheet = workbook.getSheetAt(0);
		//1.找到所有的元素对象，然后用list存放
		Row row = sheet.getRow(startRows);//获取每一行
		int columns = 0;
		//获取每一行的最后一列的列号，即总列数,这里需要注意一下，
		//这种方法读取第一行的列数的时候会多读取一列，所以这里要减1
		columns = row.getLastCellNum() - 1;
		List<List<Object>> lists = new ArrayList<>();
		List<Object> list = new ArrayList<>();
		for (int j = 0; j < columns; j++) {
			//获取每个单元格
			Cell cell = row.getCell(j);
			//设置单元格类型
			cell.setCellType(CellType.STRING);
			//获取单元格数据
			list.add(cell.getStringCellValue());
		}
		lists.add(list);
		//接下来就是自己去拼接数据格式了,相信各位大佬这里应该没有问题，以下代码就不贴了
		workbook.close();
		inputStream.close();
		return lists;
	}
}
