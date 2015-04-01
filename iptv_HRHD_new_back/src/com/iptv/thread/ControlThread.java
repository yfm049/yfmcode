package com.iptv.thread;

import android.os.Handler;

import com.iptv.utils.LogUtils;

public class ControlThread extends Thread {

	private Handler handler;
	private int what;
	private int time;
	private int temptime;
	private boolean isrun=true;
	private boolean issend=true;
	public boolean isIsrun() {
		return isrun;
	}
	public void setIsrun(boolean isrun) {
		this.isrun = isrun;
		this.issend=isrun;
	}
	public void resettime(){
		time=temptime;
	}
	public ControlThread(Handler handler,int time,int what){
		this.handler=handler;
		this.what=what;
		this.time=time;
		this.temptime=time;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		isrun=true;
		while(isrun&&!(time<=0)){
			try {
				Thread.sleep(1000);
				time=time-1;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		isrun=false;
		if(issend){
			handler.sendEmptyMessage(what);
		}
		
	}


}
