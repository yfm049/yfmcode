package com.njbst.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.njbst.pojo.NewInfo;
import com.njbst.pro.R;
import com.njbst.utils.AsyncImageLoader;
import com.njbst.view.ViewPager;

public class NewAdapter extends BaseAdapter {
	
	private List<NewInfo> lmi;
	private Context context;
	private AsyncImageLoader loader;
	public NewAdapter(Context context,List<NewInfo> lmi){
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
		if(convertView==null||convertView.getId()==0){
			holder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.fragment_new_item, null);
			convertView.setId(1);
			holder.new_img=(ImageView)convertView.findViewById(R.id.new_img);
			holder.new_tiem_title=(TextView)convertView.findViewById(R.id.new_tiem_title);
			holder.new_con=(TextView)convertView.findViewById(R.id.new_con);
			holder.iteminfo=(LinearLayout)convertView.findViewById(R.id.iteminfo);
			holder.itempager=(FrameLayout)convertView.findViewById(R.id.itempager);
			holder.pager=(ViewPager)convertView.findViewById(R.id.pager);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		NewInfo info=lmi.get(position);
		if(info.getNewinfos().size()>0){
			holder.itempager.setVisibility(View.VISIBLE);
			holder.iteminfo.setVisibility(View.GONE);
			NewPagerAdapter spadapter=new NewPagerAdapter(context, info.getNewinfos());
			holder.pager.setAdapter(spadapter);
		}else{
			holder.itempager.setVisibility(View.GONE);
			holder.iteminfo.setVisibility(View.VISIBLE);
			holder.new_tiem_title.setText(info.getTitle());
			holder.new_con.setText(info.getContent());
			holder.pager.setAdapter(null);
			loader.loadImage(info.getImageurl(), holder.new_img);
		}
		return convertView;
	}
	
	class ViewHolder{
		ImageView new_img;
		TextView new_tiem_title;
		TextView new_con;
		LinearLayout iteminfo;
		FrameLayout itempager;
		ViewPager pager;
	}

}
