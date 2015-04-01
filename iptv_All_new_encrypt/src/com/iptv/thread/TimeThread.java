package com.iptv.thread;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Handler;
import android.os.Message;


public class TimeThread extends Thread {
	public static int timeupdate=300;
	public static SimpleDateFormat rq=new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat sj=new SimpleDateFormat("HH:mm");
	private boolean isrun=true;
	public void setIsrun(boolean isrun) {
		this.isrun = isrun;
	}
	private Handler handler;
	public TimeThread(Handler handler){
		this.handler=handler;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while(isrun&&handler!=null){
			try {
				Message msg=handler.obtainMessage(timeupdate, new Date());
				handler.sendMessage(msg);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
