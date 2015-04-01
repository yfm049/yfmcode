package com.njbst.pro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import com.njbst.async.GetCodeAsyncTask;
import com.njbst.async.RegAsyncTask;
import com.njbst.utils.ComUtils;
import com.njbst.utils.ToastUtils;

public class RegActivity extends ActionBarActivity {

	private EditText phonenum,password,passworded,yzmcode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg);
		phonenum=(EditText)this.findViewById(R.id.phonenum);
		password=(EditText)this.findViewById(R.id.password);
		passworded=(EditText)this.findViewById(R.id.passworded);
		yzmcode = (EditText) this.findViewById(R.id.yzmcode);
	}
	
	public void RegClick(View view){
		String pn=phonenum.getText().toString();
		String yzm = yzmcode.getText().toString();
		String pass=password.getText().toString();
		String passed=passworded.getText().toString();
		if(!ComUtils.isMobileNO(pn)){
			ToastUtils.showToast(this, R.string.phone_toast);
		} else if (yzm.length() <= 0) {
			ToastUtils.showToast(this, R.string.yzmcode_error);
		}else if(pass.length()<6){
			ToastUtils.showToast(this, R.string.pass_toast);
		}else if(!pass.equals(passed)){
			ToastUtils.showToast(this, R.string.passed_toast);
		}else{
			new RegAsyncTask(this,handler).execute(pn,pass);
		}
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				toLogin();
			}
		}
		
	};
	
	public void codeclick(View view) {
		String pn = phonenum.getText().toString();
		if (!ComUtils.isMobileNO(pn)) {
			ToastUtils.showToast(this, R.string.phone_toast);
		}  else {
			new GetCodeAsyncTask(this).execute(pn,"opreg");
		}
	}
	
	private void toLogin(){
		Intent intent=new Intent(this,LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
	}
	
	

}
