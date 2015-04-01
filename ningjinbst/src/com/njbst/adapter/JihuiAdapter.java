package com.njbst.adapter;

import java.util.List;

import com.njbst.pro.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JihuiAdapter extends BaseAdapter {

	private Context context;
	private List<String> ls;
	public JihuiAdapter(Context context,List<String> ls){
		this.context=context;
		this.ls=ls;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ls.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return ls.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.activity_jihui_item, null);
			holder.con=(TextView)convertView.findViewById(R.id.jihui_item_text);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.con.setText(ls.get(position));
		return convertView;
	}
	class ViewHolder{
		TextView con;
	}

}
