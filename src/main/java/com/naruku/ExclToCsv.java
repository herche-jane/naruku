package com.naruku;

import com.naruku.excl.ExcelUtil;
import com.naruku.handler.CsvHandler;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * exclToCsv
 * @author herche
 * @date 2022/10/01
 */
public class ExclToCsv {
	private static Logger logger = LoggerFactory.getLogger(ExclToCsv.class);
	/**
	 * excl输出为Csv
	 * @param exclPath
	 * @param importPath
	 */
	public static boolean exclToCsv(String exclPath, String importPath) throws FileNotFoundException, UnsupportedEncodingException {
		FileWriter fileWriter = null;
		CSVPrinter print = null;
		if (!Files.exists(Paths.get(exclPath), new LinkOption[]{LinkOption.NOFOLLOW_LINKS})){
			throw new RuntimeException(exclPath + "文件找不到!");
		}
		if (Files.exists(Paths.get(importPath), new LinkOption[]{LinkOption.NOFOLLOW_LINKS})){
			// 删除
			try {
				Files.delete(Paths.get(importPath));
			} catch (IOException e) {
			}
		}
		try {
			// 开始转化
//			if ()
//			logger.info("开始转换：" + exclPath + "==》" + importPath);
			// 给文件赋权
			long start = System.currentTimeMillis();
			File file = new File(exclPath);
			fileWriter = new FileWriter(importPath);
			File importFile = new File(importPath);
			logger.info("开始给文件：" + importPath + "赋权");
			importFile.setReadable(true);
			boolean writable = importFile.setWritable(true);
			logger.info(importPath + "是否可写：" + writable);
			importFile.setExecutable(true);
			logger.info("开始csv-writer转化");
			print = CSVFormat.EXCEL.print(fileWriter);
			logger.info("csv-writer转化成功");
			CsvHandler csvHandler = new CsvHandler(fileWriter,print);
			ExcelUtil.readBySax(file, -1, csvHandler);
			long end = System.currentTimeMillis();
			logger.info("共耗时：" + (end - start) / 1000 + "秒");
			logger.info("转换成功：" + exclPath + "==》" + importPath);
		}catch (Exception e){
			e.printStackTrace();
			logger.error("发生异常！！" + e.getMessage());
			logger.error("发生异常！！" + e.getCause());
		}finally {
			if (fileWriter != null) {
				try {
					fileWriter.flush();
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
	
	public static boolean exclToCsvUtf8(String exclPath, String importPath, Logger logger, int i) throws FileNotFoundException, UnsupportedEncodingException {
		return exclToCsvCustomize(exclPath,importPath,null);
	}
	
	public static void createNewFile(String importPath) {
		if (Files.exists(Paths.get(importPath), new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
			try {
				Files.delete(Paths.get(importPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		createParentFile(importPath);
		// 创建父目录 LINUX适配
//		Set<PosixFilePermission> posixFilePermissions = new HashSet();
//		posixFilePermissions.add(PosixFilePermission.OWNER_READ);
//		posixFilePermissions.add(PosixFilePermission.OWNER_WRITE);
//		posixFilePermissions.add(PosixFilePermission.OWNER_READ);
//		try {
////			Files.createFile(Paths.get(importPath), new FileAttribute[]{PosixFilePermissions.asFileAttribute(posixFilePermissions)});
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	public static void createParentFile(String filePath) {
		File file = new File(filePath);
		while (!file.getParentFile().exists()){
			file.getParentFile().mkdir();
			createParentFile(file.getParentFile().getAbsolutePath());
		}
	}
	
	
	public static boolean exclToCsvCustomize(String exclPath, String importPath, String encoding) throws FileNotFoundException {
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
			logger.info("编码格式有误，已经默认使用utf8格式！");
			out = new OutputStreamWriter(new FileOutputStream(importPath), StandardCharsets.UTF_8);
		}
		CSVPrinter print = null;
		try {
			// 开始转化
//			if ()
//			logger.info("开始转换：" + exclPath + "==》" + importPath);
			// 给文件赋权
			long start = System.currentTimeMillis();
			File exclFile = new File(exclPath);
			File importFile = new File(importPath);
			if (!importFile.exists()){
				logger.error(importPath + "：不存在！");
			} else {
				logger.info(importPath + "：存在！");
			}
			logger.info("开始csv-writer转化");
			print = CSVFormat.EXCEL.print(out);
			//追加BOM标识
			logger.info("csv-writer转化成功");
			CsvHandler csvHandler = new CsvHandler(out,print);
			ExcelUtil.readBySax(exclFile, -1, csvHandler);
			long end = System.currentTimeMillis();
			logger.info("共耗时：" + (end - start) / 1000 + "秒");
			logger.info("转换成功：" + exclPath + "==》" + importPath);
		}catch (Exception e){
			e.printStackTrace();
			logger.error("发生异常！！" + e.getMessage());
			logger.error("发生异常！！" + e.getCause());
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
