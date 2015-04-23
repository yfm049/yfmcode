package com.pro.adapter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

import com.pro.base.BaseAdapter;
import com.pro.hsh.R;
import com.pro.view.CategoryViewbig_item;
import com.pro.view.CategoryViewbig_item_;

@EBean
public class CategoryBigAdapter extends BaseAdapter {

	public SparseBooleanArray checkarray=new SparseBooleanArray();
	
	@RootContext
	public Context context;
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 6;
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
	
	public void setCheck(int pos){
		checkarray.clear();
		checkarray.put(pos, true);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			convertView=CategoryViewbig_item_.build(context);
		}
		CategoryViewbig_item item=(CategoryViewbig_item)convertView;
		
		if(checkarray.get(position, false)){
			item.setBackgroundResource(R.color.white);
		}else{
			item.setBackgroundResource(R.color.category_left);
		}
		
		return item;
	}

}
