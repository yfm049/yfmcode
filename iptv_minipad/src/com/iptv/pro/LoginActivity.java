package com.iptv.pro;

import java.io.File;

import javax.security.auth.PrivateCredentialPermission;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iptv.pro.UpdateService.DownloadThreadListenerImpl;
import com.iptv.season.R;
import com.iptv.thread.DownLoadThread;
import com.iptv.thread.DownloadThreadListener;
import com.iptv.thread.LoginThread;
import com.iptv.utils.HttpUtils;
import com.iptv.utils.Utils;

public class LoginActivity extends Activity {

	private ProgressDialog pd;
	private SharedPreferences sp;
	private LoginThread loginthread;
	private EditText name;
	private EditText pass;
	private Button loginbut;
	private AlertDialog alert;
	private TextView mac;
	private HttpUtils hu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = this.getSharedPreferences("key", Context.MODE_PRIVATE);
		String active = sp.getString("name", "");
		String password = sp.getString("password", "");
		setContentView(R.layout.activity_login);
		Intent service=new Intent(this,UpdateService.class);
		this.startService(service);
		hu=new HttpUtils(this);
		name = (EditText) super.findViewById(R.id.name);
		pass = (EditText) super.findViewById(R.id.pass);
		loginbut = (Button) super.findViewById(R.id.loginbut);
		name.setText(active);
		pass.setText(password);
		loginbut.setOnClickListener(new OnClickListenerImpl());
		mac=(TextView)super.findViewById(R.id.mac);
		mac.setText(Utils.getLocalMacAddressFromIp(this));
		autologin();
	}

	class OnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			if (but.getId() == R.id.loginbut) {
				String active = name.getText().toString();
				String password = pass.getText().toString();
				if("".equals(active)||"".equals(password)){
					showalert("消息","用户名或密码不能为空");
				}else{
					autologin();
				}
			}
			

		}

	}
	private void autologin(){
		String active = name.getText().toString();
		String password = pass.getText().toString();
		if (!"".equals(active) && !"".equals(password)) {
			String url = "live2.jsp?active="
					+ active
					+ "&pass="
					+ Utils.md5(password,null)
					+ "&mac="
					+ Utils.getLocalMacAddressFromIp(LoginActivity.this);
			show();
			loginthread = new LoginThread(this,handler, url);
			loginthread.start();
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
		pd.setCancelable(false);
		pd.show();
	}
	private void showhuifudialog() {
		pd = new ProgressDialog(LoginActivity.this,
				ProgressDialog.THEME_HOLO_DARK);
		pd.setTitle("正在进行下载");
		pd.setMessage("正在进行下载,请稍后...");
		pd.setCancelable(false);
		pd.show();
	}
	public void zhuxiao(View v){
		savedate("", "");
		name.setText("");
		pass.setText("");
	}
	public File createFile() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/huifuiptv.apk");
			if (file.exists()) {
				Log.i("tvinfo", "文件存在");
				file.delete();
			}
			return file;
		}
		return null;
	}
	public void huifu(View v){
		File file = createFile();
		if (file != null) {
			showhuifudialog();
			DownLoadThread dt = new DownLoadThread("http://115.28.139.85:88/86/minipad.apk", file,
					new DownloadThreadListenerImpl());
			dt.start();
			Toast.makeText(this, "开始下载", Toast.LENGTH_SHORT).show();
		}
	}
	class DownloadThreadListenerImpl implements DownloadThreadListener {

		@Override
		public void afterPerDown(String uri, long count, long rcount) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void downCompleted(String uri, long count, long rcount,
				boolean isdown, File file) {
			// TODO Auto-generated method stub
			if (file != null&&file.length()>0) {
				Message msg = handler.obtainMessage();
				msg.what = 2;
				msg.obj = file;
				handler.sendMessage(msg);

			}else{
				handler.sendEmptyMessage(3);
			}
		}

		@Override
		public void returncode(int statecode) {
			// TODO Auto-generated method stub
		}

	}
	public void showinstall(final File file) {
		Builder builder = new Builder(this);
		builder.setTitle("安装");
		builder.setMessage("下载完成,是否安装...");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(file),
						"application/vnd.android.package-archive");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

			}
		});
		builder.setNegativeButton("取消", null);
		AlertDialog dialog = builder.create();
		dialog.getWindow()
				.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			String active = name.getText().toString();
			String password = pass.getText().toString();
			pd.dismiss();
			if (msg.what == 1) {
				//登录成功
				savedate(active,password);
				tohome();
			} else if (msg.what == -1) {
				if(msg.arg1==1){
					showalert("消息", "链接服务器失败");
				}else if(msg.arg1==2){
					showalert("消息", "认证失败,用户名或密码错误");
				}
			}else if(msg.what==2){
				showinstall((File) msg.obj);
			}
		}

	};
	public void savedate(String active,String password){
		Editor editor = sp.edit();
		editor.putString("name", active);
		editor.putString("password", password);
		editor.commit();
	}
	public void tohome(){
		Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		this.finish();
	}

}
