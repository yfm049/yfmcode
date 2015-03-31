package com.preview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.preview.camerapro.R;

public class SetAdapter extends BaseAdapter {

	public String[] setcon;
	private Context context;
	public SetAdapter(Context context,String[] setcon){
		this.setcon=setcon;
		this.context=context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return setcon.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return setcon[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int location, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_set_item, null);
		}
		TextView settext=(TextView)view.findViewById(R.id.settext);
		settext.setText(setcon[location]);
		return view;
	}


}
