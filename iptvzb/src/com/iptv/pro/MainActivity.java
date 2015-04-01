package com.iptv.pro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.iptv.dlerbh.R;
import com.iptv.utils.LoginThread;
import com.iptv.utils.User;
import com.iptv.utils.Utils;

public class MainActivity extends Activity {

	private ProgressDialog pd;
	private SharedPreferences sp;
	private LoginThread loginthread;
	private IptvApp iptv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent service=new Intent(this,UpdateService.class);
		this.startService(service);
		sp=this.getSharedPreferences("key", Context.MODE_PRIVATE);
		String active=sp.getString("name", "");
		String pass=sp.getString("password", "");
		if("".equals(active)||"".equals(pass)){
			tologin();
			return;
		}
		setContentView(R.layout.activity_main);
		iptv=(IptvApp)this.getApplication();
		String url="getGroup.jsp?active="+active+"&pass="+Utils.md5(pass, null)+"&mac="+Utils.getLocalMacAddressFromIp(this);
		loginthread=new LoginThread(handler, url);
		loginthread.start();
	}
	private void tologin(){
		Intent intent=new Intent(this,LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		this.finish();
	}
	private void show(){
		pd=new ProgressDialog(MainActivity.this, ProgressDialog.THEME_HOLO_DARK);
		pd.setTitle("正在进行认证");
		pd.setMessage("正在进行认证,请稍后...");
		pd.show();
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				show();
			}else if(msg.what==2){
				pd.cancel();
				User user=(User)msg.obj;
				iptv.setUser(user);
				if("1".equals(user.getRet())){
					Toast.makeText(MainActivity.this, "认证通过", Toast.LENGTH_SHORT).show();
					Intent intent=new Intent(MainActivity.this,FenleiActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					MainActivity.this.startActivity(intent);
					MainActivity.this.finish();
				}else if("0".equals(user.getRet())){
					Editor editor=sp.edit();
					editor.remove("password");
					editor.commit();
					Toast.makeText(MainActivity.this, "认证失败", Toast.LENGTH_SHORT).show();
					tologin();
				}else {
					Toast.makeText(MainActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
				}
			}
		}
		
	};
	
	



}
