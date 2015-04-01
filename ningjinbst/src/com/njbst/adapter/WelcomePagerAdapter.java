package com.njbst.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.njbst.pro.R;

public class WelcomePagerAdapter extends PagerAdapter {

	

	private Context context;
	private List<Bitmap> ads;
	private Button startbut;
	
	public WelcomePagerAdapter(Context context,List<Bitmap> ads,Button startbut){
		this.context=context;
		this.ads=ads;
		this.startbut=startbut;
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
		Bitmap ad=ads.get(position);
		View v=LayoutInflater.from(context).inflate(R.layout.welcome_item, null);
		ImageView wcimg=(ImageView)v.findViewById(R.id.wcimg);
		wcimg.setImageBitmap(ad);
		container.addView(v);
		
		return v;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView((View)object);
	}
	
}
