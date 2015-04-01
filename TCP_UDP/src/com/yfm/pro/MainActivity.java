package com.yfm.pro;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TabHost;

public class MainActivity extends TabActivity {


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(dialog==null){
			dialog=new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			View view=LayoutInflater.from(this).inflate(R.layout.exit, null);
			Button closeapp=(Button)view.findViewById(R.id.closeapp);
			Button minapp=(Button)view.findViewById(R.id.minapp);
			Button cancel=(Button)view.findViewById(R.id.cancel);
			dialog.setContentView(view);
			closeapp.setOnClickListener(new OnClickListenerImpl());
			minapp.setOnClickListener(new OnClickListenerImpl());
			cancel.setOnClickListener(new OnClickListenerImpl());
			Utils.setDialog(this, dialog);
		}
		dialog.show();
	}
	private Dialog dialog;
	private TabHost tabhost;
	public static boolean issend=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		if(!Utils.isnengyong()){
			this.finish();
		}
		
		tabhost=this.getTabHost();
		tabhost.setup();
		Resources res=this.getResources();
		tabhost.addTab(tabhost.newTabSpec("tcpserver").setIndicator("TCP Server",res.getDrawable(R.drawable.tcpserver)).setContent(new Intent(this,TCPserverFragment.class)));
		tabhost.addTab(tabhost.newTabSpec("udpserver").setIndicator("UDP Server",res.getDrawable(R.drawable.udpserver)).setContent(new Intent(this,UDPserverFragment.class)));
		tabhost.addTab(tabhost.newTabSpec("tcpclient").setIndicator("TCP Client",res.getDrawable(R.drawable.tcpclient)).setContent(new Intent(this,TCPclientFragment.class)));
		tabhost.addTab(tabhost.newTabSpec("udpclient").setIndicator("UDP Client",res.getDrawable(R.drawable.udpclient)).setContent(new Intent(this,UDPclientFragment.class)));
	}
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			if(but.getId()==R.id.closeapp){
				dialog.cancel();
				issend=false;
				MainActivity.this.finish();
			}
			if(but.getId()==R.id.minapp){
				Intent home = new Intent(Intent.ACTION_MAIN);  
				home.addCategory(Intent.CATEGORY_HOME);   
				startActivity(home);
				dialog.cancel();
			}
			if(but.getId()==R.id.cancel){
				dialog.cancel();
			}
		}
		
	}
	
}
