package com.naruku.csv;

import com.naruku.CustomizeCsv;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class CustomizeCsvTest {
	@Test
	public void importCsvTest() throws FileNotFoundException, UnsupportedEncodingException {
		CustomizeCsv.csvUtf8("E:\\herche_test\\weq.csv","E:\\herche_test\\wcnnm.csv");
	}
}
