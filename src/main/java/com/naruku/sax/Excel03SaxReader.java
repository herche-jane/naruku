package com.naruku.sax;

import com.naruku.exception.POIException;
import com.naruku.excl.ExcelSaxUtil;
import com.naruku.handler.RowHandler;
import org.apache.poi.hssf.eventusermodel.*;
import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder.SheetRecordCollectingListener;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * sax快速读取2003Excl
 * @author herche
 * @param <T>
 * @date 2022/10/01
 */
public class Excel03SaxReader<T> implements ExcelSaxReader<Excel03SaxReader>, HSSFListener {
	
	/**
	 * 如果为公式，true表示输出公式计算后的结果值，false表示输出公式本身
	 */
	private final boolean isOutputFormulaValues = true;
	
	/**
	 * 用于解析公式
	 */
	private SheetRecordCollectingListener workbookBuildingListener;
	/**
	 * 子工作簿，用于公式计算
	 */
	private HSSFWorkbook stubWorkbook;
	
	/**
	 * 静态字符串表
	 */
	private SSTRecord sstRecord;
	
	private FormatTrackingHSSFListener formatListener;
	
	/**
	 * Sheet边界记录，此Record中可以获得Sheet名
	 */
	private final List<BoundSheetRecord> boundSheetRecords = new ArrayList<>();
	
	private boolean isOutputNextStringRecord;
	
	// 存储行记录的容器
	private List<Object> rowCellList = new ArrayList<>();
	
	/**
	 * 自定义需要处理的sheet编号，如果-1表示处理所有sheet
	 */
	private int rid = -1;
	/**
	 * sheet名称，主要用于使用sheet名读取的情况
	 */
	private String sheetName;
	
	/**
	 * 当前rid索引
	 */
	private int curRid = -1;
	
	/**
	 * 行处理器
	 */
	private final RowHandler rowHandler;
	
	/**
	 * 构造
	 *
	 * @param rowHandler 行处理器
	 */
	public Excel03SaxReader(RowHandler rowHandler) {
		rowHandler.setExcelSaxReader(this);
		this.rowHandler = rowHandler;
	}
	
	private POIFSFileSystem poifsFileSystem;
	
