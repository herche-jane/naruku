package com.naruku.dbReserved.script;

import com.naruku.CharSetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.sql.Connection;

/**
 * SQL脚本执行工具 单个脚本执行
 * @author herche
 * @date 2022/10/03
 */
public class SqlScript {
	private static final Logger logger = LoggerFactory.getLogger(SqlScript.class);
	
	public static boolean readLine(File file, String encoding,Connection connection){
		// 判断文件
		if (!Files.exists(Paths.get(file.getAbsolutePath()), new LinkOption[]{LinkOption.NOFOLLOW_LINKS}) || file.isDirectory() || !file.getName().endsWith(".sql")){
			logger.error("文件不存在或者该文件不符合要求，请换一个sql文件");
			return false;
		}
		if (ObjectUtils.isEmpty(connection)){
			logger.error("连接为空！");
			return false;
		}
		if (!StringUtils.hasText(encoding)){
			logger.warn("未传文件的编码格式，尝试自动获取中");
			try {
				CharSetUtil.getCharsetName(file);
			} catch (IOException e) {
				logger.warn("获取文件编码格式失败！现在默认编码格式为“utf -8");
				e.printStackTrace();
			}
		}
		// 读文件
		return true;
		
	}
	
	public static void readLine(File file,Connection connection,String delimiter){
	
	}
}
