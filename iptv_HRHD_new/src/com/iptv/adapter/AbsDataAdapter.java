package com.iptv.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AbsDataAdapter extends BaseAdapter {

	private int itemheight=-1;
	private int vcount=9;
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}
	public int getItemheight() {
		return itemheight;
	}
	public void setItemheight(int listvieweheight) {
		this.itemheight = listvieweheight/vcount-1;
		this.notifyDataSetChanged();
	}
	public int getVcount() {
		return vcount;
	}
	public void setVcount(int vcount) {
		this.vcount = vcount;
	}

}
