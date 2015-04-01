package com.pro.yyl;

import java.util.List;

import shidian.tv.sntv.tools.Record;
import shidian.tv.sntv.tools.Result;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RecordAdapter extends BaseAdapter {

	private List<Result> lpn;
	private Context context;
	public RecordAdapter(Context context,List<Result> lpn){
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
			view=LayoutInflater.from(context).inflate(R.layout.activity_record_item, null);
		}
		TextView phone=(TextView)view.findViewById(R.id.pn);
		TextView state=(TextView)view.findViewById(R.id.state);
		TextView con=(TextView)view.findViewById(R.id.con);
		TextView time=(TextView)view.findViewById(R.id.time);
		
		TextView num=(TextView)view.findViewById(R.id.num);
		num.setText(String.valueOf(pos+1));
		Result r=lpn.get(pos);
		phone.setText(r.getPhone());
		state.setText(r.getState());
		con.setText(r.getTmsgb()+"_"+r.getGiftname());
		time.setText(r.getTime());
		return view;
	}

}
