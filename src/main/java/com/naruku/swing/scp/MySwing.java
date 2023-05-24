//package com.naruku.swing.scp;
//
//
//import javax.swing.JDialog;
//import javax.swing.JPanel;
//import java.awt.Color;
//import java.awt.BorderLayout;
//import java.awt.Dimension;
//import java.awt.Rectangle;
//import javax.swing.JProgressBar;  //进度条
//
//public class MySwing extends JDialog {
//	/**
//	 * 弹窗
//	 */
//	private static final long serialVersionUID = 1L;
//	public static JProgressBar progressbar;  //创建进度条
//
//	MySwing() {
//		this.setTitle("正在下载...");
//		this.setSize(200,100);//设置窗口大小
//
//		progressbar = new JProgressBar();  						//创建进度条
//		progressbar.setStringPainted(true);   					//显示当前进度条信息
//		progressbar.setBorderPainted(false); 					//设置进度条边框不显示
//		progressbar.setForeground(new Color(0, 210, 40)); 		//设置进度条的前景色
//		progressbar.setBackground(new Color(188, 190, 194));  	//设置进度条的背景颜色
//
//		JPanel My_panel = new JPanel(new BorderLayout());//面板
//		My_panel.add(progressbar, BorderLayout.SOUTH);
//
//		this.setContentPane(My_panel);//窗口添加面板
//		//this.setResizable(false);
//		this.setVisible(true);//设置窗口为可见
//		new Thread(new myRepaint()).start();  //进度条线程开始
//	}
//	private class myRepaint implements Runnable{
//		//另一种方法，创建一个线程运行进度条
//		public void run() {
//			while(true)
//			{
//				Dimension d = progressbar.getSize();
//				//设置进度条的值
//				progressbar.setValue(SCP.z);
//				Rectangle rect = new Rectangle(0, 0, d.width, d.height);
//				progressbar.setValue(SCP.z);
//				progressbar.paintImmediately(rect);
//				//repaint();
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//
//			}
//		}
//	}
//
//}
//
