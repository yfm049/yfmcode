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

import com.njbst.pojo.Promotion;
import com.njbst.pro.R;
import com.njbst.utils.AsyncImageLoader;
import com.njbst.view.ViewPager;

public class PromotionAdapter extends BaseAdapter {
	
	private List<Promotion> lmi;
	private Context context;
	private AsyncImageLoader loader;
	public PromotionAdapter(Context context,List<Promotion> lmi){
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
			convertView=LayoutInflater.from(context).inflate(R.layout.activity_promotion_item, null);
			holder.promotion_img=(ImageView)convertView.findViewById(R.id.promotion_img);
			holder.promotion_tiem_name=(TextView)convertView.findViewById(R.id.promotion_tiem_name);
			holder.promotion_tiem_type=(TextView)convertView.findViewById(R.id.promotion_tiem_type);
			holder.iteminfo=(LinearLayout)convertView.findViewById(R.id.iteminfo);
			holder.itempager=(FrameLayout)convertView.findViewById(R.id.itempager);
			holder.pager=(ViewPager)convertView.findViewById(R.id.pager);
			
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		Promotion pt=lmi.get(position);
		if(pt.getPromotions().size()>0){
			holder.itempager.setVisibility(View.VISIBLE);
			holder.iteminfo.setVisibility(View.GONE);
			PromotionPagerAdapter ppadapter=new PromotionPagerAdapter(context, pt.getPromotions());
			holder.pager.setAdapter(ppadapter);
		}else{
			holder.itempager.setVisibility(View.GONE);
			holder.iteminfo.setVisibility(View.VISIBLE);
			holder.pager.setAdapter(null);
			holder.promotion_tiem_name.setText(pt.getTitle());
			holder.promotion_tiem_type.setText(pt.getDetail());
			holder.promotion_img.setImageResource(R.drawable.new_item_img);
			loader.loadImage(pt.getImgurl(), holder.promotion_img);
		}
		return convertView;
	}
	
	class ViewHolder{
		ImageView promotion_img;
		TextView promotion_tiem_name;
		TextView promotion_tiem_type;
		LinearLayout iteminfo;
		FrameLayout itempager;
		ViewPager pager;
	}
	

}
