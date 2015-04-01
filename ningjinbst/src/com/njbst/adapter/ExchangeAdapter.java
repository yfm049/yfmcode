package com.njbst.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.njbst.pojo.ExchangeInfo;
import com.njbst.pro.R;
import com.njbst.utils.AsyncImageLoader;

public class ExchangeAdapter extends BaseAdapter {
	
	private List<ExchangeInfo> lmi;
	private Context context;
	private AsyncImageLoader imageloader;

	public ExchangeAdapter(Context context,List<ExchangeInfo> lmi){
		this.context=context;
		this.lmi=lmi;
		imageloader=new AsyncImageLoader(context);
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
		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.activity_my_exchange_item, null);
			holder.img=(ImageView)convertView.findViewById(R.id.img);
			holder.title=(TextView)convertView.findViewById(R.id.title);
			holder.integral=(TextView)convertView.findViewById(R.id.integral);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		ExchangeInfo info=lmi.get(position);
		holder.title.setText(info.getTitle());
		holder.integral.setText(String.valueOf(info.getIntegral()));
		imageloader.loadImage(info.getImgurl(), holder.img);
		return convertView;
	}
	class ViewHolder{
		ImageView img;
		TextView title,integral;
	}

}
