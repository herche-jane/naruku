package com.naruku.dbReserved.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

/**
 * 写的工具类，将文字写入文件中
 * @author herche
 * @data 2022/10/27
 */
public class WriterUtils {
	/**
	 * 向文件中写入
	 * @param string 写入内容
	 * @param filePath 路径
	 * @return
	 */
	public static boolean writeToFile(String string,String filePath) throws IOException {
		// 存在则删除
		if(Files.exists(Paths.get(filePath), new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
			Files.delete(Paths.get(filePath));
		}
		try {
			FileWriter writer = new FileWriter(filePath);
			writer.write(string);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return Files.exists(Paths.get(filePath), new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
	}
	
	/**
	 * 向文件中追加
	 * @param string 写入内容
	 * @param filePath 路径
	 * @return
	 */
	public static boolean writeAddFile(String string,String filePath) throws IOException {
		// 不存在则创建
		try {
			FileWriter writer = new FileWriter(filePath);
			writer.write(string);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return Files.exists(Paths.get(filePath), new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
	}
}
