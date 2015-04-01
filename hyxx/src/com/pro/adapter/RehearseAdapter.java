package com.pro.adapter;

import java.util.List;

import com.pro.hyxx.R;
import com.pro.pojo.Info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RehearseAdapter extends BaseAdapter {

	private Context context;
	private List<Info> li;
	public RehearseAdapter(Context context,List<Info> li){
		this.context=context;
		this.li=li;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return li.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return li.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Info info=li.get(arg0);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_rehearse_item, null);
		}
		TextView pinyin=(TextView)view.findViewById(R.id.pinyin);
		TextView wenzi=(TextView)view.findViewById(R.id.wenzi);
		pinyin.setText(info.getPinyin());
		wenzi.setText(info.getWenzi());
		return view;
	}

}
