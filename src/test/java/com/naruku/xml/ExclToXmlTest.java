package com.naruku.xml;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.InputStream;

public class ExclToXmlTest {
	@Test
	public void exclToXml(){
		String filename ="E:\\herche_utils\\naexcl\\src\\test\\resources\\alias.xlsx";
		OPCPackage pkg;
		try {
			pkg = OPCPackage.open(filename);
			XSSFReader r = new XSSFReader(pkg);
			FileOutputStream fos = new FileOutputStream("E:\\herche_utils\\naexcl\\src\\test\\resources\\alias.xml");
			//查看转换的xml原始文件，方便理解后面解析时的处理,
			InputStream in = r.getSheet("rId1");
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) != -1) {
				fos.write(buf, 0, len);
			}
			fos.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
