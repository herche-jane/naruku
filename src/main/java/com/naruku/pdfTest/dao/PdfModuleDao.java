package com.naruku.pdfTest.dao;

import com.naruku.exception.PDFException;
import com.naruku.pdfTest.search.KeyWordInfo;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PdfModuleDao {
	
	public boolean savePdfModule(String moduleId, String inputPdf,String fileId,Connection connection) {
		String date =  new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		Object [] obj={moduleId,inputPdf,date,date,fileId};
		QueryRunner runner=new QueryRunner();
		try {
			int count=runner.update(connection,"insert into t_naruku_pdf_module (id,file_path,create_time,update_time,file_id) values(?,?,?,?,?)",obj);
			return count > 0;
		} catch (SQLException throwables) {
			throwables.printStackTrace();
			return false;
		}
	}
	
	public KeyWordInfo findKeyInPdf(String key, String moduleId,Integer page,Connection connection) {
		QueryRunner runner=new QueryRunner();
		try {
			String sql = "select id,module_id,info_x,info_y,width,heigth,page,info_value from t_naruku_pdf_info where module_id = ? and info_value = ? and page = ?";
			List<Object[]> query = runner.query(connection, sql, new ArrayListHandler(),moduleId,key,page);
			System.out.println(query.size());
			if (ObjectUtils.isEmpty(query) || query.size() != 1){
				throw new PDFException("请保证模板数据中的元素务必唯一！否则无法解析！！！");
			}
			Object[] objects = query.get(0);
			KeyWordInfo keyWordInfo = new KeyWordInfo();
			Field[] declaredFields = keyWordInfo.getClass().getDeclaredFields();
			for (int i = 0; i < (Math.min(declaredFields.length, objects.length)); i++) {
				declaredFields[i].setAccessible(true);
				Object object = objects[i];
				if (ObjectUtils.isEmpty(object))continue;
				declaredFields[i].set(keyWordInfo,object);
			}
			return keyWordInfo;
		} catch (SQLException | IllegalAccessException throwables) {
			throwables.printStackTrace();
		}
		return null;
	}
}
