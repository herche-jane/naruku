package com.naruku.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutTest {
	public static void main(String[] a ) {
		
		List resultList = new ArrayList();
		CSVPrinter printer = null;
		try {
			Object[] objects = null;
			for (int i = 0; i < 10; i++) {
				List list = new ArrayList();
				list.add("张三");
				list.add(20);
				resultList.add(list);
			}
			//  导出为CSV文件
			FileWriter writer = new FileWriter("E:\\herche_work_qigao\\test.csv");
			printer = CSVFormat.EXCEL.print(writer);
			Object[] cells = {"name", "age"};
			printer.printRecord(cells);
			for (int i = 0; i < resultList.size(); i++) {
				List o = (List) resultList.get(i);
//				objects = o.toArray();
				printer.printRecord(o);
				System.out.println(o);
			}
			printer.flush();
			printer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
