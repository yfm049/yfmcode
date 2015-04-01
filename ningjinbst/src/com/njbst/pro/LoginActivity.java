package com.njbst.pro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import com.njbst.async.LoginAsyncTask;
import com.njbst.pro.wxapi.WXEntryActivity;
import com.njbst.utils.ToastUtils;

public class LoginActivity extends ActionBarActivity{

	private EditText phonenum, password;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		phonenum = (EditText) this.findViewById(R.id.phonenum);
		password = (EditText) this.findViewById(R.id.password);
	}
	

	public void loginclick(View view) {
		String pn = phonenum.getText().toString();
		String pw = password.getText().toString();
		if (pn.length() < 11) {
			ToastUtils.showToast(this, R.string.phone_toast);
		} else if (pw.length() < 6) {
			ToastUtils.showToast(this, R.string.pass_toast);
		} else {
			new LoginAsyncTask(this,handler).execute(pn,pw);
		}
	}

	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				Intent intent=new Intent(LoginActivity.this,MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				LoginActivity.this.startActivity(intent);
			}
		}

	};
	


	public void regclick(View view) {
		Intent intent = new Intent(this, RegActivity.class);
		this.startActivity(intent);
	}
	public void forgetclick(View view) {
		Intent intent = new Intent(this, ForgetActivity.class);
		this.startActivity(intent);
	}
	
	public void QQLogin(View view){
		Intent intent=new Intent(this,QQLoginActivity.class);
		startActivity(intent);
	}
	
	public void WxLogin(View view){
		Intent intent=new Intent(this,WXLoginActivity.class);
		startActivity(intent);
		
	}
	


}
