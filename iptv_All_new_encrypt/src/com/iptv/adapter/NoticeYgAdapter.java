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
import com.mediatv.R;

public class NoticeYgAdapter extends AbsDataAdapter {

	private List<Notice> listnotice;
	private LayoutInflater layoutinflater;
	private int itemheight=-1;
	private int vcount=6;
	public NoticeYgAdapter(Context context,List<Notice> listnotice){
		this.listnotice=listnotice;
		layoutinflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listnotice.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return listnotice.get(pos);
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
			view=layoutinflater.inflate(R.layout.notice_yg_item,null);
		}
		if(itemheight>0){
			LinearLayout itemlayout=(LinearLayout)view.findViewById(R.id.itemlayout);
			LayoutParams params=itemlayout.getLayoutParams();
			params.height=itemheight;
			itemlayout.setLayoutParams(params);
		}
		TextView ytime=(TextView)view.findViewById(R.id.ytime);
		TextView yname=(TextView)view.findViewById(R.id.yname);
		ImageView isplay=(ImageView)view.findViewById(R.id.isplay);
		Notice nc=listnotice.get(pos);
		ytime.setText(nc.getStart()+"-"+nc.getEnd());
		yname.setText(nc.getName());
		if(nc.isIsplay()){
			isplay.setVisibility(View.VISIBLE);
		}else{
			isplay.setVisibility(View.INVISIBLE);
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
