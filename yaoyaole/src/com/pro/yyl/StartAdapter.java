package com.pro.yyl;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StartAdapter extends BaseAdapter {

	private List<String> lpn;
	private Context context;
	public StartAdapter(Context context,List<String> lpn){
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
			view=LayoutInflater.from(context).inflate(R.layout.activity_result, null);
		}
		TextView phone=(TextView)view.findViewById(R.id.pn);
		phone.setText(lpn.get(pos));
		return view;
	}

}
