package com.androidpro.game;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ClassificationActivity extends Activity{

	private ListView cdatalist;
	private ClassAdapter adapter;
	private OnItemClickListenerImpl listener;
	private ClassApplication app;
	private ProgressDialog pd;
	private Builder builder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		listener=new OnItemClickListenerImpl();
		super.setContentView(R.layout.classification_activity);
		ClassApplication application=(ClassApplication)this.getApplication();
		application.AddActivity(this);
		cdatalist=(ListView)this.findViewById(R.id.cdatalist);
		app=(ClassApplication)this.getApplication();
		if(Utils.isNetworkConnected(this)){
			showDialog();
		}else{
			InitData();
		}
	}
	private void InitData(){
		adapter=new ClassAdapter(this, app.getArray());
		cdatalist.setAdapter(adapter);
		cdatalist.setOnItemClickListener(listener);
	}

	class OnItemClickListenerImpl implements OnItemClickListener,DialogInterface.OnClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(ClassificationActivity.this,SubclassificationActivity.class);
			intent.putExtra("pos", arg2);
			ClassificationActivity.this.startActivity(intent);
			
		}

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			if(arg1==DialogInterface.BUTTON_POSITIVE){
				Upgrade();
			}
			if(arg1==DialogInterface.BUTTON_NEGATIVE){
				InitData();
			}
			
		}
		
	}
	private void Upgrade(){
		pd=Utils.CreatePdDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMax(100);
		pd.setProgress(0);
		pd.setTitle("软件升级");
		pd.setMessage("升级中  正在下载更新数据");
		pd.setCancelable(false);
		pd.show();
		handler.sendEmptyMessage(100);
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==100){
				int c=pd.getProgress();
				if(c<100){
					pd.setProgress(c+1);
					pd.setMessage("升级中  正在下载更新数据 "+pd.getProgress()+"%");
					if(c<98){
						handler.sendEmptyMessageDelayed(100, 13*1000/100);
					}else{
						handler.sendEmptyMessageDelayed(100, 2000);
					}
					
				}else{
					pd.dismiss();
					showSucDialog();
				}
			}
		}
		
	};
	private void showDialog(){
		builder=Utils.CreateAlertDialog(this);
		builder.setTitle("软件升级");
		builder.setMessage("是否升级");
		builder.setPositiveButton("确定", listener);
		builder.setNegativeButton("取消", listener);
		builder.setCancelable(false);
		builder.create().show();
	}
	private void showSucDialog(){
		builder=Utils.CreateAlertDialog(this);
		builder.setTitle("软件升级");
		builder.setMessage("升级成功");
		builder.setNegativeButton("确定", listener);
		builder.setCancelable(false);
		builder.create().show();
	}
}
