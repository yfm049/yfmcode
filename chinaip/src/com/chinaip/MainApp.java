package com.chinaip;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class MainApp {

	private JFrame frame;

	private JTree tree;
	private DefaultMutableTreeNode ipc;
	private DefaultTreeModel dtmodel;
	private HttpUtils hu;
	private PatentData pd;
	private JTextField time;
	public static JTextArea msg;
	public static boolean isrun = false;
	public List<Patent> lp;
	private JButton start;
	private JButton stop;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
					MainApp window = new MainApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		hu = new HttpUtils();
		pd = new PatentData();
		frame = new JFrame();
		frame.setTitle("\u6570\u636E\u91C7\u96C6");
		frame.setResizable(false);
		frame.setBounds(100, 100, 898, 545);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 228, 487);
		frame.getContentPane().add(scrollPane);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("检索条件");
		Node zwjs=new Node();
		zwjs.setName("中外混合专利检索");
		DefaultMutableTreeNode zw=new DefaultMutableTreeNode(zwjs);
		zw.setAllowsChildren(false);
		root.add(zw);
		ipc=new DefaultMutableTreeNode("IPC分类导航");
		root.add(ipc);
		dtmodel = new DefaultTreeModel(root, true);

		tree = new JTree(dtmodel);

		tree.addTreeExpansionListener(new TreeExpansionListener() {
			public void treeCollapsed(TreeExpansionEvent event) {
			}

			public void treeExpanded(TreeExpansionEvent event) {
				DefaultMutableTreeNode dft = (DefaultMutableTreeNode) event
						.getPath().getLastPathComponent();
				Object ndf= dft.getUserObject();
				if(ndf instanceof Node){
					Node nd = (Node)ndf;
					if (!nd.isIsload()) {
						new NodeThread(nd, dft).start();
						nd.setIsload(true);
					}
				}
				
				
				

			}
		});

		scrollPane.setViewportView(tree);

		JPanel checkboxs = new JPanel();
		checkboxs.setBorder(new LineBorder(new Color(0, 0, 0)));
		FlowLayout flowLayout = (FlowLayout) checkboxs.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		checkboxs.setBounds(248, 10, 624, 140);
		frame.getContentPane().add(checkboxs);

		JLabel label = new JLabel("\u65F6\u95F4\u6BB5\uFF1A");
		label.setBounds(248, 160, 54, 15);
		frame.getContentPane().add(label);

		time = new JTextField();
		time.setText("2012 to 2013");
		time.setBounds(293, 157, 198, 21);
		frame.getContentPane().add(time);
		time.setColumns(10);

		start = new JButton("\u5F00\u59CB\u91C7\u96C6");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!isrun) {
					new CaijiThread().start();
				}

			}
		});
		start.setBounds(676, 156, 93, 23);
		frame.getContentPane().add(start);

		stop = new JButton("\u505C\u6B62\u91C7\u96C6");
		stop.setEnabled(false);
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isrun = false;
			}
		});
		stop.setBounds(779, 156, 93, 23);
		frame.getContentPane().add(stop);

		JLabel lblchinaiproot = new JLabel(
				"\u6570\u636E\u5E93\u8BF7\u5B89\u88C5\u5230\u672C\u673A\u4E0A\uFF0C\u6570\u636E\u5E93\u540D\u79F0\u4E3Achinaip,\u8D26\u6237\u4E3Aroot\uFF0C\u5BC6\u7801\u4E3A123456");
		lblchinaiproot.setBounds(248, 197, 624, 15);
		frame.getContentPane().add(lblchinaiproot);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(248, 222, 624, 275);
		frame.getContentPane().add(scrollPane_1);

		msg = new JTextArea();
		scrollPane_1.setViewportView(msg);
		lp = pd.GetData();
		for (Patent p : lp) {
			JCheckBox chckbxNewCheckBox = p.GetCheckbox();
			chckbxNewCheckBox.addItemListener(new ItemListenerImpl(p));
			checkboxs.add(chckbxNewCheckBox);
		}

		Node node = new Node();
		node.setIPCCode("0");
		node.setLevel("0");
		node.setRecursedNodes("undefined");
		new NodeThread(node, ipc).start();
		MainApp.msg.append("正在获取左侧树形菜单,请稍等...\r\n");
		MainApp.msg.setCaretPosition(MainApp.msg.getText().length());
	}

	class NodeThread extends Thread {

		private Node node;
		private DefaultMutableTreeNode dft;

		public NodeThread(Node node, DefaultMutableTreeNode dft) {
			this.node = node;
			this.dft = dft;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			hu.init();
			List<Node> ln = hu.GetChildNode(node);
			for (Node n : ln) {
				if (n.getLevel() == null) {
					dft.add(new DefaultMutableTreeNode(n, false));
				} else {
					dft.add(new DefaultMutableTreeNode(n, true));
				}

			}

			dtmodel.nodeStructureChanged(dft);
		}

	}

	public Node getNode() {
		Object ob = tree.getLastSelectedPathComponent();
		if (ob != null) {
			DefaultMutableTreeNode model = (DefaultMutableTreeNode) ob;
			Node nd = null;
			try {
				nd = (Node) model.getUserObject();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MainApp.msg.append("你不能选择最顶级分类!\r\n");
				MainApp.msg.setCaretPosition(MainApp.msg.getText().length());
			}
			return nd;
		} else {
			MainApp.msg.append("你还没有选择左侧ipc分类!\r\n");
			MainApp.msg.setCaretPosition(MainApp.msg.getText().length());
			return null;
		}
	}

	class CaijiThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			isrun = true;
			stop.setEnabled(true);
			start.setEnabled(false);
			caiji();
			isrun = false;
			stop.setEnabled(false);
			start.setEnabled(true);
		}

	}

	public void caiji() {
		try {
			int idealSize = 1000;
			int maxExcess = 500;
			Node node = getNode();
			if (node != null) {
				String tj = tiaojian(node.getIPCCode());
				if (tj == null) {
					return;
				}
				int tp = 0;
				int ct = 1;
				boolean first = true;
				float nc=0.0f;
				do {
					nc=Runtime.getRuntime().freeMemory()/1048576f;
					if(nc<20){
						MainApp.msg.append("内存不足20M,手动回收\r\n");
						MainApp.msg.setCaretPosition(MainApp.msg.getText().length());
						System.gc();
						continue;
					}
					MainApp.msg.append("正在查询第" + ct + "页数据\r\n");
					MainApp.msg
							.setCaretPosition(MainApp.msg.getText().length());
					String src = null;
					if (first) {
						src = hu.Search(tj);
					} else {
						src = hu.Search(ct);
					}
					if (src != null) {
						first = false;
						String recordnum = src.substring(
								src.indexOf("recordnum=") + 10,
								src.indexOf("&page"));

						int tpage = Integer.parseInt(recordnum);
						tp = tpage / 10;
						int m = tpage % 10;
						if (m > 0) {
							tp += 1;
						}
						MainApp.msg.append("正在分析保存第" + ct + "页数据，总共" + tp
								+ "页，共" + tpage + "条数据\r\n");
						MainApp.msg.setCaretPosition(MainApp.msg.getText()
								.length());
						Thread.sleep(500);
						if (hu.getData(src, ct == tp, m)) {
							ct++;
						}

						int excess = msg.getDocument().getLength() - idealSize;
						if (excess >= maxExcess) {
							msg.replaceRange("", 0, excess);
						}

					}

				} while (ct <= tp && isrun);
				MainApp.msg.append("采集完成\r\n");
				MainApp.msg.setCaretPosition(MainApp.msg.getText().length());
			} else {
				MainApp.msg.append("请先选择类别..\r\n");
				MainApp.msg.setCaretPosition(MainApp.msg.getText().length());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String tiaojian(String ipccode) {
		String sj = time.getText();
		String searchword = "";
		String presearchword = "";

		if (sj != null && !"".equals(sj)) {
			searchword = "pd=(" + sj + ")";
			if(ipccode!=null&&!"".equals(ipccode)){
				searchword+=" and pic=(" + ipccode.toUpperCase()+ ")";
			}
		} else {
			if(ipccode!=null&&!"".equals(ipccode)){
				searchword = "pic=(" + ipccode.toUpperCase() + ")";
			}
			
		}
		if(ipccode!=null&&!"".equals(ipccode)){
			presearchword = "pic=(" + ipccode.toLowerCase() + ")";
		}
		
		String p = GetSelectPatent();
		if (p == null) {
			MainApp.msg.append("请选择专利..\r\n");
			MainApp.msg.setCaretPosition(MainApp.msg.getText().length());
			return null;
		}
		String m = "&searchword=" + Encodeparam(searchword) + "&" + p
				+ "&presearchword=" + Encodeparam(presearchword) + "&txtD="
				+ Encodeparam(sj);
		if(ipccode!=null&&!"".equals(ipccode)){
			m=m+"&txtG=" + ipccode.toUpperCase();
		}
		return m;
	}

	public String GetSelectPatent() {
		StringBuffer sb = new StringBuffer();
		String strdb = "";
		String channel = "";
		for (int i = 0; i < lp.size(); i++) {
			Patent pt = lp.get(i);
			if (pt.isChecked()) {
				channel += pt.getValue() + ",";
				strdb += ("&strdb=" + pt.getValue());
			}
		}
		if (channel == null || "".equals(channel)) {
			return null;
		}
		channel = channel.substring(0, channel.lastIndexOf(","));
		channel = Encodeparam(channel);
		sb.append("channelid=" + channel);
		sb.append(strdb);
		sb.append("&currentChannelID=" + channel);

		return sb.toString();
	}

	public String Encodeparam(String p) {
		String m = "";
		try {
			m = URLEncoder.encode(p, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;
	}

	class ItemListenerImpl implements ItemListener {

		private Patent p;

		public ItemListenerImpl(Patent p) {
			this.p = p;
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			if (e.getStateChange() == e.SELECTED) {
				p.setChecked(true);
				if (p.getName().equals("中国失效专利")) {
					for (int i = 0; i < 4; i++) {
						pd.GetData().get(i).setChecked(false);
					}
				} else {
					if (pd.GetData().indexOf(p) < 4) {
						pd.GetData().get(4).setChecked(false);
					}
				}
			} else {
				p.setChecked(false);
			}
		}

	}
}
