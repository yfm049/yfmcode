package com.preview.camerapro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.preview.adapter.SetAdapter;

public class SetActivity extends Activity {

	private ListView setlist;
	private SetAdapter adapter;
	private String[] setcon=new String[]{"服务器IP端口设置","模式设置","早中晚时间设置","开门时间设置","机器编号设置"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setContentView(R.layout.activity_set);
		setlist=(ListView)super.findViewById(R.id.setlist);
		adapter =new SetAdapter(this,setcon);
		setlist.setAdapter(adapter);
		setlist.setOnItemClickListener(new setlistOnItemClickListener());
	}
	class setlistOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int loc,
				long arg3) {
			// TODO Auto-generated method stub
			if(loc==0){
				Intent intent=new Intent(SetActivity.this,IpSetActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				SetActivity.this.startActivity(intent);
			}
			if(loc==1){
				Intent intent=new Intent(SetActivity.this,ModeSetActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				SetActivity.this.startActivity(intent);
			}
			if(loc==2){
				Intent intent=new Intent(SetActivity.this,CftimeSetActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				SetActivity.this.startActivity(intent);
			}
			if(loc==3){
				Intent intent=new Intent(SetActivity.this,KmtimeSetActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				SetActivity.this.startActivity(intent);
			}
			if(loc==4){
				Intent intent=new Intent(SetActivity.this,CodeSetActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				SetActivity.this.startActivity(intent);
			}
		}
		
	}
}
