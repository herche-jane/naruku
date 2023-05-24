package com.naruku.shell;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SCPOutputStream;
import ch.ethz.ssh2.Session;
import com.naruku.utils.FileUtils;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.scp.client.ScpClient;
import org.apache.sshd.scp.client.ScpClientCreator;
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
 * scp移动文件
 * @author herche
 * @date 2022/10/11
 */
public class ScpUtil {
	//    private static String host = "192.168.67.48";
	private static String host = "192.168.5.7";
	//    private static String host = "192.168.71.29";
	private static String username = "root";
	private static String password = "Data2020!";
	private static Integer port = 22;
	
	//    private static String local = "F:\\miracle\\*";
	private static String local = "E:\\herche_work_qigao\\test";
	private static String remote = "/usr/kbox";
	private static Connection conn = null;
	private static boolean isAuthed = false;
//    private static String remote = "F:\\luna\\";
	private static Logger logger = LoggerFactory.getLogger(ScpUtil.class);
 
	
	
	
	
	/**
	 * 远端目录是否存在，不存在则新建
	 * @param remotePath 是否新建成功
	 * @return
	 */
	public boolean isExit(String remotePath){
		try {
			if(ConneCtionLinux()){
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
	public boolean uploadFile(File file,String remotePath) {
		if (!Files.exists(Paths.get(file.getAbsolutePath()), new LinkOption[]{LinkOption.NOFOLLOW_LINKS})){
			logger.error("文件不存在!");
			return false;
		}
		String newName ="";
//		String names = "";
		SCPOutputStream os = null;
		Session session= null;
		try {
			if(ConneCtionLinux()){
				String fn = file.getName();
				File f = new File(fn);
				String name = f.getName();
				newName =  new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"_"+name;
				//上传文件
				SCPClient scp = new SCPClient(conn);
				session= conn.openSession();//打开一个会话
				if (!file.isDirectory()){
					write(file,scp,os,session,newName,remotePath);
				} else {
					File[] files = file.listFiles();
					for (File file1 : files) {
						newName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"_"+file1.getName();
						write(file1,scp,os,session,newName,remotePath);
					}
				}
			}
//				names += newName+",";
			conn.close();
			session.close();
		}catch (Exception ex){
			ex.printStackTrace();
			logger.error("出错了：" + ex.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * 获取远程连接
	 * */
	private boolean ConneCtionLinux() throws IOException {
		conn= new Connection(host,port);
		conn.connect();
		boolean isAuthenticated = conn.authenticateWithPassword(username,password);
		if(!isAuthenticated){
			System.err.println("authentication failed");
			return false;
		}
		return true;
	}
	
	
	private void write(File file,SCPClient scp,SCPOutputStream os,Session session,String newName,String remotePath) throws IOException {
		byte[] b = FileUtils.readOnce(file);
		os = scp.put(newName,b.length, remotePath,null);
		os.write(b,0,b.length);
		//远程执行linux命令 因为上传的文件没有读的文件 需要加上才能下载 （如果你上传的文件有）
//		String cmd = "chmod +r "+ remotePath + "/" +file.getName();
//		System.out.println("linux命令=="+cmd);
//		session.execCommand(cmd);//执行命令
		os.flush();
		os.close();
	}


}
