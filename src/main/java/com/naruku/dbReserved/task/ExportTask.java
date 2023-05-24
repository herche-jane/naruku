package com.naruku.dbReserved.task;

import com.naruku.dbReserved.config.ExecutorConfig;
import com.naruku.dbReserved.entity.DataBaseInfo;
import com.naruku.dbReserved.entity.SCPBaseInfo;
import com.naruku.dbReserved.entity.TableInfo;
import com.naruku.dbReserved.export.DBExportSqlUtil;
import com.naruku.dbReserved.scp.ScpExecutor;
import com.naruku.dbReserved.task.call.ExportTaskCallable;
import com.naruku.utils.BeanUtils;
import com.naruku.utils.FileUtils;
import com.naruku.utils.OSinfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

/**
 * 导出调度
 *
 * @author herche
 * @data 2022/10/27
 */
public class ExportTask {
	private Logger logger  = LoggerFactory.getLogger(ExportTask.class);
	/**
	 * 导出任务 支持三种模式 单表  多表(支持选定多表，支持关键字筛选) 全表
	 * 当关键字为空时，默认没有条件
	 * 如果多表查询，tableInfos一定不能为空，触发线程数以dataBaseInfo数据为准
	 * @param dataBaseInfo 数据实体类
	 * @param tableInfos 需要遍历的表
	 * @param filter 过滤参数 多个以“，“隔开 在   dataBaseInfo 的tableName为空 或者 tableInfos 有多个值 单表不起作用 如果dataBaseInfo 和 tableInfos的表设计有冲突，以TableInfo为准databaseInfo中的
	 *               tableName将在下个版本直接废弃
	 */
	public void doExport(DataBaseInfo dataBaseInfo, List<TableInfo> tableInfos, SCPBaseInfo scpBaseInfo,String filter) {
		logger.info("-------创建文件父目录------：" + dataBaseInfo.getFileUrl());
		FileUtils.createCurrentDir(dataBaseInfo.getFileUrl());
		logger.info("-------创建文件父目录完毕------：" + dataBaseInfo.getFileUrl());
		logger.info("-------创建文件父目录------：" + dataBaseInfo.getErrorFilePath());
		FileUtils.createCurrentDir(dataBaseInfo.getErrorFilePath());
		logger.info("-------创建文件父目录完毕------：" + dataBaseInfo.getErrorFilePath());
		logger.info("------开始执行任务------");
		// 单表查询 dataBaseInfo
		if (ObjectUtils.isEmpty(tableInfos) || StringUtils.hasText(dataBaseInfo.getTableName())){
			try {
				DBExportSqlUtil.export(dataBaseInfo, null);
			} catch (Exception e) {
				doError(dataBaseInfo);
			}
		}
		// 多张表 但是没有达到阈值
		List<String> tableCount = DBExportSqlUtil.getTableCount(dataBaseInfo,tableInfos,filter);
		if (ObjectUtils.isEmpty(tableCount)) return;
		if (tableCount.size() < dataBaseInfo.getMaxTableWithThread()) {
			for (String tableName : tableCount) {
				dataBaseInfo.setTableName(tableName);
				try {
					DBExportSqlUtil.export(dataBaseInfo, null);
				}catch (Exception e){
					doError(dataBaseInfo);
				}
			
			}
		}
		// 达到阈值
		logger.info("======达到阈值，启用多线程======");
		List<FutureTask<Response>> futureTaskList = new ArrayList<>();
		ExecutorConfig executorConfig = new ExecutorConfig();
		logger.info("======正在创建线程池======");
		Executor taskExecutor = executorConfig.createExecutorByDataBaseInfo(dataBaseInfo);
		logger.info("======线程池创建完毕======");
		for (String tableName : tableCount) {
			// 不要公用dataBaseInfo
			DataBaseInfo dataBaseInfo1 = new DataBaseInfo();
			try {
				dataBaseInfo1.setTableName(tableName);
				BeanUtils.deepCopySameClass(dataBaseInfo,dataBaseInfo1);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			ExportTaskCallable callable = new ExportTaskCallable(dataBaseInfo1, null);
			FutureTask<Response> futureTask = new FutureTask<>(callable);
			futureTaskList.add(futureTask);
			taskExecutor.execute(futureTask);
		}
		while (!ObjectUtils.isEmpty(futureTaskList)) {
			try {
				parse(futureTaskList);
				futureTaskList.clear();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// 进行远端传输
		if (!ObjectUtils.isEmpty(scpBaseInfo)){
			logger.info("======开始传输文件到目标机器======" + "目标机器：" + scpBaseInfo.toString());
			ScpExecutor scpExecutor = new ScpExecutor(scpBaseInfo);
			File file = new File(dataBaseInfo.getFileUrl());
			if (!file.exists()){
				logger.error("导出sql错误！");
				return;
			}
			scpExecutor.uploadFile(file);
		}
	}
	
	private void parse(List<FutureTask<Response>> futureTaskList) throws ExecutionException, InterruptedException {
		if (futureTaskList != null && futureTaskList.size() > 0) {
			for (FutureTask<Response> futureTask : futureTaskList) {
				Response response = futureTask.get();
				if (!response.isSuccess()) {
					// 执行失败 日志记录
//					logger.error("文件：" + response.getFileName() + "执行失败！");
					doError(response.getDataBaseInfo());
				}
//				logger.error("文件：" + response.getFileName() + "执行成功！");
			}
		}
	}
	
	
	private void doError(DataBaseInfo dataBaseInfo) {
		logger.info("------执行任务失败------开始记录---");
		if (ObjectUtils.isEmpty(dataBaseInfo))return;
		if (!StringUtils.hasText(dataBaseInfo.getErrorFilePath())) {
			if (OSinfo.isLinux()) {
				dataBaseInfo.setErrorFilePath("/opt/error/exportError.log");
			} else if (OSinfo.isWindows()) {
				dataBaseInfo.setErrorFilePath("D:\\error\\exportError.log");
			} else {
				dataBaseInfo.setErrorFilePath("------");
			}
		}
		DBExportSqlUtil.writeErrorLog(dataBaseInfo.getErrorFilePath(), dataBaseInfo);
	}
	
	
}
