package com.iptv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iptv.news.R;
import com.iptv.pojo.BackData;
import com.iptv.pojo.PrgItem;

public class ProItemAdapter extends BaseAdapter {

	private Context context;
	private BackData user;
	private int pos=-1;
	public void setPos(int pos) {
		this.pos = pos;
	}
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
		TextView ptime=(TextView)view.findViewById(R.id.ptime);
		ImageView playicon=(ImageView)view.findViewById(R.id.playicon);
		pname.setText(prg.getName());
		ptime.setText(prg.getTime());
		if(arg0==pos){
			playicon.setVisibility(View.VISIBLE);
		}else{
			playicon.setVisibility(View.INVISIBLE);
		}
		return view;
	}

}
