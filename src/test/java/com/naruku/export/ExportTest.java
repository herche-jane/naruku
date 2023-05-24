package com.naruku.export;

import com.naruku.dbReserved.entity.DataBaseInfo;
import org.junit.Test;

import java.io.File;

public class ExportTest {
	
	public static void runSqlFile(DataBaseInfo dataBaseInfo) {
	
	}
	
	
	// scp到目标
//		ScpUtil scpUtil = new ScpUtil();
//		scpUtil.isExit("/home/sql");
//		scpUtil.uploadFile(file,"/home/sql");
	
	@Test
	public void exportTest() {
		DataBaseInfo dataBaseInfo = new DataBaseInfo();
		dataBaseInfo.setDbDriver("com.mysql.cj.jdbc.Driver");
		dataBaseInfo.setDbName("qsd");
		dataBaseInfo.setDbUrl("jdbc:mysql://192.168.5.7:3306/qsd");
		dataBaseInfo.setDbUserName("root");
		dataBaseInfo.setDbPassword("Data2020!");
//		dataBaseInfo.setTableName("act_de_model");
//		dataBaseInfo.setFileName("test.sql");
		dataBaseInfo.setFileUrl("E:\\herche_test\\sql");
		dataBaseInfo.setErrorFilePath("E:\\herche_test\\error\\error.log");
//		dataBaseInfo.setMaxTableWithThread(10);
//	    dataBaseInfo.setTableName("act_de_databasechangelog");
//		ExportTask exportTask = new ExportTask();
//		SCPBaseInfo scpBaseInfo = new SCPBaseInfo();
//		scpBaseInfo.setHost("192.168.5.7");
//		scpBaseInfo.setPort(22);
//		scpBaseInfo.setUsername("root");
//		scpBaseInfo.setPassword("Data2020!");
//		scpBaseInfo.setEncrypy(false);
//		scpBaseInfo.setRemotePath("/home/sql");
//		exportTask.doExport(dataBaseInfo,null,null,null);
		File file = new File("E:\\herche_test\\sql");
		File[] files = file.listFiles();
		
		for (File file1 : files) {
//			SQLExec sqlExec = new SQLExec();
//			sqlExec.setDriver(dataBaseInfo.getDbDriver());
//			sqlExec.setUrl(dataBaseInfo.getDbUrl());
//			sqlExec.setUserid(dataBaseInfo.getDbUserName());
//			sqlExec.setPassword(dataBaseInfo.getDbPassword());
//			sqlExec.setEncoding("UTF8");
//			//设置sql脚本
//			sqlExec.setSrc(file1);
//			// 设置是否输出 true-打印,false-不打印
//			sqlExec.setPrint(true);
//			// 要指定这个属性，不然会出错
//			sqlExec.setProject(new Project());
//			sqlExec.setOutputEncoding("UTF8");
//			sqlExec.setAutocommit(true);
//			sqlExec.execute();
//			System.out.println("sqlExec execute success!");
		}
	}
}
