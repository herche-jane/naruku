package com.naruku.sax;

/**
 * 抽象的单元格值接口，用于判断不同类型的单元格值
 * @author herche
 * @since 2022/10/01
 */
public interface CellValue<T> {

	/**
	 * 获取单元格值
	 *
	 * @return 值
	 */
	T getValue();
}
