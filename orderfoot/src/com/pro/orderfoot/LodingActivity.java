package com.pro.orderfoot;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.pro.task.LodingThread;
import com.pro.utils.ComUtils;

public class LodingActivity extends Activity {

	private SharedPreferences sp;
	private AlertDialog dialog;
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sp=this.getSharedPreferences("config", MODE_PRIVATE);
		this.setContentView(R.layout.activity_loding);
		String ip=sp.getString("ip", "192.168.0.1");
		int port=sp.getInt("port", 80);
		String pass=sp.getString("pass", "192.168.0.1");
		pd=ComUtils.createProgressDialog(this, "同步", "正在同步数据...");
		pd.show();
		LodingThread thread=new LodingThread(this, handler, ip, port, pass);
		thread.start();
		
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			pd.dismiss();
			if(msg.what==100){
				ShowDialog("消息", msg.obj.toString());
			}else if(msg.what==200){
				ToMealActivity();
			}
		}
		
	};
	
	public void ToMealActivity(){
		Intent intent=new Intent(this,MealActivity.class);
		this.startActivity(intent);
		this.finish();
	}
	
	public void ShowDialog(String title,String msg){
		Builder builder=ComUtils.CreateAlertDialog(this, title, msg);
		builder.setPositiveButton("知道了", null);
		dialog=builder.create();
		dialog.show();
	}

}
