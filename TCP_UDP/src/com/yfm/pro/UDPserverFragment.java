package com.yfm.pro;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yfm.adapter.UdpserverAdapter;
import com.yfm.pojo.UdpConnectServer;

public class UDPserverFragment extends BaseActivity {
	@Override
	public void setvalue(String msg) {
		// TODO Auto-generated method stub
		super.setvalue(msg);
		if(sendcon!=null){
			sendcon.setText(msg);
		}
	}
	@Override
	public void setpeizhi() {
		// TODO Auto-generated method stub
		super.setpeizhi();
		showConfig();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ioacceptor.unbind();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.getParent().onBackPressed();

	}

	private TextView con;
	private ImageView mimg;
	private EditText port, sendcon;
	private Button sendbut;
	private CheckBox start;
	private NioDatagramAcceptor ioacceptor;
	private IoHandlerAdapter iohandler;
	private InetSocketAddress address;
	private IoSession iosession = null;
	private LinearLayout configbut;
	private Dialog dialog, setdialog;
	private View view, setview;
	private List<UdpConnectServer> lcs = new ArrayList<UdpConnectServer>();
	private GridView datagird;
	private UdpserverAdapter adapter;
	private UdpConnectServer currcs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i("mt", "oncreat");
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_tcpserver);
		
		ioacceptor = new NioDatagramAcceptor();
		iohandler = new IoHandlerAdapterImpl();
		ioacceptor.setHandler(iohandler);
		
		datagird = (GridView) this.findViewById(R.id.datagrid);
		adapter = new UdpserverAdapter(this, lcs);
		datagird.setAdapter(adapter);
		datagird.setOnItemClickListener(new OnClickListenerImpl());
		datagird.setOnItemLongClickListener(new OnClickListenerImpl());

		mimg = (ImageView) this.findViewById(R.id.mimg);
		con = (TextView) this.findViewById(R.id.con);
		sendcon = (EditText) this.findViewById(R.id.sendcon);
		sendcon.addTextChangedListener(new OnClickListenerImpl());
		sendbut = (Button) this.findViewById(R.id.sendbut);
		configbut = (LinearLayout) this.findViewById(R.id.config);
		sendbut.setOnClickListener(new OnClickListenerImpl());
		configbut.setOnClickListener(new OnClickListenerImpl());
	}

	private void showset() {
		if (setdialog == null) {
			setdialog = new Dialog(this);
			setdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setview = LayoutInflater.from(this).inflate(
					R.layout.tcpserver_itemset, null);
			setdialog.setContentView(setview);
			Utils.setDialog(this, setdialog);
		}
		Button showtype = (Button) setview.findViewById(R.id.showtype);
		TextView ipport = (TextView) setview.findViewById(R.id.ipport);

		Button sendtype = (Button) setview.findViewById(R.id.sendtype);
		Button fktype = (Button) setview.findViewById(R.id.fktype);
		Button duankai = (Button) setview.findViewById(R.id.duankai);
		Button clear = (Button) setview.findViewById(R.id.clear);
		CheckBox huanhang = (CheckBox) setview.findViewById(R.id.huanhang);
		ipport.setText("ip:" + currcs.getIp() + " port:" + currcs.getPort());
		showtype.setText(currcs.getShowtype());
		sendtype.setText(currcs.getSendtype());
		fktype.setText(currcs.getFktype());
		huanhang.setChecked(currcs.isHuanhang());
		showtype.setOnClickListener(new OnClickListenerImpl());
		fktype.setOnClickListener(new OnClickListenerImpl());
		sendtype.setOnClickListener(new OnClickListenerImpl());

		duankai.setOnClickListener(new OnClickListenerImpl());
		clear.setOnClickListener(new OnClickListenerImpl());

		huanhang.setOnCheckedChangeListener(new OnClickListenerImpl());
		setdialog.show();

	}

	// 设置配置
	private void showConfig() {
		if (dialog == null) {
			dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			view = LayoutInflater.from(this).inflate(R.layout.tcpserver_config,
					null);
			port = (EditText) view.findViewById(R.id.port);
			port.setText(getconfig("udpport"));
			start = (CheckBox) view.findViewById(R.id.start);
			start.setOnCheckedChangeListener(new OnClickListenerImpl());
			dialog.setContentView(view);
			Utils.setDialog(this, dialog);
		}
		dialog.show();
	}

	// 消息处理类
	class IoHandlerAdapterImpl extends IoHandlerAdapter {

		@Override
		public void sessionClosed(IoSession session) throws Exception {
			// TODO Auto-generated method stub
			super.sessionClosed(session);
			UdpConnectServer cs = getServer(session);
			lcs.remove(cs);
			Message msg = handler.obtainMessage();
			msg.what = 1;
			handler.sendMessage(msg);
		}

		@Override
		public void messageSent(IoSession session, Object message)
				throws Exception {
			// TODO Auto-generated method stub
			super.messageSent(session, message);
		}

		@Override
		public void sessionCreated(IoSession session) throws Exception {
			// TODO Auto-generated method stub
			Log.i("tcpserver", "检测到新客户端"
					+ session.getRemoteAddress().toString());
			super.sessionCreated(session);
			UdpConnectServer cs = new UdpConnectServer(handler);
			cs.setIosession(session);
			lcs.add(cs);
			currcs = adapter.getserver();
			Message msg = handler.obtainMessage();
			msg.what = 1;
			handler.sendMessage(msg);
		}

		@Override
		public void exceptionCaught(IoSession session, Throwable cause)
				throws Exception {
			// TODO Auto-generated method stub
			Log.i("tcpserver", cause.getMessage());
			super.exceptionCaught(session, cause);
		}

		@Override
		public void messageReceived(IoSession session, Object message)
				throws Exception {
			// TODO Auto-generated method stub
			super.messageReceived(session, message);
			IoBuffer ib = (IoBuffer) message;
			UdpConnectServer cs = getServer(session);
			byte[] p=new byte[ib.limit()];
			ib.get(p);
			Log.i("msg", "recive"+p.length);
			String mg="";
			if (cs.isHuanhang()) {
				cs.getSb().append("\r\n");
			}
			if ("文本".equals(cs.getShowtype())) {
				mg=new String(p,"GBK");
			}
			if ("十六进制".equals(cs.getShowtype())) {
				mg=Utils.getHexString(p);
			}
			if(cs.getSb().length()>600){
				cs.getSb().delete(0, 500);
			}
			cs.getSb().append(mg);
			handler.sendEmptyMessage(3);
			if ("自动".equals(cs.getFktype())) {
				cs.SendMsg();
			}
			if ("透明".equals(cs.getFktype())) {
				cs.SendMsg(mg);
			}
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				Log.i("tcpserver", "更新gridview");
				adapter.notifyDataSetChanged();
			}
			if (msg.what == 3) {
				UdpConnectServer cs = adapter.getserver();
				if (cs != null) {
					String nc = cs.getSb().toString();
					con.setText(nc);
				}

			}
			if (msg.what == 4) {
				Toast.makeText(UDPserverFragment.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
			}
			if(msg.what==5){
				Toast.makeText(
						UDPserverFragment.this,
						"服务开启端口:" + port.getText().toString()
								+ "\r\n服务器IP:" + getLocalIpAddress(),
						Toast.LENGTH_SHORT).show();
				mimg.setImageResource(R.drawable.serveron2);
			}
			if(msg.what==6){
				Toast.makeText(UDPserverFragment.this, "服务启动异常，端口被占用",
						Toast.LENGTH_SHORT).show();
				mimg.setImageResource(R.drawable.serveroff);
			}
			if(msg.what==7){
				Toast.makeText(UDPserverFragment.this, "服务已关闭",
						Toast.LENGTH_SHORT).show();
				mimg.setImageResource(R.drawable.serveroff);
			}
		}

	};

	public UdpConnectServer getServer(IoSession session) {
		for (int i = 0; i < lcs.size(); i++) {
			if (session.equals(lcs.get(i).getIosession())) {
				return lcs.get(i);
			}
		}
		return null;
	}

	class OnClickListenerImpl implements OnClickListener,
			OnItemLongClickListener, OnItemClickListener,
			OnCheckedChangeListener, TextWatcher {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.config) {
				showConfig();
			}
			if (v.getId() == R.id.sendbut) {
				UdpConnectServer cs = adapter.getserver();
				if (cs != null) {
					su.insert(sendcon.getText().toString());
					cs.SendMsg(sendcon.getText().toString());
				}else{
					Toast.makeText(UDPserverFragment.this, "未选中连接", Toast.LENGTH_SHORT).show();
				}

			}
			if (v.getId() == R.id.showtype) {
				if ("文本".equals(currcs.getShowtype())) {
					currcs.setShowtype("十六进制");
				} else {
					currcs.setShowtype("文本");
				}
				Button bv = (Button) v;
				bv.setText(currcs.getShowtype());
			}
			if (v.getId() == R.id.sendtype) {
				if ("文本".equals(currcs.getSendtype())) {
					currcs.setSendtype("十六进制");
				} else {
					currcs.setSendtype("文本");
				}
				Button bv = (Button) v;
				bv.setText(currcs.getSendtype());
			}
			if (v.getId() == R.id.fktype) {
				if ("手动".equals(currcs.getFktype())) {
					currcs.setFktype("自动");
				} else if ("自动".equals(currcs.getFktype())) {
					currcs.setFktype("透明");
				} else {
					currcs.setFktype("手动");
				}
				Button bv = (Button) v;
				bv.setText(currcs.getFktype());
			}
			if (v.getId() == R.id.duankai) {
				currcs.getIosession().close(true);
				lcs.remove(currcs);
				adapter.notifyDataSetChanged();
				setdialog.cancel();
			}
			if (v.getId() == R.id.clear) {
				currcs.getSb().delete(0, currcs.getSb().length());
				con.setText(adapter.getserver().getSb());
			}
		}

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			adapter.setCid(arg2);
			adapter.notifyDataSetChanged();
			currcs = lcs.get(arg2);
			con.setText(currcs.getSb().toString());
			sendcon.setText(currcs.getMsg());
			showset();
			return false;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			adapter.setCid(arg2);
			currcs = lcs.get(arg2);
			adapter.notifyDataSetChanged();
			con.setText(currcs.getSb().toString());
			sendcon.setText(currcs.getMsg());
		}

		@Override
		public void onCheckedChanged(CompoundButton but, boolean arg1) {
			// TODO Auto-generated method stub
			Log.i("change", arg1 + "");
			if (but.getId() == R.id.start) {
				if (arg1) {
					new serviceThread(1).start();
				} else {
					new serviceThread(2).start();
				}
				dialog.cancel();
			}
			if (but.getId() == R.id.huanhang) {
				currcs.setHuanhang(arg1);
			}

		}

		class serviceThread extends Thread {

			private int type;

			public serviceThread(int type) {
				this.type = type;
			}

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				if (type == 1) {
					try {
						setconfig("udpport", port.getText().toString());
						address = new InetSocketAddress(Integer.parseInt(port
								.getText().toString()));
						ioacceptor.bind(address);
						handler.sendEmptyMessage(5);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendEmptyMessage(6);
					}
				}
				if(type==2){
					ioacceptor.unbind();
					handler.sendEmptyMessage(7);
				}
			}

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			UdpConnectServer cs = adapter.getserver();
			if (cs != null) {
				cs.setMsg(sendcon.getText().toString());
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

	}

	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(inetAddress
									.getHostAddress())) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return "127.0.0.1";
	}

}
