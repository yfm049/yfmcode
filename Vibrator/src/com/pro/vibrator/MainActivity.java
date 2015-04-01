package com.pro.vibrator;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Socket socket = null;
	private InputStream in;
	private PrintWriter out;
	private Button btn;
	private EditText pass,ipedit;
	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		btn = (Button) super.findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListenerImpl());
		pass = (EditText) super.findViewById(R.id.password);
		ipedit = (EditText) super.findViewById(R.id.ipedit);
	}

	public boolean isboolIp(String ipAddress) {
		Pattern pattern=Pattern.compile("^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$");
		Matcher matcher = pattern.matcher(ipAddress);
		return matcher.matches();
	}

	class OnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if ("登录".equals(btn.getText().toString())) {
				boolean ipbool= isboolIp(ipedit.getText().toString());
				Log.i("ip", "isip-"+ipbool);
				 if (socket == null&&ipbool) {
					Log.i("连接", "创建连接");
					new SocketThread().start();
				} else {
					handler.sendEmptyMessage(6);
				}
			} else {
				if (out != null) {
					out.flush();
					out.print("clientexit;");
					out.flush();
				} else {
					Toast.makeText(MainActivity.this, "连接服务端失败",
							Toast.LENGTH_SHORT).show();
				}
			}

		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 0) {
				btn.setText("退出");
				Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT)
						.show();
			}
			if (msg.what == 1) {
				new ReadThread().start();
				Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT)
						.show();
				handler.sendEmptyMessage(6);
			} else if (msg.what == 2) {
				closeSocket();
				Toast.makeText(MainActivity.this, "连接服务端失败", Toast.LENGTH_SHORT)
						.show();
			} else if (msg.what == 3) {
				closeSocket();
				Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT)
						.show();
			} else if (msg.what == 4) {
				btn.setText("登录");
				closeSocket();
				Toast.makeText(MainActivity.this, "程序退出", Toast.LENGTH_SHORT)
						.show();
			} else if (msg.what == 5) {
				int n = msg.arg1;
				long[] ln = new long[n * 2];
				for (int i = 0; i < n; i++) {
					ln[i * 2] = 500;
					ln[i * 2 + 1] = 800;
				}
				vibrator.vibrate(ln, -1);
			} else if (msg.what == 6) {
				String passtext = pass.getText().toString();
				Log.i("发送密码", "密码" + passtext);
				if (out != null) {
					out.print("password," + passtext + ";");
					out.flush();
				} else {
					closeSocket();
					Toast.makeText(MainActivity.this, "连接服务端失败,ip是否正确",
							Toast.LENGTH_SHORT).show();
				}
			}
		}

	};

	class SocketThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			try {
				socket = new Socket(ipedit.getText().toString(), 888);
				in = socket.getInputStream();
				out = new PrintWriter(socket.getOutputStream(), true);
				handler.sendEmptyMessage(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				closeSocket();
				handler.sendEmptyMessage(2);
			}

		}

	}

	private void closeSocket() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			socket = null;
			in = null;
			out = null;
		}
	}

	class ReadThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			String p = null;
			Log.i("type", "读取线程启动");
			while (socket != null) {
				try {
					int count = 0;
					while (count == 0 && socket != null) {
						count = in.available();
						Thread.sleep(100);
					}
					byte[] b = new byte[count];
					in.read(b);
					if (b.length > 0) {
						p = new String(b);
						Log.i("tag", p + "");
						if ("password,ok;".equals(p)) {
							handler.sendEmptyMessage(0);
						} else if ("password,error;".equals(p)) {
							handler.sendEmptyMessage(3);
						} else if ("clientexitok;".equals(p)) {
							handler.sendEmptyMessage(4);
						} else if (p.startsWith("result")) {
							String n = p.substring(p.indexOf(",") + 1,
									p.indexOf(";"));
							Log.i("震动", n);
							out.print("resultok," + n + ";");
							out.flush();
							Message msg = handler.obtainMessage();
							msg.what = 5;
							msg.arg1 = Integer.valueOf(n);
							handler.sendMessage(msg);
						}
					} else {
						handler.sendEmptyMessage(2);
						break;
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					closeSocket();
				}
			}
			Log.i("type", "读取线程结束");
		}

	}

}
