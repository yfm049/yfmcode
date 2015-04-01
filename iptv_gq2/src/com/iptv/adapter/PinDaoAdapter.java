package com.iptv.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iptv.xsj7188.R;
import com.iptv.pojo.BackData;
import com.iptv.pojo.Film;

public class PinDaoAdapter extends BaseAdapter {

	private Context context;
	private BackData user;
	public PinDaoAdapter(Context context,BackData user){
		this.context=context;
		this.user=user;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return user.getFilmlist().size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return user.getFilmlist().get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Film film=user.getFilmlist().get(arg0);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_pindao_item, null);
		}
		TextView pdname=(TextView)view.findViewById(R.id.ptime);
		TextView xuhao=(TextView)view.findViewById(R.id.xuhao);
		ImageView logo=(ImageView)view.findViewById(R.id.pdlogo);
		Drawable drawble=user.getImageLoader().loadDrawable(film.getUrl(), logo);
		if(drawble==null){
			logo.setImageResource(R.drawable.tubiao);
		}
		xuhao.setText((arg0+1)+"");
		pdname.setText(film.getName());
		return view;
	}

}
