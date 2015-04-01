package com.iptv.thread;

import android.os.Handler;

import com.iptv.App.IptvApp;
import com.iptv.utils.ComUtils;
import com.iptv.utils.DomUtils;
import com.iptv.utils.LogUtils;
import com.iptv.utils.SqliteUtils;

public class LoginThread extends Thread {

	public static int loginsuc=1,loginfail=2,serverfail=3;
	private String name;
	private String pass;
	private Handler handler;
	private SqliteUtils su;
	public LoginThread(String name,String pass,Handler handler,SqliteUtils su){
		this.name=name;
		this.pass=pass;
		this.handler=handler;
		this.su=su;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		String xml=IptvApp.play.getlivedata(name, pass);
		if(xml!=null){
			String dxml=ComUtils.Base64decode(xml);
			LogUtils.write("tvinfo", dxml);
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
