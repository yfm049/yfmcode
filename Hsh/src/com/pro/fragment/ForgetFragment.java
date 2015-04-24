package com.pro.fragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import com.pro.base.BaseFragment;
import com.pro.hsh.R;

@EFragment(R.layout.fragment_forget)
public class ForgetFragment extends BaseFragment {

	
	@Click
	public void back_but(){
		popBackStack();
	}
	
	@Click
	public void next_but(){
		pushFragmentAddBack(ForgetNextFragment_.builder().build());
	}
}
