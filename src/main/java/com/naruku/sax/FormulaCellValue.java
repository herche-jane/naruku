package com.naruku.sax;

import org.apache.poi.ss.usermodel.Cell;

/**
 * 公式类型的值
 *
 * @author herche
 * @date 2022/10/01
 */
public class FormulaCellValue implements CellValue<String>, CellSetter {

	/**
	 * 公式
	 */
	private final String formula;
	/**
	 * 结果，使用ExcelWriter时可以不用
	 */
	private final Object result;

	/**
	 * 构造
	 *
	 * @param formula 公式
	 */
	public FormulaCellValue(String formula) {
		this(formula, null);
	}

	/**
	 * 构造
	 *
	 * @param formula 公式
	 * @param result  结果
	 */
	public FormulaCellValue(String formula, Object result) {
		this.formula = formula;
		this.result = result;
	}

	@Override
	public String getValue() {
		return this.formula;
	}

	@Override
	public void setValue(Cell cell) {
		cell.setCellFormula(this.formula);
	}

	/**
	 * 获取结果
	 * @return 结果
	 */
	public Object getResult() {
		return this.result;
	}

	@Override
	public String toString() {
		return getResult().toString();
	}
}
