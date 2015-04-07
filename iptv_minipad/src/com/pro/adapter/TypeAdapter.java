package com.pro.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iptv.season.R;
import com.pro.pojo.Type;

public class TypeAdapter extends BaseAdapter {

	private List<Type> types;
	private Context context;
	public TypeAdapter(Context context,List<Type> types){
		this.types=types;
		this.context=context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return types.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return types.get(position);
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
			convertView=LayoutInflater.from(context).inflate(R.layout.window_type_item, null);
			holder=new VideoHolder();
			holder.typename=(TextView)convertView.findViewById(R.id.typename);
			convertView.setTag(holder);
		}else{
			holder=(VideoHolder)convertView.getTag();
		}
		Type l=types.get(position);
		holder.typename.setText(l.name);
		return convertView;
	}
	
	class VideoHolder{
		TextView typename;
	}

}
