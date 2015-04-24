package com.pro.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import com.pro.base.BaseActivity;
import com.pro.fragment.ForgetFragment_;
import com.pro.hsh.R;

@EActivity(R.layout.activity_forget)
public class ForgetActivity extends BaseActivity {
	
	
	@AfterViews
	public void init(){
		pushFragment(ForgetFragment_.builder().build());
	}
	
}
