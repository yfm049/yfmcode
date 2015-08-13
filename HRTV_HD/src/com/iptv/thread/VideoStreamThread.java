package com.iptv.thread;

import android.os.Handler;
import android.os.Message;

import com.iptv.utils.VideoStreamUtils;

import dnet.VideoStream;

public class VideoStreamThread extends Thread {

	private static String TAG = VideoStreamThread.class.getName();
	public static int ForceTvsuc = 10, ForceTvfail = 20;
	private String hotlink;
	private String playurl;
	private Handler handler;
	private boolean isrun = true;

	public VideoStreamThread(String hotlink, String playurl, Handler handler) {
		this.hotlink = hotlink;
		this.playurl = playurl;
		this.handler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			Thread.sleep(300);
			if (!isrun) {
				return;
			}
			if (isrun) {
				int ret = VideoStream.ins.switchChannel(hotlink);
				Message msg = handler.obtainMessage();
				if (ret>0) {
					msg.obj = playurl+ret;
					msg.what = ForceTvsuc;
					VideoStreamUtils.paramurl = hotlink;
				} else {
					msg.what = ForceTvfail;
				}
				handler.sendMessage(msg);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void stopreq() {
		isrun = false;
	}

}
