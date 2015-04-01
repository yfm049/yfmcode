package com.iptv.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.LinearLayout;


public class EventLinearLayout extends LinearLayout {

	public int what;
	private Handler handler;
	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler,int what) {
		this.handler = handler;
		this.what=what;
	}

	public EventLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public EventLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public EventLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if(handler!=null){
			handler.removeMessages(what);
			handler.sendEmptyMessageDelayed(what, 10000);
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if(handler!=null){
			handler.removeMessages(what);
			handler.sendEmptyMessageDelayed(what, 10000);
		}
		return super.dispatchTouchEvent(ev);
	}

}
