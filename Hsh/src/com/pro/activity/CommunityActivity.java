package com.pro.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.widget.ListView;

import com.pro.adapter.CommunityAdapter;
import com.pro.base.BaseActivity;
import com.pro.hsh.R;

@EActivity(R.layout.activity_community)
public class CommunityActivity extends BaseActivity {
	
	@ViewById
	public ListView datalist;
	@Bean
	public CommunityAdapter cadapter;
	
	@AfterViews
	public void init(){
		datalist.setAdapter(cadapter);
	}

}
