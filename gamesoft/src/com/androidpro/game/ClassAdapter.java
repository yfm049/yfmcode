package com.androidpro.game;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ClassAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String, Object>> array;
	public ClassAdapter(Context context,List<Map<String, Object>> array){
		this.context=context;
		this.array=array;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return array.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return array.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.class_item_layout, null);
		}
		Map<String, Object> ci=array.get(arg0);
		TextView itemname=(TextView)view.findViewById(R.id.itemname);
		TextView itemid=(TextView)view.findViewById(R.id.itemid);
		itemid.setText(ci.get("id").toString()+".");
		itemname.setText(ci.get("name").toString());
		return view;
	}

}
