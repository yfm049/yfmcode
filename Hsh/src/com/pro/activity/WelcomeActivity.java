package com.pro.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;

import android.os.Handler;
import android.os.Message;

import com.pro.base.BaseActivity;
import com.pro.hsh.R;

@Fullscreen
@EActivity(R.layout.activity_welcome)
public class WelcomeActivity extends BaseActivity {

	
	@AfterViews
	public void init(){
		handler.sendEmptyMessageDelayed(100, 2000);
	}
	
	public Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			LoginActivity_.intent(WelcomeActivity.this).start();
			WelcomeActivity.this.finish();
		}
		
	};
}
