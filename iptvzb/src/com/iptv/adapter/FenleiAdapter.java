package com.iptv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iptv.dlerbh.R;
import com.iptv.utils.LiveTV;
import com.iptv.utils.User;

public class FenleiAdapter extends BaseAdapter {

	private Context context;
	private User user;
	public FenleiAdapter(Context context,User user){
		this.context=context;
		this.user=user;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return user.getLivetvlist().size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return user.getLivetvlist().get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LiveTV livetv=user.getLivetvlist().get(arg0);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_fenlei_item, null);
		}
		TextView fname=(TextView)view.findViewById(R.id.fname);
		fname.setText(livetv.getName());
		System.out.println(livetv.getUrl());
		return view;
	}

}
