package com.naruku.pdfTest;

import com.naruku.dbReserved.entity.DataBaseInfo;
import com.naruku.pdfTest.service.PDFTemplateService;
import com.naruku.pdfTest.service.PdfParseService;
import org.junit.Test;

import java.sql.SQLException;

public class PdfTest {
	
	@Test
	public void initPdf() throws SQLException {
		DataBaseInfo dataBaseInfo = new DataBaseInfo();
		dataBaseInfo.setDbDriver("com.mysql.cj.jdbc.Driver");
		dataBaseInfo.setDbName("qsd");
		dataBaseInfo.setDbUrl("jdbc:mysql://127.0.0.1:3306/nruku");
		dataBaseInfo.setDbUserName("root");
		dataBaseInfo.setDbPassword("123456");
		PDFTemplateService service = new PDFTemplateService(dataBaseInfo);
		service.initPdf("E:\\herche_work_qigao\\test\\111.pdfTest");
//		service.initPdf("E:\\herche_work_qigao\\2-2\\test1\\iii.pdfTest");
//		service.findKeyInPdf("No. of teeth","ca244e31c597477b8c563cc5dde694f3");
//		System.err.println("==============================");
//		service.findKeyInPdf("9","ca244e31c597477b8c563cc5dde694f3");
//		System.out.println("==============================");
//		service.savePdfAttr("Face width","8.5000 mm","3030578b0dd94dd8a614819596b81e36",1);
//		System.err.println("==============================");
//		service.findKeyInPdf("22","bbd50852bbc0402ca4f5771f18fefa19");
		PdfParseService pdfParseService = new PdfParseService(dataBaseInfo);
		pdfParseService.initPdf("E:\\\\herche_work_qigao\\\\2-2\\\\test1\\\\iii.pdfTest");
		String value = pdfParseService.getValueByKey("3030578b0dd94dd8a614819596b81e36", "Face width", "E:\\\\herche_work_qigao\\\\2-2\\\\test1\\\\iii.pdfTest", 1);
		System.out.println(value);
	}
	
}
