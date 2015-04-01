package com.yfm.pro;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import android.app.Dialog;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yfm.adapter.TcpclientAdapter;
import com.yfm.pojo.TcpConnectClient;

public class TCPclientFragment extends BaseActivity {

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(lcon.size()>0){
			for(TcpConnectClient tc:lcon){
				tc.closecon();
			}
		}
	}

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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.getParent().onBackPressed();
	}

	private TextView con;
	private EditText ip, port, sendcon, dtime;
	private Button sendbut;
	private OnClickListener onclick;
	private Dialog dialog, setdialog;
	private View view, setview;
	private LinearLayout config;
	private List<TcpConnectClient> lcon = new ArrayList<TcpConnectClient>();
	private TcpclientAdapter adapter;
	private GridView datagird;
	private TcpConnectClient currclient;
	private IoHandlerAdapter iohandler;
	private SharedPreferences sp;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i("mt", "oncreat");
		super.onCreate(savedInstanceState);
		sp=this.getSharedPreferences("config", MODE_PRIVATE);
		this.setContentView(R.layout.activity_tcpclient);
		iohandler = new IoHandlerAdapterImpl();

		datagird = (GridView) this.findViewById(R.id.datagrid);
		adapter = new TcpclientAdapter(this, lcon);
		datagird.setAdapter(adapter);
		datagird.setOnItemClickListener(new OnClickListenerImpl());
		datagird.setOnItemLongClickListener(new OnClickListenerImpl());

		onclick = new OnClickListenerImpl();
		port = (EditText) this.findViewById(R.id.port);
		config = (LinearLayout) this.findViewById(R.id.config);
		con = (TextView) this.findViewById(R.id.con);
		sendcon = (EditText) this.findViewById(R.id.sendcon);
		sendbut = (Button) this.findViewById(R.id.sendbut);
		config.setOnClickListener(onclick);
		sendbut.setOnClickListener(onclick);
	}

	private void showset() {
		if (setdialog == null) {
			setdialog = new Dialog(this);
			setdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setview = LayoutInflater.from(this).inflate(
					R.layout.tcpclient_itemset, null);
			setdialog.setContentView(setview);
			Utils.setDialog(this, setdialog);
		}
		Button showtype = (Button) setview.findViewById(R.id.showtype);
		TextView ipport = (TextView) setview.findViewById(R.id.ipport);

		Button sendtype = (Button) setview.findViewById(R.id.sendtype);
		CheckBox dingshi = (CheckBox) setview.findViewById(R.id.dingshi);
		dtime = (EditText) setview.findViewById(R.id.dtime);
		dtime.setText(currclient.getDtime() + "");
		Button duankai = (Button) setview.findViewById(R.id.duankai);
		Button delete = (Button) setview.findViewById(R.id.delete);
		Button clear = (Button) setview.findViewById(R.id.clear);
		CheckBox huanhang = (CheckBox) setview.findViewById(R.id.huanhang);
		ipport.setText("ip:" + currclient.getIp() + " port:"
				+ currclient.getPort());
		showtype.setText(currclient.getShowtype());
		sendtype.setText(currclient.getSendtype());
		huanhang.setChecked(currclient.isHuanhang());
		showtype.setOnClickListener(new OnClickListenerImpl());
		sendtype.setOnClickListener(new OnClickListenerImpl());

		duankai.setOnClickListener(new OnClickListenerImpl());
		if (currclient.isIscon()) {
			duankai.setText("断开");
		} else {
			duankai.setText("连接");
		}
		delete.setOnClickListener(new OnClickListenerImpl());
		clear.setOnClickListener(new OnClickListenerImpl());

		huanhang.setOnCheckedChangeListener(new OnClickListenerImpl());
		dingshi.setChecked(currclient.isDingshi());
		dingshi.setOnCheckedChangeListener(new OnClickListenerImpl());
		setdialog.show();

	}

	private void showConfig() {
		if (dialog == null) {
			dialog = new Dialog(this);
			view = LayoutInflater.from(this).inflate(R.layout.tcpclient_config,
					null);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			ip = (EditText) view.findViewById(R.id.ip);
			ip.setText(getconfig("tcpip"));
			port = (EditText) view.findViewById(R.id.port);
			port.setText(getconfig("tcpport"));
			Button add = (Button) view.findViewById(R.id.add);
			Button close = (Button) view.findViewById(R.id.close);
			add.setOnClickListener(onclick);
			close.setOnClickListener(onclick);
			dialog.setContentView(view);
			Utils.setDialog(this, dialog);
		}
		dialog.show();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				adapter.notifyDataSetChanged();
				Toast.makeText(TCPclientFragment.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
			}
			if (msg.what == 2) {
				con.setText(adapter.getclient().getSb());
			}
			if (msg.what == 3) {
				adapter.notifyDataSetChanged();
			}
			if (msg.what == 4) {
				//改时间 就修改toast.length_short这个值
				Toast.makeText(TCPclientFragment.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				
			}
		}

	};

	class OnClickListenerImpl implements OnClickListener, OnItemClickListener,
			OnItemLongClickListener, OnCheckedChangeListener, TextWatcher {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.config) {
				showConfig();
			}
			if (v.getId() == R.id.sendbut) {
				TcpConnectClient client = adapter.getclient();
				if (client != null) {
					String msg = sendcon.getText().toString();
					su.insert(msg);
					client.setMsg(msg);
					client.SendMsg();
				}else{
					Toast.makeText(TCPclientFragment.this, "未选中连接", Toast.LENGTH_SHORT).show();
				}
			}
			if (v.getId() == R.id.add) {
				TcpConnectClient cc = new TcpConnectClient(handler, iohandler);
				cc.setIp(ip.getText().toString());
				cc.setPort(Integer.parseInt(port.getText().toString()));
				setconfig("tcpip", ip.getText().toString());
				setconfig("tcpport", port.getText().toString());
				lcon.add(cc);
				adapter.notifyDataSetChanged();
				cc.startcon();
				dialog.cancel();
			}
			if (v.getId() == R.id.close) {
				dialog.cancel();
			}
			if (v.getId() == R.id.showtype) {
				if ("文本".equals(currclient.getShowtype())) {
					currclient.setShowtype("十六进制");
				} else {
					currclient.setShowtype("文本");
				}
				Button bv = (Button) v;
				bv.setText(currclient.getShowtype());
			}
			if (v.getId() == R.id.sendtype) {
				if ("文本".equals(currclient.getSendtype())) {
					currclient.setSendtype("十六进制");
				} else {
					currclient.setSendtype("文本");
				}
				Button bv = (Button) v;
				bv.setText(currclient.getSendtype());
			}
			if (v.getId() == R.id.duankai) {
				Button bv = (Button) v;
				if (currclient.isIscon()) {
					currclient.closecon();
					bv.setText("连接");
				} else {
					currclient.startcon();
					bv.setText("断开");
				}
				adapter.notifyDataSetChanged();

			}
			if (v.getId() == R.id.delete) {
				if (currclient.isIscon()) {
					currclient.closecon();
				}
				lcon.remove(currclient);
				adapter.notifyDataSetChanged();
				setdialog.cancel();
			}
			if (v.getId() == R.id.clear) {
				currclient.getSb().delete(0, currclient.getSb().length());
				con.setText(adapter.getclient().getSb());
			}
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			adapter.setCid(arg2);
			adapter.notifyDataSetChanged();
			currclient = lcon.get(arg2);
			con.setText(currclient.getSb().toString());
			sendcon.setText(currclient.getMsg());
		}

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			adapter.setCid(arg2);
			adapter.notifyDataSetChanged();
			currclient = lcon.get(arg2);
			con.setText(currclient.getSb().toString());
			sendcon.setText(currclient.getMsg());
			showset();
			return false;
		}

		@Override
		public void onCheckedChanged(CompoundButton but, boolean arg1) {
			// TODO Auto-generated method stub
			if (but.getId() == R.id.dingshi) {
				if (arg1) {
					Long t = Long.parseLong(dtime.getText().toString());
					if (t < 200) {
						Toast.makeText(TCPclientFragment.this,
								"输入错误，最小发送时间为200ms", Toast.LENGTH_SHORT).show();
						but.setChecked(false);
					} else {
						String msg = sendcon.getText().toString();
						
						currclient.setMsg(msg);
						currclient.setDtime(t);
						currclient.setDingshi(true);
					}
				}else{
					currclient.setDingshi(false);
				}
				
			}
			if (but.getId() == R.id.huanhang) {
				currclient.setHuanhang(arg1);
			}
		}

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			TcpConnectClient cs = adapter.getclient();
			if (cs != null) {
				cs.setMsg(sendcon.getText().toString());
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

	}

	class IoHandlerAdapterImpl extends IoHandlerAdapter {

		@Override
		public void messageSent(IoSession session, Object message)
				throws Exception {
			// TODO Auto-generated method stub
			super.messageSent(session, message);
			Log.i("send", "发送" + message);
		}

		@Override
		public void sessionClosed(IoSession session) throws Exception {
			// TODO Auto-generated method stub
			super.sessionClosed(session);
			Log.i("Closed", "关闭");
			TcpConnectClient cs = getClient(session);
			cs.setIscon(false);
			cs.setIosession(null);
			handler.sendEmptyMessage(3);
		}

		@Override
		public void sessionCreated(IoSession session) throws Exception {
			// TODO Auto-generated method stub
			super.sessionCreated(session);

		}

		@Override
		public void exceptionCaught(IoSession session, Throwable cause)
				throws Exception {
			// TODO Auto-generated method stub
			super.exceptionCaught(session, cause);
			Log.i("Closed", "exceptionCaught");
			session.close(true);
		}

		@Override
		public void messageReceived(IoSession session, Object message)
				throws Exception {
			// TODO Auto-generated method stub
			super.messageReceived(session, message);
			try {
				IoBuffer ib = (IoBuffer) message;
				TcpConnectClient cs = getClient(session);
				if(cs.getSb().length()>600){
					cs.getSb().delete(0, 500);
				}
				byte[] p = new byte[ib.limit()];
				ib.get(p);
				Log.i("msg", "recive" + p.length);
				if (cs.isHuanhang()) {
					cs.getSb().append("\r\n");
				}
				if ("文本".equals(cs.getShowtype())) {
					cs.getSb().append(new String(p, "GBK"));
				}
				if ("十六进制".equals(cs.getShowtype())) {
					cs.getSb().append(Utils.getHexString(p));
				}
				handler.sendEmptyMessage(2);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public TcpConnectClient getClient(IoSession session) {
		for (int i = 0; i < lcon.size(); i++) {
			if (session.equals(lcon.get(i).getIosession())) {
				return lcon.get(i);
			}
		}
		return null;
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
