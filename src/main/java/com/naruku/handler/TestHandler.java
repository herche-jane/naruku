package com.naruku.handler;

import java.util.List;

/**
 * 测试处理器
 * @author herche
 * @date 2022/10/01
 */
public class TestHandler implements RowHandler {
	@Override
	public void handle(int sheetIndex, long rowIndex, List<Object> rowCells) {
		if (rowIndex == 0){
			System.out.println(rowCells);
			return;
		}
	}
	
}
