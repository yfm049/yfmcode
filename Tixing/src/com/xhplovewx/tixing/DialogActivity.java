package com.xhplovewx.tixing;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhplovewx.pojo.ItemInfo;

public class DialogActivity extends Activity {

	private WakeLock mWakelock;
	private TxApp txapp;
	private SharedPreferences sp;
	private Vibrator vibrator;
	private MediaPlayer mp;
	private boolean show;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Window win = getWindow();
		 win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
		 | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		 win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
		 | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		setContentView(R.layout.activity_dialog);
		sp=this.getSharedPreferences("config", MODE_PRIVATE);
		
		
		
		txapp=(TxApp)this.getApplication();
		int id=this.getIntent().getIntExtra("iteminfo",-1);
		int item=this.getIntent().getIntExtra("item",-1);
		
		show=this.getIntent().getBooleanExtra("show", true);
		
		ItemInfo info=getitem(id);
		if(info!=null){
			TextView title=(TextView)this.findViewById(R.id.title);
			LinearLayout titlelayout=(LinearLayout)this.findViewById(R.id.titlelayout);
			TextView con=(TextView)this.findViewById(R.id.con);
			title.setText(info.getTitle());
			if(item==1){
				con.setText(info.getContent());
			}else{
				con.setText(info.getContent2());
			}
			titlelayout.setBackgroundResource(info.getLevel());
		}
		
		
		
		Button button=(Button)this.findViewById(R.id.ok_but);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	public ItemInfo getitem(int id){
		for(ItemInfo info:txapp.getLitem()){
			if(id==info.getId()){
				return info;
			}
		}
		return null;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
			if(show){
				if(sp.getBoolean("zdcb", false)){
					try {
						vibrator=(Vibrator)this.getSystemService(Service.VIBRATOR_SERVICE);
						vibrator.vibrate(new long[]{1000, 2000, 1000, 3000}, -1);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(sp.getBoolean("sycb", false)){
					try {
						Uri type=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
						mp=MediaPlayer.create(this, type);
						mp.start();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try {
				PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
				mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");
				        mWakelock.acquire();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			mWakelock.release();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			if(show){
				try {
					if(vibrator!=null){
						vibrator.cancel();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if(mp!=null){
						mp.stop();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		
	}

}
