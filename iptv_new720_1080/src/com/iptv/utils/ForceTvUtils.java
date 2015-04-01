package com.iptv.utils;

import java.net.URL;

import android.os.Handler;
import android.util.Log;

import com.iptv.play.Player;
import com.iptv.thread.ForceTvThread;

public class ForceTvUtils {

	private static String TAG=ForceTvUtils.class.getName();
	public static String paramurl="";
	private static ForceTvThread tvthread=null;
	public static void switch_chan(String httpurl,String hotlink,String userid,Handler handler){
		try {
			String param="http://127.0.0.1:9898/cmd.xml?cmd=switch_chan&";
			URL url=new URL(httpurl.replace("p2p", "http"));
			String server=url.getAuthority();
			String videoId = url.getPath();
			videoId = videoId.subSequence(1, videoId.length()-3).toString();
			param+="id="+videoId;
			param+="&server="+server;
			param+="&link="+hotlink;
			param+="&userid="+userid;
			String playurl="http://127.0.0.1:9898/"+videoId+".ts";
			if(tvthread!=null){
				tvthread.stopreq();
			}
			LogUtils.write(TAG, param);
			LogUtils.write(TAG, playurl);
			tvthread=new ForceTvThread(param, playurl, handler);
			tvthread.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void StopChan(){
		String param="http://127.0.0.1:9898/cmd.xml?cmd=stop_chan";
		LogUtils.write(TAG, param);
		tvthread=new ForceTvThread(param, "", new Handler());
		tvthread.start();
	}
}
