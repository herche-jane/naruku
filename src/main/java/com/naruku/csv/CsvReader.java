package com.naruku.csv;

import com.naruku.CharSetUtil;
import com.naruku.ExclToCsv;
import com.naruku.exception.IORuntimeException;
import com.naruku.exception.POIException;
import com.naruku.handler.RowHandler;
import org.apache.commons.compress.utils.Lists;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  多线程读取CSV(50W)
 * @author herche
 * @date 2022/10/17
 */
public class CsvReader {
	
	

	/**
	 * @param srcPath  csv文件路径
	 */
	private static void readCSVFileData(String srcPath, RowHandler rowHandler) {
		boolean exists = Files.exists(Paths.get(srcPath), new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
		if (!exists){
			throw new POIException(srcPath + "文件不存在！");
		}
		try {
			readCSVFileData(new File(srcPath),rowHandler);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void readCSVFileData(File file,RowHandler rowHandler) throws IOException {
		boolean exists = file.exists();
		if (!exists){
			throw new POIException(file.getAbsoluteFile() + "文件不存在！");
		}
		// 先判断文件的编码格式
		String charsetName = CharSetUtil.getCharsetName(file);
		InputStreamReader fReader = null;
		try {
			fReader = new InputStreamReader(new FileInputStream(file.getAbsoluteFile()), charsetName);
		}catch (Exception e){
			fReader = new InputStreamReader(new FileInputStream(file.getAbsoluteFile()), StandardCharsets.UTF_8);
		}
		BufferedReader reader = new BufferedReader(fReader);
		String line = null;
		String[] fieldsArr = null;
		int lineNum = 0;
		try {
			List listField;
			while ((line = reader.readLine()) != null) {
				
				if (lineNum == 0) {
					//表头信息
					fieldsArr = line.split(",");
				} else {
					//数据信息
					listField = new ArrayList<>();
					String str;
					
					line += ",";
					Pattern pCells = Pattern
							.compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");
					Matcher mCells = pCells.matcher(line);
					List cells = new LinkedList();//每行记录一个list
					//读取每个单元格
					while (mCells.find()) {
						str = mCells.group();
						str = str.replaceAll(
								"(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");
						str = str.replaceAll("(?sm)(\"(\"))", "$2");
						cells.add(str);
//						rowHandler.handleCell();
					}
					//从第2行起的数据信息list
					listField.add(cells);
					
//					System.out.println(listField);
					rowHandler.handle(1,lineNum,cells);
				}
				lineNum++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
