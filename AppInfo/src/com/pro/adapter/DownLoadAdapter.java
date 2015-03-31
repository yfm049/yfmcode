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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.appinfo.DownLoadService;
import com.pro.appinfo.R;
import com.pro.model.Appinfo;
import com.pro.model.FenLei;
//程序下载列表 用于显示下载的软件
public class DownLoadAdapter extends BaseAdapter {

	private List<Appinfo> lf;
	private Context context;

	public DownLoadAdapter(Context context, List<Appinfo> lf) {
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
					R.layout.activity_download_item, null);
		}
		TextView name = (TextView) view.findViewById(R.id.name);
		name.setText(fl.getName());
		TextView state = (TextView) view.findViewById(R.id.state);
		ProgressBar pb = (ProgressBar) view.findViewById(R.id.progess);
		if(fl.getDownload()==-1){
			state.setText("下载失败");
		}else if(fl.getDownload()==100){
			pb.setProgress(fl.getDownload());
			state.setText("下载完成");
		}else{
			state.setText(fl.getDownload()+"%");
			pb.setProgress(fl.getDownload());
		}
		return view;
	}
	

}
