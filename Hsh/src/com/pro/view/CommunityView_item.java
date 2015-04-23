package com.pro.view;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.pro.adapter.CommunityGridAdapter;
import com.pro.hsh.R;

@EViewGroup(R.layout.activity_community_item)
public class CommunityView_item extends LinearLayout {

	@Bean
	public CommunityGridAdapter gadpter;
	
	
	@ViewById
	public GridView datagrid;
	
	public CommunityView_item(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public CommunityView_item(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@AfterViews
	public void init(){
		datagrid.setAdapter(gadpter);
	}
	

}
