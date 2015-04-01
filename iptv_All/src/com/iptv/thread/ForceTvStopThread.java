package com.iptv.thread;

import com.iptv.utils.HttpClientHelper;

public class ForceTvStopThread extends Thread {

	private static String TAG = ForceTvStopThread.class.getName();
	private String url;

	public ForceTvStopThread(String url) {
		this.url = url;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			HttpClientHelper.DoGet(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
