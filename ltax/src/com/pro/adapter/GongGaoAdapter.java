package com.pro.adapter;

import java.util.List;
import java.util.Map;

import com.pro.ltax.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 公告列表显示 适配器
 * @author lenovo
 *
 */
public class GongGaoAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String, String>> lmo;
	public GongGaoAdapter(Context context,List<Map<String, String>> lmo){
		this.context=context;
		this.lmo=lmo;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lmo.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lmo.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Map<String, String> mo=lmo.get(arg0);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_gonggao_item, null);
		}
		TextView title=(TextView)view.findViewById(R.id.title);
		TextView sl=(TextView)view.findViewById(R.id.sl);
		TextView time=(TextView)view.findViewById(R.id.time);
		title.setText(mo.get("title"));
		sl.setText(mo.get("sl"));
		time.setText(mo.get("time"));
		return view;
	}

}
