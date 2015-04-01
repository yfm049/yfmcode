package com.iptv.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iptv.pojo.Notice;
import com.iptv.season.R;

public class NoticeAdapter extends AbsAdapter {

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	private Context context;
	private List<Notice> ln;
	public NoticeAdapter(Context context,List<Notice> ln){
		this.context=context;
		this.ln=ln;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ln.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return ln.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Notice notice=ln.get(position);
		
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.activity_notice_lisg_item, null);
		}
		if(notice!=null){
			TextView datatime=(TextView)convertView.findViewById(R.id.datatime);
			TextView nname=(TextView)convertView.findViewById(R.id.nname);
			datatime.setText(notice.getStart()+"-"+notice.getEnd());
			nname.setText(notice.getName());
		}
		
		if(itemheight>0){
			LinearLayout itemlayout=(LinearLayout)convertView.findViewById(R.id.itemlayout);
			LayoutParams params=itemlayout.getLayoutParams();
			params.height=itemheight;
			itemlayout.setLayoutParams(params);
			Log.i("tag", itemheight+" notice");
		}
		return convertView;
	}


}
