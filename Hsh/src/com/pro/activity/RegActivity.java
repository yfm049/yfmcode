package com.pro.activity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.widget.EditText;
import android.widget.ImageView;

import com.pro.base.BaseActivity;
import com.pro.hsh.R;

@EActivity(R.layout.activity_reg)
public class RegActivity extends BaseActivity {
	
	@ViewById
	public EditText name,password,code;
	@ViewById
	public ImageView imgcode;
	
	@Click
	public void login_but(){
		MainActivity_.intent(RegActivity.this).start();
		RegActivity.this.finish();
	}
	
	@Click
	public void hyz_but(){
		
	}
	
}
