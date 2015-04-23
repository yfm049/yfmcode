package com.pro.activity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import com.pro.base.BaseActivity;
import com.pro.hsh.R;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
	
	
	
	@Click
	public void login_but(){
		MainActivity_.intent(LoginActivity.this).start();
		LoginActivity.this.finish();
	}
	
}
