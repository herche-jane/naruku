package com.naruku.handler.reader;

import com.naruku.exception.POIException;
import com.naruku.handler.RowHandler;
import com.naruku.sax.ExcelSaxReader;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.ArrayList;
import java.util.List;

public class ReadRowsHandler implements RowHandler {
	private int startRow;
	private int endRow;
	private List<List<Object>> rows = new ArrayList<>();
	private ExcelSaxReader excelSaxReader;
	public ReadRowsHandler(){
		this.startRow = 0;
		this.endRow = 1;
	}
	
	public ReadRowsHandler(int startRow){
		this.startRow = startRow;
		this.endRow = -1;
	}
	
	public ReadRowsHandler(int startRow,int endRow){
		this.startRow = startRow;
		this.endRow = endRow;
	}
	
	@Override
	public void doAfterAllAnalysed() {
	
	}
	
	@Override
	public void handle(int sheetIndex, long rowIndex, List<Object> rowCells) {
		if (startRow == 0 && endRow == 1){
			rows.add(rowCells);
		}
		if (startRow < 0)throw new POIException("startRow不能小于0");
		if (startRow > endRow && endRow != -1)throw new POIException("startRow不能大于endRow");
		if (startRow <= rowIndex && endRow >= rowIndex || (startRow <= rowIndex && endRow == -1)){
			rows.add(rowCells);
			excelSaxReader.close();
		}
	}
	
	@Override
	public void handleCell(int sheetIndex, long rowIndex, int cellIndex, Object value, CellStyle xssfCellStyle) {
	
	}
	
	public  List<List<Object>> getRows(){
		return this.rows;
	}
	
	@Override
	public void setExcelSaxReader(ExcelSaxReader excelSaxReader) {
		this.excelSaxReader = excelSaxReader;
	}
}
