package com.naruku;

import info.monitorenter.cpdetector.io.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CharSetUtil {
	public static String readFile(String filePath) throws IOException {
		//filePath为文件路径，charset为字符编码。通常使用UTF-8
		StringBuilder sb = new StringBuilder();
		File file = new File(filePath);
		String charset = getCharsetName(file);
		BufferedReader reader = null;
		String tempString = null;
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), charset);
			reader = new BufferedReader(isr);
			while ((tempString = reader.readLine()) != null) {
				sb.append(tempString);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("文件不存在");
			return null;
		} catch (IOException e) {
			System.out.println("文件读取异常");
			return null;
		}
		return sb.toString();
	}
	/**
	 * 获取文件编码方式
	 */
	public static String getCharsetName(File file) throws IOException {
		String charsetName = "UTF-8";
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(new ParsingDetector(false));
		detector.add(JChardetFacade.getInstance());
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());
		Charset charset = detector.detectCodepage(file.toURI().toURL());
		if (charset != null) charsetName = charset.name();
		return charsetName;
	}
	
	
	public static void main(String[] args) throws IOException {
		File file = new File("E:\\herche_work_qigao\\ww111111.csv");
		System.out.println(getCharsetName(file));
		
	}
}
