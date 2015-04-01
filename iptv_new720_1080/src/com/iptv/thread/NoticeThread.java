package com.iptv.thread;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.iptv.pojo.Notice;
import com.iptv.utils.DomUtils;
import com.iptv.utils.HttpClientHelper;
import com.iptv.utils.LogUtils;

public class NoticeThread extends Thread {

	public static int noticesuc=1,noticefail=2;
	private String url;
	private Handler handler;
	private boolean isrun=true;
	public void setIsrun(boolean isrun) {
		this.isrun = isrun;
	}
	public NoticeThread(String url,Handler handler){
		this.url=url;
		this.handler=handler;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			Thread.sleep(500);
			if(!isrun){
				return ;
			}
			LogUtils.write("tvinfo", url);
			String xml=HttpClientHelper.DoGet(url);
			LogUtils.write("tvinfo", xml);
			List<Notice> ln=new ArrayList<Notice>();
			if(xml!=null){
				ln=DomUtils.ParseNoticeXml(xml);
				if(ln.size()>0){
					ln.get(0).setIsplay(true);
				}
			}
			if(isrun){
				Message msg=handler.obtainMessage();
				if(xml!=null){
					msg.what=noticesuc;
					msg.obj=ln;
				}else{
					msg.what=noticefail;
				}
				handler.sendMessage(msg);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
