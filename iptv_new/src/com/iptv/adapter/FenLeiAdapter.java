package com.iptv.adapter;

import java.util.List;

import com.iptv.pojo.Notice;
import com.iptv.news.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FenLeiAdapter extends BaseAdapter {

	private Context context;
	private List<Notice> ln;
	public FenLeiAdapter(Context context,List<Notice> ln){
		this.context=context;
		this.ln=ln;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ln.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return ln.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Notice notice=ln.get(arg0);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.actitivy_play_notice, null);
		}
		TextView noticetime=(TextView)view.findViewById(R.id.noticetime);
		TextView noticename=(TextView)view.findViewById(R.id.noticename);
		ImageView playicon=(ImageView)view.findViewById(R.id.playicon);
		noticename.setText(notice.getName());
		noticetime.setText(notice.getTime());
		if(notice.isIsplay()){
			playicon.setVisibility(View.VISIBLE);
		}else{
			playicon.setVisibility(View.INVISIBLE);
		}
		return view;
	}

}
