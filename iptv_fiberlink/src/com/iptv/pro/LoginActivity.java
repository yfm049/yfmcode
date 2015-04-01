package com.iptv.pro;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
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
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fiberlink.R;
import com.iptv.thread.DownLoadThread;
import com.iptv.thread.DownloadThreadListener;
import com.iptv.thread.LoginThread;
import com.iptv.utils.HttpUtils;
import com.iptv.utils.SqliteUtils;
import com.iptv.utils.Utils;

public class LoginActivity extends Activity {

	private ProgressDialog pd;
	private SharedPreferences sp;
	private LoginThread loginthread;
	private EditText name;
	private EditText pass;
	private Button loginbut,xiufu;
	private AlertDialog alert;
	private TextView mac;
	private HttpUtils hu;
	private DownLoadThread dt;
	private File file;
	private String start;
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
		xiufu = (Button) super.findViewById(R.id.xiufu);
		
		name.setText(active);
		pass.setText(password);
		loginbut.setOnClickListener(new OnClickListenerImpl());
		xiufu.setOnClickListener(new OnClickListenerImpl());
		
		mac=(TextView)super.findViewById(R.id.mac);
		mac.setText(Utils.getLocalMacAddressFromIp(this));
		Intent intent=this.getIntent();
		if(intent!=null){
			start=intent.getStringExtra("start");
		}
		autologin();
	}

	class OnClickListenerImpl implements OnClickListener,OnCancelListener {

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
			if(but.getId()==R.id.xiufu){
				pd = new ProgressDialog(LoginActivity.this);
				pd.setMessage("正在下载中，请稍后...");
				pd.show();
				pd.setOnCancelListener(new OnClickListenerImpl());
				file=createFile();
				dt=new DownLoadThread("http://99.1.1.1:999/999.apk", file,new DownloadThreadListenerImpl());
				dt.start();
			}
			

		}

		@Override
		public void onCancel(DialogInterface arg0) {
			// TODO Auto-generated method stub
			dt.stopdownload();
		}

	}
	class DownloadThreadListenerImpl implements DownloadThreadListener {

		int bfb=0;
		@Override
		public void afterPerDown(String uri, long totalSize, long updateTotalSize) {
			// TODO Auto-generated method stub
				if((updateTotalSize * 100 / totalSize) - 1 > bfb){
					bfb=bfb+1;
					Message msg=handler.obtainMessage();
					msg.what=100;
					msg.arg1=bfb;
					handler.sendMessage(msg);
					
			}
		}

		@Override
		public void downCompleted(String uri, long count, long rcount,
				boolean isdown, File file) {
			// TODO Auto-generated method stub
			Message msg = handler.obtainMessage();
			msg.what = 4;
			handler.sendMessage(msg);
		}

		@Override
		public void returncode(int statecode) {
			// TODO Auto-generated method stub
			Message msg = handler.obtainMessage();
			msg.what = 3;
			msg.arg1 = statecode;
			handler.sendMessage(msg);
		}

	}
	public File createFile() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/iptv.apk");
			if (file.exists()) {
				Log.i("tvinfo", "文件存在");
				file.delete();
			}
			return file;
		}
		return null;
	}
	private void autologin(){
		String active = name.getText().toString();
		String password = pass.getText().toString();
		if (!"".equals(active) && !"".equals(password)) {
			String url = "live.jsp?active="
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

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Log.i("tvinfo", "123123   "+msg.what);
			String active = name.getText().toString();
			String password = pass.getText().toString();
			
			if (msg.what == 1) {
				//登录成功
				pd.dismiss();
				savedate(active,password);
				SqliteUtils su=new SqliteUtils(LoginActivity.this);
				if(su.getAllTvinfo(-1).size()>0){
					toLivePlay();
				}else{
					tohome();
				}
			} else if (msg.what == -1) {
				pd.dismiss();
				if(msg.arg1==1){
					showalert("消息", "链接服务器失败");
				}else if(msg.arg1==2){
					pass.setText("");
					savedate(active,"");
					showalert("消息", "认证失败,用户名或密码错误");
				}
			}
			if (msg.what == 3) {
				pd.dismiss();
				Toast.makeText(LoginActivity.this, "下载失败 状态码" + msg.arg1,
						Toast.LENGTH_LONG).show();
			}
			if(msg.what == 4){
				pd.dismiss();
				showinstall(file);
			}
			if(msg.what==100){
				if(pd.isShowing()){
					pd.setMessage("正在下载中，请稍后... "+msg.arg1+"%");
				}
			}
		}

	};
	public void showinstall(final File file) {
		Builder builder = new Builder(LoginActivity.this);
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
	public void toLivePlay(){
		Intent intent = new Intent(LoginActivity.this,PlayActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		this.finish();
	}

}
