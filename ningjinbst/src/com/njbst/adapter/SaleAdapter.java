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

import com.njbst.pojo.SaleInfo;
import com.njbst.pro.R;
import com.njbst.utils.AsyncImageLoader;
import com.njbst.view.ViewPager;

public class SaleAdapter extends BaseAdapter {
	
	private List<SaleInfo> lmi;
	private Context context;
	private AsyncImageLoader loader;
	public SaleAdapter(Context context,List<SaleInfo> lmi){
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
		return lmi.get(position-1);
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
			convertView=LayoutInflater.from(context).inflate(R.layout.activity_sale_item, null);
			convertView.setId(1);
			holder.sale_img=(ImageView)convertView.findViewById(R.id.sale_img);
			holder.sale_tiem_name=(TextView)convertView.findViewById(R.id.sale_tiem_name);
			holder.sale_tiem_type=(TextView)convertView.findViewById(R.id.sale_tiem_type);
			holder.sale_price=(TextView)convertView.findViewById(R.id.sale_price);
			holder.sale_zekou=(TextView)convertView.findViewById(R.id.sale_zekou);
			holder.iteminfo=(LinearLayout)convertView.findViewById(R.id.iteminfo);
			holder.itempager=(FrameLayout)convertView.findViewById(R.id.itempager);
			holder.pager=(ViewPager)convertView.findViewById(R.id.pager);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		SaleInfo info=lmi.get(position);
		if(info.getSaleinfos().size()>0){
			holder.itempager.setVisibility(View.VISIBLE);
			holder.iteminfo.setVisibility(View.GONE);
			SalePagerAdapter spadapter=new SalePagerAdapter(context, info.getSaleinfos());
			holder.pager.setAdapter(spadapter);
		}else{
			holder.itempager.setVisibility(View.GONE);
			holder.iteminfo.setVisibility(View.VISIBLE);
			holder.sale_tiem_name.setText(info.getProduct());
			holder.sale_tiem_type.setText(info.getBrand());
			holder.sale_price.setText(info.getDesc());
			holder.sale_zekou.setText(info.getZekou());
			holder.pager.setAdapter(null);
			holder.sale_img.setImageResource(R.drawable.new_item_img);
			loader.loadImage(info.getImageurl(), holder.sale_img);
		}
		
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView sale_img;
		TextView sale_tiem_name;
		TextView sale_tiem_type;
		TextView sale_price;
		TextView sale_zekou;
		LinearLayout iteminfo;
		FrameLayout itempager;
		ViewPager pager;
	}
	

}
