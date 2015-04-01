package com.iptv.thread;

import com.iptv.utils.ComUtils;
import com.iptv.utils.DomUtils;
import com.iptv.utils.HttpClientHelper;
import com.iptv.utils.SqliteUtils;

import android.os.Handler;
import android.util.Log;

public class LoginThread extends Thread {

	public static int loginsuc=1,loginfail=2,serverfail=3;
	private String url;
	private Handler handler;
	private SqliteUtils su;
	public LoginThread(String url,Handler handler,SqliteUtils su){
		this.url=url;
		this.handler=handler;
		this.su=su;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		String xml=HttpClientHelper.DoGet(HttpClientHelper.baseurl+url);
		if(xml!=null){
			String dxml=ComUtils.Base64decode(xml);
			boolean issuc=DomUtils.ParseLoginXml(dxml, su);
			if(issuc){
				handler.sendEmptyMessage(loginsuc);
			}else{
				handler.sendEmptyMessage(loginfail);
			}
		}else{
			handler.sendEmptyMessage(serverfail);
		}
		
	}

}
