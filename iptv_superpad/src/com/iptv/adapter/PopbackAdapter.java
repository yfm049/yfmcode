package com.iptv.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iptv.season.R;

public class PopbackAdapter extends AbsAdapter {

	
	private Context context;
	private List<String> datalist;
	public PopbackAdapter(Context context,List<String> datalist){
		this.context=context;
		this.datalist=datalist;
		setVcount(5);
		setWc(2);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datalist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return datalist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		String text=datalist.get(arg0);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_play_popback_item, null);
		}
		TextView rqtext=(TextView)view.findViewById(R.id.rqtext);
		rqtext.setText(text);
		
		if(itemheight>0){
			LinearLayout itemlayout=(LinearLayout)view.findViewById(R.id.itemlayout);
			LayoutParams params=itemlayout.getLayoutParams();
			params.height=itemheight;
			itemlayout.setLayoutParams(params);
			Log.i("tag", itemheight+"");
		}
		return view;
	}

}
