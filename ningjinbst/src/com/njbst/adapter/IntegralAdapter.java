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

import com.njbst.pojo.IntegralInfo;
import com.njbst.pro.R;
import com.njbst.utils.AsyncImageLoader;
import com.njbst.view.ViewPager;

public class IntegralAdapter extends BaseAdapter {
	
	private List<IntegralInfo> lmi;
	private Context context;
	private AsyncImageLoader loader;
	public IntegralAdapter(Context context,List<IntegralInfo> lmi){
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
			convertView=LayoutInflater.from(context).inflate(R.layout.activity_integral_item, null);
			convertView.setId(1);
			holder.integral_img=(ImageView)convertView.findViewById(R.id.integral_img);
			holder.integral_tiem_name=(TextView)convertView.findViewById(R.id.integral_tiem_name);
			holder.integral_con=(TextView)convertView.findViewById(R.id.integral_con);
			holder.integral_inte=(TextView)convertView.findViewById(R.id.integral_inte);
			holder.iteminfo=(LinearLayout)convertView.findViewById(R.id.iteminfo);
			holder.itempager=(FrameLayout)convertView.findViewById(R.id.itempager);
			holder.pager=(ViewPager)convertView.findViewById(R.id.pager);
			
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		IntegralInfo linfo=lmi.get(position);
		
		if(linfo.getIntegralinfos().size()>0){
			holder.itempager.setVisibility(View.VISIBLE);
			holder.iteminfo.setVisibility(View.GONE);
			
			IntegralPagerAdapter spadapter=new IntegralPagerAdapter(context, linfo.getIntegralinfos());
			holder.pager.setAdapter(spadapter);
			
		}else{
			holder.itempager.setVisibility(View.GONE);
			holder.iteminfo.setVisibility(View.VISIBLE);
			holder.integral_tiem_name.setText(linfo.getBrand());
			holder.integral_con.setText(linfo.getDesc());
			holder.integral_inte.setText(String.valueOf(linfo.getIntegral()));
			holder.integral_img.setImageResource(R.drawable.new_item_img);
			loader.loadImage(linfo.getImageurl(), holder.integral_img);
		}
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView integral_img;
		TextView integral_tiem_name;
		TextView integral_con;
		TextView integral_inte;
		LinearLayout iteminfo;
		FrameLayout itempager;
		ViewPager pager;
	}

}
