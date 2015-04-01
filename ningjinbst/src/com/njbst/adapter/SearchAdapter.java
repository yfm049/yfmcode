package com.njbst.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njbst.pojo.Search;
import com.njbst.pro.R;

public class SearchAdapter extends BaseAdapter {

	private Context context;
	private List<Search> ls;
	public SearchAdapter(Context context,List<Search> ls){
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
			convertView=LayoutInflater.from(context).inflate(R.layout.fragment_search_item, null);
			holder.search_item_name=(TextView)convertView.findViewById(R.id.search_item_name);
			holder.search_item_tel=(TextView)convertView.findViewById(R.id.search_item_tel);
			holder.search_item_address=(TextView)convertView.findViewById(R.id.search_item_address);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		Search info=ls.get(position);
		holder.search_item_name.setText(info.getName());
		holder.search_item_tel.setText(info.getTel());
		holder.search_item_address.setText(info.getAddress());
		return convertView;
	}
	class ViewHolder{
		TextView search_item_name;
		TextView search_item_tel;
		TextView search_item_address;
	}

}
