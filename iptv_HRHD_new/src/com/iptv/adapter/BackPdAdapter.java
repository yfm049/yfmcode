package com.iptv.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iptv.pojo.Channel;
import com.iptv.HRTV.R;

public class BackPdAdapter extends AbsDataAdapter {
	private Context context;
	private List<Channel> listcn;
	private LayoutInflater layoutinflater;
	private int cpos=-1;
	private int itemheight=-1;
	private int vcount=9;
	public void setCpos(int cpos) {
		this.cpos = cpos;
	}
	public BackPdAdapter(Context context,List<Channel> listcn){
		this.context=context;
		this.listcn=listcn;
		layoutinflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listcn.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return listcn.get(pos);
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
			view=layoutinflater.inflate(R.layout.back_pd_item, null);
			
		}
		if(itemheight>0){
			LinearLayout itemlayout=(LinearLayout)view.findViewById(R.id.itemlayout);
			LayoutParams params=itemlayout.getLayoutParams();
			params.height=itemheight;
			itemlayout.setLayoutParams(params);
		}
		
		TextView cid=(TextView)view.findViewById(R.id.cid);
		TextView cname=(TextView)view.findViewById(R.id.cname);
		ImageView logo=(ImageView)view.findViewById(R.id.logo);
		
		Channel cnl=listcn.get(pos);
		if(Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)){
			String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/iptv/logo/"+cnl.getLogo();
			Drawable drawble=BitmapDrawable.createFromPath(path);
			if(drawble!=null){
				logo.setImageDrawable(drawble);
			}
		}
		
		cid.setText(cnl.getId()+"");
		cname.setText(cnl.getName());
		if(cpos==pos){
			cname.getPaint().setFakeBoldText(true);
			cname.setTextColor(context.getResources().getColor(R.color.backplaychannel_color));
		}else{
			cname.getPaint().setFakeBoldText(false);
			cname.setTextColor(context.getResources().getColor(R.color.bai_color));
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
