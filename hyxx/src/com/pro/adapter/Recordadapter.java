package com.pro.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pro.hyxx.R;
import com.pro.pojo.Info;

public class Recordadapter extends PagerAdapter {

	private Context context;
	private List<Info> li;
	private List<View> lv=new ArrayList<View>();
	public Recordadapter(Context context,List<Info> li){
		this.context=context;
		this.li=li;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(lv.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		Info info=li.get(position);
		View view=LayoutInflater.from(context).inflate(R.layout.activity_record_item, null);
		TextView rpinyin=(TextView)view.findViewById(R.id.rpinyin);
		TextView rwenzi=(TextView)view.findViewById(R.id.rwenzi);
		TextView rjieshi=(TextView)view.findViewById(R.id.rjieshi);
		rpinyin.setText(info.getPinyin());
		rwenzi.setText(info.getWenzi());
		rjieshi.setText(info.getJieshi());
		lv.add(view);
		container.addView(lv.get(position));
		return lv.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return li.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}

}
