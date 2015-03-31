package com.pro.demo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DataAdapter extends BaseAdapter {

	private Context context;
	private List<Data> ls=new ArrayList<Data>();
	public List<Data> getLs() {
		return ls;
	}
	public DataAdapter(Context context){
		this.context=context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ls.size();
	}

	@Override
	public Object getItem(int item) {
		// TODO Auto-generated method stub
		return ls.get(item);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}			
		viewHolder.tvTitle.setText(ls.get(position).getMsg()+""+ls.get(position).getId());
		//System.out.println(ls.get(position));
		return view;

	}

	final static class ViewHolder {
		TextView tvTitle;
	}
	
}
