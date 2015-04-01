package com.njbst.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.njbst.adapter.SalePagerAdapter.ListenerImpl;
import com.njbst.pojo.Promotion;
import com.njbst.pojo.SaleInfo;
import com.njbst.pro.DefaultDetailActivity;
import com.njbst.pro.R;
import com.njbst.utils.AsyncImageLoader;

public class PromotionPagerAdapter extends PagerAdapter {

	

	private AsyncImageLoader imageloader;
	private Context context;
	private List<Promotion> ads;
	
	public PromotionPagerAdapter(Context context,List<Promotion> ads){
		this.context=context;
		this.ads=ads;
		imageloader=new AsyncImageLoader(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ads.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		Promotion ad=ads.get(position);
		View v=LayoutInflater.from(context).inflate(R.layout.activity_promotion_top_item_item, null);
		ImageView item_img=(ImageView)v.findViewById(R.id.item_img);
		imageloader.loadImage(ad.getImgurl(), item_img);
		container.addView(v);
		v.setOnClickListener(new ListenerImpl(ad));
		return v;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView((View)object);
	}
	class ListenerImpl implements OnClickListener{

		private Promotion ad;
		public ListenerImpl(Promotion ad){
			this.ad=ad;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(context,DefaultDetailActivity.class);
			intent.putExtra("title", ad.getTitle());
			intent.putExtra("content", ad.getDetail());
			intent.putExtra("imageurl", ad.getImgurl());
			intent.putExtra("linkurl", ad.getHttpurl());
			context.startActivity(intent);
		}
		
	}
}
