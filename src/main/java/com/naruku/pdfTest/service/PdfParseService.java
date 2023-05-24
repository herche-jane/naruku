package com.naruku.pdfTest.service;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.naruku.dbReserved.DBConnection;
import com.naruku.dbReserved.entity.DataBaseInfo;
import com.naruku.exception.PDFException;
import com.naruku.pdfTest.dao.PdfInfoDao;
import com.naruku.pdfTest.dao.PdfKeywordDao;
import com.naruku.pdfTest.dao.PdfModuleDao;
import com.naruku.pdfTest.dao.PdfValueDao;
import com.naruku.pdfTest.listener.NarukuParserListener;
import com.naruku.pdfTest.search.SearchInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * pdf解析服务 用于pdf的解析
 * @author herche
 * @date 2022/11/07
 */
public class PdfParseService {
	private  final Logger logger = LoggerFactory.getLogger(PdfParseService.class);
	private  final String SPIT_CHAR = "^";
	private  final PdfModuleDao pdfModuleDao;
	private final DataBaseInfo dataBaseInfo;
	private final PdfInfoDao pdfInfoDao;
	private final PdfKeywordDao pdfKeywordDao;
	private final PdfValueDao pdfValueDao;
	List<SearchInfo> searchInfos = new ArrayList<>();
	public PdfParseService(DataBaseInfo dataBaseInfo){
		this.dataBaseInfo = dataBaseInfo;
		this.pdfModuleDao = new PdfModuleDao();
		this.pdfInfoDao = new PdfInfoDao();
		this.pdfKeywordDao = new PdfKeywordDao();
		this.pdfValueDao = new PdfValueDao();
	}
	
	/**
	 * 初始化pdf
	 */
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
		try {
			PdfReader pdfReader = new PdfReader(inputPdf);
			//新建一个PDF解析对象
			PdfReaderContentParser parser = new PdfReaderContentParser(pdfReader);
			for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
				NarukuParserListener parserListener = new NarukuParserListener();
				//解析PDF，并处理里面的文字
				parser.processContent(i,parserListener);
				searchInfos.addAll(parserListener.getSearchInfos());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 获取Key（根据文字获取）
	 */
	public String getValueByKey(String moduleId,String keyword,String parsedPdf,Integer page) throws SQLException {
		if (StringUtils.hasText(keyword)){
			// 查询 t_naruku_keyword 表
			Connection connection = DBConnection.getConnection(dataBaseInfo);
			if (dataBaseInfo != null){
				String keyId = pdfKeywordDao.findIdByKey(moduleId, keyword, page, connection);
				// 拿到key后 根据keyId找到value的数据
				SearchInfo searchInfo = pdfValueDao.findXYByKeyId(keyId, connection);
				System.out.println(searchInfo.getOverallLengthX());
				System.out.println(searchInfo.getY());
				connection.close();
				if (ObjectUtils.isEmpty(searchInfos)){
					throw new PDFException("searchInfos是空！");
				}
				for (SearchInfo info : searchInfos) {
					if (!ObjectUtils.isEmpty(info.getValue(searchInfo.getOverallLengthX(),searchInfo.getY()))){
						return info.getKey();
					}
				}
			}
			if (!StringUtils.hasText(parsedPdf)){
				throw new PDFException("请传入需要解析的pdf路径：" + parsedPdf);
			}
			
		}
		return null;
	}
}
