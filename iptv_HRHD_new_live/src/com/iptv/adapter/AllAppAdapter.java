package com.iptv.adapter;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tvbox.tvplay.R;

public class AllAppAdapter extends BaseAdapter {

	private Context context;
	private PackageManager pm;
	private List<ApplicationInfo> lapp;
	public AllAppAdapter(Context context,List<ApplicationInfo> lapp){
		this.context=context;
		this.lapp=lapp;
		pm=context.getPackageManager();
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
		ApplicationInfo appinfo=lapp.get(arg0);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_allapp_item, null);
		}
		ImageView icon=(ImageView)view.findViewById(R.id.icon);
		TextView appname=(TextView)view.findViewById(R.id.appname);
		icon.setImageDrawable(appinfo.loadIcon(pm));
		appname.setText(appinfo.loadLabel(pm).toString());
		return view;
	}

}
