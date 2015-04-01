package com.iptv.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iptv.pojo.TvInfo;
import com.iptv.utils.Utils;
import com.iptv.news.R;

public class PlayPindaoAdapter extends BaseAdapter {

	private Context context;
	private List<TvInfo> listtv;
	public PlayPindaoAdapter(Context context,List<TvInfo> listtv){
		this.context=context;
		this.listtv=listtv;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listtv.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listtv.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		TvInfo livetv=listtv.get(arg0);
		ViewHolder holder;
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_play_lisg_item, null);
			ImageView icon=(ImageView)view.findViewById(R.id.icon);
			TextView xuhao=(TextView)view.findViewById(R.id.xuhao);
			TextView tvitem=(TextView)view.findViewById(R.id.tvitem);
			ImageView fav=(ImageView)view.findViewById(R.id.fav);
			holder=new ViewHolder();
			holder.icon=icon;
			holder.xuhao=xuhao;
			holder.tvitem=tvitem;
			holder.fav=fav;
			view.setTag(holder);
		}else{
			holder=(ViewHolder)view.getTag();
		}
		Utils.ImageLoader.loadBitmap(holder.icon, livetv.getLogourl());
		holder.tvitem.setText(livetv.getName());
		holder.xuhao.setText(String.valueOf(arg0+1));
		if("1".equals(livetv.getFlag())){
			holder.fav.setVisibility(View.VISIBLE);
		}else{
			holder.fav.setVisibility(View.INVISIBLE);
		}
		return view;
	}
	class ViewHolder{
		ImageView icon;
		TextView xuhao;
		TextView tvitem;
		ImageView fav;
	}

}
