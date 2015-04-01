package com.iptv.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iptv.season.R;
import com.iptv.pojo.AllAppinfo;

public class AllAppAdapter extends BaseAdapter {

	private Context context;
	private List<AllAppinfo> lapp;
	public AllAppAdapter(Context context,List<AllAppinfo> lapp){
		this.context=context;
		this.lapp=lapp;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lapp.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lapp.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		AllAppinfo appinfo=lapp.get(arg0);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_allapp_item, null);
		}
		ImageView icon=(ImageView)view.findViewById(R.id.icon);
		TextView appname=(TextView)view.findViewById(R.id.appname);
		icon.setImageDrawable(appinfo.getIcon());
		appname.setText(appinfo.getName());
		return view;
	}

}
