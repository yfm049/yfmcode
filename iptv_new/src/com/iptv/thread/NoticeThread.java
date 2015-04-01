package com.iptv.thread;

import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.iptv.pojo.Notice;
import com.iptv.pojo.TvInfo;
import com.iptv.pro.FenLeiPlayActivity;
import com.iptv.utils.HttpClientHelper;

public class NoticeThread extends Thread {

	private Handler handler;
	private TvInfo tvinfo;
	private boolean isrun=true;
	public NoticeThread(Handler handler,TvInfo tvinfo){
		this.handler=handler;
		this.tvinfo=tvinfo;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		String xml=HttpClientHelper.DoGet(tvinfo.getEpg());
		List<Notice> ln=HttpClientHelper.ParseNoticeXml(xml);
		if(ln.size()>0){
			ln.get(0).setIsplay(true);
		}
		if(isrun){
			Message msg=handler.obtainMessage();
			msg.obj=ln;
			msg.what=5;
			handler.sendMessage(msg);
		}
		
	}
	public void stopthread(){
		isrun=false;
	}

}
