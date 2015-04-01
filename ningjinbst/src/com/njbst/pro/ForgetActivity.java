package com.njbst.pro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import com.njbst.async.AuthCodeAsyncTask;
import com.njbst.async.GetCodeAsyncTask;
import com.njbst.utils.ComUtils;
import com.njbst.utils.ToastUtils;

public class ForgetActivity extends ActionBarActivity {

	private EditText phonenum, yzmcode;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget);
		phonenum = (EditText) this.findViewById(R.id.phonenum);
		yzmcode = (EditText) this.findViewById(R.id.yzmcode);
		
	}

	public void nextclick(View view) {
		String pn = phonenum.getText().toString();
		String yzm = yzmcode.getText().toString();
		if (!ComUtils.isMobileNO(pn)) {
			ToastUtils.showToast(this, R.string.phone_toast);
		} else if (yzm.length() <= 0) {
			ToastUtils.showToast(this, R.string.yzmcode_error);
		} else {
			new AuthCodeAsyncTask(this, handler).execute(pn,yzm);
		}
	}
	public void codeclick(View view) {
		String pn = phonenum.getText().toString();
		if (!ComUtils.isMobileNO(pn)) {
			ToastUtils.showToast(this, R.string.phone_toast);
		}  else {
			new GetCodeAsyncTask(this).execute(pn,"resetpwd");
		}
	}
	
	

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				toMain();
			}else if(msg.what==2){
				Intent intent=new Intent(ForgetActivity.this,PasswordActivity.class);
				intent.putExtra("userid", msg.arg1);
				ForgetActivity.this.startActivity(intent);
				ForgetActivity.this.finish();
			}
		}

	};
	

	private void toMain() {
		Intent intent = new Intent(this, MainActivity.class);
		this.startActivity(intent);
		this.finish();
	}

	
	


}
