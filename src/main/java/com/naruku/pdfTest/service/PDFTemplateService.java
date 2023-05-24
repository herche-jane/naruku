package com.naruku.pdfTest.service;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.naruku.dbReserved.DBConnection;
import com.naruku.dbReserved.entity.DataBaseInfo;
import com.naruku.exception.PDFException;
import com.naruku.pdfTest.dao.PdfInfoDao;
import com.naruku.pdfTest.dao.PdfKeywordDao;
import com.naruku.pdfTest.dao.PdfModuleDao;
import com.naruku.pdfTest.dao.PdfValueDao;
import com.naruku.pdfTest.listener.NarukuRenderListener;
import com.naruku.pdfTest.search.KeyWordInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * pdf模板解析
 * @author herche
 * @date 2022/11/05
 */
public class PDFTemplateService {
	private  final Logger logger = LoggerFactory.getLogger(PDFTemplateService.class);
	private  final String SPIT_CHAR = "^";
	private  final PdfModuleDao pdfModuleDao;
	private final DataBaseInfo dataBaseInfo;
	private final PdfInfoDao pdfInfoDao;
	private final PdfKeywordDao pdfKeywordDao;
	private final PdfValueDao pdfValueDao;
	public PDFTemplateService(DataBaseInfo dataBaseInfo){
		this.dataBaseInfo = dataBaseInfo;
		this.pdfModuleDao = new PdfModuleDao();
		this.pdfInfoDao = new PdfInfoDao();
		this.pdfKeywordDao = new PdfKeywordDao();
		this.pdfValueDao = new PdfValueDao();
	}
	
	/**
	 * 保存 pdf中key和value的相关关系
	 * @param key
	 * @param value
	 * @param moduleId
	 */
	public void savePdfAttr(String key,String value,String moduleId,Integer page){
		try {
			// 获得键的信息
			Connection connection = DBConnection.getConnection(dataBaseInfo);
			KeyWordInfo keyWordInfo = pdfModuleDao.findKeyInPdf(key, moduleId,page, connection);
			// 保存键的信息
			boolean savePdfKeyword = pdfKeywordDao.savePdfKeyword(keyWordInfo, connection);
			if (!savePdfKeyword){
				// 直接关掉
				connection.close();
				throw new PDFException("保存键信息失败！");
			}
			// 再取获得value
			KeyWordInfo valueWordInfo = pdfModuleDao.findKeyInPdf(value, moduleId,page,connection);
			// 保存value的信息
			boolean savePdfValue = pdfValueDao.savePdfValueword(valueWordInfo, keyWordInfo.getKeyId(), connection);
			if (!savePdfValue){
				connection.close();
				throw new PDFException("保存值信息失败！");
			}
			connection.commit();
			connection.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}
	
	/**
	 * 将所有的信息 全部存到数据库中
	 * @param inputPdf 输入的pdf文件
	 */
	public  void initPdf(String inputPdf){
		logger.info("开始初始化Pdf:" +  inputPdf);
		if (!Files.exists(Paths.get(inputPdf), new LinkOption[]{LinkOption.NOFOLLOW_LINKS})){
			logger.error("初始化Pdf:" +  inputPdf + "异常：因为文件不存在！");
			throw new PDFException("初始化Pdf:" +  inputPdf + "异常：因为文件不存在！");
		}
		File file = new File(inputPdf);
		if (file.isDirectory() || !file.getName().endsWith(".pdfTest")){
			logger.error("初始化Pdf:" +  inputPdf + "异常：因为不是pdf文件！");
			throw new PDFException("初始化Pdf:" +  inputPdf + "\"异常：因为不是pdf文件！");
		}
		if (dataBaseInfo == null){
			// TODO 使用自带的数据库
		}
		Connection connection = null;
		// 存储模板信息
		String moduleId = UUID.randomUUID().toString().replace("-","").substring(0,32);
		String fileId = UUID.randomUUID().toString().replace("-","").substring(0,32);
		try {
			connection = DBConnection.getConnection(dataBaseInfo);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
			throw new PDFException("初始化Pdf:" +  inputPdf + "\"异常：因为连接无法成功获取！");
		}
		if (ObjectUtils.isEmpty(connection)){
			throw new PDFException("初始化Pdf:" +  inputPdf + "\"异常：因为连接无法成功获取！");
		}
		boolean savePdfModule = pdfModuleDao.savePdfModule(moduleId, inputPdf, fileId,connection);
		if (!savePdfModule){
			throw new PDFException("初始化Pdf:" +  inputPdf + "\"异常：数据库保存信息失败！");
		}
		try {
			PdfReader pdfReader = new PdfReader(inputPdf);
			//新建一个PDF解析对象
			PdfReaderContentParser parser = new PdfReaderContentParser(pdfReader);
			for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
				NarukuRenderListener renderListener = new NarukuRenderListener();
				//解析PDF，并处理里面的文字
				parser.processContent(i,renderListener);
				// 保存到数据库
				List<Map<String, Rectangle2D.Float>> rows = renderListener.getRows_text_rect();
				pdfInfoDao.savePdfInfo(moduleId,i,rows,connection);
			}
			if (!connection.isClosed()){
				connection.commit();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
		
		}
		
	}
}
