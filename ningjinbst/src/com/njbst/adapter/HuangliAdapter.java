package com.njbst.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njbst.pojo.HuangliInfo;
import com.njbst.pro.R;

public class HuangliAdapter extends BaseAdapter {

	private Context context;
	private List<HuangliInfo> ls;
	public HuangliAdapter(Context context,List<HuangliInfo> ls){
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
			convertView=LayoutInflater.from(context).inflate(R.layout.activity_huangli_item, null);
			holder.time=(TextView)convertView.findViewById(R.id.huangli_tiem_time);
			holder.con=(TextView)convertView.findViewById(R.id.huangli_item_text);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		HuangliInfo info=ls.get(position);
		holder.time.setText(info.getTime());
		holder.con.setText(info.getCon());
		return convertView;
	}
	class ViewHolder{
		TextView time;
		TextView con;
	}

}
