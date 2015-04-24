package com.pro.fragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import com.pro.base.BaseFragment;
import com.pro.hsh.R;

@EFragment(R.layout.fragment_password_reset)
public class PasswordResetFragment extends BaseFragment {

	@Click
	public void back_but(){
		popBackStack();
	}
	
	@Click
	public void update_but(){
		showToast("ĞŞ¸ÄÃÜÂë");
		finishActivity();
	}
	
}
