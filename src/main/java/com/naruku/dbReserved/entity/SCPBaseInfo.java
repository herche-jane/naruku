package com.naruku.dbReserved.entity;

/**
 * 如果要往远端导，必须提供远端信息 这个类用于远端接受
 * @author herche
 * @date 2022/10/28
 */
public class SCPBaseInfo {
	private String host; // ip
	private int port; // 端口号
	private String username; // 用户名
	private String password; // 密码 TODO 加密
	private boolean isEncrypy = false; // 是否加密 密码
	private String remotePath; // 远端路径
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isEncrypy() {
		return isEncrypy;
	}
	
	public void setEncrypy(boolean encrypy) {
		isEncrypy = encrypy;
	}
	
	public String getRemotePath() {
		return remotePath;
	}
	
	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}
	
	@Override
	public String toString() {
		return "SCPBaseInfo{" +
				       "host='" + host + '\'' +
				       ", port=" + port +
				       ", username='" + username + '\'' +
				       ", password='" + password + '\'' +
				       ", isEncrypy=" + isEncrypy +
				       ", remotePath='" + remotePath + '\'' +
				       '}';
	}
}
