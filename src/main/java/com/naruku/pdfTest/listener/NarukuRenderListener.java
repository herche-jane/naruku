package com.naruku.pdfTest.listener;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.awt.geom.RectangularShape;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *naruku自定义的pdf内容处理器
 * @author herche
 * @date 2022/11/05
 */
public class NarukuRenderListener implements RenderListener {
	//用来存放文字的矩形
	 List<Rectangle2D.Float> rectText = new ArrayList<Rectangle2D.Float>();
	//用来存放文字
	 List<String> textList = new ArrayList<String>();
	//用来存放文字的y坐标
	 List<Float> listY = new ArrayList<Float>();
	//用来存放每一行文字的坐标位置
	 List<Map<String, Rectangle2D.Float>> rows_text_rect = new ArrayList<Map<String, Rectangle2D.Float>>();
	
	public NarukuRenderListener() {
	}
	
	//step 2,遇到"BT"执行
	@Override
	public void beginTextBlock() {
		// TODO Auto-generated method stub
	}
	
	//step 3
	
	/**
	 * 文字主要处理方法
	 */
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
			
			Rectangle2D.Float rect = new Rectangle2D.Float(rightX, leftY, rightX - leftX, rightY - leftY);
			System.out.println("text:" + text + "--x:" + rect.x + "--y:" + rect.y + "--width:" + rect.width + "--height:" + rect.height);
			if (listY.contains(rect.y)) {
				int index = listY.indexOf(rect.y);
				textList.set(index, textList.get(index) + text);
			} else {
				textList.add(text);
				listY.add(rect.y);
			}
			
			Map<String, Rectangle2D.Float> map = new HashMap<String, Rectangle2D.Float>();
			map.put(text, rect);
			rows_text_rect.add(map);
		}
	}
	
	//step 4(最后执行的，只执行一次)，遇到“ET”执行
	@Override
	public void endTextBlock() {
		// TODO Auto-generated method stub
	}
	
	//step 1(图片处理方法)
	@Override
	public void renderImage(ImageRenderInfo renderInfo) {
	
	}
	
//	public
	
	public List<Rectangle2D.Float> getRectText() {
		return rectText;
	}
	
	public void setRectText(List<Rectangle2D.Float> rectText) {
		this.rectText = rectText;
	}
	
	public List<String> getTextList() {
		return textList;
	}
	
	public void setTextList(List<String> textList) {
		this.textList = textList;
	}
	
	public List<Float> getListY() {
		return listY;
	}
	
	public void setListY(List<Float> listY) {
		this.listY = listY;
	}
	
	public List<Map<String, Rectangle2D.Float>> getRows_text_rect() {
		return rows_text_rect;
	}
	
	public void setRows_text_rect(List<Map<String, Rectangle2D.Float>> rows_text_rect) {
		this.rows_text_rect = rows_text_rect;
	}
}
