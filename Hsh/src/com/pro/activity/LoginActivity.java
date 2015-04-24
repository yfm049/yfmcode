package com.pro.activity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.widget.EditText;
import android.widget.ImageView;

import com.pro.base.BaseActivity;
import com.pro.hsh.R;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
	
	@ViewById
	public EditText name,password,code;
	@ViewById
	public ImageView imgcode;
	
	//登录
	@Click
	public void login_but(){
		MainActivity_.intent(LoginActivity.this).start();
		LoginActivity.this.finish();
	}
	//注册
	@Click
	public void reg_but(){
		RegActivity_.intent(LoginActivity.this).start();
	}
	
	//换一个验证码
	@Click
	public void hyz_but(){
		
	}
	//忘记密码
	@Click
	public void forget_but(){
		ForgetActivity_.intent(LoginActivity.this).start();
	}
	
	
}
