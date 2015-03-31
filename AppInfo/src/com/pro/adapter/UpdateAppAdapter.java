package com.pro.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.appinfo.DownLoadService;
import com.pro.appinfo.R;
import com.pro.model.Appinfo;
import com.pro.model.FenLei;
//软件更新适配器，用于显示软件更新列表
public class UpdateAppAdapter extends BaseAdapter {

	private List<Appinfo> lf;
	private Context context;

	public UpdateAppAdapter(Context context, List<Appinfo> lf) {
		this.context = context;
		this.lf = lf;
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
		Appinfo fl = lf.get(pos);
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.activity_update_item, null);
		}
		TextView name = (TextView) view.findViewById(R.id.name);
		name.setText(fl.getName());
		ImageView imgbut = (ImageView) view.findViewById(R.id.imgbut);
		imgbut.setOnClickListener(new OnClickListenerImpl(pos));
		return view;
	}
	class OnClickListenerImpl implements OnClickListener{

		int pos;
		public OnClickListenerImpl(int pos){
			this.pos=pos;
		}
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Appinfo info=lf.get(pos);
			Log.i("下载", info.getName());
			Intent intent=new Intent(context,DownLoadService.class);
			intent.putExtra("appinfo", info);
			context.startService(intent);
			lf.remove(pos);
			UpdateAppAdapter.this.notifyDataSetChanged();
		}
		
	}

}
