package com.iptv.utils;

import android.os.Handler;

import com.iptv.thread.VideoStreamThread;

import dnet.VideoStream;

public class VideoStreamUtils {

	private static String TAG=VideoStreamUtils.class.getName();
	public static String paramurl="";
	private static VideoStreamThread tvthread=null;
	public static void switch_chan(String httpurl,String hotlink,String userid,Handler handler){
		try {
			
			String playurl="http://127.0.0.1:";
			
			LogUtils.write(TAG, hotlink);
			LogUtils.write(TAG, playurl);
			tvthread=new VideoStreamThread(hotlink, playurl, handler);
			tvthread.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void StopChan(){
		VideoStream.ins.stop();
        VideoStream.ins.resume();
	}
}
