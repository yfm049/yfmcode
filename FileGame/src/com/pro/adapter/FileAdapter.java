package com.pro.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pro.game.R;
import com.pro.pojo.GameFile;

public class FileAdapter extends BaseAdapter {

	private Context context;
	private List<GameFile> files;
	public FileAdapter(Context context,List<GameFile> files){
		this.context=context;
		this.files=files;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return files.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return files.get(pos);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		GameFile file=files.get(pos);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.file_item, null);
		}
		ImageView filelogo=(ImageView)view.findViewById(R.id.filelogo);
		TextView filename=(TextView)view.findViewById(R.id.filename);
		TextView gametype=(TextView)view.findViewById(R.id.gametype);
		filename.setText(file.getFile().getName());
		setfilelogo(filelogo,file.getFile());
		if(file.getSimulator()!=null){
			gametype.setText(file.getSimulator().getName());
		}
		return view;
	}
	private void setfilelogo(ImageView filelogo,File file){
		if(file.isDirectory()){
			filelogo.setImageResource(R.drawable.folder);
		}else{
			filelogo.setImageResource(R.drawable.file);
		}
	}

}
