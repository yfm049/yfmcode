package com.iptv.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.iptv.utils.HttpUtils;
import com.iptv.utils.SqliteUtils;


public class LoginThread extends Thread{

	private Handler handler;
	private String url;
	private HttpUtils hu;
	private SqliteUtils su;
	public LoginThread(Context context,Handler handler,String url){
		this.handler=handler;
		this.url=url;
		this.hu=new HttpUtils();
		this.su=new SqliteUtils(context);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			Message msg=handler.obtainMessage();
			String xml=hu.doget(HttpUtils.baseurl+url);
			if(xml!=null){
				su.init();
				boolean isok=hu.parsexml(xml,su);
//				hu=new HttpUtils();
//				hu.SaveYuGaoData(su);
				if(isok){
					msg.what=1;//认证成功
				}else{
					msg.what=-1;
					msg.arg1=2;//认证失败;
				}
			}else{
				msg.what=-1;
				msg.arg1=1;//网络不通
			}
			handler.sendMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
