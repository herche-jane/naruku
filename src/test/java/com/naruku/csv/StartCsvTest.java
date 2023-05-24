package com.naruku.csv;

import com.naruku.excl.ExcelUtil;
import com.naruku.handler.CsvHandler;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class StartCsvTest {
	@Test
	public void startRead() throws IOException {
		Logger logger = LoggerFactory.getLogger(StartCsvTest.class);
		long l = System.currentTimeMillis();
		File file = new File("E:\\herche_work_qigao\\test\\33333.xlsx");
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("E:\\herche_work_qigao\\test\\33333.csv"), StandardCharsets.UTF_8);
		CSVPrinter print = CSVFormat.EXCEL.print(out);
		CsvHandler csvHandler = new CsvHandler(out,print,logger,6);
		ExcelUtil.readBySax(file, 0, csvHandler);
		long l1 = System.currentTimeMillis();
		System.out.println(l1 - l);
		out.flush();
		out.close();
	}
}
