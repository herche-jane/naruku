package com.naruku.sax;

import com.naruku.exception.IORuntimeException;
import com.naruku.exception.POIException;
import com.naruku.excl.ExcelSaxUtil;
import com.naruku.handler.RowHandler;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.StylesTable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * sax快速读取2007Excl
 *
 * @author herche
 * @date 2022/10/02
 */
public class Excel07SaxReader implements ExcelSaxReader<Excel07SaxReader> {
	
	private final SheetDataSaxHandler handler;
	private InputStream sheetInputStream = null;
	
	public Excel07SaxReader(RowHandler rowHandler) {
		rowHandler.setExcelSaxReader(this);
		this.handler = new SheetDataSaxHandler(rowHandler);
	}
	
	/**
	 * 设置行处理器
	 *
	 * @param rowHandler 行处理器
	 * @return this
	 */
	public Excel07SaxReader setRowHandler(RowHandler rowHandler) {
		this.handler.setRowHandler(rowHandler);
		return this;
	}
	
	@Override
	public Excel07SaxReader read(File file, String idOrRidOrSheetName) throws POIException {
		try (OPCPackage open = OPCPackage.open(file, PackageAccess.READ)) {
			return read(open, idOrRidOrSheetName);
		} catch (InvalidFormatException | IOException e) {
			throw new POIException(e);
		}
	}
	
	@Override
	public Excel07SaxReader read(File file, int rid) throws POIException {
		return read(file, RID_PREFIX + rid);
	}
	
	@Override
	public void close() {
		if (sheetInputStream != null){
			try {
				sheetInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 开始读取Excel，Sheet编号从0开始计数
	 *
	 * @param opcPackage         {@link OPCPackage}，Excel包，读取后不关闭
	 * @param idOrRidOrSheetName Excel中的sheet id或者rid编号或sheet名，rid必须加rId前缀，例如rId1，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException POI异常
	 */
	public Excel07SaxReader read(OPCPackage opcPackage, String idOrRidOrSheetName) throws POIException {
		try {
			return read(new XSSFReader(opcPackage), idOrRidOrSheetName);
		} catch (OpenXML4JException e) {
			throw new POIException(e);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * 开始读取Excel，Sheet编号从0开始计数
	 *
	 * @param xssfReader         {@link XSSFReader}，Excel读取器
	 * @param idOrRidOrSheetName Excel中的sheet id或者rid编号或sheet名，rid必须加rId前缀，例如rId1，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException POI异常
	 * @since 5.4.4
	 */
	public Excel07SaxReader read(XSSFReader xssfReader, String idOrRidOrSheetName) throws POIException {
		try {
			this.handler.stylesTable = xssfReader.getStylesTable();
		} catch (IOException | InvalidFormatException ignored) {
		}
		try {
			Method method = XSSFReader.class.getDeclaredMethod("getSharedStringsTable");
			this.handler.sharedStrings = (SharedStrings) method.invoke(xssfReader);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return readSheets(xssfReader, idOrRidOrSheetName);
	}
	
	/**
	 * 开始读取Excel，Sheet编号从0开始计数
	 *
	 * @param xssfReader         {@link XSSFReader}，Excel读取器
	 * @param idOrRidOrSheetName Excel中的sheet id或者rid编号或sheet名，从0开始，rid必须加rId前缀，例如rId0，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException POI异常
	 * @since 5.4.4
	 */
	private Excel07SaxReader readSheets(XSSFReader xssfReader, String idOrRidOrSheetName) throws POIException {
		this.handler.sheetIndex = getSheetIndex(xssfReader, idOrRidOrSheetName);
		try {
			if (this.handler.sheetIndex > -1) {
				// 根据 rId# 或 rSheet# 查找sheet
				sheetInputStream = xssfReader.getSheet(RID_PREFIX + (this.handler.sheetIndex + 1));
				ExcelSaxUtil.readFrom(sheetInputStream, this.handler);
				this.handler.rowHandler.doAfterAllAnalysed();
			} else {
				this.handler.sheetIndex = -1;
				// 遍历所有sheet
				final Iterator<InputStream> sheetInputStreams = xssfReader.getSheetsData();
				while (sheetInputStreams.hasNext()) {
					// 重新读取一个sheet时行归零
					this.handler.index = 0;
					this.handler.sheetIndex++;
					sheetInputStream = sheetInputStreams.next();
					ExcelSaxUtil.readFrom(sheetInputStream, this.handler);
					this.handler.rowHandler.doAfterAllAnalysed();
				}
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new POIException(e);
		} finally {
			try {
				if (sheetInputStream != null)
					sheetInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return this;
	}
	
	
	private int getSheetIndex(XSSFReader xssfReader, String idOrRidOrSheetName) {
		// rid直接处理
		if (idOrRidOrSheetName.startsWith(RID_PREFIX)) {
			return Integer.parseInt(idOrRidOrSheetName.substring(RID_PREFIX.length()));
		}
		throw new IllegalArgumentException("Invalid rId or id or sheetName: " + idOrRidOrSheetName);
	}
	
}
