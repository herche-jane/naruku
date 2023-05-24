package com.naruku.excl;

import com.naruku.exception.IORuntimeException;
import com.naruku.exception.POIException;
import com.naruku.handler.RowHandler;
import com.naruku.sax.Excel03SaxReader;
import com.naruku.sax.Excel07SaxReader;
import com.naruku.sax.ExcelSaxReader;
import com.naruku.sax.content.CellDataType;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.record.CellValueRecordInterface;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.xmlbeans.impl.store.CharUtil;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static com.naruku.sax.content.CellDataType.*;

public class ExcelSaxUtil {
	
	// 填充字符串
	public static final char CELL_FILL_CHAR = '@';
	// 列的最大位数
	public static final int MAX_CELL_BIT = 3;
	
	/**
	 * 日期格式字符串
	 */
	protected static String formatString;
	
	protected final static DataFormatter formatter = new DataFormatter();
	
	
	/**
	 * 创建 {@link ExcelSaxReader}
	 *
	 * @param isXlsx     是否为xlsx格式（07格式）
	 * @param rowHandler 行处理器
	 * @return {@link ExcelSaxReader}
	 * @since 5.4.4
	 */
	public static ExcelSaxReader<?> createSaxReader(boolean isXlsx, RowHandler rowHandler) {
		return isXlsx
				? new Excel07SaxReader(rowHandler)
				: new Excel03SaxReader(rowHandler);
	}
	
	public static void readFrom(InputStream xmlDocStream, ContentHandler handler) {
		XMLReader xmlReader = null;
		try {
			xmlReader = XMLHelper.newXMLReader();
		} catch (Exception e) {
			e.printStackTrace();
		}
		xmlReader.setContentHandler(handler);
		try {
			xmlReader.parse(new InputSource(xmlDocStream));
		} catch (IOException e) {
//			throw new IORuntimeException(e);
		} catch (SAXException e) {
			throw new POIException(e);
		}
	}
	
	/**
	 * 根据数据类型获取数据
	 *
	 * @param cellDataType  数据类型枚举
	 * @param value         数据值
	 * @param sharedStrings {@link SharedStrings}
	 * @param numFmtString  数字格式名
	 * @return 数据值
	 */
	public static Object getDataValue(CellDataType cellDataType, String value, SharedStrings sharedStrings, String numFmtString) {
		if (null == value) {
			return null;
		}
		
		if (null == cellDataType) {
			cellDataType = CellDataType.NULL;
		}
		
		Object result = null;
		switch (cellDataType) {
			case BOOL:
				result = (value.charAt(0) != '0');
				break;
			case ERROR:
				result = "\"ERROR:" + value.toString() + '"';
				break;
			case FORMULA:
				result = '"' + value.toString() + '"';
				;
				break;
			case INLINESTR:
				result = new XSSFRichTextString(value).toString();
				break;
			case SSTINDEX:
				try {
					final int index = Integer.parseInt(value);
					result = sharedStrings.getItemAt(index).getString();
				} catch (NumberFormatException e) {
					result = value;
				}
				break;
			case NUMBER:
				result = value;
				result = result.toString().replace("_", "").trim();
				break;
			case DATE:
				formatString = "yyyy-MM-dd HH:mm:ss";
				if (formatString != null) {
					if ("".equals(value) || value == null){
						result = "";
						break;
					}
					result = formatter.formatRawCellContents(Double.parseDouble(value), -1, formatString);
				} else {
					result = value;
				}
				// 对日期字符串作特殊处理，去掉T
				result = result.toString().replace("T", " ");
				break;
			// 特殊出路
			default:
				result = value;
				break;
		}
		return result;
	}
	
	
	/**
	 * 计算两个单元格之间的单元格数目(同一行)
	 *
	 * @param preRef 前一个单元格位置，例如A1
	 * @param ref    当前单元格位置，例如A8
	 * @return 同一行中两个单元格之间的空单元格数
	 */
	public static int countNullCell(String preRef, String ref) {
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
		int res = Math.abs((letter[0] - preLetter[0]) * 26 * 26 + (letter[1] - preLetter[1]) * 26 + (letter[2] - preLetter[2]));
		return res - 1;
	}
	
	
	/**
	 * 判断日期格式
	 *
	 * @param formatIndex  格式索引，一般用于内建格式
	 * @param formatString 格式字符串
	 * @return 是否为日期格式
	 * @see ExcelDateUtil#isDateFormat(int, String)
	 * @since 5.5.3
	 */
	public static boolean isDateFormat(int formatIndex, String formatString) {
		return ExcelDateUtil.isDateFormat(formatIndex, formatString);
	}
	
	/**
	 * 判断数字Record中是否为日期格式
	 *
	 * @param cell           单元格记录
	 * @param formatListener {@link FormatTrackingHSSFListener}
	 * @return 是否为日期格式
	 * @since 5.4.8
	 */
	public static boolean isDateFormat(CellValueRecordInterface cell, FormatTrackingHSSFListener formatListener) {
		final int formatIndex = formatListener.getFormatIndex(cell);
		final String formatString = formatListener.getFormatString(cell);
		return isDateFormat(formatIndex, formatString);
	}
	
	public static Object getNumberOrDateValue(CellValueRecordInterface cell, double value, FormatTrackingHSSFListener formatListener) {
		if (isDateFormat(cell, formatListener)) {
			// 可能为日期格式
			return getDateValue(value);
		}
		return getNumberValue(value, formatListener.getFormatString(cell));
	}
	
	public static Date getDateValue(double value) {
		return org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value, false);
	}
	
	private static Number getNumberValue(double numValue, String numFmtString) {
		// 普通数字
		if (null != numFmtString && !numFmtString.contains(".")) {
			final long longPart = (long) numValue;
			//noinspection RedundantIfStatement
			if (longPart == numValue) {
				// 对于无小数部分的数字类型，转为Long
				return longPart;
			}
		}
		return numValue;
	}
	
}
