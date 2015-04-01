package com.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.model.Sms;
import com.android.smsserver.R;

public class SmsAdapter extends BaseAdapter {

	private Context context;
	private List<Sms> clients;
	public SmsAdapter(Context context,List<Sms> clients){
		this.context=context;
		this.clients=clients;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return clients.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return clients.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Sms c=clients.get(pos);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_sms_list_item, null);
		}
		TextView sname=(TextView)view.findViewById(R.id.longitude);
		TextView sphone=(TextView)view.findViewById(R.id.latitude);
		TextView stype=(TextView)view.findViewById(R.id.stype);
		TextView sbody=(TextView)view.findViewById(R.id.sbody);
		TextView stime=(TextView)view.findViewById(R.id.ctime);
		sname.setText(c.getPhonename());
		sphone.setText(c.getAddress());
		stype.setText(c.getType());
		sbody.setText(c.getBody());
		stime.setText(c.getDates());
		return view;
	}

}
