package com.android.adapter;

import java.util.List;

import com.android.model.Client;
import com.android.smsserver.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ClientAdapter extends BaseAdapter {

	private Context context;
	private List<Client> clients;
	public ClientAdapter(Context context,List<Client> clients){
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
		Client c=clients.get(pos);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_main_list_item, null);
		}
		TextView clientname=(TextView)view.findViewById(R.id.set_name);
		TextView phone=(TextView)view.findViewById(R.id.phone);
		clientname.setText(c.getClientname());
		phone.setText(c.getPhone());
		return view;
	}

}
