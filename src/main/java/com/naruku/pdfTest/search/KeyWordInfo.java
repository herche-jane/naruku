package com.naruku.pdfTest.search;

/**
 * 关键字在pdf中的信息
 * @author herche
 * @date 2022/11/03
 */
public class KeyWordInfo {
	private String keyId;
	private String moduleId;
	private String x;//在pdf的x坐标
	private String y;//在pdf的y坐标
	private String width;//关键字的宽度
	private String height;//关键字高度
	private String coordinatePage ;//关键字所在页
	private String keyValue;
	
	public String getModuleId() {
		return moduleId;
	}
	
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	
	public String getX() {
		return x;
	}
	
	public void setX(String x) {
		this.x = x;
	}
	
	public String getY() {
		return y;
	}
	
	public void setY(String y) {
		this.y = y;
	}
	
	public String getWidth() {
		return width;
	}
	
	public void setWidth(String width) {
		this.width = width;
	}
	
	public String getHeight() {
		return height;
	}
	
	public void setHeight(String height) {
		this.height = height;
	}
	
	public String getCoordinatePage() {
		return coordinatePage;
	}
	
	public void setCoordinatePage(String coordinatePage) {
		this.coordinatePage = coordinatePage;
	}
	

	
	public String getKeyValue() {
		return keyValue;
	}
	
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}
	
	public String getKeyId() {
		return keyId;
	}
	
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	
	@Override
	public String toString() {
		return "KeyWordInfo{" +
				       "moduleId='" + moduleId + '\'' +
				       ", x=" + x +
				       ", y=" + y +
				       ", width=" + width +
				       ", height=" + height +
				       ", coordinatePage=" + coordinatePage +
				       ", keyValue='" + keyValue + '\'' +
				       '}';
	}
}
