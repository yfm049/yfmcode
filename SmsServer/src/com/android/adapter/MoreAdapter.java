package com.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.model.Client;
import com.android.smsserver.R;

public class MoreAdapter extends BaseAdapter {

	private Context context;
	private List<String> ls;
	private Client client;
	public MoreAdapter(Context context,List<String> ls,Client client){
		this.context=context;
		this.ls=ls;
		this.client=client;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ls.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return ls.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		String c=ls.get(pos);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_set_list_item, null);
		}
		TextView set_name=(TextView)view.findViewById(R.id.set_name);
		set_name.setText(c);
		return view;
	}

}
