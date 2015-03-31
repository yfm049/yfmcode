package com.pro.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pro.appinfo.R;
import com.pro.model.FenLei;
//分类数据显示适配器，用于显示软件分类列表
public class FLAdapter extends BaseAdapter {

	private List<FenLei> lf;
	private Context context;
	public FLAdapter(Context context,List<FenLei> lf){
		this.context=context;
		this.lf=lf;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lf.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lf.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		FenLei fl=lf.get(pos);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_fenlei_item, null);
		}
		TextView name=(TextView)view.findViewById(R.id.name);
		name.setText(fl.getName());
		return view;
	}

}
