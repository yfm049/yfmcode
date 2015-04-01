package com.iptv.LayoutListener;

import android.util.Log;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ListView;

import com.iptv.adapter.AbsDataAdapter;

public class LayoutListener implements OnGlobalLayoutListener {

	private ListView listview;
	private AbsDataAdapter adapter;
	private int height=-1;
	public LayoutListener(ListView listview,AbsDataAdapter adapter){
		this.listview=listview;
		this.adapter=adapter;
	}
	@Override
	public void onGlobalLayout() {
		// TODO Auto-generated method stub
		if(listview.getHeight()>0&&height!=listview.getHeight()){
			Log.i("tvinfo", "listview api "+listview.getHeight());
			adapter.setItemheight(listview.getHeight());
			height=listview.getHeight();
		}
		
	}

}
