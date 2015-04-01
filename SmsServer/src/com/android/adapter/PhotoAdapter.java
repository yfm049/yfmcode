package com.android.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.model.Call;
import com.android.model.Photo;
import com.android.smsserver.PhotoViewActivity;
import com.android.smsserver.R;
import com.android.utils.HttpClientFactory;
import com.android.utils.Utils;

public class PhotoAdapter extends BaseAdapter {

	private Context context;
	private List<Photo> clients;
	public PhotoAdapter(Context context,List<Photo> clients){
		this.context=context;
		this.clients=clients;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return clients.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return clients.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Photo c=clients.get(pos);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_photo_list_item, null);
		}
		TextView phototime=(TextView)view.findViewById(R.id.phototime);
		Button play=(Button)view.findViewById(R.id.play);
		phototime.setText(c.getTime());
		play.setOnClickListener(new OnClickListenerImpl(c.getFile()));
		return view;
	}
	
	class OnClickListenerImpl implements OnClickListener{

		private String url;
		public OnClickListenerImpl(String purl){
			this.url=purl;
		}
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(url!=null&&!"".equals(url)){
				Intent intent=new Intent(context,PhotoViewActivity.class);
				intent.putExtra("imgurl", "pic/"+url);
				context.startActivity(intent);
			}else{
				Utils.ShowToast(context, "文件不存在");
			}
		}
		
	}
	

}
