package com.naruku.dbReserved;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;
import org.springframework.util.ObjectUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

/**
 * DBUtils测试
 */
public class TestConnection {
	
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	final String URL = "jdbc:mysql://127.0.0.1:3306/qsd";
	final String USERNAME = "root";
	final String PASSWORD = "123456";
	Connection connection;
	
	{
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}
	
	
	@Test
	public void selectAll()throws SQLException {
		QueryRunner queryRunner = new QueryRunner();
//		String count = "select count(1) from t_ds_process_instance";
		RowDate<String> rowDate = new RowDate<>();
		rowDate.setEnd(false);
		rowDate.setStart(0);
		rowDate.setPageRows(5000);
		long start = System.currentTimeMillis();
		while (!rowDate.isEnd){
			String sql = "select * from t_ds_process_instance limit " + rowDate.start * rowDate.pageRows + "," + ((rowDate.start + 1) * rowDate.pageRows);
			rowDate.start++;
			List<Object[]> query = queryRunner.query(connection, sql, new ArrayListHandler());
			if (ObjectUtils.isEmpty(query) || query.size() < rowDate.pageRows){
				rowDate.isEnd = true;
			}
			System.out.println("查询了一次");
		}
		long end = System.currentTimeMillis();
		System.out.println("总耗时:" + (end - start) / 1000  + "秒");
//		query.stream().forEach(System.out::println);
		DbUtils.close(connection);
	}
	
	
	@Test
	public void selectDataFaster() throws SQLException {
		QueryRunner qr =new QueryRunner();
		List<FutureTask<List<Object[]>>> resultList = new ArrayList<>();
		int a = Runtime.getRuntime().availableProcessors();//当前设备的CPU个数
		System.out.println(a);//4
		ExecutorService threadPool = Executors.newFixedThreadPool(a);
		String sql = "SELECT COUNT(*) FROM t_ds_process_instance where update_time > 0";
		try{
			Object obj = qr.query(connection,sql, new ScalarHandler());
			int count = Integer.parseInt(String.valueOf(obj));
			System.out.println(count);
			RowDate<String> rowDate = new RowDate<>();
			rowDate.setEnd(false);
			rowDate.setStart(0);
			rowDate.setPageRows(5000);
			long cycles = count / rowDate.getPageRows();
			for (int i = 0; i < cycles; i++) {
				long idx = i * rowDate.getPageRows();
				long idx1 = (i + 1) * rowDate.getPageRows();
				//具体的查询任务
				String sql1 = "SELECT * FROM t_ds_process_instance where update_time > 0 LIMIT " + idx + " ," +idx1;
				FutureTask<List<Object[]>> futureTask = new FutureTask<List<Object[]>>(()->qr.query(connection, sql1, new ArrayListHandler()));
				//把任务丢给线程池调度执行
				threadPool.execute(futureTask);
				//future异步模式，把任务放进去先，先不取结果
				resultList.add(futureTask);
				while (resultList.size() > 0) {
					Iterator<FutureTask<List<Object[]>>> iterator = resultList.iterator();
					while (iterator.hasNext()) {
						try {
							List<Object[]> list = iterator.next().get();
							//获取一个就删除一个任务
							iterator.remove();
						} catch (InterruptedException  | ExecutionException e) {
						}
					}
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		DbUtils.close(connection);
	}
	
	
}
