package com.naruku;

import com.naruku.csv.CsvReader;
import com.naruku.excl.ExcelUtil;
import com.naruku.handler.CsvHandler;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

/**
 * 将csv文件以任意格式导出
 */
public class CustomizeCsv extends ExclToCsv{
	
	public static boolean csvUtf8(String exclPath, String importPath) throws FileNotFoundException, UnsupportedEncodingException {
		return csvCustomize(exclPath,importPath,null);
	}
	
	public static boolean csvCustomize(String exclPath, String importPath, String encoding) throws FileNotFoundException {
		// 设置编码格式
		if (!StringUtils.hasText(encoding)){
			encoding = "UTF-8";
		}
		createNewFile(importPath);
		OutputStreamWriter out = null;
		try {
			out = new OutputStreamWriter(new FileOutputStream(importPath), encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
//			// logger.info("编码格式有误，已经默认使用utf8格式！");
			out = new OutputStreamWriter(new FileOutputStream(importPath), StandardCharsets.UTF_8);
		}
		CSVPrinter print = null;
		try {
			// 开始转化
//			if ()
//			// logger.info("开始转换：" + exclPath + "==》" + importPath);
			// 给文件赋权
			long start = System.currentTimeMillis();
			File exclFile = new File(exclPath);
			File importFile = new File(importPath);
			if (!importFile.exists()){
				// logger.error(importPath + "：不存在！");
			} else {
				// logger.info(importPath + "：存在！");
			}
			// logger.info("开始csv-writer转化");
			print = CSVFormat.EXCEL.print(out);
			//追加BOM标识
			// logger.info("csv-writer转化成功");
			CsvHandler csvHandler = new CsvHandler(out,print);
			CsvReader.readCSVFileData(exclFile, csvHandler);
			long end = System.currentTimeMillis();
//			 logger.info("共耗时：" + (end - start) / 1000 + "秒");
			// logger.info("转换成功：" + exclPath + "==》" + importPath);
		}catch (Exception e){
			e.printStackTrace();
			// logger.error("发生异常！！" + e.getMessage());
			// logger.error("发生异常！！" + e.getCause());
		}finally {
			if (out != null) {
				try {
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (print != null) {
				try {
					print.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return Files.exists(Paths.get(importPath), new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
	}
}
