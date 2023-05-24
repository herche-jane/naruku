package com.naruku.pdfTest.search;

/**
 * 查找的关键字
 * @author herche
 * @date 2022/11/07
 */
public class SearchInfo {
	// key值
	private String key;
	// 总长度X
	private String overallLengthX;
	// y坐标
	private String y;
	
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getOverallLengthX() {
		return overallLengthX;
	}
	
	public void setOverallLengthX(String overallLengthX) {
		this.overallLengthX = overallLengthX;
	}
	
	public String getY() {
		return y;
	}
	
	public void setY(String y) {
		this.y = y;
	}
	
	public String getValue(String x, String y){
		boolean equalsX = overallLengthX.substring(0,overallLengthX.lastIndexOf(".")).equalsIgnoreCase(x.substring(0,x.lastIndexOf(".")));
		boolean equalsY = this.y.substring(0,this.y.lastIndexOf(".")).equalsIgnoreCase(y.substring(0,y.lastIndexOf(".")));
		if (equalsX || equalsY){
			System.out.println("----------------------------");
		}
		if (equalsX && equalsY){
			return key;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "SearchInfo{" +
				       "key='" + key + '\'' +
				       ", overallLengthX=" + overallLengthX +
				       ", y=" + y +
				       '}';
	}
}
