package com.naruku.pdfTest.search;

import java.math.BigDecimal;

/**
 * 点的坐标
 * @author herche
 * @date 2022-11-04
 */
public class Spot {
	//  x位置
	private BigDecimal x;
	
	//  y位置
	private BigDecimal y;
	
	public Spot(BigDecimal x,BigDecimal y){
		this.x = x;
		this.y = y;
	}
	
	public BigDecimal getX() {
		return x;
	}
	
	public void setX(BigDecimal x) {
		this.x = x;
	}
	
	public BigDecimal getY() {
		return y;
	}
	
	public void setY(BigDecimal y) {
		this.y = y;
	}
}
