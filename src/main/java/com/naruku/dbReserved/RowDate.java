package com.naruku.dbReserved;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体类
 */
public class RowDate<T> {
	// 起始处
	public int start;
	// 结束处
	public int end;
	// 总数目
	public int rowNums;
	// 包含的数据
	public final List<T> dataList = new ArrayList<>();
	// 是否已经结束
	public boolean isEnd;
	// 每一页的数目
	public int pageRows;
	// 当前页
	public int page;
	
	
	public boolean hasNext(){
		return !(end >= this.page);
	}
	
	
	public int getStart() {
		return start;
	}
	
	public void setStart(int start) {
		this.start = start;
	}
	
	public int getEnd() {
		int endPage = this.getRowNums() / this.getPageRows();
		return this.getRowNums() % this.getPageRows() == 0 ? endPage : endPage + 1 ;
	}
	
	
	public int getRowNums() {
		return rowNums;
	}
	
	public void setRowNums(int rowNums) {
		this.rowNums = rowNums;
	}
	
	public List<T> getDataList() {
		return dataList;
	}
	
	
	public boolean isEnd() {
		return isEnd;
	}
	
	public void setEnd(boolean end) {
		isEnd = end;
	}
	
	public int getPageRows() {
		return pageRows;
	}
	
	public void setPageRows(int pageRows) {
		this.pageRows = pageRows;
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
}
