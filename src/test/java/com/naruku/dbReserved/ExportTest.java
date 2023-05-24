package com.naruku.dbReserved;

import com.naruku.dbReserved.entity.DataBaseInfo;
import com.naruku.dbReserved.entity.TableInfo;
import com.naruku.dbReserved.export.mysql.MyDbInfoExport;
import org.junit.Test;

public class ExportTest {
	
	@Test
	public void testExport() throws Exception {
		DataBaseInfo dataBaseInfo = new DataBaseInfo();
		dataBaseInfo.setDbDriver("com.mysql.cj.jdbc.Driver");
		dataBaseInfo.setDbName("qsd");
		dataBaseInfo.setDbUrl("jdbc:mysql://127.0.0.1:3306/qsd");
		dataBaseInfo.setDbUserName("root");
		dataBaseInfo.setDbPassword("123456");
		dataBaseInfo.setFileName("test.sql");
		dataBaseInfo.setFileUrl("E:\\herche_test");
		MyDbInfoExport myDbInfoExport = new MyDbInfoExport();
		TableInfo tableInfo = new TableInfo();
		tableInfo.setTableName("t_ds_task_module");
		String queryAllTablesInfoSql = myDbInfoExport.getTableData(dataBaseInfo,tableInfo);
		System.out.println(queryAllTablesInfoSql);
//		Connection connection = DBConnection.getConnection(dataBaseInfo);
//		PreparedStatement preparedStatement = connection.prepareStatement(queryAllTablesInfoSql);
//		ResultSet resultSet = preparedStatement.executeQuery();
//		ResultSetMetaData metaData = resultSet.getMetaData();
//		List<Map> list = new ArrayList<>();
//		while (resultSet.next()){
//			Map<String, Object> jsonMap = new HashMap<>();
//			int columnCount = metaData.getColumnCount();
//			for (int i = 1; i <= columnCount; i++) {
//				String columnTypeName = metaData.getColumnTypeName(i);
//				String columnName = metaData.getColumnName(i);
//				if ("INT".equals(columnTypeName)) {
//					int anInt = resultSet.getInt(columnName);
//					jsonMap.put(columnName, anInt);
//				} else {
//					String s = resultSet.getString(columnName);
//					jsonMap.put(columnName, s);
//				}
//			}
//			list.add(jsonMap);
//		}
//		System.out.println(list);
	}
}
