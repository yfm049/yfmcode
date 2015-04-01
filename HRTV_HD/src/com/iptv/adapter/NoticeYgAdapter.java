package com.iptv.adapter;

import java.util.List;

import com.iptv.pojo.Notice;
import com.iptv.HRTV.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NoticeYgAdapter extends BaseAdapter {

	private List<Notice> listnotice;
	private LayoutInflater layoutinflater;
	public NoticeYgAdapter(Context context,List<Notice> listnotice){
		this.listnotice=listnotice;
		layoutinflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listnotice.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return listnotice.get(pos);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if(view==null){
			view=layoutinflater.inflate(R.layout.notice_yg_item,null);
		}
		TextView ytime=(TextView)view.findViewById(R.id.ytime);
		TextView yname=(TextView)view.findViewById(R.id.yname);
		ImageView isplay=(ImageView)view.findViewById(R.id.isplay);
		Notice nc=listnotice.get(pos);
		ytime.setText(nc.getStart()+"-"+nc.getEnd());
		yname.setText(nc.getName());
		if(nc.isIsplay()){
			isplay.setVisibility(View.VISIBLE);
		}else{
			isplay.setVisibility(View.INVISIBLE);
		}
		return view;
	}

}
