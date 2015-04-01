package com.iptv.pro;

import io.vov.vitamio.LibsChecker;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.newvision.R;

public class LoginActivity extends Activity {

	private EditText name,pass;
	private Button loginbut;
	private TextView mac;
	private OnClickListener listener;
	private SqliteUtils su;
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!LibsChecker.checkVitamioLibs(this)){
			return;
		}
		setContentView(R.layout.activity_login);
		su=new SqliteUtils(this);
		name=(EditText)this.findViewById(R.id.name);
		name.setText(ComUtils.getConfig(this, "name", ""));
		pass=(EditText)this.findViewById(R.id.pass);
		pass.setText(ComUtils.getConfig(this, "pass", ""));
		loginbut=(Button)this.findViewById(R.id.loginbut);
		mac=(TextView)this.findViewById(R.id.mac);
		mac.setText(ComUtils.getLocalMacAddressFromIp(this));
		listener=new OnClickListenerImpl();
		pd=new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
		loginbut.setOnClickListener(listener);
		if(isautologin()){
			Login();
		}
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==LoginThread.loginsuc){
				NetUtils.clolseDialog(pd);
				LogUtils.write("tvinfo", "loginsuc");
				String lname=name.getText().toString();
				String lpass=pass.getText().toString();
				ComUtils.setConfig(LoginActivity.this, "name", lname);
				ComUtils.setConfig(LoginActivity.this, "pass", lpass);
				ComUtils.showtoast(LoginActivity.this, R.string.login_suc);
				tohome();
			}else if(msg.what==LoginThread.loginfail){
				NetUtils.clolseDialog(pd);
				ComUtils.setConfig(LoginActivity.this, "pass", "");
				LogUtils.write("tvinfo", "loginfail");
				ComUtils.showtoast(LoginActivity.this, R.string.login_fail);
			}else{
				NetUtils.clolseDialog(pd);
				ComUtils.showtoast(LoginActivity.this, R.string.server_fail);
			}
		}
		
	};
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			if(view.getId()==R.id.loginbut){
				Login();
			}
		}
		
	}
	public void Login(){
		String lname=name.getText().toString();
		String lpass=pass.getText().toString();
		if("".equals(lname)){
			Toast.makeText(LoginActivity.this,R.string.name_empty, Toast.LENGTH_LONG).show();
		}else if("".equals(lpass)){
			Toast.makeText(LoginActivity.this,R.string.pass_empty, Toast.LENGTH_LONG).show();
		}else{
			NetUtils.showDialog(pd, ComUtils.getrestext(LoginActivity.this, R.string.login_title), ComUtils.getrestext(LoginActivity.this, R.string.login_msg));
			NetUtils.login(lname, lpass, handler, su);
		}
	}
	private void tohome(){
		Intent intent=new Intent(this,HomeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		this.startActivity(intent);
		this.finish();
	}
	private boolean isautologin(){
		String lname=name.getText().toString();
		String lpass=pass.getText().toString();
		if(!"".equals(lname)&&!"".equals(lpass)){
			return true;
		}else{
			return false;
		}
	}
}
