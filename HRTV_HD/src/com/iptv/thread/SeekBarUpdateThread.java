package com.iptv.thread;

import android.os.Handler;

public class SeekBarUpdateThread extends Thread {

	public static int seekBarUpdate=80;
	private boolean isrun=true;
	public void setIsrun(boolean isrun) {
		this.isrun = isrun;
	}

	private Handler handler;
	public boolean isIsrun() {
		return isrun;
	}

	public SeekBarUpdateThread(Handler handler){
		this.handler=handler;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			while(isrun){
				handler.sendEmptyMessage(seekBarUpdate);
				Thread.sleep(1000);
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
