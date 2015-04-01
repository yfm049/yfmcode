package com.iptv.utils;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.iptv.adapter.AbsAdapter;

public class LayoutListener implements OnGlobalLayoutListener {

	private View view;
	private AbsAdapter adapter;
	private int height=-1;
	public LayoutListener(View view,AbsAdapter adapter){
		this.view=view;
		this.adapter=adapter;
	}
	@Override
	public void onGlobalLayout() {
		// TODO Auto-generated method stub
		LayoutParams params=view.getLayoutParams();
		Log.i("tvinfo", "listview api --"+params.height);
		if(view.getHeight()>0&&height!=view.getHeight()){
			Log.i("tvinfo", "listview api "+view.getHeight());
			adapter.setItemheight(view.getHeight());
			height=view.getHeight();
		}
		
	}

}
