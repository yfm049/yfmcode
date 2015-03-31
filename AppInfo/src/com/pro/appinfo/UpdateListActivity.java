package com.pro.appinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;

import com.pro.adapter.UpdateAppAdapter;
//软件更新列表显示
public class UpdateListActivity extends Activity {

	private ListView datalist;
	
	private UpdateAppAdapter adapter;
	
	private ImageView download;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_update);
		datalist = (ListView) super.findViewById(R.id.datalist);
		download=(ImageView)super.findViewById(R.id.download);
		download.setOnClickListener(new OnClickListenerImpl());
		super.findViewById(R.id.sname);
		adapter = new UpdateAppAdapter(this, UpdateService.fl);
		datalist.setAdapter(adapter);
		
	}
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			if(but.getId()==R.id.download){
				//跳转到下载页面
				Intent intent=new Intent(UpdateListActivity.this,DownLoadActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				UpdateListActivity.this.startActivity(intent);
			}
			
		}
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adapter.notifyDataSetChanged();
	}
	

}
