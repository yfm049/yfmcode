package com.pro.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pro.orderfoot.R;

public class DeskListAdapter extends BaseAdapter {

	
	

	private List<Map<String, Object>> desks;
	
	private Context context;
	public DeskListAdapter(Context context,List<Map<String, Object>> lmi){
		this.desks=lmi;
		this.context=context;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return desks.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return desks.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 111;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Map<String, Object> mi=desks.get(pos);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_meal_deskitem, null);
		}
		TextView deskname=(TextView)view.findViewById(R.id.tprice);
		
		deskname.setText(mi.get("DeskName").toString());
		return view;
	}

}
