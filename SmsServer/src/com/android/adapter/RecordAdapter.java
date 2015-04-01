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

import com.android.model.Record;
import com.android.smsserver.R;
import com.android.utils.HttpClientFactory;
import com.android.utils.Utils;

public class RecordAdapter extends BaseAdapter {

	private Context context;
	private List<Record> clients;
	public RecordAdapter(Context context,List<Record> clients){
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
		Record c=clients.get(pos);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_record_list_item, null);
		}
		TextView starttime=(TextView)view.findViewById(R.id.starttime);
		Button play=(Button)view.findViewById(R.id.play);
		starttime.setText(c.getStarttime());
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
				playAudio(HttpClientFactory.httpurl+"sound/"+url);
			}else{
				Utils.ShowToast(context, "录音文件不存在");
			}
		}
		
	}
	
	
	private void playAudio(String audioPath){	
		Intent intent = new Intent();  
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(audioPath), "audio/mp3");
        context.startActivity(intent);
	}

}
