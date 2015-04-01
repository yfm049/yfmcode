package com.iptv.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iptv.pojo.TvInfo;
import com.iptv.pro.UpdateService;
import com.iptv.season.R;

public class TvinfoAdapter extends AbsAdapter {

	

	private Context context;
	private List<TvInfo> listinfo;
	public TvinfoAdapter(Context context,List<TvInfo> listinfo){
		this.context=context;
		this.listinfo=listinfo;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listinfo.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listinfo.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		TvInfo livetv=listinfo.get(arg0);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_play_lisg_item, null);
		}
		TextView tvitem=(TextView)view.findViewById(R.id.tvitem);
		TextView xuhao=(TextView)view.findViewById(R.id.xuhao);
		ImageView fav=(ImageView)view.findViewById(R.id.fav);
		ImageView icon=(ImageView)view.findViewById(R.id.icon);
		String path=Environment.getExternalStorageDirectory().getAbsolutePath()+UpdateService.path+livetv.getLogourl();
		Log.i("tvinfo", livetv.getName()+" "+path);
		Bitmap bitmap=BitmapFactory.decodeFile(path);
		if(bitmap==null){
			icon.setImageResource(R.drawable.tubiao);
		}else{
			icon.setImageBitmap(bitmap);
		}
		tvitem.setText(livetv.getName());
		xuhao.setText(String.valueOf(arg0+1));
		if("1".equals(livetv.getFlag())){
			fav.setVisibility(View.VISIBLE);
		}else{
			fav.setVisibility(View.INVISIBLE);
		}
		if(itemheight>0){
			LinearLayout itemlayout=(LinearLayout)view.findViewById(R.id.itemlayout);
			LayoutParams params=itemlayout.getLayoutParams();
			params.height=itemheight;
			itemlayout.setLayoutParams(params);
			Log.i("tag", itemheight+"");
		}
		return view;
	}
}
