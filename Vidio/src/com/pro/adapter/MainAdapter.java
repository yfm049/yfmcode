package com.pro.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pro.pojo.VideoFile;
import com.pro.vidio.R;

public class MainAdapter extends BaseAdapter {

	private DisplayImageOptions options; 

	private List<VideoFile> files;
	private Context context;
	public MainAdapter(Context context,List<VideoFile> files){
		this.files=files;
		this.context=context;
		options=new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).build();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return files.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return files.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		VideoHolder holder;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.activity_main_item, null);
			holder=new VideoHolder();
			holder.video_img=(ImageView)convertView.findViewById(R.id.vidio_img);
			holder.video_name=(TextView)convertView.findViewById(R.id.video_name);
			convertView.setTag(holder);
		}else{
			holder=(VideoHolder)convertView.getTag();
		}
		VideoFile f=files.get(position);
		holder.video_name.setText(f.name);
		ImageLoader.getInstance().displayImage(f.img, holder.video_img,options);
		return convertView;
	}
	
	class VideoHolder{
		ImageView video_img;
		TextView video_name;
	}

}
