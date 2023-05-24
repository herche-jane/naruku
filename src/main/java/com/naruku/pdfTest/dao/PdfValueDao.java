package com.naruku.pdfTest.dao;

import com.naruku.exception.PDFException;
import com.naruku.pdfTest.search.KeyWordInfo;
import com.naruku.pdfTest.search.SearchInfo;
import com.naruku.utils.ValidateUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * pdf值交互类
 *
 * @author herche
 * @date 2022/11/07
 */
public class PdfValueDao {
	public boolean savePdfValueword(KeyWordInfo keyWordInfo,String keyId, Connection connection) {
		String date =  new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String id = UUID.randomUUID().toString().substring(0,32);
		if (ValidateUtils.isEmptyOrValidateField(keyWordInfo,null)){
			throw new PDFException("key的信息不完整!!!");
		}
		Object [] obj={id,keyId,keyWordInfo.getX(),keyWordInfo.getY(),keyWordInfo.getCoordinatePage(),keyWordInfo.getWidth(),keyWordInfo.getHeight(),date,date};
		QueryRunner runner=new QueryRunner();
		try {
			int count=runner.update(connection,"insert into t_naruku_value (id,keyword_id,value_spot_x,value_spot_y,pdf_page,width,heigth,create_time,update_time) values(?,?,?,?,?,?,?,?,?)",obj);
			return count > 0;
		} catch (SQLException throwables) {
			throwables.printStackTrace();
			return false;
		}
	}
	
	public SearchInfo findXYByKeyId(String keyId, Connection connection) {
		if (!StringUtils.hasText(keyId)){
			throw new PDFException("keyId不能为空");
		}
		QueryRunner runner=new QueryRunner();
		try {
			List<Object[]> query = runner.query(connection, "select value_spot_x,value_spot_y from t_naruku_value where keyword_id = ?", new ArrayListHandler(), keyId);
			if (ObjectUtils.isEmpty(query) || query.size() != 1){
				throw new PDFException("请保证key必唯一！否则无法解析！！！");
			}
			SearchInfo searchInfo = new SearchInfo();
			searchInfo.setOverallLengthX(query.get(0)[0].toString());
			searchInfo.setY(query.get(0)[1].toString());
			return searchInfo;
		} catch (SQLException throwables) {
			throwables.printStackTrace();
			return null;
		}
		
	}
}
