package com.pro.view;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.pro.adapter.CategoryGridAdapter;
import com.pro.hsh.R;

@EViewGroup(R.layout.fragment_category_item)
public class CategoryView_item extends LinearLayout {

	@Bean
	public CategoryGridAdapter gadpter;
	
	
	@ViewById
	public GridView datagrid;
	
	public CategoryView_item(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public CategoryView_item(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@AfterViews
	public void init(){
		datagrid.setAdapter(gadpter);
	}
	

}
