package com.naruku.dbReserved.scp;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SCPOutputStream;
import ch.ethz.ssh2.Session;
import com.naruku.dbReserved.entity.SCPBaseInfo;
import com.naruku.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * scp 执行器
 * @author herche
 * @date 2022/10/28
 */
public class ScpExecutor {
	private final SCPBaseInfo scpBaseInfo;
	private Connection conn;
	private static Logger logger = LoggerFactory.getLogger(ScpExecutor.class);
	public ScpExecutor(SCPBaseInfo scpBaseInfo){
		this.scpBaseInfo = scpBaseInfo;
	}
	
	/**
	 * 获取远程连接
	 * */
	public boolean conneCtionLinux() throws IOException {
		conn= new Connection(scpBaseInfo.getHost(),scpBaseInfo.getPort());
		conn.connect();
		boolean isAuthenticated = conn.authenticateWithPassword(scpBaseInfo.getUsername(),scpBaseInfo.getPassword());
		if(!isAuthenticated){
			System.err.println("authentication failed");
			logger.error("authentication failed");
			return false;
		}
		return true;
	}
	
	
	/**
	 * 远端目录是否存在，不存在则新建
	 * @param remotePath 是否新建成功
	 * @return
	 */
	public boolean isExit(String remotePath){
		try {
			if(conneCtionLinux()){
				Session session= conn.openSession();//打开一个会话
				//远程执行linux命令 因为上传的文件没有读的文件 需要加上才能下载 （如果你上传的文件有）
				String cmd = "if [ ! -f \""+remotePath+"\" ]; then mkdir " + remotePath + "\n"+ "else echo \"文件夹已经存在\"" +"\n"+ "fi";
				System.out.println("linux命令=="+cmd);
				session.execCommand(cmd);//执行命令
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("连接失败");
			return false;
		}
		return true;
	}
	
	/**
	 *上传文件到服务器
	 */
	public boolean uploadFile(File file) {
		if (!Files.exists(Paths.get(file.getAbsolutePath()), new LinkOption[]{LinkOption.NOFOLLOW_LINKS})){
			logger.error("文件不存在!");
			return false;
		}
		SCPOutputStream os = null;
		Session session= null;
		try {
			if(conneCtionLinux()){
				String fn = file.getName();
				File f = new File(fn);
				String name = f.getName();
				//上传文件
				SCPClient scp = new SCPClient(conn);
				session= conn.openSession();//打开一个会话
				// 先检验是否存在
				logger.info("开始创建：" + scpBaseInfo.getRemotePath() + "路径");
				isExit(scpBaseInfo.getRemotePath());
				String remotePath =  scpBaseInfo.getRemotePath() + "/" + new SimpleDateFormat("yyyyMMdd").format(new Date());
				isExit(remotePath);
				logger.info(remotePath + "创建完成");
				if (!file.isDirectory()){
					write(file,scp, name,remotePath);
				} else {
					File[] files = file.listFiles();
					for (File file1 : files) {
						write(file1,scp, file1.getName(),remotePath);
					}
				}
			}
		}catch (Exception ex){
			ex.printStackTrace();
			logger.error("出错了：" + ex.getMessage());
			return false;
		}finally {
			assert session != null;
			session.close();
			conn.close();
		}
		return true;
	}
	
	/**
	 * 写
	 * @param file
	 * @param scp
	 * @param newName
	 * @param remotePath
	 * @throws IOException
	 */
	private void write(File file, SCPClient scp, String newName, String remotePath) throws IOException {
		logger.info("开始写入：" + file.getAbsolutePath() + "文件");
		byte[] b = FileUtils.readOnce(file);
		SCPOutputStream os = scp.put(newName, b.length, remotePath, null);
		os.write(b,0,b.length);
		//远程执行linux命令 因为上传的文件没有读的文件 需要加上才能下载 （如果你上传的文件有）
//		String cmd = "chmod +r "+ remotePath + "/" +file.getName();
//		System.out.println("linux命令=="+cmd);
//		session.execCommand(cmd);//执行命令
		os.flush();
		os.close();
		logger.info("文件：" + file.getAbsolutePath() + "写入完毕");
	}
	
	
}
