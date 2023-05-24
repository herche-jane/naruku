package com.naruku.pdfTest.dao;

import com.naruku.exception.PDFException;
import com.naruku.pdfTest.search.KeyWordInfo;
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
 * pdf关键字交互类
 *
 * @author herche
 * @date 2022/11/07
 */
public class PdfKeywordDao {
	public boolean savePdfKeyword(KeyWordInfo keyWordInfo, Connection connection) {
		String date =  new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String id = UUID.randomUUID().toString().substring(0,32);
		keyWordInfo.setKeyId(id);
		if (ValidateUtils.isEmptyOrValidateField(keyWordInfo,null)){
			throw new PDFException("key的信息不完整!!!");
		}
		Object [] obj={id,keyWordInfo.getModuleId(),keyWordInfo.getKeyValue(),keyWordInfo.getX(),keyWordInfo.getY(),keyWordInfo.getCoordinatePage(),keyWordInfo.getWidth(),keyWordInfo.getHeight(),date,date};
		QueryRunner runner=new QueryRunner();
		try {
			int count=runner.update(connection,"insert into t_naruku_keyword (id,module_id,keyword_name,keyword_spot_x,keyword_spot_y,pdf_page,width,heigth,create_time,update_time) values(?,?,?,?,?,?,?,?,?,?)",obj);
			return count > 0;
		} catch (SQLException throwables) {
			throwables.printStackTrace();
			return false;
		}
	}
	
	public String findIdByKey(String moduleId, String keyword,Integer page,Connection connection) {
		if (!StringUtils.hasText(moduleId) || !StringUtils.hasText(keyword)){
			throw new PDFException("moduleId或者keyword为空");
		}
		QueryRunner runner=new QueryRunner();
		try {
			List<Object[]> query = runner.query(connection, "select id from t_naruku_keyword where module_id = ? and keyword_name = ? and pdf_page = ?", new ArrayListHandler(), moduleId, keyword, page);
			if (ObjectUtils.isEmpty(query) || query.size() != 1){
				throw new PDFException("请保证key必唯一！否则无法解析！！！");
			}
			return query.get(0)[0].toString();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
			return null;
		}
	
	}
}
