package com.naruku.pdfTest.listener;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.awt.geom.RectangularShape;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.naruku.pdfTest.search.SearchInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析时候用到的监听器，用于将坐标全部收集
 * @author herche
 * @date 2022/11/07
 */
public class NarukuParserListener implements RenderListener {
	
	List<SearchInfo> searchInfos = new ArrayList<>();
	
	@Override
	public void beginTextBlock() {
	
	}
	
	@Override
	public void renderText(TextRenderInfo renderInfo) {
		//获取文字的下面的矩形
		//Rectangle2D.Float rectBase = renderInfo.getBaseline().getBoundingRectange();
		String text = renderInfo.getText();
		if (text.length() > 0) {
			RectangularShape rectBase = renderInfo.getBaseline().getBoundingRectange();
			//获取文字下面的矩形
			Rectangle2D.Float rectAscen = renderInfo.getAscentLine().getBoundingRectange();
			//计算出文字的边框矩形
			float leftX = (float) rectBase.getMinX();
			float leftY = (float) rectBase.getMinY() - 1;
			float rightX = (float) rectAscen.getMaxX();
			float rightY = (float) rectAscen.getMaxY() + 1;
			SearchInfo searchInfo = new SearchInfo();
			searchInfo.setKey(text);
			searchInfo.setOverallLengthX(String.valueOf(rightX));
			searchInfo.setY(String.valueOf(leftY));
			searchInfos.add(searchInfo);
		}
	}
	
	@Override
	public void endTextBlock() {
	
	}
	
	@Override
	public void renderImage(ImageRenderInfo renderInfo) {
	
	}
	
	public List<SearchInfo> getSearchInfos() {
		return searchInfos;
	}
	
	public void setSearchInfos(List<SearchInfo> searchInfos) {
		this.searchInfos = searchInfos;
	}
}
