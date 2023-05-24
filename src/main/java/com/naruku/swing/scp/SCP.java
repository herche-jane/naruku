//package com.naruku.swing.scp;
//
//import ch.ethz.ssh2.Connection;
//import ch.ethz.ssh2.SCPClient;
//import ch.ethz.ssh2.Session;
//import ch.ethz.ssh2.StreamGobbler;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.*;
//
////监听器
//
//
//public class SCP extends JFrame implements ActionListener {
//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 1L;
//	static int g;
//	static int ipsum;
//	static int z;
//	private static final String DEFAULTCHART = "UTF-8";
//	String[] arr = null;
//	JTextField FilePath;
//	JTextField FileName;
//	String filepath;
//	String filename;
//	JLabel label_pathfile;
//	JLabel label_FileName;
//	Workbook wb;
//	int sum = 0;
//	private final JButton button_scp;
//	private final JButton button_unzip;
//	private final JButton button_panduanfile;
//	private final JButton button_delete;
//	private final JButton button_kill;
//	private final String pwd = "gdlocal";
//	private final String user = "gdlocal";
//	private final int port = 22;
//	private Connection conn;
//
//
//	SCP() {
//		this.setTitle("liyangwei");        //创建窗口
//		this.setSize(450, 450);        //设置窗口大小
//		this.setLocationRelativeTo(null);//设置窗口相对与指定组件的位置,null表示在中间
//		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//窗口关闭时退出程序
//
//		Container con = getContentPane();
//		con.setLayout(new FlowLayout(0, 10, 10)); //第一个参数向左对齐，第二/三个参数，组件间隔
//		button_scp = new JButton("上传");
//		button_unzip = new JButton("解压 文件名不用后缀");
//		button_delete = new JButton("删除");
//		button_panduanfile = new JButton("判断文件存不存在");
//		button_kill = new JButton("退出WiPASXNext");
//
//		button_scp.addActionListener(this);
//		button_unzip.addActionListener(this);
//		button_delete.addActionListener(this);
//		button_panduanfile.addActionListener(this);
//		button_kill.addActionListener(this);
//
//		FilePath = new JTextField(30);    //文件路径
//		FilePath.setText("/Users/holmes/Desktop/");
//		FileName = new JTextField(30);  //文件名
//		FileName.setText("WiPASXNext");
//		label_pathfile = new JLabel("文件路径");
//		label_FileName = new JLabel("文件名");
//
//		con.add(label_FileName);
//		con.add(FileName);
//		con.add(label_pathfile);
//		con.add(FilePath);
//		con.add(button_scp);
//		con.add(button_unzip);
//		con.add(button_delete);
//		con.add(button_panduanfile);
//		con.add(button_kill);
//
//		//设置窗口为可见的
//		this.setVisible(true);
//
//		arr = new String[200];
//		File file = new File("/Users/holmes/eclipse-workspace/SCP/data/ip.xls");
//		try {
//			wb = Workbook.getWorkbook(file);
//		} catch (BiffException | IOException e) {
//			e.printStackTrace();
//		}
//		Sheet sheet = wb.getSheet("Sheet1");
//		for (int i = 0; i < sheet.getRows(); i++) {//i行
//			for (int j = 0; j < sheet.getColumns(); j++) {//j列
//				Cell cell = sheet.getCell(j, i);
//				arr[i] = cell.getContents();
//			}
//		}
//		ipsum = sheet.getRows();
//		System.out.print("ipsum:" + ipsum);
//		g = 100 / ipsum;
//		wb.close();
//	}
//
//	public static void main(String[] args) throws Exception {
//		SCP liyangwei = new SCP();
//		new mySwing();
//	}
//
//	private static String processStdout(InputStream in, String charset) {
//		InputStream stdout = new StreamGobbler(in);
//		StringBuffer buffer = new StringBuffer();
//		try {
//			@SuppressWarnings("resource")
//			BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
//			String line = null;
//			while ((line = br.readLine()) != null) {
//				buffer.append(line + "\n");
//			}
//		} catch (UnsupportedEncodingException e) {
//			System.out.println("解析脚本出错");
//			//e.printStackTrace();
//		} catch (IOException e) {
//			System.out.println("解析脚本出错");
//			//e.printStackTrace();
//		}
//		return buffer.toString();
//	}
//
//	//动作
//	public void actionPerformed(ActionEvent e) {
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException g) {
//			g.printStackTrace();
//		}
//		if (e.getSource() == button_scp) {
//			scp();
//		} else if (e.getSource() == button_panduanfile) {
//			panduanFile();
//		} else if (e.getSource() == button_unzip) {
//			unzip();
//		} else if (e.getSource() == button_delete) {
//			deleteFile();
//		} else if (e.getSource() == button_kill) {
//			actionExitAPP();
//		}
//	}
//
//	public void scp() {
//		z = 0;
//		filename = FileName.getText();
//		filepath = FilePath.getText();
//		int i = 0;
//		sum = 0;
//		while (arr[i] != null) {
//			z = z + g;
//			String ip = arr[i];
//			//创建链接
//			conn = new Connection(ip, port);
//			try {
//				conn.connect();
//				//登陆
//				boolean isAuthed = conn.authenticateWithPassword(user, pwd);
//				//获取SCPClient
//				if (isAuthed) {
//					System.out.println("登陆成功!");
//					SCPClient scp = conn.createSCPClient();
//					//SCPClient scp = new SCPClient(conn);
//					scp.put(filepath + filename , "/Users/gdlocal/Desktop/");
//					System.out.println(arr[i] + "成功上传文件");
//					//SCPInputStream is =  scp.get("/users/gdlocal/Desktop/WiPASXNext_88.zip");
//					conn.close();
//				} else {
//					System.out.println(arr[i] + "登陆失败!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//					sum = sum + 1;
//					conn.close();
//				}
//			} catch (IOException e) {
//				System.out.println(arr[i] + "连接远程电脑失败！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
//				sum = sum + 1;
//				i = i + 1;
//				//e.printStackTrace();
//				conn.close();
//				continue;
//			}
//			i = i + 1;
//		}
//		JOptionPane.showMessageDialog(this, "登陆失败数量：" + sum + " ", "帮助", 1);
//	}
//
//	public String panduanFile() {
//		z = 0;
//		filename = FileName.getText();
//		String result = "";
//		int i = 0;
//		sum = 0;
//		while (arr[i] != null) {
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				//e.printStackTrace();
//			}
//			z = z + g;
//			String ip = arr[i];
//			try {
//				//创建链接
//				conn = new Connection(ip, port);
//				conn.connect();
//				//登陆
//				boolean isAuthed = conn.authenticateWithPassword(user, pwd);
//				//获取SCPClient
//				try {
//					if (isAuthed && conn != null) {
//						//打开一个会话
//						Session session = conn.openSession();
//						//判断远程电脑文件存不存在
//						//session.execCommand("[ -f /Users/gdlocal/Desktop/WiPASXNext136_886.zip ] && echo OK");
//						session.execCommand("[ -a /Users/gdlocal/Desktop/" + filename + " ] && echo OK");
//						result = processStdout(session.getStdout(), DEFAULTCHART);
//						if (!result.isEmpty()) {
//							System.out.println("文件存在");
//						} else {
//							System.out.println(arr[i] + "文件不存在!!!!!!!");
//							sum = sum + 1;
//						}
//						//如果为得到标准输出为空，说明脚本执行出错了
//						if (StringUtils.isBlank(result)) {
//							System.out.println("命令出错");
//						} else {
//							System.out.println("命令执行成功");
//						}
//						conn.close();
//						session.close();
//					} else {
//						System.out.println(arr[i] + "登陆失败!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//						sum = sum + 1;
//						conn.close();
//					}
//				} catch (IOException e) {
//					System.out.println("执行命令失败，链接conn:" + conn);
//					i = i + 1;
//					sum = sum + 1;
//					//e.printStackTrace();
//					continue;
//				}
//			} catch (IOException e) {
//				System.out.println(arr[i] + "登陆失败");
//				i = i + 1;
//				sum = sum + 1;
//				//e.printStackTrace();
//				continue;
//			}
//			i = i + 1;
//		}
//		JOptionPane.showMessageDialog(this, "文件不存在的数量：" + sum + " ", "帮助", 1);
//		return result;
//	}
//
//	public String unzip() {
//		z = 0;
//		filename = FileName.getText();
//		String result = "";
//		int i = 0;
//		sum = 0;
//		while (arr[i] != null) {
//			z = z + g;
//			String ip = arr[i];
//			try {
//				//创建链接
//				conn = new Connection(ip, port);
//				conn.connect();
//				//登陆
//				boolean isAuthed = conn.authenticateWithPassword(user, pwd);
//				//获取SCPClient
//				try {
//					if (isAuthed && conn != null) {
//						//打开一个会话
//						Session session = conn.openSession();
//						//判断远程电脑文件存不存在
//						//session.execCommand("[ -f /Users/gdlocal/Desktop/WiPASXNext136_886.zip ] && echo OK");
//						session.execCommand("[ -a /Users/gdlocal/Desktop/" + filename + ".app  ] && echo OK");
//						result = processStdout(session.getStdout(), DEFAULTCHART);
//						if (result.isEmpty()) {
//							System.out.println("文件不存在，解压文件");
//							session.close();
//							Session session2 = conn.openSession();
//							session2.execCommand("cd /Users/gdlocal/Desktop/ && open " + filename + ".zip ");
//							session2.close();
//						} else {
//							System.out.println(arr[i] + "文件存在，不用解压！！！！！！！！！！！！");
//							sum = sum + 1;
//						}
//						//result = processStdout(session.getStderr(),DEFAULTCHART);
//						//System.out.println(result);
//						//如果为得到标准输出为空，说明脚本执行出错了
//						if (StringUtils.isBlank(result)) {
//							System.out.println("命令出错");
//						} else {
//							System.out.println("命令执行成功");
//						}
//						conn.close();
//						session.close();
//					} else {
//						System.out.println(arr[i] + "登陆失败!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//						sum = sum + 1;
//						conn.close();
//					}
//				} catch (IOException e) {
//					System.out.println("执行命令失败，链接conn:" + conn);
//					i = i + 1;
//					sum = sum + 1;
//					//e.printStackTrace();
//					conn.close();
//					continue;
//				}
//			} catch (IOException e) {
//				System.out.println(arr[i] + "登陆失败");
//				i = i + 1;
//				sum = sum + 1;
//				//e.printStackTrace();
//				continue;
//			}
//			i = i + 1;
//		}
//		JOptionPane.showMessageDialog(this, "解压文件不成功的数量：" + sum + " ", "帮助", 1);
//		return result;
//	}
//
//	public String deleteFile() {
//		z = 0;
//		filename = FileName.getText();
//		String result = "";
//		int i = 0;
//		sum = 0;
//		while (arr[i] != null) {
//			z = z + g;
//			String ip = arr[i];
//			try {
//				//创建链接
//				conn = new Connection(ip, port);
//				conn.connect();
//				//登陆
//				boolean isAuthed = conn.authenticateWithPassword(user, pwd);
//				//获取SCPClient
//				try {
//					if (isAuthed && conn != null) {
//						//打开一个会话
//						Session session = conn.openSession();
//						//判断远程电脑文件存不存在
//						//session.execCommand("[ -f /Users/gdlocal/Desktop/WiPASXNext136_886.zip ] && echo OK");
//						session.execCommand("[ -a /Users/gdlocal/Desktop/" + filename + " ] && echo OK");
//						result = processStdout(session.getStdout(), DEFAULTCHART);
//						if (!result.isEmpty()) {
//							System.out.println("文件存在，删除文件");
//							session.close();
//							Session session2 = conn.openSession();
//							session2.execCommand("rm -rf /Users/gdlocal/Desktop/" + filename + " ");
//							//session2.execCommand("cd /Users/gdlocal/Desktop/ && rm -rf "+filename+" ");
//							session2.close();
//						} else {
//							System.out.println(arr[i] + "要删除的文件不存在");
//							sum = sum + 1;
//						}
//						//如果为得到标准输出为空，说明脚本执行出错了
//						if (StringUtils.isBlank(result)) {
//							System.out.println("命令出错");
//						} else {
//							System.out.println("命令执行成功");
//						}
//						conn.close();
//						session.close();
//					} else {
//						System.out.println(arr[i] + "登陆失败!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//						sum = sum + 1;
//						conn.close();
//					}
//				} catch (IOException e) {
//					System.out.println("执行命令失败，链接conn:" + conn);
//					i = i + 1;
//					sum = sum + 1;
//					//e.printStackTrace();
//					conn.close();
//					continue;
//				}
//			} catch (IOException e) {
//				System.out.println(arr[i] + "登陆失败");
//				i = i + 1;
//				sum = sum + 1;
//				//e.printStackTrace();
//				continue;
//			}
//			i = i + 1;
//		}
//		JOptionPane.showMessageDialog(this, "删除文件不成功的数量：" + sum + " ", "帮助", 1);
//		return result;
//	}
//
//	public String actionExitAPP() {
//		z = 0;
//		filename = FileName.getText();
//		String result = "";
//		int i = 0;
//		sum = 0;
//		while (arr[i] != null) {
//			z = z + g;
//			mySwing.progressbar.setValue(SCP.z);
//			String ip = arr[i];
//			try {
//				//创建链接
//				conn = new Connection(ip, port);
//				conn.connect();
//				//登陆
//				boolean isAuthed = conn.authenticateWithPassword(user, pwd);
//				//获取SCPClient
//				try {
//					if (isAuthed && conn != null) {
//						System.out.println("登陆成功！退出所有应用！");
//						//打开一个会话
//						Session session = conn.openSession();
//						//退出所有应用
//						session.execCommand("killall WiPASXNext ");
//						//session.execCommand("killall "+filename+" ");
//						result = processStdout(session.getStdout(), DEFAULTCHART);
//						//如果为得到标准输出为空，说明脚本执行出错了
//						if (StringUtils.isBlank(result)) {
//							System.out.println("命令出错");
//						} else {
//							System.out.println("命令执行成功");
//						}
//						conn.close();
//						session.close();
//					} else {
//						System.out.println(arr[i] + "登陆失败");
//						sum = sum + 1;
//						conn.close();
//					}
//				} catch (IOException e) {
//					System.out.println("执行命令失败，链接conn:" + conn);
//					i = i + 1;
//					sum = sum + 1;
//					//e.printStackTrace();
//					conn.close();
//					continue;
//				}
//			} catch (IOException e) {
//				System.out.println(arr[i] + "登陆失败");
//				i = i + 1;
//				sum = sum + 1;
//				//e.printStackTrace();
//				continue;
//			}
//			i = i + 1;
//		}
//		JOptionPane.showMessageDialog(this, "退出应用不成功的数量：" + sum + " ", "帮助", 1);
//		return result;
//	}
//
//
//}
//
