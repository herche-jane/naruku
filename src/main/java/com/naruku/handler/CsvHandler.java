package com.naruku.handler;

import com.naruku.handler.RowHandler;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * csv处理器
 * @author herche
 * @date 2022/10/01
 */
public class CsvHandler implements RowHandler {
	// 写出文件
	private Object writer;
	private CSVPrinter printer;
	private Logger logger;
	private int maxCo=0;
	private int startRow = 0;
//	private OutputStreamWriter out;
	
	
	public CsvHandler(OutputStreamWriter out, CSVPrinter printer) {
		this.writer = out;
		this.printer = printer;
	}
	
	public CsvHandler(FileWriter writer, CSVPrinter printer, Logger logger) {
		this.writer = writer;
		this.printer = printer;
		this.logger = logger;
	}
	
	
	public CsvHandler(OutputStreamWriter out, CSVPrinter printer, Logger logger) {
		this.writer = out;
		this.printer = printer;
		this.logger = logger;
	}
	
	public CsvHandler(OutputStreamWriter out, CSVPrinter printer, Logger logger,int startRow) {
		this.writer = out;
		this.printer = printer;
		this.logger = logger;
		this.startRow = startRow;
	}


//	@Override
//	public void doAfterAllAnalysed() {
//		System.out.println("关闭了");
//		if (printer != null) {
//			try {
//				printer.flush();
//				printer.close();
//			} catch (IOException e) {
////				e.printStackTrace();
//			}
//		}
//		// 写完关
//		if (writer != null) {
//			try {
//				writer.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	@Override
	public void handle(int sheetIndex, long rowIndex, List<Object> rowCells) {
		System.out.println(rowCells);
		// 直接写成csv
		// 确定有多少列
		// 从开始列开始打
		if (rowIndex < startRow){
			return;
		}
		if (rowIndex == 0){
			maxCo = rowCells.size();
		}
		// 0 1 2
		// 0 1
		try {
//			// 如果一行中最后一个是空 补上
//			if (rowCells.size() < maxCo){
//				List<Object> rows = new ArrayList<>();
//				for (int i = 0 ; i < maxCo; i++) {
//					if (i < rowCells.size()){
//						Object o = rowCells.get(i);
//						rows.add(o);
//					} else {
//						rows.add("");
//					}
//				}
//				printer.printRecord(rows);
//				return;
//			}
//			printer = CSVFormat.EXCEL.print(writer);
			List<Object> rowCellsWithoutLine = new ArrayList<>();
			rowCells.forEach(obj ->{
				rowCellsWithoutLine.add(obj.toString().replaceAll("\\s*|\r|\n|\t",""));
			});
			printer.printRecord(rowCellsWithoutLine);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	@Override
//	public void handleCell(int sheetIndex, long rowIndex, int cellIndex, Object value, CellStyle xssfCellStyle) {
//
//	}
}
