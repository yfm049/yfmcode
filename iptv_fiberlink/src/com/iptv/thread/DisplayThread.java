package com.iptv.thread;

import android.os.Handler;

public class DisplayThread extends Thread {

	public int time=5;
	public boolean isrun=false;
	private Handler handler;
	public boolean isIsrun() {
		return isrun;
	}

	public void reset(){
		time=3;
	}
	public DisplayThread(Handler handler){
		this.handler=handler;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			isrun=true;
			while(time!=0){
				Thread.sleep(1000);
				time--;
			}
			isrun=false;
			handler.sendEmptyMessage(-100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
