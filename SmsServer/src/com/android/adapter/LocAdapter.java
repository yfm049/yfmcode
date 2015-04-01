package com.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.model.Location;
import com.android.smsserver.R;

public class LocAdapter extends BaseAdapter {

	private Context context;
	private List<Location> clients;
	public LocAdapter(Context context,List<Location> clients){
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
		Location c=clients.get(pos);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_loc_list_item, null);
		}
		TextView longitude=(TextView)view.findViewById(R.id.longitude);
		TextView latitude=(TextView)view.findViewById(R.id.latitude);
		TextView location=(TextView)view.findViewById(R.id.location);
		TextView stime=(TextView)view.findViewById(R.id.ctime);
		longitude.setText(c.getLongitude());
		latitude.setText(c.getLatitude());
		location.setText(c.getAddr());
		stime.setText(c.getTime());
		return view;
	}
	
	
}
