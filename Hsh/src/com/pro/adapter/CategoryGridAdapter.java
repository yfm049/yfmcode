package com.pro.adapter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.pro.base.BaseAdapter;
import com.pro.view.CategoryView_grid_item;
import com.pro.view.CategoryView_grid_item_;

@EBean
public class CategoryGridAdapter extends BaseAdapter {

	
	@RootContext
	public Context context;
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 7;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			convertView=CategoryView_grid_item_.build(context);
		}
		CategoryView_grid_item item=(CategoryView_grid_item)convertView;
		
		return item;
	}

}
