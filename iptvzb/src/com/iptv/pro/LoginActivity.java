package com.iptv.pro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iptv.dlerbh.R;
import com.iptv.utils.LoginThread;
import com.iptv.utils.User;
import com.iptv.utils.Utils;

public class LoginActivity extends Activity {

	private ProgressDialog pd;
	private SharedPreferences sp;
	private LoginThread loginthread;
	private EditText name;
	private EditText pass;
	private Button loginbut, free;
	private AlertDialog alert;
	private IptvApp iptv;
	private TextView mac;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = this.getSharedPreferences("key", Context.MODE_PRIVATE);
		String active = sp.getString("name", "");
		String password = sp.getString("password", "");
		setContentView(R.layout.activity_login);
		iptv = (IptvApp) this.getApplication();
		name = (EditText) super.findViewById(R.id.name);
		pass = (EditText) super.findViewById(R.id.pass);
		loginbut = (Button) super.findViewById(R.id.loginbut);
		free = (Button) super.findViewById(R.id.free);
		name.setText(active);
		pass.setText(password);
		loginbut.setOnClickListener(new OnClickListenerImpl());
		free.setOnClickListener(new OnClickListenerImpl());
		mac=(TextView)super.findViewById(R.id.mac);
		mac.setText(Utils.getLocalMacAddressFromIp(this));
	}

	class OnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			if (but.getId() == R.id.loginbut) {
				String active = name.getText().toString();
				String password = pass.getText().toString();
				if (!"".equals(active) && !"".equals(password)) {
					String url = "getGroup.jsp?active="
							+ active
							+ "&pass="
							+ Utils.md5(password,null)
							+ "&mac="
							+ Utils.getLocalMacAddressFromIp(LoginActivity.this);
					loginthread = new LoginThread(handler, url);
					loginthread.start();
				} else {
					showalert("消息", "用户名或密码不能为空");
				}
			}
			if (but.getId() == R.id.free) {
				String url="free.jsp";
				loginthread = new LoginThread(handler, url);
				loginthread.start();
			}

		}

	}

	private void showalert(String title, String msg) {
		Builder builder = new Builder(this);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton("确定", null);
		alert = builder.create();
		alert.show();
	}

	private void show() {
		pd = new ProgressDialog(LoginActivity.this,
				ProgressDialog.THEME_HOLO_DARK);
		pd.setTitle("正在进行认证");
		pd.setMessage("正在进行认证,请稍后...");
		pd.show();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				show();
			} else if (msg.what == 2) {
				pd.cancel();
				User user = (User) msg.obj;
				iptv.setUser(user);
				if ("1".equals(user.getRet())) {
					String active = name.getText().toString();
					String password = pass.getText().toString();
					Editor editor = sp.edit();
					editor.putString("name", active);
					editor.putString("password", password);
					editor.commit();
					Toast.makeText(LoginActivity.this, "认证通过",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(LoginActivity.this,
							FenleiActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					LoginActivity.this.startActivity(intent);
					LoginActivity.this.finish();
				} else if ("0".equals(user.getRet())) {
					Editor editor = sp.edit();
					editor.remove("password");
					editor.commit();
					Toast.makeText(LoginActivity.this, "认证失败",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(LoginActivity.this, "网络错误",
							Toast.LENGTH_SHORT).show();
				}
			}
		}

	};

}
