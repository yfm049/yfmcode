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

import com.njbst.adapter.IntegralPagerAdapter.ListenerImpl;
import com.njbst.pojo.Advert;
import com.njbst.pojo.IntegralInfo;
import com.njbst.pro.DefaultDetailActivity;
import com.njbst.pro.R;
import com.njbst.utils.AsyncImageLoader;

public class HomePagerAdapter extends PagerAdapter {

	

	private AsyncImageLoader imageloader;
	private Context context;
	private List<Advert> ads;
	
	public HomePagerAdapter(Context context,List<Advert> ads){
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
		Advert ad=ads.get(position);
		View v=LayoutInflater.from(context).inflate(R.layout.fragment_home_item, null);
		ImageView navimg=(ImageView)v.findViewById(R.id.navimg);
		if(ad!=null){
			imageloader.loadImage(ad.getUrl(), navimg);
		}
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

		private Advert ad;
		public ListenerImpl(Advert ad){
			this.ad=ad;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(context,DefaultDetailActivity.class);
			intent.putExtra("title", ad.getAdname());
			intent.putExtra("content", "");
			intent.putExtra("imageurl", ad.getUrl());
			intent.putExtra("linkurl", ad.getLinkurl());
			context.startActivity(intent);
		}
		
	}
}
