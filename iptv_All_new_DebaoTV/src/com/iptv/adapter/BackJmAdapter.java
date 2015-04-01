package com.iptv.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iptv.pojo.Notice;
import com.iptv.pojo.PrgItem;
import com.tv.debao.R;

public class BackJmAdapter extends AbsDataAdapter {

	private List<PrgItem> listpro;
	private LayoutInflater layoutinflater;
	private int selectpos=-1;
	private int itemheight=-1;
	private int vcount=13;
	public void setSelectpos(int selectpos) {
		this.selectpos = selectpos;
	}
	public BackJmAdapter(Context context,List<PrgItem> listpro){
		this.listpro=listpro;
		layoutinflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listpro.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return listpro.get(pos);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if(view==null){
			view=layoutinflater.inflate(R.layout.back_jm_item,null);
		}
		TextView backtime=(TextView)view.findViewById(R.id.backtime);
		TextView backname=(TextView)view.findViewById(R.id.backname);
		ImageView playicon=(ImageView)view.findViewById(R.id.playicon);
		PrgItem item=listpro.get(pos);
		backtime.setText(item.getTime());
		backname.setText(item.getName());
		if(selectpos!=-1&&selectpos==pos){
			playicon.setVisibility(View.VISIBLE);
		}else{
			playicon.setVisibility(View.INVISIBLE);
		}
		if(itemheight>0){
			LinearLayout itemlayout=(LinearLayout)view.findViewById(R.id.itemlayout);
			LayoutParams params=itemlayout.getLayoutParams();
			params.height=itemheight;
			itemlayout.setLayoutParams(params);
		}
		return view;
	}
	public int getItemheight() {
		return itemheight;
	}
	public void setItemheight(int itemheight) {
		this.itemheight = itemheight/vcount-1;
		this.notifyDataSetChanged();
	}
	public int getVcount() {
		return vcount;
	}
}
