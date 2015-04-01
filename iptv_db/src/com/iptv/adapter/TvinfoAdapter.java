package com.iptv.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iptv.ovpbox.R;
import com.iptv.pojo.TvInfo;

public class TvinfoAdapter extends BaseAdapter {

	private Context context;
	private List<TvInfo> listinfo;
	public TvinfoAdapter(Context context,List<TvInfo> listinfo){
		this.context=context;
		this.listinfo=listinfo;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listinfo.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listinfo.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		TvInfo livetv=listinfo.get(arg0);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_play_lisg_item, null);
		}
		TextView fname=(TextView)view.findViewById(R.id.tvitem);
		TextView xuhao=(TextView)view.findViewById(R.id.xuhao);
		ImageView fav=(ImageView)view.findViewById(R.id.fav);
		fname.setText(livetv.getName());
		xuhao.setText(String.valueOf(arg0+1));
		if("1".equals(livetv.getFlag())){
			fav.setVisibility(View.VISIBLE);
		}else{
			fav.setVisibility(View.INVISIBLE);
		}
		Log.i("tvinfo",livetv.getName());
		return view;
	}

}
