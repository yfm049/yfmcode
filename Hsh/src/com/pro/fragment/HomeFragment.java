package com.pro.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.widget.LinearLayout;

import com.pro.activity.CommunityActivity_;
import com.pro.base.BaseFragment;
import com.pro.hsh.R;
import com.pro.view.HomeView_item;
import com.pro.view.HomeView_item_;

@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {

	@ViewById
	public LinearLayout itemcontent;
	
	
	@AfterViews
	public void init(){
		itemcontent.removeAllViews();
		for(int i=0;i<5;i++){
			HomeView_item view_item=HomeView_item_.build(getActivity());
			itemcontent.addView(view_item);
		}
	}
	
	
	@Click
	public void community_but(){
		CommunityActivity_.intent(getActivity()).start();
	}
}
