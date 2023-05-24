package com.naruku.dbReserved.config;

import com.naruku.dbReserved.entity.DataBaseInfo;

import java.util.concurrent.*;

/**
 * 线程池配置 用户可以通过Db实体类来配置 核心线程数 最大线程数 其他的皆不可动
 * @author herche
 * @date 2022/10/27
 */
public class ExecutorConfig {
	/**
	 * 根据用户给的参数 创建一个线程池  只有当dataBase中的表数量达到 某种数量以上  才会创建
	 * @param dataBaseInfo
	 * @return
	 */
	public Executor createExecutorByDataBaseInfo(DataBaseInfo dataBaseInfo) {
		Integer coreThreads =  dataBaseInfo.getnThreads() == null ? Runtime.getRuntime().availableProcessors() > 2 ? Runtime.getRuntime().availableProcessors() * 2 : 8 : dataBaseInfo.getnThreads();//获取到服务器的cpu内核
		ThreadPoolExecutor  executor = new ThreadPoolExecutor (coreThreads,coreThreads * 2,60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(3000));
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());//配置拒绝策略
		return executor;
	}
}
