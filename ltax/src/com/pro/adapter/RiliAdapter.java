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
 * 日历展示适配器类
 * @author lenovo
 *
 */
public class RiliAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String, String>> lmo;
	public RiliAdapter(Context context,List<Map<String, String>> lmo){
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
			view=LayoutInflater.from(context).inflate(R.layout.activity_rili_item, null);
		}
		TextView start=(TextView)view.findViewById(R.id.start);
		TextView end=(TextView)view.findViewById(R.id.end);
		TextView tend=(TextView)view.findViewById(R.id.tend);
		TextView con=(TextView)view.findViewById(R.id.con);
		start.setText(mo.get("start"));
		end.setText(mo.get("end"));
		tend.setText(mo.get("tend"));
		con.setText(mo.get("content"));
		return view;
	}

}
