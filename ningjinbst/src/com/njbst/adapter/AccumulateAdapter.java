package com.njbst.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.njbst.pojo.Accumulate;
import com.njbst.pro.R;
import com.njbst.utils.AsyncImageLoader;

public class AccumulateAdapter extends BaseAdapter {
	
	private List<Accumulate> lmi;
	private Context context;
	private AsyncImageLoader loader;

	public AccumulateAdapter(Context context,List<Accumulate> lmi){
		this.context=context;
		this.lmi=lmi;
		loader=new AsyncImageLoader(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lmi.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lmi.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.activity_accumulate_item, null);
			holder.item_img=(ImageView)convertView.findViewById(R.id.item_img);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			holder.accumulate_item_count=(TextView)convertView.findViewById(R.id.accumulate_item_count);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		Accumulate al=lmi.get(position);
		holder.name.setText(al.getName());
		holder.accumulate_item_count.setText(String.valueOf(al.getZancount()));
		loader.loadImage(al.getImgurl(), holder.item_img);
		return convertView;
	}
	
	class ViewHolder{
		ImageView item_img;
		TextView name;
		TextView accumulate_item_count;
	}

}
