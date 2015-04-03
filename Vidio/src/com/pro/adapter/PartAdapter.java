package com.pro.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.pro.pojo.link;
import com.pro.vidio.R;

public class PartAdapter extends BaseAdapter {

	private List<link> links;
	private Context context;
	public PartAdapter(Context context,List<link> links){
		this.links=links;
		this.context=context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return links.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return links.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		VideoHolder holder;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.activity_detail_item, null);
			holder=new VideoHolder();
			holder.partid=(TextView)convertView.findViewById(R.id.partid);
			convertView.setTag(holder);
		}else{
			holder=(VideoHolder)convertView.getTag();
		}
		link l=links.get(position);
		holder.partid.setText(l.filmname);
		return convertView;
	}
	
	class VideoHolder{
		TextView partid;
	}

}
