package com.iptv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iptv.season.R;
import com.iptv.pojo.BackData;
import com.iptv.pojo.PrgItem;

public class ProItemAdapter extends BaseAdapter {

	private Context context;
	private BackData user;
	public ProItemAdapter(Context context,BackData user){
		this.context=context;
		this.user=user;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return user.getPrglist().size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return user.getPrglist().get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		PrgItem prg=user.getPrglist().get(arg0);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_pro_item, null);
		}
		TextView pname=(TextView)view.findViewById(R.id.pname);
		TextView data=(TextView)view.findViewById(R.id.data);
		TextView ptime=(TextView)view.findViewById(R.id.ptime);
		pname.setText(prg.getName());
		data.setText(prg.getDate());
		ptime.setText(prg.getTime());
		return view;
	}

}
