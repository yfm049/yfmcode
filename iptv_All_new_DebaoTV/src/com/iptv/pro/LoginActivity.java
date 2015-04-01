package com.iptv.pro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iptv.thread.LoginThread;
import com.iptv.utils.ComUtils;
import com.iptv.utils.LogUtils;
import com.iptv.utils.NetUtils;
import com.iptv.utils.SqliteUtils;
import com.tv.debao.R;

public class LoginActivity extends Activity {

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (connectionReceiver != null) {
			unregisterReceiver(connectionReceiver);
			connectionReceiver=null;
		}
	}

	private EditText name, pass;
	private Button loginbut;
	private TextView mac;
	private OnClickListener listener;
	private SqliteUtils su;
	private ProgressDialog pd;
	private BroadcastReceiver connectionReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		su = new SqliteUtils(this);
		name = (EditText) this.findViewById(R.id.name);
		name.setText(ComUtils.getConfig(this, "name", ""));
		pass = (EditText) this.findViewById(R.id.pass);
		pass.setText(ComUtils.getConfig(this, "pass", ""));
		loginbut = (Button) this.findViewById(R.id.loginbut);
		mac = (TextView) this.findViewById(R.id.mac);
		mac.setText(ComUtils.getLocalMacAddressFromIp(this));
		listener = new OnClickListenerImpl();
		pd = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
		loginbut.setOnClickListener(listener);
		if (isautologin()) {
			if (NetUtils.isNetworkConnected(this)) {
				Login();
			} else {
				initbr();
				IntentFilter intentFilter = new IntentFilter();
				intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
				registerReceiver(connectionReceiver, intentFilter);
			}

		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == LoginThread.loginsuc) {
				NetUtils.clolseDialog(pd);
				LogUtils.write("tvinfo", "loginsuc");
				String lname = name.getText().toString();
				String lpass = pass.getText().toString();
				ComUtils.setConfig(LoginActivity.this, "name", lname);
				ComUtils.setConfig(LoginActivity.this, "pass", lpass);
				ComUtils.showtoast(LoginActivity.this, R.string.login_suc);
				tohome();
			} else if (msg.what == LoginThread.loginfail) {
				NetUtils.clolseDialog(pd);
				ComUtils.setConfig(LoginActivity.this, "pass", "");
				LogUtils.write("tvinfo", "loginfail");
				ComUtils.showtoast(LoginActivity.this, R.string.login_fail);
			} else {
				NetUtils.clolseDialog(pd);
				ComUtils.showtoast(LoginActivity.this, R.string.server_fail);
			}
		}

	};

	class OnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			if (view.getId() == R.id.loginbut) {
				Login();
			}
		}

	}

	public void Login() {
		String lname = name.getText().toString();
		String lpass = pass.getText().toString();
		if ("".equals(lname)) {
			Toast.makeText(LoginActivity.this, R.string.name_empty,
					Toast.LENGTH_LONG).show();
		} else if ("".equals(lpass)) {
			Toast.makeText(LoginActivity.this, R.string.pass_empty,
					Toast.LENGTH_LONG).show();
		} else {
			NetUtils.showDialog(pd, ComUtils.getrestext(LoginActivity.this,
					R.string.login_title), ComUtils.getrestext(
					LoginActivity.this, R.string.login_msg));
			NetUtils.login(lname, lpass, mac.getText().toString(), handler, su);
		}
	}

	private void tohome() {
		Intent intent = new Intent(this, HomeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		this.startActivity(intent);
		this.finish();
	}

	private boolean isautologin() {
		String lname = name.getText().toString();
		String lpass = pass.getText().toString();
		if (!"".equals(lname) && !"".equals(lpass)) {
			return true;
		} else {
			return false;
		}
	}
	
	private void initbr(){
		connectionReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
				NetworkInfo mobNetInfo = connectMgr
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				NetworkInfo wifiNetInfo = connectMgr
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

				if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
					Login();
				}
			}
		};
	} 

}
