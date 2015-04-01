package com.pro.phonemessage;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.pro.utils.ComUtils;
import com.pro.view.PhoneLayout;

public class MainActivity extends Activity {

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()==R.id.exit){
			exit();
		}
		if(item.getItemId()==R.id.set){
			Intent set=new Intent(this,SetActivity.class);
			this.startActivity(set);
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(receiver);
	}
	private PhoneLayout phonelayout;
	private progessReceiver receiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startservice();
		phonelayout=(PhoneLayout)this.findViewById(R.id.phonelayout);
		Intent intent=this.getIntent();
		String pn=intent.getStringExtra("pn");
		String pcon=intent.getStringExtra("pcon");
		if(pn!=null&&pcon!=null){
			phonelayout.setvalue(pn, pcon);
		}
		receiver=new progessReceiver();
		this.registerReceiver(receiver, new IntentFilter("com.pro.phonemessage.progress"));
		
	}
	private void startservice(){
		Intent service=new Intent(this,PhoneService.class);
		service.putExtra("action", "close");
		this.startService(service);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void exit(){
		Intent service=new Intent(this,PhoneService.class);
		this.stopService(service);
		this.finish();
	}
	class progessReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action=intent.getAction();
			if("com.pro.phonemessage.progress".equals(action)){
				int pro=intent.getIntExtra("progress", 0);
				phonelayout.setprogressvalue(pro);
			}
		}
		
	}
}
