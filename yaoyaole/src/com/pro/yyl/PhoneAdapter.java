package com.pro.yyl;

import java.util.List;

import shidian.tv.sntv.tools.PhoneInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PhoneAdapter extends BaseAdapter {

	private List<PhoneInfo> lpn;
	private Context context;
	public PhoneAdapter(Context context,List<PhoneInfo> lpn){
		this.context=context;
		this.lpn=lpn;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lpn.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return lpn.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_phone, null);
		}
		TextView phone=(TextView)view.findViewById(R.id.pn);
		TextView pnstate=(TextView)view.findViewById(R.id.pnstate);
		TextView num=(TextView)view.findViewById(R.id.num);
		num.setText(String.valueOf(pos+1));
		phone.setText(lpn.get(pos).getPhone());
		pnstate.setText(lpn.get(pos).getState());
		return view;
	}

}
