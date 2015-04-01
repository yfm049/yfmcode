package com.iptv.thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.iptv.App.IptvApp;
import com.iptv.pro.PlayTVActivity;
import com.iptv.utils.DomUtils;
import com.iptv.utils.ForceTvUtils;
import com.iptv.utils.HttpClientHelper;
import com.iptv.utils.LogUtils;

public class ForceTvThread extends Thread {

	private static String TAG = ForceTvThread.class.getName();
	public static int ForceTvsuc = 10, ForceTvfail = 20;
	private String url;
	private String link;
	private String uid;
	private Handler handler;
	private boolean isrun = true;

	public ForceTvThread(String url,String hotlink,String uid, Handler handler) {
		this.url = url;
		this.link=hotlink;
		this.uid=uid;
		this.handler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			Thread.sleep(100);
			if (!isrun) {
				return;
			} else {
				LogUtils.write("tvinfo", "开始调用play.play()方法,参数httpurl:"+url+" link:"+link+" uid:"+uid);
				String playurl = IptvApp.play.play(url,link,uid);
				LogUtils.write("tvinfo", "调用play 完成");
				Message msg = handler.obtainMessage();
				msg.obj = playurl;
				msg.what = ForceTvsuc;
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
