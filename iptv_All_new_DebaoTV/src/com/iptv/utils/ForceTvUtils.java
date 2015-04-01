package com.iptv.utils;

import android.os.Handler;

import com.iptv.play.Player;
import com.iptv.thread.ForceTvStopThread;
import com.iptv.thread.ForceTvThread;

public class ForceTvUtils {

	private static String TAG = ForceTvUtils.class.getName();
	private static ForceTvThread tvthread = null;
	private static String url;

	public static void switch_chan(String httpurl,String link,String uid, Handler handler) {
			tvthread = new ForceTvThread(httpurl,link,uid, handler);
			tvthread.start();
	}

	public static void StopChan() {
		String param = "http://127.0.0.1:9898/cmd.xml?cmd=stop_chan";
		LogUtils.write(TAG, param);
		url="";
		ForceTvStopThread tvstopthread = new ForceTvStopThread(param);
		tvstopthread.start();
	}
}
