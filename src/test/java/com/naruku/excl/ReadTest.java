package com.naruku.excl;

import com.naruku.ExclToCsv;
import com.naruku.handler.CsvHandler;
import com.naruku.handler.TestHandler;
import com.naruku.utils.ReadUtils;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * 读测试类
 */
public class ReadTest {
	// 填充字符串
	public static final char CELL_FILL_CHAR = '@';
	// 列的最大位数
	public static final int MAX_CELL_BIT = 3;
	
	@Test
	public void readExcl() throws IOException {
		long l = System.currentTimeMillis();
		File file = new File("E:\\herche_work_qigao\\112.xlsx");
		FileWriter writer = new FileWriter("E:\\herche_work_qigao\\311.csv");
//		CsvHandler csvHandler = new CsvHandler("E:\\herche_work_qigao\\311.csv");
//		ExcelUtil.readBySax(file, 0, csvHandler);
		long l1 = System.currentTimeMillis();
		System.out.println(l - l1);
	}
	
	@Test
	public void readXls() throws IOException {
//		long l = System.currentTimeMillis();
//		File file = new File("E:\\herche_work_qigao\\111.xls");
//		FileWriter fileWriter = new FileWriter("E:\\herche_work_qigao\\133.csv");
//		CsvHandler csvHandler = new CsvHandler(fileWriter);
//		ExcelUtil.readBySax(file, 0, csvHandler);
		List<List<Object>> lists = ReadUtils.readRows("E:\\herche_work_qigao\\test\\www.xlsx", 0, 0);
		System.out.println(lists);
//		long l1 = System.currentTimeMillis();
//		System.out.println(l1 - l);
//		fileWriter.flush();
//		fileWriter.close();
	}
	
	@Test
	public void exclToCsv() throws FileNotFoundException, UnsupportedEncodingException {
		Logger logger = LoggerFactory.getLogger(ReadTest.class);
//		ExclToCsv.exclToCsvUtf8("E:\\herche_test\\www.xls","E:\\herche_test\\www111.csv",logger);
	}
	
	@Test
	public void testRead() {
		long start = System.currentTimeMillis();
		List<List<Object>> lists = ReadUtils.readRows("E:\\herche_work_qigao\\2-2\\aaa.xlsx", 0, 0);
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		System.out.println(lists);
	}
	
	@Test
	public void covertCsv() throws FileNotFoundException, UnsupportedEncodingException {
		Logger logger = LoggerFactory.getLogger(ReadTest.class);
		ExclToCsv.exclToCsvUtf8("E:\\herche_work_qigao\\test1\\21年历史库存1.10.xlsx", "E:\\herche_test\\123456.csv", logger, 1);
	}
	
	@Test
	public void read() {
//		LOGGER.info("本服务存在，直接开读！");
		int rowNum = 5;
		List<List<Object>> lists = ReadUtils.readRows("E:\\herche_work_qigao\\test\\wcnn.xls", rowNum - 1, rowNum - 1);
		System.out.println("lists ：" + lists);
		System.out.println("lists.size ：" + lists.size());
	}
	
	@Test
	public void testRead1() {
		List<List<Object>> lists = ReadUtils.readRows("E:\\herche_test\\333.xlsx", 1 - 1, 1 - 1);
		System.out.println(lists);
	}
	
	@Test
	public void test() {
		String preRef = "EF3";
		String ref = "AF3";
		// excel2007最大行数是1048576，最大列数是16384，最后一列列名是XFD
		// 数字代表列，去掉列信息
		String preXfd = ("".equals(preRef) ? "@" : preRef).replaceAll("\\d+", "");
		String xfd = ("".equals(ref) ? "@" : ref).replaceAll("\\d+", "");
		
		// A表示65，@表示64，如果A算作1，那@代表0
		// 填充最大位数3
		while (preXfd.length() < MAX_CELL_BIT) {
			preXfd  = CELL_FILL_CHAR + preXfd;
		}
		while (xfd.length() < MAX_CELL_BIT) {
			xfd = CELL_FILL_CHAR + xfd;
		}
		char[] preLetter = preXfd.toCharArray();
		char[] letter = xfd.toCharArray();
		// 用字母表示则最多三位，每26个字母进一位
		int res = (letter[0] - preLetter[0]) * 26 * 26 + (letter[1] - preLetter[1]) * 26 + (letter[2] - preLetter[2]);
		System.out.println(res);
	}
}
