package com.a.a;


import android.app.Application;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class a extends Application implements java.lang.Thread.UncaughtExceptionHandler {
	public static sm Msg;

	@Override
	public void onCreate() {
		Thread.setDefaultUncaughtExceptionHandler(this);
		super.onCreate();
		Msg = new sm();
	}

	public void uncaughtException(Thread thread, final Throwable ex) {
		Log.e("T_DEBUG", "异常", ex);
		if (u.DEBUG) {
			new Thread(new Runnable() {
				public void run() {
					ex.printStackTrace();
					Looper.prepare();
					Toast.makeText(a.this, "出现异常：" + ex.getMessage(), Toast.LENGTH_LONG).show();
					Looper.loop();
				}
			}).start();
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
			}
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}
