package com.njbst.pro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import com.njbst.async.PasswordAsyncTask;
import com.njbst.utils.ToastUtils;

public class PasswordActivity extends ActionBarActivity {

	private EditText password,passworded;
	private int userid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password);
		password=(EditText)this.findViewById(R.id.password);
		passworded=(EditText)this.findViewById(R.id.passworded);
		userid=this.getIntent().getIntExtra("userid", -1);
		if(userid==-1){
			toLogin();
		}
	}
	
	public void resetClick(View view){
		String pass=password.getText().toString();
		String passed=passworded.getText().toString();
		if(pass.length()<6){
			ToastUtils.showToast(this, R.string.pass_toast);
		}else if(!pass.equals(passed)){
			ToastUtils.showToast(this, R.string.passed_toast);
		}else{
			new PasswordAsyncTask(this,handler).execute(String.valueOf(userid),pass);
		}
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==2){
				toLogin();
			}
		}
		
	};
	
	private void toLogin(){
		Intent intent=new Intent(this,LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		this.finish();
	}
	
	

}
