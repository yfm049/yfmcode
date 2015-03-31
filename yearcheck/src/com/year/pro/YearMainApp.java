package com.year.pro;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

public class YearMainApp {

	private JFrame frame;
	private JTextField filepath;
	private JTextField path;
	private JFileChooser chooser;
	private ExeclUtils exutil;
	private HttpUtils hutil;
	public static JTextArea msg;
	public static boolean isrun=false;
	private JButton startbtn;
	private JButton stopbtn;
	public static JTextArea urltext;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					YearMainApp window = new YearMainApp();
					window.frame.setVisible(true);
					PasswordDialog pd=new PasswordDialog();
					pd.setModal(true);
					pd.setLocationRelativeTo(window.frame);
					pd.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public YearMainApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		exutil=new ExeclUtils();
		hutil=new HttpUtils();
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 696, 364);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblexcel = new JLabel("\u9009\u62E9EXCEL\u6587\u4EF6\uFF1A");
		lblexcel.setBounds(10, 20, 90, 15);
		frame.getContentPane().add(lblexcel);
		
		filepath = new JTextField();
		filepath.setEditable(false);
		filepath.setBounds(101, 17, 245, 21);
		frame.getContentPane().add(filepath);
		filepath.setColumns(10);
		
		JButton wjbtn = new JButton("\u9009\u62E9\u6587\u4EF6");
		wjbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooser=new JFileChooser();
				chooser.setFileFilter(new FileFilterImpl());
				chooser.showOpenDialog(frame);
				File file=chooser.getSelectedFile();
				filepath.setText(file.getAbsolutePath());
			}
		});
		wjbtn.setBounds(356, 16, 93, 23);
		frame.getContentPane().add(wjbtn);
		
		JLabel label = new JLabel("\u6570\u636E\u5B58\u653E\u76EE\u5F55\uFF1A");
		label.setBounds(10, 50, 90, 15);
		frame.getContentPane().add(label);
		
		path = new JTextField();
		path.setEditable(false);
		path.setBounds(101, 47, 245, 21);
		frame.getContentPane().add(path);
		path.setColumns(10);
		
		JButton mubtn = new JButton("\u9009\u62E9\u76EE\u5F55");
		mubtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooser=new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.showOpenDialog(frame);
				File file=chooser.getSelectedFile();
				path.setText(file.getAbsolutePath());
			}
		});
		mubtn.setBounds(356, 49, 93, 23);
		frame.getContentPane().add(mubtn);
		
		startbtn = new JButton("\u5F00\u59CB\u91C7\u96C6");
		startbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!isrun){
					
					new ThreadImpl().start();
				}
				
				
			}
		});
		startbtn.setBounds(356, 82, 93, 23);
		frame.getContentPane().add(startbtn);
		
		stopbtn = new JButton("\u505C\u6B62\u91C7\u96C6");
		stopbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				msg.append("正在停止...\r\n");
				isrun=false;
			}
		});
		stopbtn.setBounds(253, 82, 93, 23);
		frame.getContentPane().add(stopbtn);
		
		JLabel label_1 = new JLabel("\u91C7\u96C6\u65E5\u5FD7\u663E\u793A\uFF1A");
		label_1.setBounds(10, 86, 93, 15);
		frame.getContentPane().add(label_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 115, 444, 211);
		frame.getContentPane().add(scrollPane);
		
		msg = new JTextArea();
		scrollPane.setViewportView(msg);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(459, 10, 221, 316);
		frame.getContentPane().add(scrollPane_1);
		
		urltext = new JTextArea();
		scrollPane_1.setViewportView(urltext);
	}
	class FileFilterImpl extends FileFilter{

		@Override
		public boolean accept(File f) {
			// TODO Auto-generated method stub
			if(f.getName().endsWith(".xls")||f.isDirectory()){
				return true;
			}
			return false;
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	class ThreadImpl extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			String fp=filepath.getText();
			if(fp==null||"".equals(fp)){
				msg.append("请先选择excel文件\r\n");
				return;
			}
			String p=path.getText();
			if(p==null||"".equals(p)){
				msg.append("请先选择保存目录\r\n");
				return;
			}
			isrun=true;
			stopbtn.setEnabled(true);
			startbtn.setEnabled(false);
			hutil.initUrl();
			msg.setText("开始采集\r\n");
			msg.append("开始解析excel文件,请稍后...\r\n");
			Map<String, List<Info>> msl=exutil.ReadFile(new File(fp));
			msg.append("解析Excel完成\r\n");
			if(msl.size()>0){
				hutil.Caiji(msl, p);
			}else{
				msg.append("无数据\r\n");
			}
			startbtn.setEnabled(true);
			stopbtn.setEnabled(false);
			isrun=false;
			msg.append("采集停止\r\n");
		}
		
	}
}
