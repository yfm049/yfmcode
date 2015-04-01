package com.iptv.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AbsAdapter extends BaseAdapter {

	public int itemheight=-1;
	public int vcount=9;
	private int wc=1;
	

	public AbsAdapter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	public int getItemheight() {
		return itemheight;
	}
	public void setItemheight(int listvieweheight) {
		this.itemheight = listvieweheight/vcount-wc;
		this.notifyDataSetChanged();
	}
	public int getVcount() {
		return vcount;
	}
	public void setVcount(int vcount) {
		this.vcount = vcount;
	}
	public void setWc(int wc) {
		this.wc = wc;
	}
}
