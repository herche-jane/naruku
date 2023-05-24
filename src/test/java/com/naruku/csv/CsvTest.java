package com.naruku.csv;

import com.naruku.csvTest.CsvReader;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

public class CsvTest {
	@Test
	public void read() throws IOException {
		CsvReader csvReader = new CsvReader("E:\\herche_work_qigao\\eeeeee.csv",',');
		boolean b = csvReader.readHeaders();
		boolean b1 = csvReader.readRecord();
		System.out.println(b);
		System.out.println(b1);
	}
}
