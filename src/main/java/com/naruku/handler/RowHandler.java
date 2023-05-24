package com.naruku.handler;

import com.naruku.sax.ExcelSaxReader;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.List;

/**
 * 行处理器
 * @author herche
 * @date 2022/10/01
 */
public interface RowHandler {
	
	/**
	 * 处理一个sheet页完成的操作
	 */
	default void doAfterAllAnalysed() {
		//pass
	}
	
	
	/**
	 * 处理一行数据
	 *
	 * @param sheetIndex 当前Sheet序号
	 * @param rowIndex   当前行号，从0开始计数
	 * @param rowCells   行数据，每个Object表示一个单元格的值
	 */
	void handle(int sheetIndex, long rowIndex, List<Object> rowCells);
	
	
	default void handleCell(int sheetIndex, long rowIndex, int cellIndex, Object value, CellStyle xssfCellStyle) {
		//pass
	}
	
	default void setExcelSaxReader(ExcelSaxReader excelSaxReader) {
		//pass
	}
	
	
}
