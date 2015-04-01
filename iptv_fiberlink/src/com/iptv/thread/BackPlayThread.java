package com.iptv.thread;

import com.iptv.utils.HttpUtils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class BackPlayThread extends Thread{

	private Handler handler;
	private String url;
	private HttpUtils hu;
	private boolean isqh=true;
	public BackPlayThread(Handler handler,String url){
		this.handler=handler;
		this.url=url;
		hu=new HttpUtils();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			sendmsg(1,"正在切换至 ");
			Log.i("tvinfo", url);
			String xml=hu.doget(url);
			Log.i("tvinfo", xml);
			if(xml!=null&&isqh){
				sendmsg(2,"开始加载");
				if(isqh){
					sendmsg(3,"开始播放 ");
				}else{
					sendmsg(4,"加载失败 ");
				}
			}else{
				sendmsg(4,"切换失败 ");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void close(){
		isqh=false;
	}
	public void sendmsg(int i,String message){
		Message msg=handler.obtainMessage();
		msg.what=i;
		msg.obj=message;
		handler.sendMessage(msg);
	}
	
}
