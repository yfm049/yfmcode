package com.yfm.adapter;

import java.util.List;

import com.yfm.pro.R;
import com.yfm.pro.SqliteUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class DataAdapter extends BaseAdapter {

	private List<String> ls;
	private Context context;
	private SqliteUtils su;
	public DataAdapter(Context context,List<String> ls){
		this.context=context;
		this.ls=ls;
		su=new SqliteUtils(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ls.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return ls.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.list_item, null);
		}
		TextView item=(TextView)view.findViewById(R.id.item_data);
		item.setText(ls.get(arg0));
		Button delete=(Button)view.findViewById(R.id.deteteitem);
		delete.setOnClickListener(new OnClickListenerImpl(ls.get(arg0)));
		return view;
	}
	class OnClickListenerImpl implements OnClickListener{

		private String mg;
		public OnClickListenerImpl(String mg){
			this.mg=mg;
		}
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			su.delete(mg);
			ls.remove(mg);
			DataAdapter.this.notifyDataSetChanged();
		}
		
	}
}
