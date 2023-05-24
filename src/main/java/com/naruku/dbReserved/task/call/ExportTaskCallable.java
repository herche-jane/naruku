package com.naruku.dbReserved.task.call;

import com.naruku.dbReserved.entity.DataBaseInfo;
import com.naruku.dbReserved.entity.TableInfo;
import com.naruku.dbReserved.export.DBExportSqlUtil;
import com.naruku.dbReserved.task.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * 导出回调
 * @author herche
 * @data 2022/10/26
 */
public class ExportTaskCallable implements Callable<Response> {
	private Logger logger = LoggerFactory.getLogger(ExportTaskCallable.class);
	private DataBaseInfo dataBaseInfo;
	private TableInfo tableInfo;
	
	public ExportTaskCallable(DataBaseInfo dataBaseInfo,TableInfo tableInfo){
		this.dataBaseInfo = dataBaseInfo;
		this.tableInfo = tableInfo;
	}
	
	/**
	 * 导出  如果失败的话 记录到一个结构日志中，有专门日志跑
	 * @return
	 * @throws Exception
	 */
	@Override
	public Response call() throws Exception {
		logger.info("开始回到" + dataBaseInfo.getTableName() + "导入到：" +dataBaseInfo.getFileUrl() + "/" + dataBaseInfo.getFileName() + "中");
		Response response = new Response();
		response.setTableName(dataBaseInfo.getTableName());
		response.setSuccess(false);
		response.setDataBaseInfo(dataBaseInfo);
		try {
			DBExportSqlUtil.export(dataBaseInfo,tableInfo);
			response.setSuccess(true);
		}catch (Exception e){
			response.setErrorMsg(e.getMessage());
		}
		return response;
	}
}