	@Override
	public Excel03SaxReader read(File file, String idOrRidOrSheetName) throws POIException {
		try {
			poifsFileSystem = new POIFSFileSystem(file, true);
			return read(poifsFileSystem, idOrRidOrSheetName);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void close() {
		if (poifsFileSystem != null){
			try {
				poifsFileSystem.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 读取
	 *
	 * @param fs  {@link POIFSFileSystem}
	 * @param idOrRidOrSheetName sheet id或者rid编号或sheet名称，从0开始，rid必须加rId前缀，例如rId0，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException IO异常包装
	 */
	public Excel03SaxReader read(POIFSFileSystem fs, String idOrRidOrSheetName) throws POIException {
		this.rid = getSheetIndex(idOrRidOrSheetName);
		
		formatListener = new FormatTrackingHSSFListener(new MissingRecordAwareHSSFListener(this));
		final HSSFRequest request = new HSSFRequest();
		if (isOutputFormulaValues) {
			request.addListenerForAllRecords(formatListener);
		} else {
			workbookBuildingListener = new SheetRecordCollectingListener(formatListener);
			request.addListenerForAllRecords(workbookBuildingListener);
		}
		final HSSFEventFactory factory = new HSSFEventFactory();
		try {
			factory.processWorkbookEvents(request, fs);
		} catch (IOException e) {
//			throw new POIException(e);
		} finally {
			try {
				if (fs != null)
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return this;
	}
	
	/**
	 * 获得Sheet序号，如果处理所有sheet，获得最大的Sheet序号，从0开始
	 *
	 * @return sheet序号
	 */
	public int getSheetIndex() {
		return this.rid;
	}
	
	/**
	 * 获得Sheet名，如果处理所有sheet，获得后一个Sheet名，从0开始
	 *
	 * @return Sheet名
	 */
	public String getSheetName() {
		if(null != this.sheetName){
			return this.sheetName;
		}
		
		if (this.boundSheetRecords.size() > this.rid) {
			return this.boundSheetRecords.get(this.rid > -1 ? this.rid : this.curRid).getSheetname();
		}
		
		return null;
	}
	
	@Override
	public void processRecord(Record record) {
		if (this.rid > -1 && this.curRid > this.rid) {
			// 指定Sheet之后的数据不再处理
			return;
		}
		
		if (record instanceof BoundSheetRecord) {
			// Sheet边界记录，此Record中可以获得Sheet名
			final BoundSheetRecord boundSheetRecord = (BoundSheetRecord) record;
			boundSheetRecords.add(boundSheetRecord);
			final String currentSheetName = boundSheetRecord.getSheetname();
			if(null != this.sheetName && this.sheetName.equals(currentSheetName)){
				this.rid = this.boundSheetRecords.size() -1;
			}
		} else if (record instanceof SSTRecord) {
			// 静态字符串表
			sstRecord = (SSTRecord) record;
		} else if (record instanceof BOFRecord) {
			BOFRecord bofRecord = (BOFRecord) record;
			if (bofRecord.getType() == BOFRecord.TYPE_WORKSHEET) {
				// 如果有需要，则建立子工作薄
				if (workbookBuildingListener != null && stubWorkbook == null) {
					stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
				}
				curRid++;
			}
		} else if (record instanceof EOFRecord){
			if(this.rid < 0 && null != this.sheetName){
				throw new POIException();
			}
			processLastCellSheet();
		} else if (isProcessCurrentSheet()) {
			if (record instanceof MissingCellDummyRecord) {
				// 空值的操作
				MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
				addToRowCellList(mc);
			} else if (record instanceof LastCellOfRowDummyRecord) {
				// 行结束
				processLastCell((LastCellOfRowDummyRecord) record);
			} else {
				// 处理单元格值
				processCellValue(record);
			}
		}
		
	}
	
	/**
	 * 将空数据加入到行列表中
	 *
	 * @param record MissingCellDummyRecord
	 */
	private void addToRowCellList(MissingCellDummyRecord record) {
		addToRowCellList(record.getRow(), record.getColumn(), "");
	}
	
	
	/**
	 * 将单元格数据加入到行列表中
	 *
	 * @param record 单元格
	 * @param value  值
	 */
	private void addToRowCellList(CellValueRecordInterface record, Object value) {
		addToRowCellList(record.getRow(), record.getColumn(), value);
	}
	
	/**
	 * 将单元格数据加入到行列表中
	 *
	 * @param row    行号
	 * @param column 单元格
	 * @param value  值
	 */
	private void addToRowCellList(int row, int column, Object value) {
		while (column > this.rowCellList.size()) {
			// 对于中间无数据的单元格补齐空白
			this.rowCellList.add(" ");
			this.rowHandler.handleCell(this.curRid, row, rowCellList.size() - 1, value, null);
		}
		
		this.rowCellList.add(column, value);
		this.rowHandler.handleCell(this.curRid, row, column, value, null);
	}
	
	
	/**
	 * 处理单元格值
	 *
	 * @param record 单元格
	 */
	private void processCellValue(Record record) {
		Object value = null;
		
		switch (record.getSid()) {
			case BlankRecord.sid:
				// 空白记录
				addToRowCellList(((BlankRecord) record), "");
				break;
			case BoolErrRecord.sid:
				// 布尔类型
				final BoolErrRecord berec = (BoolErrRecord) record;
				addToRowCellList(berec, berec.getBooleanValue());
				break;
			case FormulaRecord.sid:
				// 公式类型
				final FormulaRecord formulaRec = (FormulaRecord) record;
				if (isOutputFormulaValues) {
					if (Double.isNaN(formulaRec.getValue())) {
						// Formula result is a string
						// This is stored in the next record
						isOutputNextStringRecord = true;
					} else {
						value = ExcelSaxUtil.getNumberOrDateValue(formulaRec, formulaRec.getValue(), this.formatListener);
					}
				} else {
					value = HSSFFormulaParser.toFormulaString(stubWorkbook, formulaRec.getParsedExpression());
				}
				addToRowCellList(formulaRec, value);
				break;
			case StringRecord.sid:
				// 单元格中公式的字符串
				if (isOutputNextStringRecord) {
					// String for formula
					// value = ((StringRecord) record).getString();
					isOutputNextStringRecord = false;
				}
				break;
			case LabelRecord.sid:
				final LabelRecord lrec = (LabelRecord) record;
				value = lrec.getValue();
				addToRowCellList(lrec, value);
				break;
			case LabelSSTRecord.sid:
				// 字符串类型
				LabelSSTRecord lsrec = (LabelSSTRecord) record;
				if (null != sstRecord) {
					value = sstRecord.getString(lsrec.getSSTIndex()).toString();
				}
				addToRowCellList(lsrec, value);
				break;
			case NumberRecord.sid: // 数字类型
				final NumberRecord numrec = (NumberRecord) record;
				value = ExcelSaxUtil.getNumberOrDateValue(numrec, numrec.getValue(), this.formatListener);
				// 向容器加入列值
				addToRowCellList(numrec, value);
				break;
			default:
				break;
		}
	}
	
	/**
	 * 处理行结束后的操作，{@link LastCellOfRowDummyRecord}是行结束的标识Record
	 *
	 * @param lastCell 行结束的标识Record
	 */
	private void processLastCell(LastCellOfRowDummyRecord lastCell) {
		// 每行结束时， 调用handle() 方法
		this.rowHandler.handle(curRid, lastCell.getRow(), this.rowCellList);
		// 清空行Cache
		this.rowCellList = new ArrayList<>(this.rowCellList.size());
	}
	
	/**
	 * 处理sheet结束后的操作
	 */
	private void processLastCellSheet(){
		this.rowHandler.doAfterAllAnalysed();
	}
	
	/**
	 * 是否处理当前sheet
	 *
	 * @return 是否处理当前sheet
	 */
	private boolean isProcessCurrentSheet() {
		// rid < 0 且 sheet名称存在，说明没有匹配到sheet名称
		return (this.rid < 0 && null == this.sheetName) || this.rid == this.curRid;
	}
	
	/**
	 * 获取sheet索引，从0开始
	 * <ul>
	 *     <li>传入'rId'开头，直接去除rId前缀</li>
	 *     <li>传入纯数字，表示sheetIndex，直接转换为rid</li>
	 * </ul>
	 *
	 * @param idOrRidOrSheetName Excel中的sheet id或者rid编号或sheet名称，从0开始，rid必须加rId前缀，例如rId0，如果为-1处理所有编号的sheet
	 * @return sheet索引，从0开始
	 * @since 5.5.5
	 */
	private int getSheetIndex(String idOrRidOrSheetName) {
		Assert.notNull(idOrRidOrSheetName, "id or rid or sheetName must be not blank!");
		// rid直接处理
		if (idOrRidOrSheetName.startsWith(RID_PREFIX)) {
			return Integer.parseInt(idOrRidOrSheetName.substring(RID_PREFIX.length()));
		} else if(idOrRidOrSheetName.startsWith(SHEET_NAME_PREFIX)){
			// since 5.7.10，支持任意名称
			this.sheetName = idOrRidOrSheetName.substring(SHEET_NAME_PREFIX.length());
		} else {
			try {
				return Integer.parseInt(idOrRidOrSheetName);
			} catch (NumberFormatException ignore) {
				// 如果用于传入非数字，按照sheet名称对待
				this.sheetName = idOrRidOrSheetName;
			}
		}
		
		return -1;
	}
	
}
